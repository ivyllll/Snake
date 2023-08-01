package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class xueweiActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuewei);
        textView = findViewById(R.id.welcome);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
                Intent it = new Intent(xueweiActivity.this,MainActivity.class);
                startActivity(it);
            }
        });
    }
}
