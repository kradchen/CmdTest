package com.company;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SocketNIOTester {

    private ThreadPoolExecutor executor;

    public void Test() {
        executor = new ThreadPoolExecutor(5,
                5,1000L, SECONDS, new LinkedBlockingQueue<>());
        getSocketServerThread(39999).start();
        try {
            Thread.sleep(100);

        for (int i=0 ; i<=10 ; i++) {
            int beginPort = 40000;
            getSocketClientThread(beginPort+i, 39999).start();
            Thread.sleep(50);
        }
        Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Thread getSocketClientThread(int port, int remotePort)
    {
        return new Thread(()->{
            try {
                SocketChannel channel = SocketChannel.open();
                channel.socket().bind(new InetSocketAddress(port));
                channel.connect(new InetSocketAddress("127.0.0.1", remotePort));
                System.out.println("channel connected on port:"+port);
                if (channel.isConnected()) {
                    ByteBuffer buffer =  ByteBuffer.allocate(200);
                    buffer.clear();
                    String msg =  "Socket message from port:"+port;
                    buffer.put(msg.getBytes());
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                    System.out.println("channel send msg on port:"+port);
                    System.out.println("hold connection for a while!");
                    Thread.sleep(200);
                    channel.close();
                    System.out.println("channel from port:"+port
                            +" closed! "+channel.socket().isClosed());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {

            }

        });
    }
    public Thread getSocketServerThread(int port)
    {
        return new Thread(()->{
            try {
                ServerSocketChannel channel = ServerSocketChannel.open();
                channel.bind(new InetSocketAddress(port));
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Server begin listerning!");
                    SocketChannel socketChannel = channel.accept();
                    executor.execute(()->{
                        ByteBuffer buf = ByteBuffer.allocate(200);
                        buf.clear();
                        try {
                            String remotePort = socketChannel.getRemoteAddress().toString();
                            socketChannel.read(buf);
                            buf.flip();
                            Charset charset = Charset.forName("UTF-8");
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer charBuffer = decoder.decode(buf.asReadOnlyBuffer());
                            System.out.println("Server received message:"+charBuffer.toString());
                            socketChannel.close();
                            System.out.println("1.socket on port:"+remotePort+" is "+socketChannel.socket().isClosed());
                        } catch (IOException  e) {
                            e.printStackTrace();
                        }
                    });
                }
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }
}
