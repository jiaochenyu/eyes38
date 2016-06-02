package com.example.eyes38.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.eyes38.R;

public class HomexptjActivity extends AppCompatActivity {
  TextView mTextView;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homexptj);
       mTextView = (TextView) findViewById(R.id.nogoods_title);
        Intent intent = getIntent();
        name=intent.getStringExtra("values");
       mTextView.setText(name);
    }

    private void initView() {
    }

    public void nogoods(View view) {
        finish();
    }
}
