package com.example.nabil.e_commerceproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    ArrayList<TheProduct> prodData=new ArrayList<TheProduct>();
    ArrayList<TheProduct> prodDatabuf=new ArrayList<TheProduct>();
    TheProduct prod;
    ThProductAdapter ProductAdapter=null;
    ListView lvproducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final myDbHelper shopping=new myDbHelper(this);
        final Spinner catspin=(Spinner)findViewById(R.id.spCategory);
        lvproducts=(ListView)findViewById(R.id.ListViewProducts);


        String custname= getIntent().getExtras().getString("thename");
        String birthday=shopping.getBirthday(custname);
       Calendar calendar=Calendar.getInstance();

        String month=String.valueOf((calendar.get(Calendar.MONTH)+1));
        String day=String.valueOf((calendar.get(Calendar.DAY_OF_MONTH)));
        String date="/"+month+"/"+day;
        if(birthday.contains(date))
          {
              NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
              builder.setSmallIcon(R.drawable.iconperf);
              builder.setContentTitle("Happy Birthday : )");
              builder.setContentText("We Celebrate with u, wishing be the happies birthday ");

              Intent i=new Intent(this,birthdayNotification.class);
              i.putExtra("name",custname);
              TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
              stackBuilder.addParentStack(birthdayNotification.class);
              stackBuilder.addNextIntent(i);
              PendingIntent pendingIntent=stackBuilder.getPendingIntent(5,PendingIntent.FLAG_UPDATE_CURRENT);
              builder.setContentIntent(pendingIntent);
              NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              nm.notify(5,builder.build());
          }





        Cursor products=shopping.GetProducts();
      if(products!=null) {
          for (products.moveToFirst(); !products.isAfterLast(); products.moveToNext()) {
              prod = new TheProduct(products.getString(products.getColumnIndex("prodname")),
                      products.getDouble(products.getColumnIndex("price")),
                      products.getInt(products.getColumnIndex("quantity")),
                      products.getString(products.getColumnIndex("imgname")),
                      products.getInt(products.getColumnIndex("catIDf")));
              prodData.add(prod);

          }
          prodDatabuf=prodData;
          ProductAdapter=new ThProductAdapter(getApplicationContext(),R.layout.activity_row,prodData);
          if(lvproducts!=null){
              lvproducts.setAdapter(ProductAdapter);}
      }
        catspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(catspin.getSelectedItem().toString().equals("ALL")){
                    ProductAdapter=new ThProductAdapter(getApplicationContext(),R.layout.activity_row,prodData);
                    prodDatabuf=prodData;
                    if(lvproducts!=null){
                        lvproducts.setAdapter(ProductAdapter);}
                }
                else if(catspin.getSelectedItem().toString().equals("MEN")){

                    ArrayList<TheProduct> newprodData=new ArrayList<TheProduct>();
                    int idknown=shopping.getCatID("MEN");
                    for(TheProduct pro:prodData){
                        if(pro.catIDf==idknown){
                            newprodData.add(pro);
                        }
                    }
                    prodDatabuf=newprodData;
                    ProductAdapter=new ThProductAdapter(Home.this,R.layout.activity_row,newprodData);
                    lvproducts.setAdapter(ProductAdapter);
                }
                else if(catspin.getSelectedItem().toString().equals("WOMEN")){

                    ArrayList<TheProduct> newprodData=new ArrayList<TheProduct>();
                    int idknown=shopping.getCatID("WOMEN");
                    for(TheProduct pro:prodData){
                        if(pro.catIDf==idknown){
                            newprodData.add(pro);
                        }
                    }
                    prodDatabuf=newprodData;
                    ProductAdapter=new ThProductAdapter(Home.this,R.layout.activity_row,newprodData);
                    lvproducts.setAdapter(ProductAdapter);

                }

                lvproducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle pro=new Bundle();
                       pro.putString("prodname",prodDatabuf.get(position).prodname);
                        pro.putString("imgname",prodDatabuf.get(position).imgname);
                        pro.putDouble("price",prodDatabuf.get(position).price);
                        pro.putInt("quantity",prodDatabuf.get(position).quantity);
                        pro.putInt("catIDf",prodDatabuf.get(position).catIDf);
                        pro.putInt("idPro",shopping.getprodctID(prodDatabuf.get(position).prodname));
                        Intent productDes=new Intent(getApplicationContext(),productDescription.class);
                        productDes.putExtra("pro",pro);
                        startActivity(productDes);
                        return false;
                    }
                });

            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu1,menu);
        String custname= getIntent().getExtras().getString("thename");
        if(custname!=null) {
            MenuItem item = menu.findItem(R.id.customerName);
            item.setTitle(custname);
        }


        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 if(newText!=null&&!newText.isEmpty()){
                     ArrayList<TheProduct> newprodData=new ArrayList<TheProduct>();
                     for(TheProduct pro:prodDatabuf){
                         if(pro.prodname.toUpperCase().contains(newText.toUpperCase())||
                                 String.valueOf(pro.price).contains(newText)||
                                 String.valueOf(pro.quantity).contains(newText)){
                             newprodData.add(pro);
                         }
                     }
                     ProductAdapter=new ThProductAdapter(Home.this,R.layout.activity_row,newprodData);
                         lvproducts.setAdapter(ProductAdapter);

                 }
                else{
                     ProductAdapter=new ThProductAdapter(Home.this,R.layout.activity_row,prodDatabuf);
                     lvproducts.setAdapter(ProductAdapter);
                 }
             return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.mcart){
            Intent cart=new Intent(getApplicationContext(),myCart.class);
            startActivity(cart);
        }

      if(item.getItemId()==R.id.customerName){
          String email= getIntent().getExtras().getString("themail");
          if(email.equals("nabil@ya.com")){
              Intent Admn=new Intent(getApplicationContext(),AdminPage.class);
              startActivity(Admn);
          }
      }
       if(item.getItemId()==R.id.logout){
           SharedPreferences sharedPreferences = getSharedPreferences("rememb", MODE_PRIVATE);
           SharedPreferences.Editor editor = sharedPreferences.edit();
           editor.putString("email",null);
           editor.putString("password",null);
           editor.putString("custrname", null);
           editor.commit();
           Intent MainAct=new Intent(this,MainActivity.class);
           startActivity(MainAct);
       }
        if(item.getItemId()==R.id.searchvoice){
            Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Up soon!");
            if(i.resolveActivity(getPackageManager())!=null){
                startActivityForResult(i,10);

            }
            else{
                Toast.makeText(getApplicationContext(),"doesnot support",Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       lvproducts=(ListView)findViewById(R.id.ListViewProducts);
        ArrayList<TheProduct>newBufferlistproducts=new ArrayList<TheProduct>();
        if(requestCode==10&&resultCode==RESULT_OK){
            ArrayList<String>arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(TheProduct  r : prodDatabuf){
                for (String y : arrayList){
                    if(r.prodname.toUpperCase().contains(y.toUpperCase())){
                        newBufferlistproducts.add(r);
                    }
                }
            }

            lvproducts.setAdapter(new ThProductAdapter(getApplicationContext(),R.layout.activity_row,newBufferlistproducts));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
