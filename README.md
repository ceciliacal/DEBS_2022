# DEBS_2022

To run the solution, you need to have installed on your machine:
-Java
-Docker
-Docker compose
-Gradle

And follow the next steps:
-> go into docker folder and execute: sudo docker-compose up
-> in thesis folder, open a terminal and execute: gradle consumer
-> after running gradle consumer, open another terminal and execute: gradle run 

So it's necessary to execute "gradle consumer" FIRST and "gradle run" later in two separated shells.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is my solution for Grand Challenge DEBS 2022. My group ID is 16. 

There are two main applications inside one single Gradle project. 
To send data batches to my solution, it was used a Kafka producer application (class "kakfa.Producer"). A kafka consumer (kafka.Consumer) application was instead created to listen to the Kafka broker's topic the producer sends data to, and it processes received data with the stream processing framework Apache Flink. 
Once one 5 minutes window fires its results, it sends them back to the producer application through a Socket API using ip "localhost" and port "6667". Results are eventually sent to the evaluation platform through gRPC API provided by Grand Challenge DEBS. 
Kafka and Zookeeper runs on Docker containers defined in docker-compose.yml file. Each one of the two applications (producer and consumer) has its own main method and be built and launched using Gradle. 
	
## Technologies
Project is created with:
* Java version: openjdk 11.0.14.1
* Gradle version: 7.4
* Docker: 20.10.12
* Docker-compose: 1.27.4
* Kafka: 5.3.0
* Zookeeper: 3.8.0
* Apache Flink 
	
## Setup
To run this project, install it locally using:

```
$ cd ../lorem
$ npm install
$ npm start
```
