package utils;

import kafka.Producer;
import scala.Tuple2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static utils.Output.geek_output;

public class Input {

    public static void main(String[] args) throws IOException, InterruptedException {

        String s ="s";
/*
        java.util.Map<Tuple2<String, Integer>,Integer> bho = new HashMap<>();
        //bho.put(new Tuple2<>("s",1),10);
        //bho.put(new Tuple2<>("s",2),20);
        //bho.put(new Tuple2<>("s",3),30);
        //bho.put(new Tuple2<>("s",4),40);
        //bho.put(new Tuple2<>("s",5),50);
        int curr = 5;
        int i=2;
        int res =0;
        if (curr>0) {
            if (bho.containsKey(new Tuple2<>("s", curr-1))) {
                System.out.println("NO!");
            } else {
                System.out.println("i="+i+" curr="+curr);
                while (i != curr) {
                    System.out.println("key: "+new Tuple2<>(s, curr -i));
                    if (bho.containsKey(new Tuple2<>(s, curr -i))){
                        res=bho.get(new Tuple2<>(s,curr-i));
                        System.out.println("break!");
                        break;
                    }
                    i++;
                }
            }
            System.out.println(res);
        }



            String x2 = "[\"3\",\"1\"]";
            String x = "[a,1]";
            String[] array = x2.split("\"");
            array = Arrays.stream(array).filter(k -> k.matches("[\\dA-Za-z]")).toArray(String[]::new);
            //String array2 = Arrays.toString(array);
            //System.out.println(Arrays.toString(array));
        for(int j=0;j<array.length;j++){
            System.out.println(array[j]);
        }
            System.out.println(array);




        /*

        TimeUnit.SECONDS.sleep(3);
        Socket s = new Socket("localhost",6666);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF("caccaaa ciao");
        dout.flush();
        dout.close();
        s.close();

         */

        bho();
    }

    public static void bho(){
        //String str = "[2021-11-08 08:20:00.0, 2021-11-08 08:15:00.0, 2021-11-08 08:10:00.0]";
        String str = "[2021-11-08 08:10:00.0]";

        List<Timestamp> myList = new ArrayList<>();
        String[] line = str.split(",");
        for(int i=0;i<line.length;i++){
            System.out.println(line[i]);
        }
        int len = line.length;
        System.out.println("len = "+len);
        if (len>1){
            line[0] = line[0].substring(1);
            int lastStrlen = line[len-1].length();
            line[len-1] = line[len-1].substring(0,lastStrlen-1);
        } else {
            int lastStrlen = line[0].length();
            System.out.println("lastStrlen = "+lastStrlen);
            line[0]  = line[0].substring(1,lastStrlen-1);
        }
        System.out.println("DOPO");
        for(int i=0;i<line.length;i++){
            System.out.println(line[i]);
            //TRASFORMO IN LISTA DI TS
            Timestamp ts = Producer.stringToTimestamp(line[i],0);
            myList.add(ts);

        }
        System.out.println("myList = "+myList);
    }
}
