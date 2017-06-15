package com.example.ritika.checkgif;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.StringTokenizer;

public class BatchActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    int [] imagesArrayList=new int[50];
    int totalSize;
    ScrollView scrollView;
    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);

        scrollView=(ScrollView)findViewById(R.id.ScrollView);
        gridLayout=(GridLayout)findViewById(R.id.GridLayout);
        //gridView=(GridView)findViewById(R.id.GridView);



//        Display display = getWindowManager().getDefaultDisplay();
//        LinearLayout layout= new LinearLayout();
//        int width=display.getWidth();
//        int height=display.getHeight();
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
//        layout.setLayoutParams(parms);



        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        totalSize=MainActivity.totalSize;
        //Log.i("totalsize",Integer.toString(totalSize));
        display();
    }

    public void display(){

        //fill values into imagesArrayList
        String savedString = sharedPref.getString("string", "");
        Log.i("value of string",savedString);
        StringTokenizer st = new StringTokenizer(savedString, ",");
        for (int i = 0; i < 50; i++) {
            if(st.hasMoreTokens()) {
                imagesArrayList[i] = Integer.parseInt(st.nextToken());
                Log.i("Placing in batch ",Integer.toString(imagesArrayList[i]) );
            }
            else{
                break;
            }
        }


        //insert badges
        for(int i=0;i<totalSize;i++){
            ImageView imageView=new ImageView(getApplicationContext());
            if(imagesArrayList[i]==0){
                continue;
            }
            String uri = "@drawable/image" + imagesArrayList[i];
            Log.i("screen",uri);
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//            Drawable res = getResources().getDrawable(imageResource);
//            imageView.setImageDrawable(res);
//            scrollView.addView(imageView);
            LinearLayout.LayoutParams imParams =
                    new LinearLayout.LayoutParams(250, 250);
            ImageView imSex = new ImageView(getApplicationContext());
            imSex.setImageResource(imageResource);
            //LinearLayout ll=(LinearLayout) findViewById(R.id.LinearLayout);
            gridLayout.addView(imSex,imParams);

        }
    }
}
