package client;

import contant.ChatConstant;

import java.io.IOException;
import java.net.Socket;

public class SingleTalkClient {

    public static void main(String[] args) throws IOException {
        Socket client = new Socket(ChatConstant.serverIP,ChatConstant.serverPort);
        LoginUI loginUI = new LoginUI(client);
    }
}
