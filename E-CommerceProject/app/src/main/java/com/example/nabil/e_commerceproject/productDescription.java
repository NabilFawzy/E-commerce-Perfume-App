package com.example.nabil.e_commerceproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class productDescription extends AppCompatActivity {
     myDbHelper Shopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        ImageView imageView=(ImageView)findViewById(R.id.imgdes);
        TextView  textViewDescription=(TextView)findViewById(R.id.tv1des) ;
        TextView  textViewQuantity=(TextView)findViewById(R.id.tv2des) ;
        TextView  textViewPrice=(TextView)findViewById(R.id.tv3des) ;

        Bundle pro=getIntent().getExtras().getBundle("pro");
        String imgname=pro.getString("imgname");
        int res=getResources().getIdentifier(imgname,"drawable",getPackageName());
        imageView.setImageResource(res);
        textViewDescription.setText(String.valueOf(pro.getString("prodname")));
        textViewQuantity.setText("Quantity : "+String.valueOf(pro.getInt("quantity"))+" Piece");
        textViewPrice.setText("Price : "+String.valueOf(pro.getDouble("price"))+"$");

      final int idPro= pro.getInt("idPro");
       Shopping=new myDbHelper(this);
        SharedPreferences pref = getSharedPreferences("rememb",MODE_PRIVATE);
        final String myemail = pref.getString("email", null);

        final EditText AddQuantity=(EditText)findViewById(R.id.etAddurquantity);
        final EditText littleAddress=(EditText)findViewById(R.id.etAddress);
        final Spinner  SmallCountry=(Spinner)findViewById(R.id.spsmallcontries);
        final int idCustomer=Shopping.getCustomerID(myemail);
        Button btaddtoCart=(Button)findViewById(R.id.btaddtoCart);

        btaddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Shopping=new myDbHelper(getApplicationContext());
                String Address=littleAddress.getText().toString();
                if(SmallCountry.getSelectedItem().toString().equals("Cairo")){
                    Address+="/Cairo";
                }
                else if(SmallCountry.getSelectedItem().toString().equals("Alexandria")){
                    Address+="/Alexandria";
                }
                else if(SmallCountry.getSelectedItem().toString().equals("Menoufia")){
                    Address+="/Menoufia";
                }
                int mquantity=0;
                if(!AddQuantity.getText().toString().equals(""))
                    mquantity=Integer.parseInt(AddQuantity.getText().toString());
                Calendar calendar=Calendar.getInstance();
                String year=String.valueOf((calendar.get(Calendar.YEAR)));
                String month=String.valueOf((calendar.get(Calendar.MONTH)+1));
                String day=String.valueOf((calendar.get(Calendar.DAY_OF_MONTH)));
                String date=year+"/"+month+"/"+day;
                int idOrder=Shopping.getNewOrderID();
                boolean check=Shopping.IsExistOrderwithPeoduct(idPro,idCustomer);
                int Availablequantityforproduct=Shopping.getprodctQuantity(idPro);
                if(check==false) {
                    if(Availablequantityforproduct>=mquantity){
                    Shopping.AddOrder(idOrder, date, idCustomer, Address);
                    Shopping.AddOrderDetails(idOrder, idPro,mquantity);
                        Toast.makeText(getApplicationContext(),"Added to your cart ",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Your order over the available products",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                   int savedQuantity= Shopping.getQuantityOrderDetailsExisting(idPro,idCustomer);

                    if(Availablequantityforproduct>=savedQuantity+mquantity){
                    Shopping.AddtolastAddedOrder( idPro, mquantity, idCustomer, Address);
                        Toast.makeText(getApplicationContext(),"Added to your cart ",Toast.LENGTH_LONG).show();
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"Over the available products your last order "+String.valueOf(savedQuantity)+" Piece",Toast.LENGTH_LONG).show();
                    }


                }

            }
        });
        Button btedittoCart=(Button)findViewById(R.id.btedittoCart);
        btedittoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=Shopping.IsExistOrderwithPeoduct(idPro,idCustomer);
                if(check==true) {
                    Shopping = new myDbHelper(getApplicationContext());
                    String Address = littleAddress.getText().toString();
                    if (SmallCountry.getSelectedItem().toString().equals("Cairo")) {
                        Address += "/Cairo";
                    } else if (SmallCountry.getSelectedItem().toString().equals("Alexandria")) {
                        Address += "/Alexandria";
                    } else if (SmallCountry.getSelectedItem().toString().equals("Menoufia")) {
                        Address += "/Menoufia";
                    }

                    int mquantity=0;
                    if(!AddQuantity.getText().toString().equals(""))
                        mquantity=Integer.parseInt(AddQuantity.getText().toString());

                    int Availablequantityforproduct=Shopping.getprodctQuantity(idPro);
                    if(Availablequantityforproduct>=mquantity){
                    Shopping.EditYourOrder(idPro, idCustomer, Address,mquantity );
                    Toast.makeText(getApplicationContext(), "your cart Edited", Toast.LENGTH_LONG).show();}
                    else{
                        Toast.makeText(getApplicationContext(), "your order over Available Pieces", Toast.LENGTH_LONG).show();}
                       }
                else {
                    Toast.makeText(getApplicationContext(), "your cart hasnot this product", Toast.LENGTH_LONG).show();
                     }
                }

        });

        ImageButton imageButtondel=(ImageButton)findViewById(R.id.imageButtondel);
        imageButtondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Shopping.RemoveOrder(idPro,idCustomer);
                Toast.makeText(getApplicationContext(),"Removed From the Cart",Toast.LENGTH_LONG).show();
            }
        });



    }
}
