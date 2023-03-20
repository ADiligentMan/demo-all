package com.wang.peng.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
