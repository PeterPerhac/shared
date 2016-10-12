attachToDocker() {
    docker exec -i -t $1 /bin/bash
}

whoListensOnPort() {
    if [[ $(($1)) -lt 1000 ]]
    then
        sudo lsof -sTCP:LISTEN -iTCP:$1
    else
        lsof -sTCP:LISTEN -iTCP:$1
    fi
}

portsByPid(){
    sudo lsof -iTCP -sTCP:LISTEN -P -n | grep $1
}

alias port=whoListensOnPort
alias portsByPid=portsByPid
