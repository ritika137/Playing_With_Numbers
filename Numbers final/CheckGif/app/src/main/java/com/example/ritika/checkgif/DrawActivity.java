package com.example.ritika.checkgif;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DrawActivity extends AppCompatActivity {
    int num;
    final float growTo = 31.0f;
    final long duration = 20;
    TextView textView;
    String datapath="";
    MediaPlayer mediaPlayer;
    RelativeLayout activity_draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_draw);

        datapath = getFilesDir() + "/tesseract/";
        checkFile(new File(datapath + "tessdata/"));

        activity_draw=(RelativeLayout)findViewById(R.id.activity_draw);
        TouchEventView view=new TouchEventView(this,datapath, Integer.toString(MainActivity.rightNumber));
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

        textView=(TextView)findViewById(R.id.numTextView);
        //Toast.makeText(this, Integer.toString(MainActivity.rightNumber), Toast.LENGTH_SHORT).show();
        textView.setText(Integer.toString(MainActivity.rightNumber));
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
                mediaPlayer.start();
            }
        }, duration );

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, duration * 500);

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
