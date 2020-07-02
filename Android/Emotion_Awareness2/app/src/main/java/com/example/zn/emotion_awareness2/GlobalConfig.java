package com.example.zn.emotion_awareness2;

import android.media.AudioFormat;

public class GlobalConfig {
    /**
     * 採樣率，現在能夠保證在所有設備上的採樣率都是44100HZ，但其他的採樣率(22050,16000,11025)在一些設備也可以使用
     */
    public static final int SAMPLE_RATE_INHZ=8000;

    /**
     * 聲道數。CHANNEL_IN_MONO and CHANNEL_IN_STEREO.其中CHANNEL_IN_NOMO使可以保證所有設備都可以使用的。
     */
    public static  final  int CHANNEL_CONFIG= AudioFormat.CHANNEL_IN_MONO;
    /**
     * 返回的音頻數據的格式
     */
    public  static final int AUDIO_FORMAT=AudioFormat.ENCODING_PCM_16BIT;
}
