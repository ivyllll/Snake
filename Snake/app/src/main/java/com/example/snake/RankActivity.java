package com.example.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import static com.example.snake.UserDatamannage.MODE;
import static com.example.snake.UserDatamannage.USER_SCORE;

public class RankActivity extends AppCompatActivity {
    ImageButton home;
    ListView lv;
    String[] keys = {"user_name","user_score","mode"};
    int[] ids = {R.id.username,R.id.userscore,R.id.usermode};
    private static final String TAG = "RankActivity";
    UserDatamannage userDatamannage;
    String mode2=" 'easy' ";
    private static final String[] m={"'easy'","'normal'","'hard'"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        spinner = findViewById(R.id.spinner);  //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,m);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置下拉列表的风格
        spinner.setAdapter(adapter);//将adapter 添加到spinner中
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode2 =m[position];
                setrank(mode2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mode2 ="'easy'";
                setrank(mode2);
            }
        });//添加事件Spinner事件监听
        spinner.setVisibility(View.VISIBLE);  //设置默认值
        lv = findViewById(R.id.lv);
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setrank(String mode){
        lv = findViewById(R.id.lv);
        userDatamannage = new UserDatamannage(RankActivity.this);
        SQLiteDatabase db = userDatamannage.getreadDataBase();
        //Cursor cur1 = db.rawQuery("Select * from score where "+MODE+ "= "+ mode + " order by "+USER_SCORE+"  desc limit 10",null);
        //Cursor cur =db.rawQuery("update score set"+" USER_SCORE= "+"6"+ "where MODE="+mode,null);
        Cursor cur = db.rawQuery("Select * from score where "+MODE+ "= "+ mode + " order by "+USER_SCORE+"  desc limit 10",null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(RankActivity.this,R.layout.list_item,cur,keys,ids);
        RankActivity.this.lv.setAdapter(adapter);
    }
}
