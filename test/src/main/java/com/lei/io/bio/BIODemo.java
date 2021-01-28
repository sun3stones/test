package nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * 命令：nc localhost 8000
 */
public class BIODemo {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("bio服务端已启动,等待连接...");
        test2(serverSocket);
    }

    /**
     * 最基础的版本，accept()阻塞等待建立连接然后读数据,建立连接后线程被占用
     * @param serverSocket
     * @throws IOException
     */
    private static void test1(ServerSocket serverSocket) throws IOException {
        byte[] buff = new byte[1024];
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("bio服务端建立连接：" + socket.getPort());
            InputStream inputStream = socket.getInputStream();
            inputStream.read(buff);
            System.out.println("bio服务端收到数据：" + new String(buff));
        }
    }

    /**
     * 多线程的版本，accept()阻塞等待建立连接然后读数据，每次有新的连接进来开启新的线程
     * @param serverSocket
     * @throws IOException
     */
    private static void test2(ServerSocket serverSocket) throws IOException {
        while (true){
            Socket socket = serverSocket.accept();
            Executors.newCachedThreadPool().execute(() -> {
                System.out.println("bio服务端建立连接：" + socket.getPort());
                byte[] buff = new byte[1024];
                while (true){
                    try {
                        InputStream inputStream = socket.getInputStream();
                        inputStream.read(buff);
                        System.out.println("bio服务端" + socket.getPort() + "收到数据：" + new String(buff));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}