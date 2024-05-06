package DayTwo.TaskThree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int PORT = 5420;
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            // Mở cổng 9999 để chờ kết nối từ Client
            System.out.println("Server is ready");

            byte[] receiveData = new byte[2048];


            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientIP = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.println("Client: " + clientMessage);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Server: ");
                String serverMessage = reader.readLine();
                byte[] sendData;
                sendData = serverMessage.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
