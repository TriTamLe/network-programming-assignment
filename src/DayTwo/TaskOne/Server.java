package DayTwo.TaskOne;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(5420);
            System.out.println("Server is running...");


            while (true) {
                Thread thread = new Thread(new ClientHandler(serverSocket));
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final DatagramSocket serverSocket ;
    private final byte[] receiveData = new byte[1024];


    public ClientHandler (DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            DatagramPacket receivePacket = new DatagramPacket(this.receiveData, this.receiveData.length);
            this.serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String text = sentence;

            String upperMessage = text.toUpperCase();
            String lowerMessage = text.toLowerCase();
            int wordCount = text.split("\\s+").length;
            String response = "Upper case: " + upperMessage + '\n' + "Lower case: " + lowerMessage + '\n'
                    + "Word count: " + String.valueOf(wordCount);


            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
