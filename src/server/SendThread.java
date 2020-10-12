package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SendThread extends Thread {

    private Socket client;

    private PrintWriter printWriter;

    private BufferedReader bufferedReader;

    private String username;

    public SendThread(Socket client, PrintWriter printWriter,BufferedReader bufferedReader,String username) throws IOException {
        this.client = client;
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.username = username;
    }

    @Override
    public void run() {
        while (true){
            try {
                Map<String,Socket> clientMap = SingleTalkServer.getClientMap();
                String message = bufferedReader.readLine();
                if (message != null && !"".equals(message)){
                    if ("下线".equals(message)){
                        printWriter.close();
                        bufferedReader.close();
                        client.close();
                        clientMap.remove(username);
                        System.out.println(username+"客户端下线");
                        break;
                    }
                    String[] split = message.split(": ");
                    String friend = split[0];
                    if (split[1]!= null && "聊天窗口关闭".equals(split[1])){
                        printWriter.println(message);
                        continue;
                    }
                    if (clientMap.containsKey(friend)) {
                        Socket friendClient = clientMap.get(friend);
                        PrintWriter friendPrintWriter = new PrintWriter(friendClient.getOutputStream(),true);
                        friendPrintWriter.println(username+": "+split[1]);
                        System.out.println("======= 消息发送成功 ======="+username+" to "+friend+split[1]);
                    } else {
                        System.out.println("====== 好友不在线 ======");
                        printWriter.println(friend+": "+"不在线，消息无法传达");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
