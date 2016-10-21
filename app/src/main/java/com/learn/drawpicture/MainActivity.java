package com.learn.drawpicture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MySurfaceView mMySurfaceView;
    private Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }



    private void initViews() {
        mMySurfaceView = (MySurfaceView) findViewById(R.id.my_surface_view);


    }

    @Override
    public void onBackPressed() {
        if(!mMySurfaceView.back()) {
            super.onBackPressed();
        }
    }
}
