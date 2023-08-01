package com.example.snake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDatamannage {
    //一些宏定义和声明
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "user_data";
    public static final String ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PWD = "user_pwd";
    public static final String USER_SCORE ="user_score";
    public static final String MODE ="mode";
    private static final int DB_VERSION = 2;
    private Context mContext = null;

    //创建用户数据表
    private static final String DB_CREATE = "CREATE TABLE  users  ("
            + ID + " integer primary key," + USER_NAME + " varchar,"
            + USER_PWD + " varchar"+")";
    private static final  String DB_CREATE2 = "CREATE TABLE  score  ("
            + ID + " integer primary key," + USER_NAME + " varchar," +USER_SCORE + " int," + MODE+ " varchar"+")";
    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;
    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql1 = "insert into score values(null,null,null,'easy')";
            String sql2 = "insert into score values(null,null,null,'normal')";
            String sql3 = "insert into score values(null,null,null,'hard')";
            db.execSQL("DROP TABLE IF EXISTS users ");
            db.execSQL(DB_CREATE);
            db.execSQL("DROP TABLE IF EXISTS score ");
            db.execSQL(DB_CREATE2);
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.execSQL(sql3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public UserDatamannage(Context context) {
        mContext = context;
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    public SQLiteDatabase getreadDataBase() throws SQLException{
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        return mSQLiteDatabase;
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }
    //添加新用户，即注册
    public long insertUserData(UserData userData) {
        String userName=userData.getUserName();
        String userPwd=userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, userName);
        values.put(USER_PWD, userPwd);
        return mSQLiteDatabase.insert("users", ID, values);
    }
    //更新分数
    public void updateScore(UserData userData,String  mode) {
        String userName = userData.getUserName();
        int userSCORE = userData.getUserScore();
        String name = null;
        if(userSCORE<5){
            name=userName+" lv1";
        }
        if(userSCORE>=5 && userSCORE<10){
            name=userName+" lv2";
        }
        if(userSCORE<15 && userSCORE>=10){
            name=userName+" lv3";
        }
        String Query = "Select * from score where USER_NAME =? and MODE = ?";
        Cursor cursor = mSQLiteDatabase.rawQuery(Query, new String[]{userName, mode});

            if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put(USER_SCORE, userSCORE);
                //if ()
                mSQLiteDatabase.update("score", values, USER_NAME + "= ? " + "and " + MODE + " = ?", new String[]{userName, mode});
            }
            else{
                ContentValues values = new ContentValues();
                values.put(USER_NAME, name);
                values.put(USER_SCORE, userSCORE);
                values.put(MODE,mode);
                mSQLiteDatabase.insert("score",null, values);
            }


    }
    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(String userName){

        int result=0;
        String Query = "Select * from users where USER_NAME =?";
        Cursor cursor = mSQLiteDatabase.rawQuery(Query,new String[] { userName });
        if(cursor!=null){
            result=cursor.getCount();
            Log.w("result=",String.valueOf(result));
            cursor.close();
        }
        return result;
    }
    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(String userName,String pwd){
        int result=0;
        String Query = "Select * from users where USER_NAME =? and USER_PWD =?";
        Cursor mCursor = mSQLiteDatabase.rawQuery(Query,new String[] { userName,pwd});
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
        }
        return result;
    }
}

