# DEBS_2022
This project is the solution proposed for the DEBS 2022 Grand Challenge by the group having ID group-16.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info

There are two main applications inside one single Gradle project. 

To send data batches to our solution, it was created a Kafka producer application (class "kakfa.Producer"). A kafka consumer ("kafka.Consumer") application was instead realized to listen to the Kafka broker's topic the producer sends data to, and to process those data with the open-source, data stream processing framework Apache Flink. 
Each time the 5-minute long tumbling window fires its results, it sends them back to the producer application through a Socket API. IP address is set to "localhost" but port must be passed as argument - e.g.: "6668" (see below "Setup"). 

Results are eventually sent from the producer application to the evaluation platform through gRPC API  provided by the Grand Challenge chairs. 

Kafka and Zookeeper run in Docker containers defined in docker-compose.yml file. Each one of the two applications (producer and consumer) has its own main method and can be built and launched using Gradle. 
	
## Technologies
The proposed solution is based on the following frameworks and tools:
* Java version: openjdk 11.0.14.1
* Gradle version: 7.4
* Docker: 20.10.12
* Docker-compose: 1.27.4
* Kafka: 5.3.0
* Zookeeper: 3.8.0
* Apache Flink 
	
## Setup
To run this project, you need to have previously installed Java and Gradle on your machine. 
Solution can be installed locally using:

```
$ git clone "this repo's url"
$ cd docker/
$ sudo docker-compose up
$ cd .. 
```
At this point you are back to the root folder "thesis". Now you need to open two new separate terminal shells to run the two applications in parallel. Type the next command in the former, and the last command in the latter: 
```
$ gradle consumer --args='#port'
$ gradle run --args='#port'
```
Port number must be identical in both cases, for example --args='6668' (or whatever available port on your machine). 

So, in order to launch the application properly, you HAVE TO to run the docker-compose file first, then run consumer application inside a shell (using command "gradle consumer --args='#port'") and only eventually run the producer in a new separated shell (command "gradle run --args='#port'"). 

To stop the docker container, you can type:
```
$ cd docker/
$ sudo ./stop.sh
```
