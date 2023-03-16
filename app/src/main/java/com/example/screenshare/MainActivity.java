package com.example.screenshare;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ScreenCaptureClient";
    private ImageView imageView;
    private Bitmap bitmap;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            imageView.setImageBitmap(bitmap);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);


        new Thread(new ClientThread()).start();
    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                Socket socket = new Socket("192.168.1.3",8000);
                while(true){
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    byte[] bytes = (byte[]) objectInputStream.readObject();
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    //Rotate bitmap
                    //Matrix matrix = new Matrix();
                    //matrix.postRotate(90);
                    //bitmap = Bitmap.createBitmap(bitmap, 0 , 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);

                    //delete bitmap

                }
            } catch ( IOException |ClassNotFoundException e) {
                Log.e(TAG, "Error :" +e.getMessage());
            }
        }
    }
}