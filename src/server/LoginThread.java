package server;

import client.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

public class LoginThread extends Thread {

    UserDao userDao = UserDao.getInstance();

    private Socket client;

    private PrintWriter printWriter;

    private BufferedReader loginReader;

    public LoginThread(Socket client) throws IOException {
        this.client = client;
        this.printWriter = new PrintWriter(client.getOutputStream(),true);
        this.loginReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public String login(String username,String password){
        User user = userDao.findUser(username);
        if (user == null){
            return "用户不存在";
        } else if (!user.getPassword().equals(password)){
            return "密码错误";
        } else {
            return "登录成功";
        }
    }

    @Override
    public void run() {
        String successMessage = null;
        while (true){
            try {
                String loginMessage = loginReader.readLine();
                if("取消登录".equals(loginMessage)){
                    System.out.println("======= 客户端取消登录 =======");
                    client.close();
                    printWriter.close();
                    loginReader.close();
                    break;
                }
                String[] info = loginMessage.split(",");
                String username = info[0];
                String password = info[1];
                successMessage = login(username, password);
                printWriter.println(successMessage);
                if ("登录成功".equals(successMessage)){
                    System.out.println(username);
                    System.out.println("======= 登录成功 =======");
                    SingleTalkServer.getClientMap().put(username, client);
                    new SendThread(client,printWriter,loginReader,username).start();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("====== 登录结束 =====");
    }
}
