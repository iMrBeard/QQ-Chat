package server;

import client.User;
import contant.ChatConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
*@author: hxq
*@date: 2020/10/9 下午9:23
*@description: 服务端
*/
public class SingleTalkServer {

    //服务端socket
    ServerSocket serverSocket;

    //在线客户端socket
    private static Map<String, Socket> clientMap;

    public SingleTalkServer() throws IOException {
        this.serverSocket = new ServerSocket(ChatConstant.serverPort);
        clientMap = new HashMap<>();
    }

    public static Map<String,Socket> getClientMap(){
        return clientMap;
    }

    public static void main(String[] args) throws IOException {
        SingleTalkServer singleTalkServer = new SingleTalkServer();
        System.out.println("======= 等待客户端连接 =======");
        while (true){
            Socket client = singleTalkServer.serverSocket.accept();
            System.out.println("======= 客户端连接,开始登录 =======");
            new LoginThread(client).start();
        }
    }

}
