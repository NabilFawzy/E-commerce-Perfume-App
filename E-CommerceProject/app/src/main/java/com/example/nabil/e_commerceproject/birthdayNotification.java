package com.example.nabil.e_commerceproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class birthdayNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_notification);

        String name=getIntent().getExtras().getString("name");
        TextView textView=(TextView)findViewById(R.id.hbdtou);
        textView.setText(name);
    }
}
