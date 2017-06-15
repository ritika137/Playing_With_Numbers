package com.example.ritika.checkgif;

import android.media.MediaPlayer;
import android.widget.TextView;

/**
 * Created by RITIKA on 13-01-2017.
 */

public class Balloon {

    public
    int position;
    //MediaPlayer associatedVoice;
    int numberOnBalloon;
    TextView textViewOnBalloon;

    Balloon(int pos, int number )
    {
        position = pos;
        numberOnBalloon = number;
    }

    void set(int pos, int number)
    {
        position = pos;
        numberOnBalloon = number;
    }
    int returnNumber()
    {
        return numberOnBalloon;
    }
    int returnPosition()
    {
        return position;
    }

}
