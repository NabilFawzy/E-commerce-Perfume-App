package com.example.nabil.e_commerceproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class categoriesfunctions extends AppCompatActivity {
    public ListView lv;
    public ArrayAdapter av;
    Button but;
    myDbHelper Shopping;
    public ArrayList<String> arrayList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shopping=new myDbHelper(getApplicationContext());
        setContentView(R.layout.activity_categoriesfunctions);
        arrayList=new ArrayList<String>();
        Cursor cursor=Shopping.ShowCategories();

        if(cursor!=null) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("catname")));
            }
        }

        lv=(ListView)findViewById(R.id.mlistCateg);
        but=(Button) findViewById(R.id.addCateg);
        av=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        if(av!=null)
            lv.setAdapter(av);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shopping=new myDbHelper(getApplicationContext());

                EditText editTextnum=(EditText)findViewById(R.id.categorynambr);
                EditText editText=(EditText)findViewById(R.id.categoryname);
                av.add(editText.getText().toString());
                Shopping.AddCategries(Integer.parseInt(editTextnum.getText().toString()),editText.getText().toString());
                editText.getText().clear();
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Shopping=new myDbHelper(getApplicationContext());
                String catnam=arrayList.get(i);
                arrayList.remove(i);
                av.notifyDataSetChanged();
                av.notifyDataSetInvalidated();
                Shopping.DeleteCategries(catnam);
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}
