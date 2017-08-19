package com.awesome.fun.withNumbers;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.view.View.INVISIBLE;
import static com.awesome.fun.withNumbers.R.drawable.zero;

public class DrawActivity extends AppCompatActivity {
    int num;
    final float growTo = 31.0f;
    final long duration = 20;
    ImageView textView;
    String datapath="";
    MediaPlayer mediaPlayer;
    RelativeLayout activity_draw;
    Boolean createTouchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_draw);
        createTouchView=true;
        datapath = getFilesDir() + "/tesseract/";
        checkFile(new File(datapath + "tessdata/"));



        activity_draw=(RelativeLayout)findViewById(R.id.activity_draw);
        ImageView drawHint = (ImageView) findViewById(R.id.hintDraw);

        drawHint.setVisibility(INVISIBLE);
        textView=(ImageView) findViewById(R.id.numTextView);

            TouchEventView view = new TouchEventView(this, datapath, Integer.toString(Main2Activity.rightNumber), drawHint, textView);
            activity_draw.addView(view);

        //setContentView(new TouchEventView(this,null));
        set();



        //rightPosition=MainActivity.rightNumberPosition;

    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {

            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(){


        //Toast.makeText(this, Integer.toString(Main2Activity.rightNumber), Toast.LENGTH_SHORT).show();
        textView.setVisibility(View.VISIBLE);

        if(Main2Activity.rightNumber==0)
            textView.setImageDrawable(getDrawable(zero));
        else if (Main2Activity.rightNumber==1)
            textView.setImageResource(R.drawable.one);
        else if (Main2Activity.rightNumber==2)
            textView.setImageResource(R.drawable.two);
        else if (Main2Activity.rightNumber==3)
            textView.setImageResource(R.drawable.three);
        else if (Main2Activity.rightNumber==4)
            textView.setImageResource(R.drawable.four);
        else if (Main2Activity.rightNumber==5)
            textView.setImageResource(R.drawable.five);
        else if (Main2Activity.rightNumber==6)
            textView.setImageResource(R.drawable.six);
        else if (Main2Activity.rightNumber==7)
            textView.setImageResource(R.drawable.seven);
        else if (Main2Activity.rightNumber==8)
            textView.setImageResource(R.drawable.eight);
        else
        textView.setImageResource(R.drawable.nine);
        //textView.setImageDrawable(getDrawable(zero));

        //PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", growTo);
        //PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", growTo);
        //ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(textView, pvhX, pvhY);
        //animator.setDuration(duration * 2);
        //animator.start();

        //To set the sound to draw particular number
        mediaPlayer=new MediaPlayer();
        if(Main2Activity.rightNumber==0){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw0);
        }
        else if(Main2Activity.rightNumber==1){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw1);
        }
        else if(Main2Activity.rightNumber==2){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw2);
        }
        else if(Main2Activity.rightNumber==3){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw3);
        }
        else if(Main2Activity.rightNumber==4){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw4);
        }
        else if(Main2Activity.rightNumber==5){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw5);
        }
        else if(Main2Activity.rightNumber==6){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw6);
        }
        else if(Main2Activity.rightNumber==7){
            mediaPlayer=MediaPlayer.create(this,R.raw.draw7);
        }
        else if(Main2Activity.rightNumber==8){
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
                mediaPlayer.start();
            }
        }, duration );

        /*handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);
                finish();
            }
        }, duration * 700);*/

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
