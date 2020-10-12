package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Receive extends Thread {

    private HashMap<String,ChatUI> chatMap;

    private BufferedReader bufferedReader;

    private Socket client;

    private PrintWriter printWriter;

    public Receive(Socket client,PrintWriter printWriter,HashMap<String,ChatUI> chatMap, BufferedReader bufferedReader){
        this.client = client;
        this.printWriter = printWriter;
        this.chatMap = chatMap;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        while (true){
            try {
                String message = bufferedReader.readLine();
                String[] split = message.split(": ");
                String friend = split[0];
                if(chatMap.containsKey(friend)){
                    if ("聊天窗口关闭".equals(split[1])) {
                        chatMap.remove(friend);
                    } else {
                        ChatUI chatUI = chatMap.get(friend);
                        JTextArea messageText = chatUI.getMessageText();
                        messageText.append(message+"\n");
                    }
                } else {
                    ChatUI chatUI = new ChatUI(client, friend, printWriter, bufferedReader);
                    chatUI.getMessageText().setText(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
