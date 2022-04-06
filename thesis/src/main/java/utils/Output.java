package utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static utils.Config.accTime;

public class Output {

    public static PipedOutputStream geek_output;

    public static void main(String[] args) throws IOException, InterruptedException {


        ServerSocket ss = new ServerSocket(6666);
        Socket s = ss.accept();

        DataInputStream dis = new DataInputStream(s.getInputStream());
        String str = (String) dis.readUTF();
        System.out.println("message = "+str);
        ss.close();

    }

}
