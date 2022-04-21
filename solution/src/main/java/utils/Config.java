package utils;


public interface Config {
    //public static String KAFKA_BROKERS = "kafka1:19091";
    public static String KAFKA_BROKERS = "localhost:9091";
    public static String TOPIC = "debsTopic";
    public static String CLIENT_ID = "myclient";
    public static String TOPIC_RES = "resultsTopic";
    public static String pattern = "dd-MM-yyyy HH:mm:ss.SSSS";
    public static String pattern2 = "yyyy-MM-dd HH:mm:ss.SSSS";
    public static Integer windowLen = 5; //minutes
    public static Integer ema38 = 38;
    public static Integer ema100 = 100;


}

