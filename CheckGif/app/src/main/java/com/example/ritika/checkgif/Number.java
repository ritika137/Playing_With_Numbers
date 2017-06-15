package com.example.ritika.checkgif;

import android.media.MediaPlayer;

import static java.security.AccessController.getContext;

/**
 * Created by RITIKA on 13-01-2017.
 */

public class Number extends MediaPlayer {

    int number;
    MediaPlayer associatedVoice;

    Number(int n, int resId)
    {
        number = n;
        //associatedVoice = MediaPlayer.create(getContext(), resId);
    }
}
