import cats.effect.{IO, IOApp, Resource}
import fs2.Stream
import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.broker.BrokerService
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter

import java.io.File
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Fs2ActiveMQResumeExample extends IOApp.Simple {

  val queueName = "test-queue"
  val brokerDataDir = new File("./activemq-data")

  def startBroker: IO[BrokerService] = IO {
    val broker = new BrokerService()
    broker.setPersistent(true)
    broker.setDataDirectoryFile(brokerDataDir)
    val kaha = new KahaDBPersistenceAdapter()
    kaha.setDirectory(new File(brokerDataDir, "kahadb"))
    broker.setPersistenceAdapter(kaha)
    broker.setBrokerName("localhost")
    broker.addConnector("vm://localhost")
    broker.start()
    broker
  }

  def stopBroker(broker: BrokerService): IO[Unit] = IO {
    broker.stop()
    broker.waitUntilStopped()
  }

  def createConnectionFactory: ActiveMQConnectionFactory =
    new ActiveMQConnectionFactory("vm://localhost")

  def producerResource: Resource[IO, MessageProducer] = for {
    connection <- Resource.make(IO(createConnectionFactory.createConnection()))(c => IO(c.close()))
    session    <- Resource.make(IO(connection.createSession(false, Session.CLIENT_ACKNOWLEDGE)))(s => IO(s.close()))
    producer   <- Resource.make(IO {
                     val queue = session.createQueue(queueName)
                     session.createProducer(queue)
                   })(p => IO(p.close()))
  } yield producer

  def publishMessages: IO[Unit] = {
    val cf = createConnectionFactory
    val connection = cf.createConnection()
    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val queue = session.createQueue(queueName)
    val producer = session.createProducer(queue)
    producer.setDeliveryMode(DeliveryMode.PERSISTENT)

    (1 to 1000).toList.traverse_ { i =>
      IO {
        val msg = session.createTextMessage(s"message-$i")
        producer.send(msg)
      }
    } *> IO {
      producer.close()
      session.close()
      connection.close()
    }
  }

  def consumerStream: Stream[IO, Unit] = {
    def createConsumer: Resource[IO, (Connection, Session, MessageConsumer)] = for {
      conn <- Resource.make(IO(createConnectionFactory.createConnection()))(c => IO(c.close()))
      _    <- Resource.eval(IO(conn.start()))
      sess <- Resource.make(IO(conn.createSession(false, Session.CLIENT_ACKNOWLEDGE)))(s => IO(s.close()))
      queue = sess.createQueue(queueName)
      cons <- Resource.make(IO(sess.createConsumer(queue)))(c => IO(c.close()))
    } yield (conn, sess, cons)

    Stream.resource(createConsumer).flatMap { case (_, session, consumer) =>
      Stream.repeatEval(IO.blocking(consumer.receive(500)))
        .takeWhile(_ != null)
        .evalMap(msg =>
          for {
            text <- IO(msg.asInstanceOf[TextMessage].getText)
            _    <- IO(println(s"[consumer] received: $text"))
            _    <- IO(msg.acknowledge())
          } yield ()
        )
    }
  }

  override def run: IO[Unit] = {
    for {
      broker <- startBroker
      _ <- publishMessages
      _ <- IO(println("[main] published 1000 messages"))

      // Start consumer in background
      fiber <- consumerStream.compile.drain.start

      // Let it run for 1 second
      _ <- IO.sleep(1.second)

      // Stop the broker
      _ <- IO(println("[main] stopping broker"))
      _ <- stopBroker(broker)

      // Wait for consumer to terminate (it should die because the connection is closed)
      _ <- fiber.join
      _ <- IO(println("[main] consumer stream terminated"))

      // Restart the broker
      broker2 <- startBroker
      _ <- IO(println("[main] broker restarted"))

      // Restart the consumer stream
      _ <- consumerStream.compile.drain
      _ <- IO(println("[main] finished consuming remaining messages"))

    } yield ()
  }
}

