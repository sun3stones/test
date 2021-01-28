package com.lei.io.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
/**
 * 命令：nc localhost 8001
 */
public class NIODemo {

    public static void main(String[] args) throws IOException {
        //服务初始化
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        //设置为非阻塞
        serverSocket.configureBlocking(false);
        //绑定端口
        serverSocket.bind(new InetSocketAddress("localhost", 8001));
        //注册OP_ACCEPT事件（即监听该事件，如果有客户端发来连接请求，则该键在select()后被选中）
        Selector selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("nio服务端已启动,等待连接...");
        while (true) {
            //选择准备好的事件
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            //处理准备就绪的事件
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //删除当前键，避免重复消费
                iterator.remove();
                if (key.isAcceptable()) {//连接
                    //接收请求
                    SocketChannel socket = serverSocket.accept();
                    socket.configureBlocking(false);
                    //注册read，监听客户端发送的消息
                    socket.register(selector, SelectionKey.OP_READ);
                    InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                    System.out.println("nio服务端建立连接：" + address.getPort());
                } else if (key.isReadable()) {//读取
                    SocketChannel socket = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    byte[] data = new byte[1024];
                    //读取缓存
                    int read = socket.read(buffer);
                    buffer.flip();
                    buffer.get(data, 0, read);
                    InetSocketAddress address = (InetSocketAddress) socket.getRemoteAddress();
                    System.out.println("nio服务端" + address.getPort() + "读取到数据：" + new String(data, 0, read));
                }
            }
        }
    }
}