package utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static utils.Output.geek_output;

public class Input {

    public static void main(String[] args) throws IOException, InterruptedException {
        int a;
        a=2;
        System.out.println("a= "+a);
        a=a+3;
        System.out.println("a= "+a);


        /*

        TimeUnit.SECONDS.sleep(3);
        Socket s = new Socket("localhost",6666);
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        dout.writeUTF("caccaaa ciao");
        dout.flush();
        dout.close();
        s.close();

         */

    }
}
