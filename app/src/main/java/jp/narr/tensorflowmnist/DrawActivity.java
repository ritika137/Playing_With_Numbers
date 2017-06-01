/*
   Copyright 2016 Narrative Nights Inc. All Rights Reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package jp.narr.tensorflowmnist;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.duration;

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "DrawActivity";

    private static final int PIXEL_WIDTH = 28;
    int num;
    final float growTo = 31.0f;
    final long duration = 20;
    TextView textView;
    MediaPlayer mediaPlayer;
    //private TextView mResultText;

    private float mLastX;
    private float mLastY;

    private DrawModel mModel;
    private DrawView mDrawView;

    private PointF mTmpPiont = new PointF();

    private DigitDetector mDetector = new DigitDetector();


    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity);


        // mDrawView.setB

        boolean ret = mDetector.setup(this);
        if( !ret ) {
            Log.i(TAG, "Detector setup failed");
            return;
        }

        mModel = new DrawModel(PIXEL_WIDTH, PIXEL_WIDTH);
        //textView=(TextView)findViewById(R.id.numTextView);

        //Toast.makeText(this, Integer.toString(MainActivity.rightNumber), Toast.LENGTH_SHORT).show();
//        textView.setText(Integer.toString(MainActivity.rightNumber));
//        textView.setVisibility(View.VISIBLE);

        mDrawView = (DrawView) findViewById(R.id.view_draw);
        mDrawView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        mDrawView.setModel(mModel);
        mDrawView.setOnTouchListener(this);


        // View detectButton = findViewById(R.id.button_detect);
        //detectButton.setOnClickListener(new View.OnClickListener() {

          /*  public void onClick(View v) {
                onDetectClicked();
            }
        });*/

        //  View clearButton = findViewById(R.id.button_clear);
/*        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearClicked();
            }
        });*/

        //mResultText = (TextView)findViewById(R.id.text_result);
        set();

    }
    public void set(){

        textView=(TextView)findViewById(R.id.numTextView);
        textView.bringToFront();
        Toast.makeText(this, Integer.toString(MainActivity.rightNumber), Toast.LENGTH_SHORT).show();
        textView.setText(Integer.toString(MainActivity.rightNumber));
        textView.setVisibility(View.VISIBLE);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", growTo);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", growTo);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(textView, pvhX, pvhY);
        animator.setDuration(duration * 2);
        animator.start();

        //To set the sound to draw particular number

        mediaPlayer=new MediaPlayer();
        if(MainActivity.rightNumber==0){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw0);
        }
        else if(MainActivity.rightNumber==1){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw1);
        }
        else if(MainActivity.rightNumber==2){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw2);
        }
        else if(MainActivity.rightNumber==3){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw3);
        }
        else if(MainActivity.rightNumber==4){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw4);
        }
        else if(MainActivity.rightNumber==5){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw5);
        }
        else if(MainActivity.rightNumber==6){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw6);
        }
        else if(MainActivity.rightNumber==7){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw7);
        }
        else if(MainActivity.rightNumber==8){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw8);
        }
        else{
            mediaPlayer=MediaPlayer.create(this,R.raw.draw9);
        }



        Handler handler = new Handler();
        //To delay the draw sound
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){

                //Integer digit = 100;
                //Toast.makeText(DrawActivity.this, Integer.toString(digit), Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
            }
        }, duration );

        //Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                onDetectClicked();
//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, duration * 500);
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                //onDetectClicked();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, duration * 700);

    }

    @Override
    protected void onResume() {
        mDrawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDrawView.onPause();
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_DOWN) {
            processTouchDown(event);
            return true;

        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;

        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp();
            return true;
        }
        return false;
    }

    private void processTouchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
        mDrawView.calcPos(mLastX, mLastY, mTmpPiont);
        float lastConvX = mTmpPiont.x;
        float lastConvY = mTmpPiont.y;
        mModel.startLine(lastConvX, lastConvY);
    }

    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        mDrawView.calcPos(x, y, mTmpPiont);
        float newConvX = mTmpPiont.x;
        float newConvY = mTmpPiont.y;
        mModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        mDrawView.invalidate();
    }

    private void processTouchUp() {
        mModel.endLine();
    }

    private void onDetectClicked() {
        int pixels[] = mDrawView.getPixelData();
        int digit = mDetector.detectDigit(pixels);

        Log.i(TAG, "digit =" + digit);

        //mResultText.setText("Detected = " + digit);
        Toast.makeText(this, Integer.toString(digit), Toast.LENGTH_SHORT).show();
        if(digit==MainActivity.rightNumber)
        {
            MediaPlayer correct = new MediaPlayer();
            correct=MediaPlayer.create(this,R.raw.diditright);
            correct.start();
        }
        else{
            MediaPlayer wrong = new MediaPlayer();
            wrong = MediaPlayer.create(this,R.raw.tryagain);
            wrong.start();

        }
    }

    private void onClearClicked() {
        mModel.clear();
        mDrawView.reset();
        mDrawView.invalidate();

        //mResultText.setText("");
    }
}
