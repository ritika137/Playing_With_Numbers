package com.example.ritika.checkgif;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import static android.support.v7.appcompat.R.id.time;
import static android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.json.JSONObject.NULL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout imageView;
    FrameLayout addBalloon1;
    FrameLayout addBalloon2;
    FrameLayout addBalloon3;
    FrameLayout addBalloon4;
    TextView number1;
    TextView number2;
    TextView number3;
    TextView number4;

    //Number to select image view on screen for batches
    int imageV=1;
    int imgNo;
    static int totalSize=0;
    int x=1;

    Balloon balloon1;
    Balloon balloon2;
    Balloon balloon3;
    Balloon balloon4;

    int []imagesArrayList=new int[51];

    float size;
    static int rightNumber;
    static int rightNumberPosition;
    int previousNumber=-1;
    LinearLayout ll;
    MediaPlayer popSound;
    MediaPlayer popNo;
    MediaPlayer batch;
    static float totWidth, totHeight, width, height, posx, posy;
    int duration =1000;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ///for full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        Display display = getWindowManager().getDefaultDisplay();
        Point size1 = new Point();
        display.getSize(size1);
        totWidth = size1.x;
        totHeight = size1.y;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        x = sharedPref.getInt("x", 1);
        Log.i("valofx",Integer.toString(x));

        x++;
        Log.i("valofx",Integer.toString(x));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("x", x);
        editor.commit();

        if(x>10){

            editor.clear();
            totalSize=0;
            editor.commit();
        }


        addBalloon1 = (FrameLayout) findViewById(R.id.balloon1);
        addBalloon2 = (FrameLayout) findViewById(R.id.balloon2);
        addBalloon3 = (FrameLayout) findViewById(R.id.balloon3);
        addBalloon4 = (FrameLayout) findViewById(R.id.balloon4);
        number1 = (TextView) findViewById(R.id.textView);
        number2 = (TextView) findViewById(R.id.textView2);
        number3 = (TextView) findViewById(R.id.textView3);
        number4 = (TextView) findViewById(R.id.textView4);

        popSound = MediaPlayer.create(this, R.raw.ballonpopshort);
        batch=MediaPlayer.create(this,R.raw.makeballoone1);

        number1.setVisibility(INVISIBLE);
        number2.setVisibility(INVISIBLE);
        number3.setVisibility(INVISIBLE);
        number4.setVisibility(INVISIBLE);

        size = (float) number1.getTextSize();

        addBalloon1.setBackgroundResource(R.drawable.create);
        AnimationDrawable anim = (AnimationDrawable) addBalloon1.getBackground();
        anim.start();

        //find previous number just to ensure that the new ques called does not create same question again
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        previousNumber=sharedPref.getInt("previousNumber",-1);
        imageV=sharedPref.getInt("imageV",1);
        Log.i("valofimgV",Integer.toString(imageV));
        imgNo=sharedPref.getInt("imgNo",1);
        //Toast.makeText(this, "previous number: "+previousNumber, Toast.LENGTH_SHORT).show();

        String savedString = sharedPref.getString("string", "");
        Log.i("value of string",savedString);
        StringTokenizer st = new StringTokenizer(savedString, ",");
        for (int i = 0; i < 50; i++) {
            if(st.hasMoreTokens()) {
                imagesArrayList[i] = Integer.parseInt(st.nextToken());
                Log.i("Placing in main ",Integer.toString(imagesArrayList[i]) );
            }
            else{
                break;
            }
        }

        totalSize=sharedPref.getInt("totalSize",0);
        int begin=totalSize;
        begin--;
        for(int i=1;i<=5;i++){
            Log.i("for loop",Integer.toString(begin));
            if(begin<0){
                break;
            }

            int x=imagesArrayList[begin];
            Log.i("Value of x",Integer.toString(x));
            if(x==0){
                break;
            }
            ImageView imageView ;

            if(i==1) {
                imageView = (ImageView) findViewById(R.id.imageView1);
            }
            else if(i==2){
                imageView = (ImageView) findViewById(R.id.imageView2);
            }
            else if(i==3){
                imageView = (ImageView) findViewById(R.id.imageView3);
            }
            else if(i==4){
                imageView = (ImageView) findViewById(R.id.imageView4);
            }
            else{
                imageView = (ImageView) findViewById(R.id.imageView5);
            }

            String uri = "@drawable/image" + x;
            Log.i("trying to access",uri);
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
           // Drawable res = getResources().getDrawable(imageResource);
            imageView.setImageResource(imageResource);
            //if(res!=NULL)
            //imageView.setImageDrawable(res);
            begin--;
        }


        // Delay mechanism



        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                number1.setVisibility(VISIBLE);
                addBalloon2.setBackgroundResource(R.drawable.create);
                AnimationDrawable anim = (AnimationDrawable) addBalloon2.getBackground();
                anim.start();

            }}, 1000);

        //Handler handlerTimer3 = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                number2.setVisibility(VISIBLE);
                addBalloon3.setBackgroundResource(R.drawable.create);
                AnimationDrawable anim = (AnimationDrawable) addBalloon3.getBackground();
                anim.start();

            }}, 2000);

        //Handler handlerTimer4 = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {

                number3.setVisibility(VISIBLE);
                addBalloon4.setBackgroundResource(R.drawable.create);
                AnimationDrawable anim = (AnimationDrawable) addBalloon4.getBackground();
                anim.start();


            }}, 3000);
        handlerTimer.postDelayed(new Runnable(){
            public void run() {

                number4.setVisibility(VISIBLE);
                addBalloon1.setBackgroundResource(R.drawable.frame_25);
                addBalloon2.setBackgroundResource(R.drawable.frame_25);
                addBalloon3.setBackgroundResource(R.drawable.frame_25);
                addBalloon4.setBackgroundResource(R.drawable.frame_25);

            }}, 4000);


        addBalloon1.setOnClickListener(this);
        addBalloon2.setOnClickListener(this);
        addBalloon3.setOnClickListener(this);
        addBalloon4.setOnClickListener(this);

        nextquestion();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {
                popNo.start();
            }}, 4000);

        //
        ll=(LinearLayout)findViewById(R.id.BatchLayout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BatchActivity.class);
                startActivity(intent);
            }
        });



    }

    public void grow(TextView textView) {
        //gets the x and y positions of the text view
        //textView.setVisibility(View.VISIBLE);
        posx = textView.getLeft();
        posy = textView.getTop();
        height= textView.getHeight();
        width=textView.getWidth();

        final Intent intent=new Intent(getApplicationContext(),DrawActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){

                startActivity(intent);
                finish();
            }
        }, (long) (duration * 4));

    }

    public void burst(FrameLayout balloonBurst, int positionOfballoon) {

        if(positionOfballoon==rightNumberPosition) {
            balloonBurst.setBackgroundResource(R.drawable.burst);
            AnimationDrawable anim = (AnimationDrawable) balloonBurst.getBackground();
            anim.start();
            popSound.start();

            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable(){
                public void run() {
                    popNo = MediaPlayer.create(MainActivity.this, R.raw.correct);
                    popNo.start();


                }}, 800);

            //To set the central animation of the batch
            handlerTimer.postDelayed(new Runnable(){
                public void run() {
                    batch.start();
                    LinearLayout ll=(LinearLayout) findViewById(R.id.LinearLayout1);
                    ll.setVisibility(INVISIBLE);
                    ll=(LinearLayout) findViewById(R.id.LinearLayout2);
                    ll.setVisibility(INVISIBLE);
                    final ImageView zoom = (ImageView) findViewById(R.id.imageViewf);
                    final Animation zoomAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
                    zoom.startAnimation(zoomAnimation);

                }}, 2000);

            Random rand=new Random();
            int newImageNo=rand.nextInt(5)+1;
            while(newImageNo==imgNo){
                newImageNo=rand.nextInt(5)+1;
            }

            ImageView imageView ;

            if(imageV==1) {
                imageView = (ImageView) findViewById(R.id.imageView1);
                imageV++;
            }
            else if(imageV==2){
                imageView = (ImageView) findViewById(R.id.imageView2);
                imageV++;
            }
            else if(imageV==3){
                imageView = (ImageView) findViewById(R.id.imageView3);
                imageV++;
            }
            else if(imageV==4){
                imageView = (ImageView) findViewById(R.id.imageView4);
                imageV++;
            }
            else{
                imageView = (ImageView) findViewById(R.id.imageView5);
                imageV=1;
            }


            totalSize=sharedPref.getInt("totalSize",0);
            imagesArrayList[totalSize]=newImageNo;
            totalSize++;
            Log.i("val new tottal size",Integer.toString(totalSize));
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("imageV", imageV);
            editor.commit();
            editor.putInt("totalSize", totalSize);
            editor.commit();
            editor.putInt("imgNo", newImageNo);
            editor.commit();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < totalSize; i++) {
                str.append(imagesArrayList[i]).append(",");
            }
            sharedPref.edit().putString("string", str.toString()).commit();

            String uri = "@drawable/image" + newImageNo;  // where myresource (without the extension) is the file
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            final Drawable res = getResources().getDrawable(imageResource);
            imageView.setImageDrawable(res);

            //To set the central batch image
            handlerTimer.postDelayed(new Runnable(){
                public void run() {
                    ImageView i=(ImageView)findViewById(R.id.imageViewf);
                    i.setImageDrawable(res);
                }}, 1000);

            //reset();
            if (rightNumberPosition == 1) {
                number1.setVisibility(View.INVISIBLE);
                grow(number1);

            }
            else if(rightNumberPosition == 2){
                number2.setVisibility(View.INVISIBLE);
                grow(number2);
            }
            else if(rightNumberPosition == 3){
                number3.setVisibility(View.INVISIBLE);
                grow(number3);
            }
            else if(rightNumberPosition == 4){
                number4.setVisibility(View.INVISIBLE);
                grow(number4);
            }
        }
        else
        {

            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
            final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
            if(animShake!=NULL)
                balloonBurst.startAnimation(animShake);
        }


    }

    @Override
    public void onClick(View v) {

        if(v==addBalloon1)
        {
            burst(addBalloon1, 1);
        }
        else if(v==addBalloon2)
        {
            burst(addBalloon2,2);
        }
        else if(v==addBalloon3)
        {
            burst(addBalloon3,3);
        }
        else if(v==addBalloon4)
        {
            burst(addBalloon4,4);
        }

    }



    public void nextquestion(){
        //num++;
        //score1=(TextView)findViewById(R.id.myscore);
        Random r = new Random();
        int i1 = r.nextInt(10);
        int i2= r.nextInt(10);
        while(i1==previousNumber){
            i1 = r.nextInt(10);
        }
        while(i1==i2 || i2==previousNumber)
        {
            i2 = r.nextInt(10);
        }

        int i3 = r.nextInt(10);

        while(i3==i2 || i1==i3 || i3==previousNumber)
        {
            i3 = r.nextInt(10);
        }
        int i4= r.nextInt(10);

        while(i1==i4 || i2==i4 || i3==i4 || i4==previousNumber)
        {
            i4 = r.nextInt(10);
        }

        int Position;
        Position = r.nextInt(4)+1;
        if(Position==1)
        {
            rightNumber = i1;
            rightNumberPosition =1;
        }
        else if(Position==2)
        {
            rightNumber = i2;
            rightNumberPosition =2;
        }
        else if(Position==3)
        {
            rightNumber = i3;
            rightNumberPosition =3;
        }
        else if(Position==4)
        {
            rightNumber = i4;
            rightNumberPosition=4;
        }

        balloon1= new Balloon(1,i1);
        balloon2 = new Balloon(2,i2);

        Toast.makeText(this, "Pop the balloon with number " + Integer.toString(rightNumber),Toast.LENGTH_SHORT).show();

        if(rightNumber==0){
            popNo = MediaPlayer.create(this, R.raw.pop0);
        }
        else if(rightNumber==1){
            popNo = MediaPlayer.create(this, R.raw.pop1);
        }
        else if(rightNumber==2){
            popNo = MediaPlayer.create(this, R.raw.pop2);
        }
        else if(rightNumber==3){
            popNo = MediaPlayer.create(this, R.raw.pop3);
        }
        else if(rightNumber==4){
            popNo = MediaPlayer.create(this, R.raw.pop4);
        }
        else if(rightNumber==5){
            popNo = MediaPlayer.create(this, R.raw.pop5);
        }
        else if(rightNumber==6){
            popNo = MediaPlayer.create(this, R.raw.pop6);
        }
        else if(rightNumber==7){
            popNo = MediaPlayer.create(this, R.raw.pop7);
        }
        else if(rightNumber==8){
            popNo = MediaPlayer.create(this, R.raw.pop8);
        }
        else {
            popNo = MediaPlayer.create(this, R.raw.pop9);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("previousNumber", rightNumber);
        editor.commit();



        number1.setText(Integer.toString(i1));
        number2.setText(Integer.toString(i2));
        number3.setText(Integer.toString(i3));
        number4.setText(Integer.toString(i4));

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


