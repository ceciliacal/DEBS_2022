package utils;


import data.Event;

import java.util.List;

public interface Config {
    //public static String KAFKA_BROKERS = "kafka1:19091";
    public static String KAFKA_BROKERS = "localhost:9091";
    public static String CLIENT_ID = "myclient";
    //public static String datasetPath = "dataset/debs-2022-gc-test-set-trading";
    public static String datasetPath = "dataset/test3";
    public static String TOPIC1 = "provaTopic";
    public static String TOPIC2 = "results";
    public static Double accTime = 0.0;
    public static String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
    public static String pattern2 = "yyyy-MM-dd HH:mm:ss.SSSS";

}

