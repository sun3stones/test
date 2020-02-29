package com.lei.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioClient {

    private static class Client implements Runnable{

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                Socket client = new Socket();
                client.connect(new InetSocketAddress(8000));
                writer = new PrintWriter(client.getOutputStream(),true);
                writer.println(Thread.currentThread().getName()+":hello!");
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("服务端回传消息："+reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    writer.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        while (true) {
            for (int i = 0; i < 10; i++) {
                es.execute(new Client());
            }
            Thread.sleep(10000);
        }

    }
}
