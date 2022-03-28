package kafka;

import java.util.HashMap;
import java.util.Map;

public class TestClass {

    private static Map<Integer, Double> windowEma38;

    public static void main(String[] args){

        System.out.println("Hello World!!!!!!!!!!");
        //windowEma38 = new HashMap<>();

        ciao(windowEma38);



    }

    public static void ciao(Map<Integer, Double> bho){
        if (windowEma38==null){
            System.out.println("È NULL");
            windowEma38 = new HashMap<>();
        }
        System.out.println("windowEma38 SIZE = "+windowEma38.size());
    }

}
