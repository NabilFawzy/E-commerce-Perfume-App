package com.example.nabil.e_commerceproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class resetforgetpassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final myDbHelper shopping=new myDbHelper(this);
        setContentView(R.layout.activity_resetforgetpassword);
        final EditText email=(EditText)findViewById(R.id.etEmailagain);
        final EditText unclename=(EditText)findViewById(R.id.etuncleagain);
        final EditText newpassagain=(EditText)findViewById(R.id.newpassagain);

        Button reset=(Button)findViewById(R.id.btinagain);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Boolean check= shopping.IsEmailExist(email.getText().toString());
                if(check==true){
                   String uncleinDB= shopping.getUnclename(email.getText().toString());
                    if(unclename.getText().toString().equals(uncleinDB)){
                        if(!newpassagain.getText().toString().equals("")){
                            shopping.EditpasswordwithNew(newpassagain.getText().toString(),email.getText().toString());
                            Toast.makeText(getApplicationContext(),"Password reset",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),signin.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"please fill new password field",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Sorry uncle's name not correct cannot reset",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"not valid email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
