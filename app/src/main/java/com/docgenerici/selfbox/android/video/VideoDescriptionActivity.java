package com.docgenerici.selfbox.android.video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.MainContentPresenter;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.ContentShared;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.persistence.ItemShared;

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
    @BindView(R.id.btShare)
    Button btShare;
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.btHelp)
    Button btHelp;
    @BindView(R.id.ivType)
    ImageView ivType;
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
    private boolean canShare;
    private String typeContent;
    private ContentShared contentDoc;
    private boolean training;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_description);
        ButterKnife.bind(this);
        MainContentPresenter presenter = SelfBoxApplicationImpl.appComponent.mainContentPresenter();
        changeStatusBar(presenter.getContentDarkColor());
        rlToolbar.setBackgroundColor(presenter.getContentColor());
        btHelp.setBackground(presenter.getBackGroundhelp());
        category= presenter.getCategory();
        showHideController(true);

        if (getIntent() != null) {
            id= getIntent().getIntExtra("id",0);
           if(id > 0){
               Realm realm= SelfBoxApplicationImpl.appComponent.realm();
               ContentBox contentBox= realm.where(ContentBox.class).equalTo("id", id).findFirst();
               pathVideo= contentBox.getLocalfilePath();
               pathVideo= pathVideo.replace("file://", "");
               canShare = getIntent().getBooleanExtra("canShare", false);
               training = getIntent().getBooleanExtra("training", false);
               typeContent = getIntent().getStringExtra("type");
               contentDoc = (ContentShared) getIntent().getParcelableExtra("contentSelect");
               if(pathVideo.startsWith("file://")){
                   pathVideo= pathVideo.replace("file://", "");
               }
               if(!canShare){
                   btShare.setVisibility(View.GONE);
               }
               tvTitle.setText(contentBox.name);
               tvDescription.setText(contentBox.descrFull);
               setupShare();
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
        setTypeIcon(category);

    }


    private void setTypeIcon(String category) {
        switch (category){
            case "isf":
                ivType.setImageResource(R.drawable.isf_white);
                break;

            case "medico":
                if(training){
                    ivType.setImageResource(R.drawable.medico_grey);
                }else{
                    ivType.setImageResource(R.drawable.medico_white);
                }
                break;
            case "pharma":
                if(training){
                    ivType.setImageResource(R.drawable.pharma_grey);
                }else{
                    ivType.setImageResource(R.drawable.pharma_white);
                }
                break;
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

    private void changeStatusBar(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    private void setupShare() {
        if(contentDoc !=null){
            String id= String.valueOf(contentDoc.id);
            Realm realm= SelfBoxApplicationImpl.appComponent.realm();
            ItemShared itemShared = realm.where(ItemShared.class).equalTo("id", id).findFirst();
            if(itemShared !=null){
                btShare.setBackgroundResource(R.drawable.ic_share_red);
            }else{
                btShare.setBackgroundResource(R.drawable.ic_share_white);

            }
        }
    }

    @OnClick(R.id.btShare)
    void addOrDeleteShare(){
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        if(contentDoc !=null) {
            String id = String.valueOf(contentDoc.id);

            final ItemShared sharedItem = realm.where(ItemShared.class).equalTo("id", id).findFirst();
            if (sharedItem != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        sharedItem.deleteFromRealm();
                    }
                });
            } else {
                try {

                    realm.beginTransaction();

                    ItemShared newSharedItem = new ItemShared();
                    newSharedItem.setId(id);
                    newSharedItem.setName(contentDoc.name);
                    newSharedItem.setType("content");
                    realm.copyToRealmOrUpdate(newSharedItem);
                } catch (Exception ex) {

                } finally {
                    realm.commitTransaction();
                }
            }
        }
        setupShare();

    }

}
