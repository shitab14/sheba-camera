package com.shitab.sheba_camera.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.shitab.sheba_camera.R;
import com.shitab.sheba_camera.ShebaCameraActivity;

public class TakenPictureActivity extends AppCompatActivity {

    ImageView ivPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_picture);
        initView();
        getData();
    }

    private void initView() {
        ivPreview=findViewById(R.id.ivPreview);
    }

    private void getData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivPreview.setImageBitmap(ShebaCameraActivity.bitmap);
        }
        else{
            //JANINA
        }
    }
}
