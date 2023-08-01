package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.provider.Telephony.Carriers.PASSWORD;
import static com.example.snake.UserDatamannage.USER_NAME;

public class MainActivity extends AppCompatActivity {

    int pwdresetFlag=0;//用户id
    EditText et1, et2;//用户名编辑、密码编辑
    Button login, register;//登录按钮、注册按钮
    CheckBox checkBox,show;//记住密码 显示密码
    UserDatamannage userDatamannage;
    private SharedPreferences login_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=new Intent(MainActivity.this, qiantai.class);
        startService(intent);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.username);
        et2 = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        checkBox = findViewById(R.id.remember);
        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");//从sp中取出用户名
        String pwd =login_sp.getString("PASSWORD", "");//从sp中取出密码
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        Log.w("check",String.valueOf(choseRemember));
        if(choseRemember){
            et1.setText(name);
            et2.setText(pwd);
            checkBox.setChecked(true);
        }//如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        show= findViewById(R.id.show);
        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_to_register = new Intent(MainActivity.this,registerActivity.class);
                startActivity(login_to_register);
            }//点击注册按钮 跳转到登录界面
        });
    }

    public void login() {
        if(UsernameAndPwdExist()){//姓名和密码不为空
            String userName = et1.getText().toString().trim();
            String userPwd = et2.getText().toString().trim();//获取当前输入的用户名和密码
            SharedPreferences.Editor editor = login_sp.edit();
            int result = userDatamannage.findUserByNameAndPwd(userName,userPwd);//查看数据库中是否存在该用户 用户名是否正确
            if(result ==1 ){ //用户名和密码均正确
                //保存用户名到SharePerferences
                editor.putString("USER_NAME",userName);//将当前用户名放入sp
                editor.putString("PASSWORD",userPwd);//讲当前密码放入sp
                if(checkBox.isChecked()){
                    editor.putBoolean("mRememberCheck",true);
                }
                else{
                    editor.putBoolean("mRememberCheck",false);
                }//存放是否勾选记住密码的信息
                editor.apply();//提交editor信息

                Intent login_to_choice = new Intent(MainActivity.this,choice.class);
                startActivity(login_to_choice);
                finish();//登录成功
                Toast.makeText(this,"Login successfully",Toast.LENGTH_SHORT).show();
            }else if (result == 0){
                Toast.makeText(this,"Fail，please check the username and password!",Toast.LENGTH_SHORT).show();//登录失败提示
            }
        }
    }

    private boolean UsernameAndPwdExist() {
        if (et1.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Empty username", Toast.LENGTH_SHORT).show();
            return false;
        } else if
        (et2.getText().toString().trim().equals("")) {
            Toast.makeText(this,"Empty password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        if (userDatamannage == null) {
            userDatamannage = new UserDatamannage(this);
            userDatamannage.openDataBase();
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        if (userDatamannage != null) {
            userDatamannage.closeDataBase();
            userDatamannage = null;
        }
        super.onPause();
    }
}
