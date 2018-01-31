package cafe.adriel.androidaudiorecorder.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.IOException;

import cafe.adriel.androidaudiorecorder.Activity.R;

public class AudioPlayerView extends LinearLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "AudioPlayerView";
    private static final String NO_AUDIO_DATA = "暂无音频资源";
    private static final String DEFAULT_TIME_TEXT = "00:00";
    private static final String RESOURCE_NOT_READY = "资源未准备好，请稍后重试";

    private TextView mTvTime;
    private SeekBar mSbProgress;
    private ImageView mIvIcon;

    private Context mContext;
    private Resources mResources;
    private Thread mTimeListenerThread;
    private MediaPlayer mMediaPlayer;

    private boolean mIsPlaying = false;
    private boolean mIsAbleToPlay = false;
    private boolean mIsDragging = false;
    private boolean mIsSettingData = false;

    private float mTextSize;
    private int mCurProgress;
    private int mInnerViewMargin;
    private int mPlayIconId;

    private android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            syncProgress();
        }
    };

    public AudioPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mResources = getResources();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        initView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioPlayerView);
        if (typedArray != null) {
            mTextSize = typedArray.getDimension(R.styleable.AudioPlayerView_timeTextSize, 10);
            mInnerViewMargin = (int) typedArray.getDimension(R.styleable.AudioPlayerView_innerViewMargin, 10);
            mPlayIconId = typedArray.getResourceId(R.styleable.AudioPlayerView_playIcon, 0);
            typedArray.recycle();
        }
    }

    private void initView() {
        mIvIcon = new ImageView(mContext);
        mTvTime = new TextView(mContext);
        mSbProgress = new SeekBar(mContext);

        setParams();
        setDefaultValue();
        setListener();

        addView(mIvIcon);
        addView(mTvTime);
        addView(mSbProgress);
    }

    private void setListener() {
        mIvIcon.setOnClickListener(this);
        mSbProgress.setOnSeekBarChangeListener(this);
    }

    private void setParams() {
        LayoutParams ivParams = new LayoutParams((int) (mResources.getDimension(R.dimen.audio_player_icon_size)),
                (int) (mResources.getDimension(R.dimen.audio_player_icon_size)));
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams sbParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        tvParams.leftMargin = mInnerViewMargin;
        sbParams.leftMargin = mInnerViewMargin;

        mIvIcon.setLayoutParams(ivParams);
        mTvTime.setLayoutParams(tvParams);
        mSbProgress.setLayoutParams(sbParams);
    }

    private void setDefaultValue() {
        mTvTime.setText(DEFAULT_TIME_TEXT);
        mIvIcon.setImageResource(mPlayIconId);
        mTvTime.setTextSize(mTextSize);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvIcon) {
            if (mIsPlaying) {
                stopPlayingAudio();
            } else {
                if (mIsAbleToPlay) {
                    startPlayingAudio();
                } else {
                    if (mIsSettingData) {
                        ToastUtil.showToast(mContext, RESOURCE_NOT_READY);
                    } else {
                        ToastUtil.showToast(mContext, NO_AUDIO_DATA);
                    }
                }
            }
        }
    }

    private void stopPlayingAudio() {
        mIsPlaying = false;
        mIvIcon.setSelected(false);

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void startPlayingAudio() {
        mIsPlaying = true;

        mIvIcon.setSelected(true);

        startTimeListener();

        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    private void startTimeListener() {
        if (mTimeListenerThread == null) {
            mTimeListenerThread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (mIsPlaying && !mIsDragging) {
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }
            };
            mTimeListenerThread.start();
        }
    }

    private void syncProgress() {
        mCurProgress = mMediaPlayer.getCurrentPosition();
        int second = mCurProgress / 1000;
        mTvTime.setText(formatTime(second));
        mSbProgress.setProgress(mCurProgress);
    }

    private String formatTime(int time) {
        StringBuilder timeStringBuilder = new StringBuilder();
        int min = time / 60;
        int second = time % 60;

        if (min < 10) {
            timeStringBuilder.append("0");
        }
        timeStringBuilder.append(min);
        timeStringBuilder.append(":");

        if (second < 10) {
            timeStringBuilder.append("0");
        }
        timeStringBuilder.append(second);

        return timeStringBuilder.toString();
    }

    public void setAudioPath(String audioPath) {
        Log.e(TAG, "audioPath : " + audioPath);
        if (TextUtils.isEmpty(audioPath)) {
            mIsSettingData = false;
            return;
        } else {
            mIsSettingData = true;
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        } else {
            resetState();
        }

        try {
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.prepareAsync();
            mIsAbleToPlay = false;
        } catch (IOException e) {
            mIsSettingData = false;
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsAbleToPlay = true;

        mSbProgress.setMax(mMediaPlayer.getDuration());  // 设置歌曲长度
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int second = progress / 1000;
            mTvTime.setText(formatTime(second));

            if (mIsSettingData) {
                mCurProgress = progress;
            } else {
                mCurProgress = 0;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStartTrackingTouch");
        mIsDragging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e(TAG, "onStopTrackingTouch");
        mIsDragging = false;

        mSbProgress.setProgress(mCurProgress);
        mMediaPlayer.seekTo(mCurProgress);
        mTvTime.setText(formatTime(mCurProgress / 1000));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        resetState();
    }

    public void resetState() {
        mIsPlaying = false;
        mTvTime.setText(DEFAULT_TIME_TEXT);
        mSbProgress.setProgress(0);
        mIvIcon.setSelected(false);
        mCurProgress = 0;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }
    }
}