package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {

    private EditText et3,et4,et5;                        //用户名编 密码编辑 密码确认
    private Button SureButton,CancelButton;             //取消按钮、 确定按钮
    private UserDatamannage mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et3 = findViewById(R.id.register_username);
        et4 = findViewById(R.id.register_password1);
        et5 = findViewById(R.id.register_password2);

        SureButton = findViewById(R.id.sure);
        CancelButton = findViewById(R.id.cancel);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDatamannage(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

        SureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_check();
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Register_to_Login = new Intent(registerActivity.this,MainActivity.class);
                startActivity(intent_Register_to_Login);
                finish();
            }
        });
    }

    public void register_check() {
        if (isUserNameAndPwdValid()) {
            String userName = et3.getText().toString().trim();
            String userPwd = et4.getText().toString().trim();
            String userPwdCheck = et5.getText().toString().trim();
            //检查用户是否存在
            int count=mUserDataManager.findUserByName(userName);
            //用户已经存在时返回，给出提示文字
            if(count>0){
                Toast.makeText(this, "username already exists",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                Toast.makeText(this, "The password entered twice is different.",Toast.LENGTH_SHORT).show();
                return ;
            } else {
                UserData mUser = new UserData(userName, userPwd);
                mUserDataManager.openDataBase();
                long flag = mUserDataManager.insertUserData(mUser); //新建用户信息
                if (flag == -1) {
                    Toast.makeText(this, "failed to create new user",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "create new user successfully",Toast.LENGTH_SHORT).show();
                    Intent intent_Register_to_Login = new Intent(registerActivity.this,MainActivity.class);    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                }
            }
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (et3.getText().toString().trim().equals("")) {
            Toast.makeText(this,"username empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et4.getText().toString().trim().equals("")) {
            Toast.makeText(this, "password empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(et5.getText().toString().trim().equals("")) {
            Toast.makeText(this,"confirmed password empty" , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
