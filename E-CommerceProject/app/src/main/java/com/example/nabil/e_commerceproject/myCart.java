package com.example.nabil.e_commerceproject;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class myCart extends AppCompatActivity {
    ThProductAdapter ProductAdapter=null;
    ArrayList<TheProduct> prodData=new ArrayList<TheProduct>();
    ListView lvproducts;
    TheProduct prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);


        TextView tvorderprice=(TextView)findViewById(R.id.tvSubmitorderprice);
        double totalprice=0.0;
        final myDbHelper shopping=new myDbHelper(this);
        SharedPreferences pref = getSharedPreferences("rememb",MODE_PRIVATE);
        String myemail = pref.getString("email", null);
        final int userid=shopping.getCustomerID(myemail);
        lvproducts=(ListView)findViewById(R.id.ListViewProductsmycart);
        Cursor orderDetails=shopping.getOrderDetails();
        if(orderDetails!=null){
            for(orderDetails.moveToFirst();!orderDetails.isAfterLast();orderDetails.moveToNext()){
                int prodIDf= orderDetails.getInt(orderDetails.getColumnIndex("prodIDf"));
                int quantityDetails= orderDetails.getInt(orderDetails.getColumnIndex("quantityDetails"));
                prod=shopping.getproductByID(prodIDf);
                prod.quantity=quantityDetails;
                prodData.add(prod);
                totalprice+=((double)quantityDetails)*prod.price;
            }
        }
        tvorderprice.setText(String.valueOf(totalprice)+"$");
        ProductAdapter=new ThProductAdapter(getApplicationContext(),R.layout.activity_row,prodData);
        if(lvproducts!=null){
            lvproducts.setAdapter(ProductAdapter);}

      Button submit=(Button)findViewById(R.id.btinOrderNow);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor orderDetails=shopping.getOrderDetails();
                if(orderDetails!=null){
                    for(orderDetails.moveToFirst();!orderDetails.isAfterLast();orderDetails.moveToNext()){
                        int prodIDf= orderDetails.getInt(orderDetails.getColumnIndex("prodIDf"));
                        int quantityDetails= orderDetails.getInt(orderDetails.getColumnIndex("quantityDetails"));
                        shopping.editprodctQuantity(prodIDf,quantityDetails);
                        shopping.RemoveOrder(prodIDf,userid);
                    }
                }

                Toast.makeText(getApplicationContext(),"Successfully your order submitted",Toast.LENGTH_LONG).show();

            }
        });

    }
}
