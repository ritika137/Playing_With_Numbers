package com.example.ritika.checkgif;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by nupur on 14/01/17.
 */

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
