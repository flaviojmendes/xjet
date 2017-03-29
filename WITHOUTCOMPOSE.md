### Starting environment without docker compose

###### Pre-Requisites

* [Install Docker](https://docs.docker.com/engine/installation/linux/)


##### Creating network

      docker network create --driver bridge todolistNet

##### Starting Mongo DB Container

      docker run --net=todolistNet --name=db -v /data/mongodb:/data/db -d mongo

##### Building Java App

      ./todolistapp/gradlew build -p todolistapp/

##### Building Java Container

      docker build -t todolistapp -f todolistapp/Dockerfile .

##### Starting Java Container

      docker run --net=todolistNet --name=todolistapp -p 8080:8080 -v $PWD/todolistapp:/app/ -d todolistapp
