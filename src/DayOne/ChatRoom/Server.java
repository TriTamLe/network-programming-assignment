package DayOne.ChatRoom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final int PORT = 5420;
    private static HashSet<String> usernames = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chờ kết nối...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to a client");


                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {

                while (true) {
                    writer.println("Enter your name: ");
                    username = reader.readLine();
                    if (username == null) {
                        return;
                    }
                    synchronized (usernames) {
                        if (!usernames.contains(username)) {
                            usernames.add(username);
                            break;
                        }
                    }
                    writer.println("Existed username, please enter another.");
                }


                writer.println("welcome " + username + " to the Chat Room!");
                writer.println("To Exit, press the button '/quit'.");

                synchronized (writers) {
                    writers.add(writer);
                }

                broadcast(username + " joined the room.");


                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("/quit")) {
                        break;
                    }
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (username != null) {
                    usernames.remove(username);
                    broadcast(username + " left the room.");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (writers) {
                    writers.remove(writer);
                }
            }
        }

        // Phát sóng tin nhắn cho tất cả các người dùng
        private void broadcast(String message) {
            synchronized (writers) {
                for (PrintWriter writer : writers) {
                    writer.println(message);
                }
            }
        }
    }
}



