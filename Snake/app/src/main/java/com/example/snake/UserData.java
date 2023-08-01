package com.example.snake;

public class UserData {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private int userId;
    private int score;//用户ID号
    public int pwdresetFlag=0;
    public String getUserName() {             //获取用户名
        return userName;
    }
    //设置用户名
    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }
    //获取用户密码
    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }
    //设置用户密码
    public void setUserPwd(String userPwd) {     //输入用户密码
        this.userPwd = userPwd;
    }
    //获取用户id
    public int getUserId() {                   //获取用户ID号
        return userId;
    }
    //设置用户id
    public void setUserId(int userId) {       //设置用户ID号
        this.userId = userId;
    }
    //获取用户id

    public int getUserScore() {                   //获取用户ID号
        return score;
    }
    //设置用户id
    public void setUserScore(int score) {       //设置用户ID号
        this.score = score;
    }
    public UserData(String userName, String userPwd) {  //这里只采用用户名和密码
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }
    public UserData(String userName, int  userscore) {  //这里只采用用户名和分数
        super();
        this.userName = userName;
        this.score = userscore;
    }
}
