package com.example.nabil.e_commerceproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class myproductsfunctions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myproductsfunctions);

        final myDbHelper shopping=new myDbHelper(this);

        final EditText ProdName=(EditText)findViewById(R.id.etproductnameadd);
        final EditText Prodprice=(EditText)findViewById(R.id.priceadd);
        final EditText ProdQuantity=(EditText)findViewById(R.id.quantityadd);
        final EditText ProdImgName=(EditText)findViewById(R.id.imageNameadd);
        final EditText ProdCatID=(EditText)findViewById(R.id.categotyidadd);

        final EditText ProdNameRemov=(EditText)findViewById(R.id.productNameRemove);

        Button addproduct=(Button)findViewById(R.id.btnproductadd);
        Button removeproduct=(Button)findViewById(R.id.btnproductRemove);
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!shopping.IsNameproductExist(ProdName.getText().toString())){

                   shopping.AddnewProduct(ProdName.getText().toString(),Double.parseDouble(Prodprice.getText().toString())
                           ,Integer.parseInt(ProdQuantity.getText().toString()),ProdImgName.getText().toString(),
                           Integer.parseInt(ProdCatID.getText().toString()));
                   Toast.makeText(getApplicationContext(),"Product is Added",Toast.LENGTH_LONG).show();
                   ProdName.setText("");
                   Prodprice.setText("");
                   ProdQuantity.setText("");
                   ProdImgName.setText("");
                   ProdCatID.setText("");

               }
                else{
                   Toast.makeText(getApplicationContext(),"Product Name is exist change name please",Toast.LENGTH_LONG).show();
               }

            }
        });
        removeproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shopping.IsNameproductExist(ProdNameRemov.getText().toString())) {

                    shopping.Deleteproduct(ProdNameRemov.getText().toString());
                    Toast.makeText(getApplicationContext(), "Product is Removed", Toast.LENGTH_LONG).show();
                    ProdName.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Product not exist", Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}
