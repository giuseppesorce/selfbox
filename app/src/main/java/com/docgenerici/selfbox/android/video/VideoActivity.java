package com.docgenerici.selfbox.android.video;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.debug.Dbg;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity implements View.OnTouchListener {

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.vProgressPlay)
    View vProgressPlay;
    @BindView(R.id.rlController)
    RelativeLayout rlController;
    @BindView(R.id.btPlayPause)
    Button btPlayPause;
    @BindView(R.id.backControler)
    ImageView backControler;
    private MediaController ctlr;
    private Handler mHandler = new Handler();
    private Timer timer = new Timer();
    private boolean controllerOn = false;
    private float pixelMoveHide = 1650.0f;
    private CountDownTimer hideCountDown;
    private String pathVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_activiy);
        ButterKnife.bind(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        showHideController(true);

        if (getIntent() != null) {
            pathVideo= getIntent().getStringExtra("path");
            if(pathVideo.startsWith("file://")){
                pathVideo= pathVideo.replace("file://", "");
            }
        }
        if(pathVideo !=null && !pathVideo.isEmpty()){
            File file= new File(pathVideo);
            if(file.exists()){
                final Uri video = Uri.fromFile(new File(pathVideo));
                videoView.setVideoURI(video);
                videoView.requestFocus();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        startTimer();
                    }
                });
                //
                videoView.start();
                vProgressPlay.setPivotX(0);
                videoView.setOnTouchListener(this);
                backControler.setOnTouchListener(this);


                hideCountDown = new CountDownTimer(5000, 5000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        controllerOn = false;
                        showHideController(false);
                    }
                };

                btPlayPause.setBackgroundResource(R.drawable.ic_pause);

            }
        }

        Dbg.p("pathVideo: "+pathVideo);



    }


    @OnClick(R.id.btPlayPause)
    void playPauseController() {
        hideCountDown.cancel();
        hideCountDown.start();
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
                btPlayPause.setBackgroundResource(R.drawable.ic_play);
            } else {
                videoView.start();
              timer = new Timer();
                timer.scheduleAtFixedRate(new  MyTimer(), 100, 100);
                btPlayPause.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }


    private void startTimer() {

        timer.scheduleAtFixedRate(new  MyTimer(), 100, 100);
    }


   private class MyTimer extends  TimerTask {
        @Override
        public void run() {
            float percentage = videoView.getCurrentPosition() * 100 / videoView.getDuration();
            final float scaleX = percentage / 100;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    vProgressPlay.setScaleX(scaleX);
                    if (scaleX > 0) {
                        vProgressPlay.setVisibility(View.VISIBLE);

                    }
                    if (videoView.getCurrentPosition() > 0 && videoView.getDuration() > 0 && videoView.getCurrentPosition() >= videoView.getDuration()) {
                        timer.cancel();
                        btPlayPause.setBackgroundResource(R.drawable.ic_play);

                    }
                }
            });

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        }
    }


    @Override
    protected void onDestroy() {

        if (timer != null) {
            timer.cancel();

        }
        super.onDestroy();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.videoView) {

            float y = event.getY();
            Dbg.p("Y: " + y);
            if ( y< 900 ){
                    videoViewTouched();
            }else{
                if(!controllerOn){
                    videoViewTouched();
                }
            }
        }

        if (v.getId() == R.id.backControler) {
            boolean isMoving;
            float percentageTouchProgress;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    float x = event.getX();


                     percentageTouchProgress= event.getX() * 100 / v.getWidth();
                    moveTimeLine(percentageTouchProgress);
                    break;
                case MotionEvent.ACTION_MOVE:
                    isMoving = true;
                    Dbg.p("ACTION_MOVE " );
                    percentageTouchProgress= event.getX() * 100 / v.getWidth();
                    // implement your move codes
                    moveTimeLine(percentageTouchProgress);
                    break;


            }
        }
        return false;
    }

    private void moveTimeLine(float percentageTouchProgress) {
        if(videoView !=null){
            int duration= videoView.getDuration();
            int seek= (int)(duration * percentageTouchProgress/100);
            videoView.seekTo(seek);
        }
    }

    private void videoViewTouched() {
        controllerOn = !controllerOn;
        showHideController(false);
    }

    private void showHideController(boolean fast) {
        int duration = fast ? 0 : 400;
        if (controllerOn) {
            rlController.animate().translationY(0.0f).setDuration(duration);


        } else {
            rlController.animate().translationY(pixelMoveHide).setDuration(duration);
        }
    }

}
