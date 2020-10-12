package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginUI extends JFrame {

    //请求登录
    private Socket socket;

    //传输用户名密码
    PrintWriter printWriter;

    //获取登录信息
    BufferedReader bufferedReader;

    //用户名输入框
    private JTextField usernameField;

    //密码框
    private JPasswordField passwordField;

    //登录错误提示
    private JLabel errorMessageLabel;

    public LoginUI (Socket socket) throws IOException {

        //请求登录的socket,PrintWriter,BufferReader
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream(),true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        JPanel panel = new JPanel();
        panel.setLayout(null);

        /**
         * 用于显示登录失败信息
         */
        this.errorMessageLabel = new JLabel();
        errorMessageLabel.setBounds(150, 20, 150, 10);
        errorMessageLabel.setVisible(true);
        panel.add(errorMessageLabel);

        /**
         * 设置用户名输入提示信息
         */
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 40, 80, 25);
        panel.add(usernameLabel);

        /**
         * 用户名输入框
         */
        this.usernameField = new JTextField(20);
        usernameField.setBounds(100,40,165,25);
        panel.add(usernameField);

        /**
         * 设置密码输入提示信息
         */
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 70, 80, 25);
        panel.add(passwordLabel);

        /**
         * 密码输入框
         */
        this.passwordField = new JPasswordField(20);
        passwordField.setBounds(100,70,165,25);
        panel.add(passwordField);

        /**
         * 登录按钮
         */
        JButton loginButton = new JButton("login");
        loginButton.setBounds(50, 100, 80, 25);
        //添加事件监听器，调用登录方法，向服务器请求登录
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        this.add(panel);
        this.setSize(350, 200);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printWriter.println("取消登录");
            }
        });
    }

    private void login(){
        String serverMessage = null;
        StringBuilder message = new StringBuilder();
        message.append(usernameField.getText()+",");
        message.append(new String(passwordField.getPassword()));
        printWriter.println(message);
        try {
            serverMessage = bufferedReader.readLine();
            if(!"登录成功".equals(serverMessage)){
                errorMessageLabel.setText(serverMessage);
                errorMessageLabel.setVisible(true);
            } else {
                new FriendUI(usernameField.getText(),socket,printWriter,bufferedReader);
                this.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        EventQueue.invokeLater(()->{
//            LoginUI frame =new LoginUI();
//            frame.setTitle("QQ-Chat");
//            frame.setVisible(true);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        });
//    }

}
