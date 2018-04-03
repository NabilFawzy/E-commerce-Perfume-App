package com.example.nabil.e_commerceproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nabil on 05-Dec-17.
 */
public class myDbHelper extends SQLiteOpenHelper {

    private static String dbname="shoppingDb";
    SQLiteDatabase shoppingDb;

    public myDbHelper(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table customers(CustID integer primary key autoincrement," +
                "cutname text not null,username text not null, password text not null," +
                "gender text not null,birthday text not null,job text not null,unclename text not null)");

        sqLiteDatabase.execSQL("create table orders(ordID integer primary key," +
                "ordDate text ,custIDF integer , address text," +
                "FOREIGN KEY(custIDF) REFERENCES customers(CustID) )");

        sqLiteDatabase.execSQL("create table categories(catID integer primary key, catname text not null )");

        sqLiteDatabase.execSQL("create table products(prodID integer primary key, prodname text not null," +
                " price real not null , quantity integer not null,imgname text not null,catIDf integer not null," +
                " FOREIGN KEY(catIDf) REFERENCES categories(catID))");

        sqLiteDatabase.execSQL("create table orderDetails(ordIDf integer not null, prodIDf integer not null" +
                ", quantityDetails integer not null, PRIMARY KEY(ordIDf,prodIDf), " +
                " FOREIGN KEY(ordIDf) REFERENCES orders(ordID)" +
                ",FOREIGN KEY(prodIDf) REFERENCES products(prodID))");

    }

    public Cursor getOrderDetails(){
          shoppingDb=getReadableDatabase();
        String []col={"ordIDf","prodIDf","quantityDetails"};
        Cursor cursor=shoppingDb.query("orderDetails",col,null,null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }


    public void AddOrderDetails(int ordIDf,int prodIDf,int quantityDetails){
        shoppingDb=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ordIDf",ordIDf);
        contentValues.put("prodIDf",prodIDf);
        contentValues.put("quantityDetails",quantityDetails);
        shoppingDb.insert("orderDetails",null,contentValues);
        shoppingDb.close();
    }


    public int getNewOrderID(){
        shoppingDb=getReadableDatabase();
        String []args={"ordID"};
        Cursor corder=shoppingDb.query("orders",args,null,null,null,null,null);
        if(corder==null)
            return 1;
        int cont=corder.getCount();
        return  cont+1;
    }

    public void AddOrder(int ordID,String ordDate,int custIDF,String address){
        shoppingDb=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ordID",ordID);
        contentValues.put("ordDate",ordDate);
        contentValues.put("custIDF",custIDF);
        contentValues.put("address",address);
        shoppingDb.insert("orders",null,contentValues);
        shoppingDb.close();
    }


    public void AddCategries(int catID,String catname){
        shoppingDb=getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("catID",catID);
        contentValues.put("catname",catname);
        shoppingDb.insert("categories",null,contentValues);
        shoppingDb.close();
    }

    public int getQuantityOrderDetailsExisting(int prodID,int custIDF){
        shoppingDb=getReadableDatabase();
        Cursor c=shoppingDb.rawQuery("select * from  orders inner join orderDetails on orders.ordID = orderDetails.ordIDf where orderDetails.prodIDf = '"+prodID+"' and orders.custIDF = '"+custIDF+"'",null);
         c.moveToFirst();
        return c.getInt(c.getColumnIndex("quantityDetails"));
    }

    public boolean IsExistOrderwithPeoduct(int prodID,int custIDF){
        shoppingDb=getReadableDatabase();
        Cursor c=shoppingDb.rawQuery("select * from  orders inner join orderDetails on orders.ordID = orderDetails.ordIDf where orderDetails.prodIDf = '"+prodID+"' and orders.custIDF = '"+custIDF+"'",null);
      int i=  c.getCount();
        if(i==0)
           return false;
        else
            return true;
    }

    public void RemoveOrder(int prodID,int custIDF){
        shoppingDb=getReadableDatabase();
        Cursor c=shoppingDb.rawQuery("select * from  orders inner join orderDetails on orders.ordID = orderDetails.ordIDf where orderDetails.prodIDf = '"+prodID+"' and orders.custIDF = '"+custIDF+"'",null);
        if(c.getCount()!=0) {
            c.moveToFirst();
            int idOrdr = c.getInt(c.getColumnIndex("ordID"));
            shoppingDb.close();
            shoppingDb = getWritableDatabase();
            shoppingDb.delete("orderDetails", "ordIDf ='" + idOrdr + "'", null);
            shoppingDb.delete("orders", "ordID ='" + idOrdr + "'", null);

        }
        shoppingDb.close();

    }


    public void AddtolastAddedOrder(int prodID,int quantity,int custIDF,String address){
        shoppingDb=getReadableDatabase();
        Cursor c=shoppingDb.rawQuery("select * from  orders inner join orderDetails on orders.ordID = orderDetails.ordIDf where orderDetails.prodIDf = '"+prodID+"' and orders.custIDF = '"+custIDF+"'",null);
        if(c!=null)
            c.moveToFirst();
        int idOrdr= c.getInt(c.getColumnIndex("ordID"));
        String oldAddress= c.getString(c.getColumnIndex("address"));
        int oldAndNewQuantity= c.getInt(c.getColumnIndex("quantityDetails"))+quantity;
        if(address!=null){
            String upd="Update orders set address=? where ordID = '"+idOrdr+"'";
            shoppingDb.execSQL(upd, new String[]{address});
        }
        String upd="Update orderDetails set quantityDetails='"+oldAndNewQuantity+"' where ordIDf = '"+idOrdr+"'";
        shoppingDb.execSQL(upd, new String[]{});

    }
    public void EditpasswordwithNew(String password,String username){
        shoppingDb=getReadableDatabase();
        String upd="Update customers set password=? where username=?";
        shoppingDb.execSQL(upd, new String[]{password,username});
    }

    public void EditYourOrder(int prodID,int custIDF,String address,int quantity){
        shoppingDb=getReadableDatabase();
        Cursor c=shoppingDb.rawQuery("select * from  orders inner join orderDetails on orders.ordID = orderDetails.ordIDf where orderDetails.prodIDf = '"+prodID+"' and orders.custIDF = '"+custIDF+"'",null);
        if(c!=null)
            c.moveToFirst();
        int idOrdr= c.getInt(c.getColumnIndex("ordID"));
        String oldAddress= c.getString(c.getColumnIndex("address"));
       int cc= c.getInt(c.getColumnIndex("quantityDetails"));
        if(address!=null){
            String upd="Update orders set address=? where ordID = '"+idOrdr+"'";
            shoppingDb.execSQL(upd, new String[]{address});
        }
            String upd="Update orderDetails set quantityDetails='"+quantity+"' where ordIDf = '"+idOrdr+"'";
            shoppingDb.execSQL(upd, new String[]{});


    }

    public void editprodctQuantity(int prodID,int quantity){
        shoppingDb=getReadableDatabase();
       int editedQuantity=-1*quantity;
        String [] col={"prodID","quantity"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);

        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("prodID"))==prodID) {
                    editedQuantity+=cursor.getInt(cursor.getColumnIndex("quantity"));
                    break;
                }
            }
        }
        String upd="Update products set quantity='"+editedQuantity+"' where prodID = '"+prodID+"'";
        shoppingDb.execSQL(upd,  new String[]{});
    }

    public TheProduct getproductByID(int prodID){
        TheProduct product=new TheProduct();
        shoppingDb=getReadableDatabase();

        String [] col={"prodID","prodname","price","quantity","imgname","catIDf"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("prodID"))==prodID) {
                    product.prodname=cursor.getString(cursor.getColumnIndex("prodname"));
                    product.price=cursor.getDouble(cursor.getColumnIndex("price"));
                    product.quantity=cursor.getInt(cursor.getColumnIndex("quantity"));
                    product.imgname=cursor.getString(cursor.getColumnIndex("imgname"));
                    product.catIDf=cursor.getInt(cursor.getColumnIndex("catIDf"));
                    break;
                }
            }
        }
        return product;
    }


    public int getprodctQuantity(int prodID){
        shoppingDb=getReadableDatabase();
        int quantity=0;
        String [] col={"prodID","quantity"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("prodID"))==prodID) {
                    quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                    break;
                }
            }
        }
        return quantity;
    }

    public int getprodctID(String prodname){
        shoppingDb=getReadableDatabase();
        int prodID=1;
        String [] col={"prodID","prodname"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("prodname")).equals(prodname)) {
                    prodID = cursor.getInt(cursor.getColumnIndex("prodID"));
                    break;
                }
            }
        }
        return prodID;
    }

    public int getCatID(String catname){
        shoppingDb=getReadableDatabase();
        int idCat=1;
        String [] col={"catID","catname"};
        Cursor cursor=shoppingDb.query("categories",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("catname")).equals(catname)) {
                    idCat = cursor.getInt(cursor.getColumnIndex("catID"));
                    break;
                }
            }
        }
        return idCat;
    }


    public void DeleteCategries(String catname){
        shoppingDb=getWritableDatabase();
        shoppingDb.delete("categories","catname='"+catname+"'",null);
        shoppingDb.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" alter table orderDetails");
        sqLiteDatabase.execSQL(" alter table products");
        sqLiteDatabase.execSQL(" alter table categories");
        sqLiteDatabase.execSQL(" alter table orders");
        sqLiteDatabase.execSQL(" alter table customers");
        onCreate(sqLiteDatabase);
    }

    public boolean IsNameproductExist(String prodname){
        shoppingDb=getReadableDatabase();
        String [] col={"prodname"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);
        if(cursor!=null) {
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex("prodname")).equals(prodname)){
                    return true;
                }
            }
            return false;}
        return false;
    }

    public void Deleteproduct(String prodname){
        shoppingDb=getWritableDatabase();
        shoppingDb.delete("products","prodname='"+prodname+"'",null);
        shoppingDb.close();
    }


    public void AddnewProduct(String prodname,double price,int quantity,String imgname,int catIDf){
        shoppingDb =getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("prodname",prodname);
        contentValues.put("price",price);
        contentValues.put("quantity",quantity);
        contentValues.put("imgname",imgname);
        contentValues.put("catIDf",catIDf);
        shoppingDb.insert("products",null,contentValues);
        shoppingDb.close();
    }


    public Cursor GetProducts(){
        shoppingDb=getReadableDatabase();
        String [] col={"prodname","price","quantity","imgname","catIDf"};
        Cursor cursor=shoppingDb.query("products",col,null,null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();

        shoppingDb.close();
        return cursor;
    }


    public void AddNewUser(String cutname,String username,String password,String gender,String birthday,String job,String unclename){
        shoppingDb =getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cutname",cutname);
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("gender",gender);
        contentValues.put("birthday",birthday);
        contentValues.put("job",job);
        contentValues.put("unclename",unclename);
        shoppingDb.insert("customers",null,contentValues);
        shoppingDb.close();
    }


    public Cursor ShowCategories(){
        shoppingDb=getReadableDatabase();
        String [] col={"catname"};
        Cursor cursor=shoppingDb.query("categories",col,null,null,null,null,null);
        if(cursor!=null)
        cursor.moveToFirst();

        shoppingDb.close();
        return cursor;
    }

    public int getCustomerID(String email){
        shoppingDb=getReadableDatabase();
        int iduser=0;
        String [] col={"cutname","CustID"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("cutname")).equals(email)) {
                    iduser = cursor.getInt(cursor.getColumnIndex("cutname"));
                    break;
                }
            }
        }
        return iduser;
    }



    public String getName(String email){
        shoppingDb=getReadableDatabase();
        String name="";
        String [] col={"cutname","username"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("username")).equals(email)) {
                    name = cursor.getString(cursor.getColumnIndex("cutname"));
                    break;
                }
            }
        }
        return name;
    }


    public String getUnclename(String username){
        shoppingDb=getReadableDatabase();
        String name="";
        String [] col={"username","unclename"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("username")).equals(username)) {
                    name = cursor.getString(cursor.getColumnIndex("unclename"));
                    break;
                }
            }
        }
        return name;
    }
    public String getBirthday(String cutname){
        shoppingDb=getReadableDatabase();
        String name="";
        String [] col={"cutname","birthday"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("cutname")).equals(cutname)) {
                    name = cursor.getString(cursor.getColumnIndex("birthday"));
                    break;
                }
            }
        }
        return name;
    }


    public boolean IsEmailExist(String username){
        shoppingDb=getReadableDatabase();
        String [] col={"username"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
        if(cursor!=null) {
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex("username")).equals(username)){
                    return true;
                }
            }
            return false;}
        return false;
    }
    public boolean ValidateEmailAndPass(String username,String password){
        shoppingDb=getReadableDatabase();
        String [] col={"username","password"};
        Cursor cursor=shoppingDb.query("customers",col,null,null,null,null,null);
       if(cursor!=null) {
       for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
           String r1=cursor.getString(cursor.getColumnIndex("username"));
           String r2=cursor.getString(cursor.getColumnIndex("password"));
           if(cursor.getString(cursor.getColumnIndex("username")).equals(username)&&cursor.getString(cursor.getColumnIndex("password")).equals(password)){

               return true;
           }
       }
        return false;}
        return false;
    }

}

