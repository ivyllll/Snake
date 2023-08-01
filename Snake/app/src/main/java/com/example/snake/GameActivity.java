package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.snake.Snakespace.PAUSE;
import static com.example.snake.Snakespace.QUIT;
import static com.example.snake.Snakespace.RUNNING;
import static com.example.snake.Snakespace.mDirection;
import static com.example.snake.Snakespace.mNextDirection;
import static com.example.snake.Snakespace.mMode;
import static com.example.snake.Snakespace.mMoveDelay;
import static com.example.snake.Snakespace.textView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ICICLE_KEY = "aa";
    ImageButton pause;
    private  Snakespace snakeview;
    private ImageButton mLeft,mRight,mUp,mDown,out;

    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int RIGHT = 3;
    private static final int LEFT = 4;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        snakeview = findViewById(R.id.snakeview);
        sp = getSharedPreferences("difficulty",MODE_PRIVATE);
        mMoveDelay = sp.getInt("model",500);
        mMode = 2;
        textView = findViewById(R.id.showscore);
        //方向键控制移动
        mLeft = findViewById(R.id.left);
        mRight = findViewById(R.id.right);
        mUp = findViewById(R.id.up);
        mDown = findViewById(R.id.down);
        mLeft.setOnClickListener(this);
        mUp.setOnClickListener(this);
        mRight.setOnClickListener(this);
        mDown.setOnClickListener(this);


        //点击暂停弹出对话框
        pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = 1;
              mydialog();
            }
           });

    }

    public  void gameover(){
        Intent it = new Intent(GameActivity.this,MainActivity.class);
        startActivity(it);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            // 使界面上的方向按钮起作用
            case R.id.left:
                if (mDirection != RIGHT) {
                    mNextDirection = LEFT;
                }
                break;
            case R.id.right:
                if (mDirection != LEFT) {
                    mNextDirection = RIGHT;
                }
                break;
            case R.id.up:
                if (mDirection != DOWN) {
                    mNextDirection = UP;
                }
                break;
            case R.id.down:
                if (mDirection != UP) {
                    mNextDirection = DOWN;
                }
                break;
            default:
                break;
        }



    }


    private void mydialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);//点击外部 dialog 消失
        dialog.setContentView(R.layout.dialog);//添加自定义布局
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//设置背景透明
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.CENTER);
        //继续游戏
        final ImageButton start = dialog.findViewById(R.id.game_start);
        start.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              snakeview.setMode(RUNNING);
              Toast.makeText(GameActivity.this, "Continue", Toast.LENGTH_SHORT).show();
              snakeview.update();
              dialog.dismiss();
          }
        });

        //重新开始游戏
        ImageButton restart = dialog.findViewById(R.id.game_restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(GameActivity.this, "Restart", Toast.LENGTH_SHORT).show();
               restart(dialog);
            }
        });
        //停止游戏
        ImageButton over = dialog.findViewById(R.id.game_over);
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeview.setMode(QUIT);
                dialog.dismiss();
                Toast.makeText(GameActivity.this, "Quit", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(GameActivity.this,choice.class);
                startActivity(it);
            }
        });
    }
    public void restart(Dialog dialog){

        mMoveDelay=sp.getInt("model",0);
        Log.w("nandu",String.valueOf(mMoveDelay));
        snakeview.initNewGame();
        snakeview.setMode(RUNNING);
        snakeview.update();
        dialog.dismiss();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //保存游戏状态
        super.onSaveInstanceState(outState);
        outState.putBundle(ICICLE_KEY, snakeview.saveState());
    }
}
