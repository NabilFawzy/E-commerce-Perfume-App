package com.example.nabil.e_commerceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signinB=(Button)findViewById(R.id.butSignIn);
        Button signupB=(Button)findViewById(R.id.butSignUp);

        SharedPreferences pref = getSharedPreferences("rememb",MODE_PRIVATE);
        String myemail = pref.getString("email", null);
        String myPassword = pref.getString("password", null);
        String custrname = pref.getString("custrname", null);
        if(myemail!=null&&myPassword!=null){
            Intent home=new Intent(getApplicationContext(),Home.class);
            home.putExtra("thename",custrname);
            home.putExtra("themail",myemail);
            startActivity(home);
        }


        signinB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinIntent=new Intent(getApplicationContext(),signin.class);
                startActivity(signinIntent);
            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent=new Intent(getApplicationContext(),signup.class);
                startActivity(signupIntent);
            }
        });

    }
}
