package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length <= 1){
            System.err.println("Please provide the port and doc root!");
        }
        else if (args.length > 2){
            System.err.println("Too many arguments provided!");
        }

        Integer port = Integer.parseInt(args[0]);
        String docRoot = args[1];

        ServerSocket server = new ServerSocket(port);

        ExecutorService thrPool = Executors.newFixedThreadPool(2);

        while (true){
            System.out.println("Waiting for client connection...");
            Socket client = server.accept();
            System.out.println("Client connection established!");

            Runnable handler = new ClientHandler(client, docRoot);
            thrPool.submit(handler);
        }
    }
}
