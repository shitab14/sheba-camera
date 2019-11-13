package com.shitab.sheba_camera.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.shitab.sheba_camera.R;
import com.shitab.sheba_camera.ShebaCameraActivity;

public class TakenPictureActivity extends AppCompatActivity {

    ImageView ivPreview;
    Bitmap originalPhoto, croppedPhoto, resultPhoto;
    float rotation;

    Context context;
    Intent intent;

    public float cropStartX, cropStartY, cropEndX, cropEndY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_picture);
        initValue();
        initView();
        getData();
        processImage();
        showPreview();
    }

    private void initValue() {
        context = this;
        intent = getIntent();

        cropStartX = Math.round(intent.getFloatExtra("cropStartX", cropStartX));
        cropStartY = Math.round(intent.getFloatExtra("cropStartY", cropStartY));
        cropEndX = Math.round(intent.getFloatExtra("cropEndX", cropEndX));
        cropEndY = Math.round(intent.getFloatExtra("cropEndY", cropEndY));
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

        //CROP
        croppedPhoto = Bitmap.createBitmap(originalPhoto, (int)cropStartX, (int)cropStartY, (int)cropEndX, (int)cropEndY);

        //ROTATE
        float degrees = 90;//rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        resultPhoto = Bitmap.createBitmap(croppedPhoto, 0, 0, croppedPhoto.getWidth(), croppedPhoto.getHeight(), matrix, true);

        //resultPhoto=croppedPhoto;

    }

    private void showPreview() {
        ivPreview.setImageBitmap(resultPhoto);
    }
}
