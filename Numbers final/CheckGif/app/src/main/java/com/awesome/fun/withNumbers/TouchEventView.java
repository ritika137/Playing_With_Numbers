package com.awesome.fun.withNumbers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.os.AsyncTask;
import android.util.Log;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.widget.Toast.makeText;
import static com.awesome.fun.withNumbers.R.drawable.happy;
import static com.awesome.fun.withNumbers.R.drawable.sad;


public class TouchEventView extends View implements Runnable  {
    String datapath = "";
    MediaPlayer mpCorrect, mpWrong, mpTryAgain;
    ImageView drawHint;
    ImageView textView;
    Boolean check_again,call_next_activity;
    int checkCount;
    int wrongTask;
    private Boolean keepDrawing= false;
    private float latestX, latestY;
    Context mContext;


    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean drawable) {
        isDrawable = drawable;
    }

    private boolean isDrawable;

    int w1,w2, h1, h2;
    float clearX=0, clearY=0;

    int flag=0;

    //drawing path
    ExecutorService pool = Executors.newFixedThreadPool(10);

    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    Paint paint;
    private int paintColor = Color.BLUE;
    private String mdatapath;

    private float x_min,x_max,y_min,y_max;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    public Bitmap canvasBitmap;

    private TessBaseAPI mTess;

    private int times_run;

    private boolean is_correct;
    boolean mpTryAgainReleased ;



    private char toCheck;
    public TouchEventView(Context context) {
        super(context);
        isDrawable = true;
        setupDrawing();

    }


    public TouchEventView(Context context, String datapath, String letter, ImageView imageViewObject, ImageView makeInvisible) {
        super(context);
        check_again=true;
        call_next_activity=true;
        mContext = context;
        isDrawable = true;
        flag=0;
        setupDrawing();
        times_run=0;
        toCheck=letter.charAt(0);
        Log.v("test", "char is + " + toCheck);
        mdatapath = datapath;

        mTess = new TessBaseAPI();
        is_correct=false;
        mpCorrect=new MediaPlayer();
        mpCorrect=MediaPlayer.create(getContext(),R.raw.correct);
        textView = makeInvisible;

        drawHint = imageViewObject;

        mpWrong=new MediaPlayer();
        mpWrong=MediaPlayer.create(getContext(),R.raw.un_correct);
        wrongTask =0;

        mpTryAgain=new MediaPlayer();
        mpTryAgainReleased = true;

        //mpTryAgain=MediaPlayer.create(getContext(),R.raw.tryagain);

        mpCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

        mpWrong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });


    }

    static public float percentTransparent(Bitmap bm) {
        final int width = bm.getWidth();
        final int height = bm.getHeight();

        int totalTransparent = 0;
        for(int x = 0; x < width; x=x+5) {
            for(int y = 0; y < height; y=y+5) {
                if (bm.getPixel(x, y) == 0) {
                    totalTransparent++;
                }
            }
        }
        return ((float)totalTransparent)/(width * height/25);

    }


    private void setupDrawing() {
        //get drawing area setup for interaction

        paint = new Paint();

        drawPath = new Path();
        drawPaint = new Paint();

        drawPath.reset();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dpHeight = displayMetrics.heightPixels;
        int dpWidth = displayMetrics.widthPixels;
        x_min=dpWidth/2;
        y_min=dpHeight/2;
        x_max=dpWidth/2;
        y_max=dpHeight/2;
        drawPaint.setStrokeWidth(50f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size

        w1=w;
        w2=oldw;
        h1=h;
        h2=oldh;

        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        this.setDrawingCacheEnabled(true);

    }



    @Override
    public void run()  {

        //                work2();
        Bitmap bitmap = canvasBitmap;

        Log.v("test", "touch handles");
        //bitmap = Bitmap.createScaledBitmap(bitmap,90,90,false);
        mTess.init(mdatapath, "eng");
        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        Log.v("test", " "+result);

        //Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();

        if (mTess != null)
            mTess.end();
    }

    public void work2()  {
        Bitmap bitmap = Bitmap.createBitmap(canvasBitmap,(int)x_min-5,(int)y_min-5,(int)(x_max-x_min)+5,(int)(y_max-y_min)+5);
        bitmap = Bitmap.createScaledBitmap(bitmap,90,90,true);
        //bitmap = toGrayscale(bitmap);
        Log.v("test", "touch handles");
        //Log.v("vals "," "+ String.valueOf(x_min)+ " "+String.valueOf(x_max)+ " "+String.valueOf(y_min)+ " "+String.valueOf(y_max));

        //bitmap = Bitmap.createScaledBitmap(bitmap,90,90,false);
        mTess.init(mdatapath, "eng");
        mTess.setImage(bitmap);
//            mTess.setRectangle((int)x_min-5,(int)y_min-5,(int)(x_max-x_min)+5,(int)(y_max-y_min)+5);

        String result = mTess.getUTF8Text();
        String conf= String.valueOf(mTess.meanConfidence());

        Log.v("test", result);
        Log.v("conf",conf);

        makeText(getContext(),result,Toast.LENGTH_SHORT).show();

        if (mTess != null)
            mTess.end();

    }


    public void clear(float tx, float ty)
    {
        canvasBitmap = Bitmap.createBitmap(w1,h1 ,
                Bitmap.Config.ARGB_8888);

        clearX = tx;
        clearY = ty;

        Log.v("CKEAR X IS" + clearX, "CLEARY IS "+clearY);

        wrongTask++;

        flag=-25;

        drawCanvas = new Canvas(canvasBitmap);

    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        latestX=touchX;
        latestY=touchY;
        if(times_run==0)
        {   x_min=touchX;
            x_max=touchX+5;
            y_min=touchY;
            y_max=touchY+5;


            times_run=1;
        }

        if(touchX<x_min)
            x_min=touchX;
        if(touchX>x_max)
            x_max=touchX;
        if(touchY<y_min)
            y_min=touchY;
        if(touchY>y_max)
            y_max=touchY;
        //Log.v("vals", String.valueOf(x_min)+ " "+String.valueOf(x_max)+ " "+String.valueOf(y_min)+ " "+String.valueOf(y_max));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //if(redValue==0&&greenValue==0 &&(blueValue==0|| blueValue==255)
                drawPath.moveTo(touchX, touchY);
                //mp.setLooping(true);
                //mp.start();
                //drawCanvas.drawPath(drawPath, drawPaint);

                break;
            case MotionEvent.ACTION_MOVE:

                if(flag==-25)
                {
                    //drawPath.lineTo(clearX, clearY);
                    flag=0;
                }
                else
                    drawPath.lineTo(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                float percent = percentTransparent(canvasBitmap);
//                Log.v("Percent_empty", String.valueOf(percent));
//                if (percent <= 1.00) {
//                    Log.v("Low", "Percent Empty");
//                    setupDrawing();
//                    clear(touchX, touchY);
//                    setupDrawing();
//                    invalidate();
//                    // Toast.makeText(getContext(), "Wrong", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
                drawCanvas.drawPath(drawPath, drawPaint);

                invalidate();
                //}


                float check_x=canvasBitmap.getWidth(),check_y=canvasBitmap.getHeight();

                try {
                    if(check_again==true) {
                        if(x_max<check_x && y_max<check_y && x_min-5>=0 && y_min-5>=0 && (x_max-x_min+5)>=0 && (y_max-y_min+5)>=0)
                            new TheTask().execute();
                        else
                        {
                            setupDrawing();
                            clear(0, 0);
                            drawPath.moveTo(latestX,latestY);
                            invalidate();

                            textView.setVisibility(INVISIBLE);
                            drawHint.setVisibility(INVISIBLE);

                            //Toast.makeText(getContext(),"Value detected = "+val +" conf= "+ conf,Toast.LENGTH_SHORT).show();



                            if(wrongTask<=3) {
                                //drawHint.setVisibility(INVISIBLE);
                                //Toast.makeText(mContext, "WT IS  "+wrongTask, Toast.LENGTH_SHORT).show();
                                //wrongTask = wrongTask+1;

//                        setupDrawing();
//                        clear(0, 0);
//                        drawPath.moveTo(latestX,latestY);
//                        invalidate();

                                drawHint.setVisibility(VISIBLE);
                                if (toCheck == '0')
                                    drawHint.setBackgroundResource(R.drawable.draw0);
                                else if (toCheck == '1')
                                    drawHint.setBackgroundResource(R.drawable.draw1);
                                else if (toCheck == '2')
                                    drawHint.setBackgroundResource(R.drawable.draw2);
                                else if (toCheck == '3')
                                    drawHint.setBackgroundResource(R.drawable.draw3);
                                else if (toCheck == '4')
                                    drawHint.setBackgroundResource(R.drawable.draw4);
                                else if (toCheck == '5')
                                    drawHint.setBackgroundResource(R.drawable.draw5);
                                else if (toCheck == '6')
                                    drawHint.setBackgroundResource(R.drawable.draw6);
                                else if (toCheck == '7')
                                    drawHint.setBackgroundResource(R.drawable.draw7);
                                else if (toCheck == '8')
                                    drawHint.setBackgroundResource(R.drawable.draw8);
                                else
                                    drawHint.setBackgroundResource(R.drawable.draw9);

                                AnimationDrawable anim = (AnimationDrawable) drawHint.getBackground();

                                anim.start();
                                if(mpTryAgainReleased) {

                                    mpTryAgain = MediaPlayer.create(getContext(), R.raw.tryagain);
                                    mpTryAgain.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        public void onCompletion(MediaPlayer mp) {
                                            mp.release();
                                            mpTryAgainReleased = true;

                                        }
                                    });
                                    mpTryAgain.start();
                                }
                            }




                        }
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}

                break;


        }
        return true;
    }



    public class TheTask extends AsyncTask<Void,Void,ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            Bitmap bitmap = Bitmap.createBitmap(canvasBitmap,(int)x_min-5,(int)y_min-5,(int)(x_max-x_min)+5,(int)(y_max-y_min)+5);
            bitmap = Bitmap.createScaledBitmap(bitmap,90,90,true);

           // float value  = percentTransparent(bitmap);

            //Log.v("test", "percent empty : "+ value);

           // Log.v("test", "touch handles");
           // Log.v("vals "," "+ String.valueOf(x_min)+ " "+String.valueOf(x_max)+ " "+String.valueOf(y_min)+ " "+String.valueOf(y_max));

            //bitmap = Bitmap.createScaledBitmap(bitmap,90,90,false);
            mTess.init(mdatapath, "eng",TessBaseAPI.OEM_TESSERACT_ONLY);
            mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789");

            mTess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, " /+_)!?-(*&^%$#@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

            mTess.setImage(bitmap);
//            mTess.setRectangle((int)x_min-5,(int)y_min-5,(int)(x_max-x_min)+5,(int)(y_max-y_min)+5);

            String result = mTess.getUTF8Text();
            String conf= String.valueOf(mTess.meanConfidence());
            ArrayList<String> to_send= new ArrayList<>();
            if (result=="")
                result="`";
            if(result==null)
                result="`";
            to_send.add(result);
            to_send.add(conf);
            Log.v("test", "Result is :::: "+result);
            Log.v("conf","CONF IS :::: " + conf);


            if (mTess != null)
                mTess.end();
            return to_send;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            String conf= result.get(1);
            String val= result.get(0);
            Log.v("test","Value detected = "+val + " conf= "+ conf);
            try {
                if ( val != null && !val.isEmpty() && (val.charAt(0) == toCheck) && (val.length() == 1 )&& (Integer.valueOf(conf)>30) && (!is_correct) ) {


                    drawHint.setVisibility(INVISIBLE);
//                    setupDrawing();
//                    clear(0, 0);
//                    setupDrawing();
//                    invalidate();

                    setupDrawing();
                    clear(0, 0);
                    drawPath.moveTo(latestX,latestY);
                    invalidate();


                    //int sdk = android.os.Build.VERSION.SDK_INT;
                    //if(sdk < Build.VERSION_CODES.KITKAT)
                    //    Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                    //else
                    //  Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();

//                    Toast.makeText(getContext(), "Correct with " + conf + "% accuracy", Toast.LENGTH_SHORT).show();
                    //TTS to_speak = new TTS();
                    //to_speak.Speech(getContext(), "Very Good!! ");

                    Handler handlerTimer = new Handler();
                    check_again=false;
                    mpCorrect.start();





                    textView.getLayoutParams().height = 600;
                    textView.getLayoutParams().width = 600;
                    textView.requestLayout();

                    textView.setImageDrawable(mContext.getDrawable(happy));

                    textView.setVisibility(VISIBLE);




                    handlerTimer.postDelayed(new Runnable(){
                        public void run() {

                            isDrawable = false;
                            is_correct=true;
                            if(call_next_activity==true) {
//                                if(mpWrong.isPlaying()==true)
//                                    mpWrong.stop();
//                                if(mpTryAgain.isPlaying()==true)
//                                    mpTryAgain.stop();
                                Intent i = new Intent(mContext, Main2Activity.class);
                                mContext.startActivity(i);
                                call_next_activity=false;
                                ((Activity)(mContext)).finish();
                            }
                        }}, 1000);

                }
                else
                {

//
//                  drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                  canvasBitmap.eraseColor(Color.TRANSPARENT);

                    setupDrawing();
                    clear(0, 0);
                    drawPath.moveTo(latestX,latestY);
                    invalidate();

                    textView.setVisibility(INVISIBLE);
                    drawHint.setVisibility(INVISIBLE);

                    //Toast.makeText(getContext(),"Value detected = "+val +" conf= "+ conf,Toast.LENGTH_SHORT).show();



                    if(wrongTask<=3) {
                        //drawHint.setVisibility(INVISIBLE);
                        //Toast.makeText(mContext, "WT IS  "+wrongTask, Toast.LENGTH_SHORT).show();
                        //wrongTask = wrongTask+1;

//                        setupDrawing();
//                        clear(0, 0);
//                        drawPath.moveTo(latestX,latestY);
//                        invalidate();

                        drawHint.setVisibility(VISIBLE);
                        if (toCheck == '0')
                            drawHint.setBackgroundResource(R.drawable.draw0);
                        else if (toCheck == '1')
                            drawHint.setBackgroundResource(R.drawable.draw1);
                        else if (toCheck == '2')
                            drawHint.setBackgroundResource(R.drawable.draw2);
                        else if (toCheck == '3')
                            drawHint.setBackgroundResource(R.drawable.draw3);
                        else if (toCheck == '4')
                            drawHint.setBackgroundResource(R.drawable.draw4);
                        else if (toCheck == '5')
                            drawHint.setBackgroundResource(R.drawable.draw5);
                        else if (toCheck == '6')
                            drawHint.setBackgroundResource(R.drawable.draw6);
                        else if (toCheck == '7')
                            drawHint.setBackgroundResource(R.drawable.draw7);
                        else if (toCheck == '8')
                            drawHint.setBackgroundResource(R.drawable.draw8);
                        else
                            drawHint.setBackgroundResource(R.drawable.draw9);

                        AnimationDrawable anim = (AnimationDrawable) drawHint.getBackground();

                        anim.start();
                        if(mpTryAgainReleased) {

                            mpTryAgain = MediaPlayer.create(getContext(), R.raw.tryagain);
                            mpTryAgain.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                public void onCompletion(MediaPlayer mp) {
                                    mp.release();
                                    mpTryAgainReleased = true;

                                }
                            });
                            mpTryAgain.start();
                        }
                    }
                    else
                    {
//
                        drawHint.setVisibility(INVISIBLE);

                        textView.getLayoutParams().height = 600;
                        textView.getLayoutParams().width = 600;
                        textView.requestLayout();

//                        setupDrawing();
//                        clear(0, 0);
//                        drawPath.moveTo(latestX,latestY);

                         mpWrong.start();
                        check_again=false;
                        //Toast.makeText(mContext, "MORE THAN 2 ", Toast.LENGTH_SHORT).show();
                        textView.setImageDrawable(mContext.getDrawable(sad));

                        textView.setVisibility(VISIBLE);

                        Handler handlerTimer = new Handler();
                        handlerTimer.postDelayed(new Runnable(){
                            public void run() {
                                if(call_next_activity==true) {
                                    Intent i = new Intent(mContext, Main2Activity.class);
                                    mContext.startActivity(i);
                                    call_next_activity=false;
                                    ((Activity)(mContext)).finish();
                                }
                            }}, 1000);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

}