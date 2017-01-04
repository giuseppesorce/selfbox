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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.contents.ContentBox;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class VideoDescriptionActivity extends AppCompatActivity implements View.OnTouchListener {

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.vProgressPlay)
    View vProgressPlay;
    @BindView(R.id.rlController)
    RelativeLayout rlController;
    @BindView(R.id.btPlayPause)
    Button btPlayPause;
    @BindView(R.id.btVolume)
    Button btVolume;
    @BindView(R.id.backControler)
    ImageView backControler;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

    private MediaController ctlr;
    private Handler mHandler = new Handler();
    private Timer timer = new Timer();
    private boolean controllerOn = false;
    private float pixelMoveHide = 1650.0f;
    private CountDownTimer hideCountDown;
    private String pathVideo;
    private int id;
    private MediaController mediaController;
    private MediaPlayer myMediaPlayer;
    private boolean hasVolume=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_description);
        ButterKnife.bind(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        showHideController(true);

        if (getIntent() != null) {
            id= getIntent().getIntExtra("id",0);
           if(id > 0){
               Realm realm= SelfBoxApplicationImpl.appComponent.realm();
               ContentBox contentBox= realm.where(ContentBox.class).equalTo("id", id).findFirst();
               pathVideo= contentBox.getLocalfilePath();
               pathVideo= pathVideo.replace("file://", "");
               if(pathVideo.startsWith("file://")){

               }
               tvTitle.setText(contentBox.name);
               tvDescription.setText(contentBox.descrFull);
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
                        myMediaPlayer= mp;
                        startTimer();
                        btVolume.setBackgroundResource(R.drawable.ic_volume3);
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

    @OnClick(R.id.btVolume)
    void changeVolume(){
        if(videoView !=null && myMediaPlayer !=null){
            hasVolume= !hasVolume;
            if(hasVolume){
                myMediaPlayer.setVolume(1f, 1f);
                btVolume.setBackgroundResource(R.drawable.ic_volume3);
            }else{

                myMediaPlayer.setVolume(0f, 0f);
                btVolume.setBackgroundResource(R.drawable.ic_volume0);
            }

        }
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
