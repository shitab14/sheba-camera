package com.shitab.sheba_camera;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.shitab.sheba_camera.view.TakenPictureActivity;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ShebaCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.AutoFocusMoveCallback {

    SurfaceView svCameraView, svTransparentView;
    SurfaceHolder holder,holderTransparent;
    android.hardware.Camera camera;
    private Camera.PictureCallback pictureCallback;
    public static Bitmap bitmap;
    Canvas canvas;
    private float RectLeft, RectTop,RectRight,RectBottom ;

    int  deviceHeight,deviceWidth;

    RelativeLayout rlButtonHolder;
    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheba_camera);

        if(checkPermission()){

        }
        else{
            requestPermission();
        }
        initView();
        setListeners();
    }

    private void setListeners() {

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

    }


    private void initView() {

        //LAYOUT
        rlButtonHolder=findViewById(R.id.rlButtonHolder);
        btnCapture=findViewById(R.id.btnCapture);


        //FUNCTIONALITY
        svCameraView = (SurfaceView)findViewById(R.id.svCameraView);
        holder = svCameraView.getHolder();

        holder.addCallback((SurfaceHolder.Callback) this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            svCameraView.setSecure(true);
        }
        else {
            // JANINA
        }

        svTransparentView = (SurfaceView)findViewById(R.id.svTransparentView);

        holderTransparent = svTransparentView.getHolder();

        holderTransparent.addCallback((SurfaceHolder.Callback) this);

        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);

        svTransparentView.setZOrderMediaOverlay(true);

        //getting the device height and width
        deviceWidth= Resources.getSystem().getDisplayMetrics().widthPixels;
        deviceHeight=Resources.getSystem().getDisplayMetrics().heightPixels;

        camera = Camera.open(); //open a camera

        pictureCallback = getPictureCallback();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        synchronized (holder)
        {draw();}   //call a draw method
        camera.cancelAutoFocus();
        //CAMERA PARAMS
        Camera.Parameters param;
        param = camera.getParameters();
        //param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);


        //DISPLAY
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0) {
            param.setPreviewSize(deviceHeight, deviceWidth);
            camera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90) {
            param.setPreviewSize(deviceWidth, deviceHeight);
        }

        if(display.getRotation() == Surface.ROTATION_180) {
            param.setPreviewSize(deviceWidth, deviceHeight);
        }

        if(display.getRotation() == Surface.ROTATION_270) {
            param.setPreviewSize(deviceWidth, deviceHeight);
            camera.setDisplayOrientation(180);
        }

        camera.setParameters(param);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            camera.setAutoFocusMoveCallback(this);
        }
        //camera.autoFocus((Camera.AutoFocusCallback) this);

        //START CAMERA PREVIEW

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //camera.stopPreview();
        camera.release();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void draw() {

        //canvas = holderTransparent.lockCanvas(null);

        canvas = holderTransparent.lockCanvas();

        //PAINT FOR FRAME
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.border_mask));
        //paint.setStrokeWidth(3);

        //PAINT FOR CORNER
        Paint paintWhite =new Paint();
        paintWhite.setStyle(Paint.Style.STROKE);
        paintWhite.setColor(getResources().getColor(R.color.frame_corner));
        paint.setStrokeWidth(6);

        RectLeft = (deviceWidth-640)/2;
        RectTop = deviceHeight-(400+250+rlButtonHolder.getHeight());
        RectRight = deviceWidth-((deviceWidth-640)/2) ;
        //RectRight = RectLeft+ deviceWidth-100;

        RectBottom = deviceHeight-(250+rlButtonHolder.getHeight());
                //250+400;
    //deviceHeight-(rlButtonHolder.getHeight()+380);

        //Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);

        if(canvas==null){
            Log.e("SHITAB","CANVAS nai!!!");
        }
        else{
            Log.e("SHITAB","CANVAS ase!!!");

            //RECTANGLE FRAME DRAW
            //canvas.drawRect(rec,paint);
            canvas.drawRect(0, 0, deviceWidth, RectTop, paint);
            canvas.drawRect(0, RectTop, RectLeft, RectBottom, paint);
            canvas.drawRect(RectRight , RectTop, deviceWidth, RectBottom, paint);
            canvas.drawRect(0, RectBottom, deviceWidth, deviceHeight, paint);

            //EDGE LINE DRAW
            canvas.drawLine(RectLeft, RectTop, RectLeft+50, RectTop, paintWhite); //  _
            canvas.drawLine(RectLeft, RectTop, RectLeft, RectTop+50, paintWhite); // |

            canvas.drawLine(RectLeft, RectBottom, RectLeft, RectBottom-50, paintWhite); // |
            canvas.drawLine(RectLeft, RectBottom, RectLeft+50, RectBottom, paintWhite); //  _

            canvas.drawLine(RectRight, RectTop, RectRight, RectTop+50, paintWhite); //  |
            canvas.drawLine(RectRight, RectTop, RectRight-50, RectTop, paintWhite); // _

            canvas.drawLine(RectRight, RectBottom, RectRight, RectBottom-50, paintWhite); //  |
            canvas.drawLine(RectRight, RectBottom, RectRight-50, RectBottom, paintWhite); // _

            holderTransparent.unlockCanvasAndPost(canvas);
        }

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                200);
    }

    @Override
    public void onAutoFocusMoving(boolean start, Camera camera) {

    }

    private Camera.PictureCallback getPictureCallback() {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Intent intent = new Intent(ShebaCameraActivity.this, TakenPictureActivity.class);
                startActivity(intent);
            }
        };
        return picture;
    }
}
