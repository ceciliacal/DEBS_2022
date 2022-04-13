#!bin/sh

sudo docker exec kafka kafka-topics --list --zookeeper zookeeper:2181
sudo docker exec kafka kafka-topics --delete --zookeeper zookeeper:2181 --topic provaTopic
echo "--- topic was removed ---"
sudo docker exec kafka kafka-topics --list --zookeeper zookeeper:2181
