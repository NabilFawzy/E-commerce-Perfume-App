package com.example.nabil.e_commerceproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final myDbHelper shopping=new myDbHelper(this);


        final EditText name=(EditText)findViewById(R.id.etUserNameup);
        final EditText email=(EditText)findViewById(R.id.etEmailup);
        final EditText job=(EditText)findViewById(R.id.etJobup);
        final EditText password=(EditText)findViewById(R.id.etPasswordup);
        final EditText day=(EditText)findViewById(R.id.day);
        final EditText month=(EditText)findViewById(R.id.month);
        final EditText year=(EditText)findViewById(R.id.year);
        final EditText unclename=(EditText)findViewById(R.id.uncleNMame);


        final RadioButton radioButtonMale=(RadioButton)findViewById(R.id.RBMale);
        final RadioButton radioButtonFemale=(RadioButton)findViewById(R.id.RBFemale);
        Button ButSignUp=(Button)findViewById(R.id.btsnup);

        ButSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!shopping.IsEmailExist(email.getText().toString())) {
                   String gender = "Male";
                   if (radioButtonMale.isChecked())
                       gender = "Male";
                   else if (radioButtonFemale.isChecked())
                       gender = "Female";
                   String birthday = year.getText().toString() + "/" + month.getText().toString() + "/" + day.getText().toString();
                   shopping.AddNewUser(name.getText().toString(), email.getText().toString(), password.getText().toString(), gender, birthday, job.getText().toString(),unclename.getText().toString());
                   Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();
                   Intent signIn = new Intent(getApplicationContext(), signin.class);
                   startActivity(signIn);
               }
                else{
                   Toast.makeText(getApplicationContext(),"Not Added email is used",Toast.LENGTH_LONG).show();

               }

            }
        });
    }
}
