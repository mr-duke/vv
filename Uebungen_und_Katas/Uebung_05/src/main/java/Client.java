import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    public static void main(String[] args) {
        BlockingQueue queueSender = new LinkedBlockingQueue();
        BlockingQueue queueReceiver = new LinkedBlockingQueue();

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //queueSender.take();
                    while(true){
                    System.out.println(queueSender.take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }});
        sender.start();





        while (true) {
            Scanner scan = new Scanner(System.in);
            String mess = scan.nextLine();
            queueSender.offer(mess);
            //System.out.println(mess);
        }


    }
}
