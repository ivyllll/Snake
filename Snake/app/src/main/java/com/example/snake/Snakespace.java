package com.example.snake;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;



class Snakespace extends TileView {
    static int mMoveDelay = 500;//延迟 用于调节蛇的移动速度
    private long mLastMove;

    private static final int snakehead= 1;//蛇头的标识符
    private static final int snakebody = 2;//蛇身的标识符
    private static final int wall = 3;//墙的标识符

    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int RIGHT = 3;
    private static final int LEFT = 4;
    static int mDirection = RIGHT;//方向
    static int mNextDirection = RIGHT;//下一方向
    // 这里把游戏界面分为5种状态，便于逻辑实现
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    public static final int QUIT = 4;
    public static int mMode = READY;
    public  int newMode;

    static TextView textView;
    int count = 0;

    private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>(); // 存储蛇的所有坐标的数组
    private ArrayList<Coordinate> mFoodList = new ArrayList<Coordinate>(); // 存储食物的所有坐标的数组
    private ArrayList<Coordinate> mObstacle = new ArrayList<>();//存储障碍物坐标

    public static int mScore = 0;//得分



    private static final Random RNG = new Random();//随机生成食物的坐标
    MyHandler handler=new MyHandler();//通过子线程通知主线程更新ui
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Snakespace.this.update();     //不断调用update()方法
            Snakespace.this.invalidate();  //请求重绘，不断调用ondraw()方法
        }
        //调用sleep后,在一段时间后再sendmessage进行UI更新
        public void sleep(int delayMillis) {
            this.removeMessages(0);     //清空消息队列
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    public Snakespace(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initNewGame();
    }

    public Snakespace(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        initNewGame();
    }

    public Snakespace(Context context) {
        super(context);
    }

    private void addRandomFood() {
        Coordinate newCoord = null;
        boolean found = false;
        while (!found) {
            int newX =1 +  RNG.nextInt(20);
            int newY =4 + RNG.nextInt(15);
            newCoord = new Coordinate(newX, newY);
            boolean collision = false;
            int snakelength = mSnakeTrail.size();
            //遍历snake, 看新添加的apple是否在snake体内, 如果是,重新生成坐标
            for (int index = 0; index < snakelength; index++) {
                if (mSnakeTrail.get(index).equals(newCoord)) {
                    collision = true;
                }
            }for (int index = 0; index < mObstacle.size(); index++) {
                if (mObstacle.get(index).equals(newCoord)) {
                    collision = true;
                }
            }

            found = !collision;
        }
        mFoodList.add(newCoord);//储存已产生坐标

    }

    //绘制边界
    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(wall, x, 2);
            setTile(wall, x, mYTileCount-1);
        }
        for (int y = 2; y < mYTileCount -1; y++) {
            setTile(wall, 0, y);
            setTile(wall, mXTileCount - 1, y);
        }
    }

    //更新蛇的轨迹
    private void updateSnake() {
        boolean growSnake = false;
        Coordinate head = mSnakeTrail.get(0);
        Coordinate newHead = new Coordinate(1, 1);

        mDirection = mNextDirection;
        switch (mDirection) {//蛇移动的方向
            case RIGHT: {
                newHead = new Coordinate(head.x + 1, head.y);
                break;
            }
            case LEFT: {
                newHead = new Coordinate(head.x - 1, head.y);
                break;
            }
            case UP: {
                newHead = new Coordinate(head.x, head.y - 1);
                break;
            }
            case DOWN: {
                newHead = new Coordinate(head.x, head.y + 1);
                break;
            }
        }
        //检测投是否撞墙
            if ((newHead.x < 1) || (newHead.y < 3) || (newHead.x > mXTileCount-2 )
                || (newHead.y > mYTileCount-2)) {
            setMode(LOSE);
            return;
        }
        //检测蛇头是否撞到自己
        int snakelength = mSnakeTrail.size();
        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
            Coordinate c = mSnakeTrail.get(snakeindex);
            if (c.equals(newHead)) {
                setMode(LOSE);
                return;
            }
        }
        //检测蛇头撞见障碍物
        for(int index = 0;index < mObstacle.size();index++ ){
            Coordinate c = mObstacle.get(index);
            if (c.equals(newHead)) {
                setMode(LOSE);
                return;
            }
        }
        //检测蛇是否吃到食物
        int foodcount = mFoodList.size();
        for (int foodindex = 0; foodindex < foodcount; foodindex++) {
            Coordinate c = mFoodList.get(foodindex);
            if (c.equals(newHead)) {
                mFoodList.remove(c);
                addRandomFood();
                mScore++;
                mMoveDelay *= 0.95;  //蛇每迟到一个食物，延时就会减少，蛇的速度就会加快
                growSnake = true;
                if(mMoveDelay<150) {
                    count++;
                    Log.w("count", "" + count);
                    if (count == 4) {
                        addrandomObstacle(count);
                        count = 0;
                    }
                }
            }
        }
        mSnakeTrail.add(0, newHead);
        if (!growSnake) {
            mSnakeTrail.remove(mSnakeTrail.size() - 1);
        }
        //添加蛇的图片
        int index = 0;
        for (Coordinate c : mSnakeTrail) {
            if (index == 0) {
                setTile(snakehead, c.x, c.y);
            } else {
                setTile(snakebody, c.x, c.y);
            }
            index++;
        }
    }

    //加载食物图片
    private void updateFood() {
        for (Coordinate c : mFoodList) {
            setTile(snakebody, c.x, c.y);
        }
    }

    //刷新界面
    public void update() {
        if (mMode == RUNNING) {
            long now = System.currentTimeMillis();
            if (now - mLastMove > mMoveDelay) {
                clearTiles();
                updateWalls();
                updateSnake();
                updateFood();
                updateObstacle();
                mLastMove = now;
            }
            textView.setText(String.valueOf(mScore));
            handler.sleep(mMoveDelay);
        }
    }

    //加载图片资源
    private void initSnakeView() {
        setFocusable(true);   //添加焦点
        Resources r = this.getContext().getResources();
        //添加几种不同的tile
        resetTiles(4);
        //从文件中加载图片
        loadTile(snakehead, r.getDrawable(R.drawable.head));
        loadTile(snakebody, r.getDrawable(R.drawable.body));
        loadTile(wall, r.getDrawable(R.drawable.wall));
        update();//用于更新食物和蛇的坐标
    }


    private void updateObstacle() {
        for (Coordinate c : mObstacle) {
            setTile(wall, c.x, c.y);
        }
    }


    private void addrandomObstacle(int count) {
        Coordinate newCoord = null;
       if(count ==4){
            int newX = 1+RNG.nextInt(28);//坐标0-22+1
            int newY = 3+RNG.nextInt(20);//坐标0-23+3
            newCoord = new Coordinate(newX, newY);
            //遍历snake, 看新添加的apple是否在snake体内, 如果是,重新生成坐标

        mObstacle.add(newCoord);//储存已产生坐标
    }}


    public  void setMode(int newMode) {
        this.newMode = newMode;
        int oldMode = mMode;
        mMode = newMode;
        if(newMode == RUNNING && oldMode != RUNNING){
            update();
            return;
        }
        if(mMode==LOSE){
            Intent it = new Intent(getContext(),OutcomeActivity.class);
            it.putExtra("score",mScore);
            getContext().startActivity(it);
        }
    }




    //初始化游戏
    public void initNewGame() {
        mSnakeTrail.clear();
        mFoodList.clear();
        mSnakeTrail.add(new Coordinate(8, 7));
        mSnakeTrail.add(new Coordinate(7, 7));
        mSnakeTrail.add(new Coordinate(6, 7));
        mSnakeTrail.add(new Coordinate(5, 7));
        mDirection = RIGHT;//蛇的方向 向右
        mNextDirection = RIGHT; // 这个变量必须初始化，不然每次游戏结束重新开始后，蛇初始的方向将不是向右，而是你游戏结束时蛇的方向，
        // 如果死的时候，蛇的方向向左，那么再次点击开始时会无法绘出蛇的图像
        addRandomFood();//添加食物
        mScore = 0;//得分

    }

    public void onDraw(Canvas canvas){//遍历地图 绘制游戏界面

        super.onDraw(canvas);
        Paint paint=new Paint();
        initSnakeView();
        //遍历地图绘制界面

        for (int x = 0; x < mXTileCount; x++) {//33
            for (int y = 0; y < mYTileCount; y++) {//32
                if (mTileGrid[x][y] > 0) {  // 被加了图片的点mTileGird是大于0的
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x * mTileSize, mYOffset + y * mTileSize, paint);
                }
            }
        }
    }

    //记录坐标的位置
    private class Coordinate {
        int x;
        int y;
        Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }
        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }
    }


    private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
        int count = cvec.size();
        int[] rawArray = new int[count * 2];
        for (int index = 0; index < count; index++) {
            Coordinate c = cvec.get(index);
            rawArray[2 * index] = c.x;
            rawArray[2 * index + 1] = c.y;
        }
        return rawArray;
    }

    //将当前所有的游戏数据全部保存
    public Bundle saveState() {
        Bundle map = new Bundle();
        map.putIntArray("mAppleList", coordArrayListToArray(mFoodList));
        map.putInt("mDirection", Integer.valueOf(mDirection));
        map.putInt("mNextDirection", Integer.valueOf(mNextDirection));
        map.putInt("mMoveDelay", Integer.valueOf(mMoveDelay));
        map.putLong("mScore", Long.valueOf(mScore));
        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));
        return map;
    }
    //是coordArrayListToArray()的逆过程，用来读取数组中的坐标数据
    private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
        ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();
        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 2) {
            Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
            coordArrayList.add(c);
        }
        return coordArrayList;
    }
    //saveState()的逆过程,用于恢复游戏数据
    public void restoreState(Bundle icicle) {
        setMode(PAUSE);
        mFoodList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
        mDirection = icicle.getInt("mDirection");
        mNextDirection = icicle.getInt("mNextDirection");
        mMoveDelay = icicle.getInt("mMoveDelay");
        mScore = icicle.getInt("mScore");
        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
    }
}