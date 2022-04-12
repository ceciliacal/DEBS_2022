package utils;


import scala.Int;

public interface Config {
    //public static String KAFKA_BROKERS = "kafka1:19091";
    public static String KAFKA_BROKERS = "localhost:9091";
    public static String TOPIC1 = "provaTopic";
    public static String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
    public static String pattern2 = "yyyy-MM-dd HH:mm:ss.SSSS";
    public static Integer windowLen = 5; //minutes
    public static Integer ema38 = 38;
    public static Integer ema100 = 100;


}

