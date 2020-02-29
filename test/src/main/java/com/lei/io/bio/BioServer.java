package com.lei.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    private static ExecutorService es = Executors.newCachedThreadPool();

    private static class HandleMsg implements Runnable{
        Socket socket;
        public HandleMsg(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream(),true);
                String bufString = null;
                while ((bufString = br.readLine()) != null){
                    System.out.println("接收消息："+bufString);
                    pw.println(bufString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    pw.close();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket clientSocket = null;
        while (true){
            System.out.println("等待连接···");
            clientSocket = serverSocket.accept();
            System.out.println("客户端连接："+clientSocket.getRemoteSocketAddress());
            es.execute(new HandleMsg(clientSocket));
        }
    }

}