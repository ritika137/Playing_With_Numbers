package com.example.ritika.checkgif;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.view.View;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static android.widget.Toast.makeText;


public class TouchEventView extends View implements Runnable  {
    String datapath = "";
    MediaPlayer mpCorrect, mpWrong;

    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean drawable) {
        isDrawable = drawable;
    }

    private boolean isDrawable;
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



    private char toCheck;
    public TouchEventView(Context context) {
        super(context);
        isDrawable = true;
        setupDrawing();

    }


    public TouchEventView(Context context, String datapath, String letter) {
        super(context);
        isDrawable = true;
        setupDrawing();
        times_run=0;
        toCheck=letter.charAt(0);
        Log.v("test", "char is + " + toCheck);
        mdatapath = datapath;

        mTess = new TessBaseAPI();
        is_correct=false;
        mpCorrect=new MediaPlayer();
        mpCorrect=MediaPlayer.create(getContext(),R.raw.correct);

        mpWrong=new MediaPlayer();
        mpWrong=MediaPlayer.create(getContext(),R.raw.tryagain);
        //mp = MediaPlayer.create(getContext(), R.raw.final_effect);




    }


    private void setupDrawing() {


        //get drawing area setup for interaction

        paint = new Paint();

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dpHeight = displayMetrics.heightPixels;
        int dpWidth = displayMetrics.widthPixels;
        x_min=dpWidth/2;
        y_min=dpHeight/2;
        x_max=dpWidth/2;
        y_max=dpHeight/2;
        drawPaint.setStrokeWidth(25f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size

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








    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
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

                //if(redValue==0&&greenValue==0 &&(blueValue==0|| blueValue==255))
                drawPath.moveTo(touchX, touchY);
                //mp.setLooping(true);
                //mp.start();
                //drawCanvas.drawPath(drawPath, drawPaint);

                break;
            case MotionEvent.ACTION_MOVE:

                drawPath.lineTo(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //mp.pause();
                drawCanvas.drawPath(drawPath, drawPaint);
                invalidate();


                try {
                    new TheTask().execute();
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
            Log.v("test", "touch handles");
            Log.v("vals "," "+ String.valueOf(x_min)+ " "+String.valueOf(x_max)+ " "+String.valueOf(y_min)+ " "+String.valueOf(y_max));

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
            Log.v("test", " "+result);
            Log.v("conf",conf);


            if (mTess != null)
                mTess.end();
            return to_send;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            String conf= result.get(1);
            String val= result.get(0);
            Toast.makeText(getContext(),"Value detected = "+val +" conf= "+ conf,Toast.LENGTH_SHORT).show();
            try {
                if (val.charAt(0) == toCheck && val.length() == 1&& Integer.valueOf(conf)>30 && !is_correct) {


                    //int sdk = android.os.Build.VERSION.SDK_INT;
                    //if(sdk < Build.VERSION_CODES.KITKAT)
                    //    Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                    //else
                      //  Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();

//                    Toast.makeText(getContext(), "Correct with " + conf + "% accuracy", Toast.LENGTH_SHORT).show();
                    //TTS to_speak = new TTS();
                    //to_speak.Speech(getContext(), "Very Good!! ");
                   mpCorrect.start();


                    isDrawable = false;
                    is_correct=true;

                }
                else
                {
                    mpWrong.start();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }


}

/**
 * Created by nupur on 14/01/17.


public class TouchEventView extends View {
    private Paint paint= new Paint();
    private Path path=new Path();
    LinearLayout layout;
    TextView textView;
    int duration=1000;


     public TouchEventView(Context ctx, AttributeSet attrs){
         super(ctx,attrs);
         paint.setAntiAlias(true);
         paint.setStyle(Paint.Style.STROKE);
         paint.setStrokeWidth(25f);
         paint.setColor(Color.BLACK);
         paint.setStrokeJoin(Paint.Join.ROUND);


// To place the text view somewhere specific:
//canvas.translate(0, 0);


     }
    public void set(){

        //textView=(TextView)findViewById(R.id.numTextView);
        //textView.setText(Integer.toString(MainActivity.rightNumber));
        textView.setText("0");
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 33.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 33.0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(textView, pvhX, pvhY);
        animator.setDuration(duration * 2);
        animator.start();


    }
    @Override
    protected void onDraw(final Canvas canvas){

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
        canvas.drawPath(path,paint);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float xPos = event.getX();
        float yPos = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN :
                path.moveTo(xPos,yPos);
                return  true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default: return false;
        }

        invalidate();
        return true;
    }
}
*/