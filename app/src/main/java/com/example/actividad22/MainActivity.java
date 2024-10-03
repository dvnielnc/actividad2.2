package com.example.actividad22;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnDescargarImagen;
    private ImageView mImageView;
    private String imageUrl = "https://intecorecoleta.cl/wp-content/uploads/2022/08/programacion-2-e1551291144973.jpg";
    private Bitmap loadedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnDescargarImagen = findViewById(R.id.btnDescargarImagen);
        mImageView = findViewById(R.id.mImageView);

        if (savedInstanceState != null) {
            loadedBitmap = savedInstanceState.getParcelable("imageBitmap");
            if (loadedBitmap != null) {
                mImageView.setImageBitmap(loadedBitmap);
            }
        }

        btnDescargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadedBitmap = loadImageFromNetwork(imageUrl);
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(loadedBitmap);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("imageBitmap", loadedBitmap);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (loadedBitmap != null) {
            mImageView.setImageBitmap(loadedBitmap);
        }
    }

    private Bitmap loadImageFromNetwork(String urlString) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
