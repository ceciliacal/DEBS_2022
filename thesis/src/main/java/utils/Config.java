package utils;

import java.text.SimpleDateFormat;

public interface Config {
    //public static String KAFKA_BROKERS = "kafka1:19091";
    public static String KAFKA_BROKERS = "localhost:9091";
    public static String CLIENT_ID = "myclient";
    public static String datasetPath = "dataset/debs-2022-gc-test-set-trading";
    public static String TOPIC1 = "provaTopic";
    public static String TOPIC_Q1 = "QUERY1";
    public static Double accTime = 0.0;
    public static Integer windowSize = 5;
    public static Integer TIME_DAYS = 2;
    public static Integer TIME_DAYS_7 = 7;
    public static Integer TIME_MONTH = 28;
    public static final SimpleDateFormat dateFormats = new SimpleDateFormat("dd/MM/yy HH:mm");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM:ss.ssss");

}

