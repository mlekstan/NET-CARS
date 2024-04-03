package com.example.shop_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;




public class MainPageActivity extends AppCompatActivity {
    private static final String TAG = "MainPageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagemain);
        Log.d(TAG, "onCreate: MainPageActivity created successfully");
    }
}