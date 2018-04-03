package com.example.nabil.e_commerceproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        final CheckBox rememberMe=(CheckBox)findViewById(R.id.rememberMe);
        final EditText email=(EditText)findViewById(R.id.etEmailin) ;
        final EditText password=(EditText)findViewById(R.id.etPasswordin) ;

        Button  sign=(Button)findViewById(R.id.btin) ;
        final myDbHelper shopping=new myDbHelper(this);


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shopping.ValidateEmailAndPass(email.getText().toString(),password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"please check your if your email or password correct",Toast.LENGTH_LONG).show();
                }
                else{
                    String custrname=shopping.getName(email.getText().toString());
                    if(rememberMe.isChecked()) {
                        SharedPreferences sharedPreferences = getSharedPreferences("rememb", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putString("custrname", custrname);
                        editor.commit();
                    }

                   Intent home=new Intent(getApplicationContext(),Home.class);
                    home.putExtra("thename",custrname);
                    home.putExtra("themail",email.getText().toString());
                    startActivity(home);
                }
            }
        });
        Button  forgetbtn=(Button)findViewById(R.id.btforget) ;
        forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),resetforgetpassword.class);
                startActivity(i);
            }
        });


    }
}
