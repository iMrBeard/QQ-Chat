package client;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.sql.ClientInfoStatus;

public class ChatUI extends JFrame {

    private JTextArea messageTextArea;

    private JTextArea sendArea;

    public ChatUI(Socket client, String friend,PrintWriter printWriter,BufferedReader bufferedReader) throws IOException {

        JPanel messagePanel = new JPanel();
        JPanel sendPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        messagePanel.setBounds(0,0,700, 300);
        messagePanel.setLayout(new BorderLayout());
        sendPanel.setBounds(0,325,700, 275);
        sendPanel.setLayout(new BorderLayout());
        buttonPanel.setBounds(0,300, 700, 25);
        buttonPanel.setBackground(new Color(248,248,248));
        buttonPanel.setLayout(null);



        //消息面板
        this.messageTextArea = new JTextArea();
        messageTextArea.setVisible(true);
        messageTextArea.setEditable(false);
        messagePanel.add(messageTextArea,BorderLayout.CENTER);

        //发送面板
        this.sendArea = new JTextArea();
        sendArea.setVisible(true);
        sendArea.setEditable(true);
        sendPanel.add(sendArea,BorderLayout.CENTER);

        //发送按钮
        JButton sendButton = new JButton("发送");
        JButton fileButton = new JButton("文件");
        sendButton.addActionListener(e ->{
            if(!sendArea.getText().equals("")){
                printWriter.println(friend+": "+sendArea.getText());
                messageTextArea.append("我发给"+friend+": "+sendArea.getText()+"\n");
                sendArea.setText("");
            }
        });
        sendButton.setBounds(500, 0, 80, 25);
        fileButton.setBounds(610, 0, 80, 25);
        buttonPanel.add(sendButton);
        buttonPanel.add(fileButton);


        //初始化frame,踩坑，先要添加内部组件再设置底层frame为可见，顺序不能乱
        this.setLayout(null);
        this.add(sendPanel);
        this.add(messagePanel);
        this.add(buttonPanel);
        this.setVisible(true);
        this.setTitle(friend);
        setSize(700, 600);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printWriter.println(friend+": "+"聊天窗口关闭");
            }
        });

    }

    public JTextArea getMessageText(){
        return this.messageTextArea;
    }

}
