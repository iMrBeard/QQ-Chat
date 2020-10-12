package server;

import client.User;

import java.util.*;

/**
*@author: hxq
*@date: 2020/10/10 上午9:45
*@description: 模拟数据库，存储用户列表
*/
public class UserDao {

    //模拟数据库数据
    private Map<String, User> userMap;

    private static UserDao instance = new UserDao();

    private UserDao(){
        userMap = new HashMap<>();
        //存入用户数据，在这只存入两个用户
        User user1 = new User("user1","000");
        user1.setFriends(Arrays.asList("user2"));
        userMap.put(user1.getUsername(),user1 );
        User user2 = new User("user2","000");
        user2.setFriends(Arrays.asList("user1"));
        userMap.put(user2.getUsername(),user2 );
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    //判断用户是否存在
    public User findUser(String username){
        if (userMap.containsKey(username)){
            return userMap.get(username);
        } else {
            return null;
        }
    }


    //单例，数据库为同一个
    public static UserDao getInstance(){
        return instance;
    }



}
