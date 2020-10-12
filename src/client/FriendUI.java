package client;

import server.UserDao;

import javax.print.attribute.standard.JobOriginatingUserName;
import javax.swing.*;
import javax.swing.text.TabExpander;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendUI extends JFrame {

    private UserDao userDao = UserDao.getInstance();

    public FriendUI(String username, Socket client, PrintWriter printWriter, BufferedReader bufferedReader) throws IOException {

        super(username+"的聊天栏");
        HashMap<String,ChatUI> chatMap = new HashMap<>();

        //初始化数据
        User user = userDao.findUser(username);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printWriter.println("下线");
                System.exit(0);
            }
        });

        //初始化frame
        this.setSize(300, 640);
        this.setVisible(true);

        JPanel panel =new JPanel();
        panel.setVisible(true);
        this.add(panel);

        //添加组件
        List<String> friends = user.getFriends();
        panel.setLayout(new GridLayout(8,1));
        for (String friend : friends){
            JButton button = new JButton(friend);
            button.setBounds(0, 0, 300, 80);
            button.setVisible(true);
            button.addActionListener(e -> {
                try {
                    if (!chatMap.containsKey(friend)) {
                        chatMap.put(friend, new ChatUI(client, friend, printWriter, bufferedReader));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("======== 聊天窗口创建失败 ========");
                }
            });
            panel.add(button);
        }

        new Receive(client,printWriter,chatMap, bufferedReader).start();


    }

}
