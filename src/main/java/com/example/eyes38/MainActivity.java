package com.example.eyes38;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.eyes38.fragment.CarFragment;

public class MainActivity extends AppCompatActivity {
    private CarFragment mCarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCarFragment = new CarFragment();
    }
}
