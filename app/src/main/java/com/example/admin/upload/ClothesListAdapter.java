package com.example.admin.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class ClothesListAdapter extends BaseAdapter {
  private Context context;
    private int layout;
    private ArrayList<Clothes> clothesList;

    public ClothesListAdapter(Context context, int layout, ArrayList<Clothes> clothesList) {
        this.context = context;
        this.layout = layout;
        this.clothesList = clothesList;
    }


    @Override
    public int getCount() {
        return clothesList.size();
    }

    @Override
    public Object getItem(int position) {
        return clothesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textName,textPrice,textDetails;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
      View row=view;
        ViewHolder holder=new ViewHolder();
        if (row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout,null);
            holder.textName=(TextView)row.findViewById(R.id.textName);
            holder.textPrice=(TextView)row.findViewById(R.id.textPrice);
            holder.textDetails=(TextView)row.findViewById(R.id.textDetails);
            holder.imageView=(ImageView) row.findViewById(R.id.clothesImage);
            row.setTag(holder);

        }
        else {
            holder=(ViewHolder)row.getTag();
        }
        Clothes clothes=clothesList.get(position);
        holder.textName.setText(clothes.getName());
        holder.textPrice.setText(clothes.getPrice());
        holder.textDetails.setText(clothes.getDetails());
        byte[] clothesImage=clothes.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(clothesImage,0,clothesImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
