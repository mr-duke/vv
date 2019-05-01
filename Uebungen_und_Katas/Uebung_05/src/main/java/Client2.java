import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockingQueue<String> outgoing = new LinkedBlockingQueue();
        BlockingQueue<String> incoming = new LinkedBlockingQueue();
        Socket s = new Socket ("localhost", 10025 );

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try (OutputStream out = s.getOutputStream())
                {
                    while(true){
                        String message = outgoing.take();
                        out.write(message.getBytes());
                        out.write("\r\n".getBytes());
                        out.flush();
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }});
        sender.start();

        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try (InputStream in = s.getInputStream())
                {
                    Scanner scan = new Scanner(in);
                    while(true){
                        String zeile = scan.nextLine();
                        incoming.offer(zeile);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();

        while (true) {
            while (incoming.peek() == null){
                Thread.sleep(1000);
            }
            printIncoming(incoming);
            readConsole(outgoing);
        }
    }

    private static void printIncoming(BlockingQueue<String> incoming) {
        String response = incoming.poll();
        System.out.println(response);
    }

    private static void readConsole(BlockingQueue<String> outgoing) {
        Scanner scan = new Scanner(System.in);
        String message = scan.nextLine();
        outgoing.offer(message);
    }
}
