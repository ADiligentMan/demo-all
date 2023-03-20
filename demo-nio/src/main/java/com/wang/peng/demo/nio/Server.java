package com.wang.peng.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Hello world!
 */
public class Server {
    public static void main(String[] args) {
        try {
            Selector open = Selector.open();
            ServerSocketChannel channel = ServerSocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(3333);
            channel.configureBlocking(false);
            channel.bind(socketAddress);
            SelectionKey selectionKey = channel.register(open, SelectionKey.OP_ACCEPT | SelectionKey.OP_READ);
            int select = open.select();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
