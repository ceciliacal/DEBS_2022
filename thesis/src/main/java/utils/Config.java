package utils;


public interface Config {
    //public static String KAFKA_BROKERS = "kafka1:19091";
    public static String KAFKA_BROKERS = "localhost:9091";
    public static String CLIENT_ID = "myclient";
    public static String TOPIC1 = "provaTopic";
    public static String TOPIC2 = "results";
    public static Double accTime = 0.0;
    public static String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
    public static String pattern2 = "yyyy-MM-dd HH:mm:ss.SSSS";
    public static String buyAdvise = "BUY";
    public static String sellAdvise = "SELL";
    public static Integer windowLen = 5; //minutes
    public static Integer ema38 = 38;
    public static Integer ema100 = 100;


}

