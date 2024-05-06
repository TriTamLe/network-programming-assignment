package DayTwo.TaskThree;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        int PORT = 5420;
        try {
            socket = new DatagramSocket();
            InetAddress serverIP = InetAddress.getByName("localhost");
            byte[] sendData;
            byte[] receiveData = new byte[1024];

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Client: ");
                String message = reader.readLine();
                sendData = message.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, PORT);
                socket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server " + serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}
