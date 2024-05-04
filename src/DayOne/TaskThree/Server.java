package DayOne.TaskThree;

import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5420;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chờ kết nối...");

            // Chấp nhận kết nối từ client
            Socket clientSocket = serverSocket.accept();
            System.out.println("Một client đã kết nối!");

            // Tạo một luồng để nhận tin nhắn từ client
            Thread receiveThread = new Thread(new ReceiveMessage(clientSocket));
            receiveThread.start();

            // Luồng chính của server để gửi tin nhắn cho client
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            String message;
            while (true) {
                message = reader.readLine();
                writer.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReceiveMessage implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;

    public ReceiveMessage(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("Client: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

