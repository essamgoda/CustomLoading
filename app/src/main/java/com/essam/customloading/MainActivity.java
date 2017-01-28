package com.essam.customloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Handler mImageChangingHandler;
    private int mCurrentImageIndex = 0;
    private BackgroundThread mImageUpdateThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.img);
        Bitmap mainBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spinner);
        //number of individual bitmaps, which you should know.
        final int numberOfImages = 21;
        //individual bitmap width
        int bitmapWidth = mainBitmap.getWidth() / numberOfImages;
        //individual bitmap height
        int bitmapHeight = mainBitmap.getHeight();
        //list which holds individual bitmaps.
        final List<Bitmap> animBitmaps = new ArrayList<>(numberOfImages);
        //split your bitmap in to individual bitmaps
        for (int index = 0; index < numberOfImages; index++) {
            animBitmaps.add(Bitmap.createBitmap(mainBitmap, index * bitmapWidth, 0, bitmapWidth, bitmapHeight));
        }
        //set 1st bitmap to imageView.
        mImageView.setImageBitmap(animBitmaps.get(mCurrentImageIndex));
        mImageChangingHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //increament current bitmap index
                mCurrentImageIndex++;
                //if current index is greater than the number of images, reset it to 0
                if (mCurrentImageIndex > numberOfImages - 1) {
                    mCurrentImageIndex = 0;
                }
                mImageView.setImageBitmap(animBitmaps.get(mCurrentImageIndex));
                return true;
            }
        });
//Create the background thread by passing the handler and start.
        mImageUpdateThread = new BackgroundThread(mImageChangingHandler);
        mImageUpdateThread.setRunning(true);
        mImageUpdateThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageUpdateThread.setRunning(false);
        boolean stopThread = true;
        while (stopThread) {
            try {
                mImageUpdateThread.join();
                //thread already stopped
                stopThread = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mImageUpdateThread = null;
    }
}
