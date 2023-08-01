package com.example.snake;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class SnakeService extends Service {
    MediaPlayer mediaPlayer;
    public SnakeService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        Log.w("started","onCreat");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int music = intent.getIntExtra("music",0);
        Log.w("music=",String.valueOf(music));
        switch (music){
            case 1:
                this.mediaPlayer = MediaPlayer.create(this,R.raw.music1);
                break;
            case 2:
                this.mediaPlayer = MediaPlayer.create(this,R.raw.music2);
                break;
            case 3:
                this.mediaPlayer = MediaPlayer.create(this,R.raw.music3);
                break;
            default:
                break;
        }
        this.mediaPlayer.seekTo(0);
        this.mediaPlayer.setLooping(true);
        this.mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mediaPlayer.stop();
    }
}
