package com.shitab.sheba_camera.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.shitab.sheba_camera.R;
import com.shitab.sheba_camera.ShebaCameraActivity;

public class TakenPictureActivity extends AppCompatActivity {

    ImageView ivPreview;
    Bitmap originalPhoto, rotatedPhoto;
    float rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_picture);
        initView();
        getData();
        processImage();
        showPreview();
    }

    private void initView() {
        ivPreview=findViewById(R.id.ivPreview);
    }

    private void getData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //ivPreview.setImageBitmap(ShebaCameraActivity.bitmap);
            originalPhoto = ShebaCameraActivity.bitmap;
        }
        else{
            //JANINA
        }
    }

    private void processImage() {

        //originalPhoto.

        //
        float degrees = 90;//rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        rotatedPhoto = Bitmap.createBitmap(originalPhoto, 0, 0, originalPhoto.getWidth(), originalPhoto.getHeight(), matrix, true);


    }

    private void showPreview() {
        ivPreview.setImageBitmap(rotatedPhoto);
    }
}
