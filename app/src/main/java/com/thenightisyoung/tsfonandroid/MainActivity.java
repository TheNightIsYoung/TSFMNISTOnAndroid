package com.thenightisyoung.tsfonandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView txt;
    TextView tv;
    ImageView imageView;
    Bitmap bitmap;
    MyTSFMNISTDemo preTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        txt=(TextView)findViewById(R.id.txt_id);
        imageView =(ImageView)findViewById(R.id.imageView1);

        bitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/test.bmp"));
        imageView.setImageBitmap(bitmap);

        preTF = new MyTSFMNISTDemo(getAssets());
    }

    public void click01(View v){
        String res = "The Result Of Prediction：";

        int[] result = preTF.getPredict(bitmap);

        for (int i=0;i<result.length;i++){
            Log.i(TAG, res + result[i] );
            res = res + String.valueOf(result[i])+" ";
        }
        txt.setText(res);
    }
}
