package com.example.nabil.e_commerceproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Button prodcs=(Button)findViewById(R.id.productsffun);
        Button cat=(Button)findViewById(R.id.CategoriesFun);

        prodcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prod=new Intent(getApplicationContext(),myproductsfunctions.class);
                startActivity(prod);
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cat=new Intent(getApplicationContext(),categoriesfunctions.class);
                startActivity(Cat);
            }
        });

    }
}
