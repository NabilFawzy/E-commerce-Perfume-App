package com.example.nabil.e_commerceproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nabil on 08-Dec-17.
 */
public class ThProductAdapter extends ArrayAdapter<TheProduct>{

    Context mContext;
    Integer mLayoutID;
    ArrayList<TheProduct> productdata;

    public ThProductAdapter(Context context, int resource, ArrayList<TheProduct> productdata) {
        super(context, resource,productdata);
        this.mContext = context;
        this.mLayoutID = resource;
        this.productdata = productdata;
    }

    @Override
    public int getPosition(TheProduct item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        PlaceHolder  placeHolder=null;
        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(mContext);
            row=inflater.inflate(mLayoutID,parent,false);
            placeHolder=new PlaceHolder();
            placeHolder.nameView=(TextView)row.findViewById(R.id.tv1);
            placeHolder.sView=(TextView)row.findViewById(R.id.tv2);
            placeHolder.quantity=(TextView)row.findViewById(R.id.tv5);
            placeHolder.imageView=(ImageView)row.findViewById(R.id.img);
            row.setTag(placeHolder);
        }
        else{
            placeHolder=(PlaceHolder) row.getTag();
        }

        TheProduct theProduct=productdata.get(position);
        placeHolder.imageView.setOnClickListener(PopupListener);
        Integer rowPostion=position;

        placeHolder.imageView.setTag(rowPostion);
        placeHolder.nameView.setText(theProduct.prodname);
        placeHolder.quantity.setText("Quantity : "+String.valueOf(theProduct.quantity)+" Piece");
        placeHolder.sView.setText("Price : "+String.valueOf(theProduct.price)+"$");

        int res=mContext.getResources().getIdentifier(theProduct.imgname,"drawable",mContext.getPackageName());
        placeHolder.imageView.setImageResource(res);
        return row;
    }

    private static class PlaceHolder{
        ImageView imageView;
        TextView nameView;
        TextView quantity;
        TextView sView;
    };


    View.OnClickListener PopupListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer viewPos=(Integer)view.getTag();
            TheProduct p=productdata.get(viewPos);
            Toast.makeText(getContext(),p.prodname,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

}
