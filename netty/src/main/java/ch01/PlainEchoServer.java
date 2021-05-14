package ch01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PlainEchoServer {

    public void serve(int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        System.out.println(String.format("Started server on %d", port));
        try {
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.out.println(String.format("Accepted connection from %s", clientSocket));

                new Thread(() -> {
                    try {

                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        while (true) {
                            writer.println(reader.readLine());
                            writer.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            // ignore on close
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new PlainEchoServer().serve(54321);
    }

}
