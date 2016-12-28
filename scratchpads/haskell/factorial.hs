import System.Environment   
import Data.List 

factorial :: Integer -> Integer
factorial 1 = 1
factorial n = n * (factorial (n-1))
  
main = do  
    args <- getArgs  
    putStrLn $ "Factorial of " ++ head args
    putStrLn . show $ factorial (read)
