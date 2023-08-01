package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class choice extends AppCompatActivity {
    Button begin,setmodel,setting,out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        begin = findViewById(R.id.Begin);
        setmodel = findViewById(R.id.SetModel);
        setting = findViewById(R.id.setting);
        out = findViewById(R.id.about);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chocie_to_Game = new Intent(choice.this,GameActivity.class);
                startActivity(chocie_to_Game);
            }
        });
        setmodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choice_to_Rank = new Intent(choice.this,RankActivity.class);
                startActivity(choice_to_Rank);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choice_to_setting = new Intent(choice.this,SettingActivity.class);
                startActivity(choice_to_setting);
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(choice.this,MainActivity.class);
                Intent it1 = new Intent(choice.this,SnakeService.class);
                startActivity(it);
                stopService(it1);
            }
        });

    }
}
