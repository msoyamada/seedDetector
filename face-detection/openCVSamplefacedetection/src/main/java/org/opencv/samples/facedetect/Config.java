package org.opencv.samples.facedetect;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.TextView;

public class Config extends Activity {
    float lHSV[]= new float[3];
    float uHSV[]= new float[3];
    SeekBar lowerH;
    SeekBar lowerS;
    SeekBar lowerV;

    SeekBar upperH;
    SeekBar upperS;
    SeekBar upperV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        lowerH= findViewById(R.id.seekBarLowH);
        lowerS= findViewById(R.id.seekBarLowS);
        lowerV= findViewById(R.id.seekBarLowV);
        upperH= findViewById(R.id.seekBarUpperH);
        upperS= findViewById(R.id.seekBarUpperS);
        upperV= findViewById(R.id.seekBarUpperV);

        TextView tf=findViewById(R.id.LowerHSV);
        tf.setText("LowerHSV "+ " H= "+ FdActivity.lHSV[0] + " S="+ FdActivity.lHSV[1] + " V="+FdActivity.lHSV[2] );
        tf.setTextColor(Color.WHITE);
        lHSV[0]= (float)(FdActivity.lHSV[0]*2);
        lHSV[1]= (float)(FdActivity.lHSV[1]/255);
        lHSV[2]= (float)(FdActivity.lHSV[2]/255);
        tf.setBackgroundColor(Color.HSVToColor(lHSV));
        lowerH.setProgress(FdActivity.lHSV[0]);
        lowerS.setProgress(FdActivity.lHSV[1]);
        lowerV.setProgress(FdActivity.lHSV[1]);


        TextView tfUpper=findViewById(R.id.UpperHSV);
        tf.setText("LowerHSV "+ " H= "+ FdActivity.lHSV[0] + " S="+ FdActivity.lHSV[1] + " V="+FdActivity.lHSV[2] );
        tf.setTextColor(Color.WHITE);
        uHSV[0]= (float)(FdActivity.uHSV[0]*2);
        uHSV[1]= (float)(FdActivity.uHSV[1]/255);
        uHSV[2]= (float)(FdActivity.uHSV[2]/255);
        tf.setBackgroundColor(Color.HSVToColor(lHSV));
        upperH.setProgress(FdActivity.uHSV[0]);
        upperS.setProgress(FdActivity.uHSV[1]);
        upperV.setProgress(FdActivity.uHSV[1]);




        lowerH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.LowerHSV);
                FdActivity.lHSV[0]= progress;
                lHSV[0]= (float)progress *2;
                int color= Color.HSVToColor(lHSV);
                tf.setBackgroundColor(color);
                tf.setText("LowerHSV "+ " H= "+ FdActivity.lHSV[0] + " S="+ FdActivity.lHSV[1] + " V="+FdActivity.lHSV[2] );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        lowerS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.LowerHSV);
                float f= (float)progress;
                FdActivity.lHSV[1]= progress;
                lHSV[1]= f/255;
                int color= Color.HSVToColor(lHSV);
                tf.setBackgroundColor(color);
                tf.setText("LowerHSV "+ " H= "+ FdActivity.lHSV[0] + " S="+ FdActivity.lHSV[1] + " V="+FdActivity.lHSV[2] );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        lowerV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.LowerHSV);
                float f= (float)progress;
                FdActivity.lHSV[2]= progress;
                lHSV[2]= f/255;
                int color= Color.HSVToColor(lHSV);
                tf.setBackgroundColor(color);
                tf.setText("LowerHSV "+ " H= "+ FdActivity.lHSV[0] + " S="+ FdActivity.lHSV[1] + " V="+FdActivity.lHSV[2] );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //UpperPart
        upperH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.LowerHSV);
                FdActivity.uHSV[0]= progress;
                uHSV[0]= (float)progress *2;
                int color= Color.HSVToColor(uHSV);
                tf.setBackgroundColor(color);
                tf.setText("UpperHSV "+ " H= "+ FdActivity.uHSV[0] + " S="+ FdActivity.uHSV[1] + " V="+FdActivity.uHSV[2] );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        upperS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.UpperHSV);
                float f= (float)progress;
                FdActivity.uHSV[1]= progress;
                uHSV[1]= f/255;
                int color= Color.HSVToColor(uHSV);
                tf.setBackgroundColor(color);
                tf.setText("UpperHSV "+ " H= "+ FdActivity.uHSV[0] + " S="+ FdActivity.uHSV[1] + " V="+FdActivity.uHSV[2] );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        upperV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tf=findViewById(R.id.UpperHSV);
                float f= (float)progress;
                FdActivity.uHSV[2]= progress;
                uHSV[2]= f/255;
                int color= Color.HSVToColor(uHSV);
                tf.setBackgroundColor(color);
                tf.setText("UpperHSV "+ " H= "+ FdActivity.uHSV[0] + " S="+ FdActivity.uHSV[1] + " V="+FdActivity.uHSV[2] );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });







    }


}
