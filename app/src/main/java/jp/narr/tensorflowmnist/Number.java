package jp.narr.tensorflowmnist;

import android.media.MediaPlayer;

/**
 * Created by RITIKA on 09-02-2017.
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
