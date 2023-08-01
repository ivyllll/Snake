package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class OutcomeActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView username,userscore,out_game,mode_outgame;
    private Button upload,restart;
    UserDatamannage userDatamannage;
    UserData user;
    String mode = "easy";
    @Override
    protected void onResume() {
        if (userDatamannage == null) {
            userDatamannage = new UserDatamannage(this);
            userDatamannage.openDataBase();
        }
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outcome);
        username = findViewById(R.id.username);
        userscore = findViewById(R.id.userscore);
        mode_outgame = findViewById(R.id.mode_outgame);
        Intent it = getIntent();
        int score   = it.getIntExtra("score",0);
        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        String name = sp.getString("USER_NAME",null);
        sp = getSharedPreferences("difficulty",MODE_PRIVATE);
        int mMoveDelay = sp.getInt("model",0);
        if(mMoveDelay == 200){
           mode = "normal";
        }

        else if(mMoveDelay == 100){
           mode = "hard";
        }
        username.setText(name);
        userscore.setText(String.valueOf(score));
        mode_outgame.setText(mode);
        user = new UserData(name,score);//用来上传分数的对象
        upload = findViewById(R.id.upload);
        restart = findViewById(R.id.game_restart);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatamannage.updateScore(user,mode);
                Toast.makeText(OutcomeActivity.this,"upload successfully",Toast.LENGTH_SHORT).show();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OutcomeActivity.this,GameActivity.class);
                startActivity(it);
            }
        });
        out_game = findViewById(R.id.outgame);
        out_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(OutcomeActivity.this,choice.class);
                startActivity(it);
            }
        });
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
