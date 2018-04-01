package com.example.admin.upload;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Admin on 18/11/2017.
 */

public class ClothesList extends AppCompatActivity {
   ArrayList<Clothes> list;
   GridView gridView;
    ClothesListAdapter adapter=null;
    Cursor cursor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothes_list_activity);
    gridView=(GridView)findViewById(R.id.gridView);
        list=new ArrayList<>();
        adapter=new ClothesListAdapter(this,R.layout.clothes_items,list);
           gridView.setAdapter(adapter);

        try {
            cursor = MainActivity.sqLiteHelper.getData();
            list.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id1 = cursor.getString(0);
                int id = Integer.parseInt(id1);
                String name = cursor.getString(1);
                String price = cursor.getString(2);
                String details = cursor.getString(3);
                byte[] image = cursor.getBlob(4);
                list.add(new Clothes(id, name, price, details, image));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Msg:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                CharSequence [] items={"Update","Delete"};
                AlertDialog.Builder dialog=new AlertDialog.Builder(ClothesList.this);
                dialog.setTitle("Choose an Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                           if (item==0){
                           Cursor c=MainActivity.sqLiteHelper.getData();
                           ArrayList<Integer> arrID=new ArrayList<Integer>() ;
                               while (c.moveToNext()){
                                   arrID.add(c.getInt(0));
                               }

                              showDialogUpdate(ClothesList.this,arrID.get(position));
                               // Toast.makeText(getApplicationContext(),"Update...",Toast.LENGTH_SHORT).show();
                           }else {
                               Cursor c=MainActivity.sqLiteHelper.getData();
                               ArrayList<Integer> arrID=new ArrayList<Integer>() ;
                               while (c.moveToNext()){
                                   arrID.add(c.getInt(0));
                               }
                                  showDialogDelete(arrID.get(position));
                              // Toast.makeText(getApplicationContext(),"Delete...",Toast.LENGTH_SHORT).show();
                           }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }
    ImageView imageViewClothes;
    Button btnUpdate;
    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.update_clothes_activity);
        dialog.setTitle("Edit");
         imageViewClothes=(ImageView)dialog.findViewById(R.id.imageViewClothes);
        final EditText edtName=(EditText)dialog.findViewById(R.id.edtName);
        final EditText edtPrice=(EditText)dialog.findViewById(R.id.edtPrice);
        final EditText edtDetails=(EditText)dialog.findViewById(R.id.edtDetails);

        btnUpdate=(Button)dialog.findViewById(R.id.btnUpdate);

        int width= (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height= (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();
        imageViewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ClothesList.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},888);
            }
        });

    btnUpdate.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (edtName.getText().toString().equals("")||edtPrice.getText().toString().equals("")|| edtDetails.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Some information missing!",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    System.out.println("--------------------result here---------------------");
                    System.out.println(position);
                    //System.out.println(edtPrice);
                    //  System.out.println(editDetails.getText().toString().trim());
                    MainActivity.sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            edtDetails.getText().toString().trim(


                            ),
                            MainActivity.imageViewToByte(imageViewClothes),
                            position
                    );
                    dialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Your post is updated", Toast.LENGTH_SHORT).show();


                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }

            }
            updateClothesList();
            showNotificationUpdate();
        }
    });

    }
    private void showDialogDelete(final int idClothes){
      AlertDialog.Builder dialogDelete= new AlertDialog.Builder(ClothesList.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to delete this post");
         dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int i) {
             try {
                 MainActivity.sqLiteHelper.deleteData(idClothes);
                 Toast.makeText(getApplicationContext(), "Your post is deleted", Toast.LENGTH_SHORT).show();
             }catch (Exception e){
               Log.e("Delete error!",e.getMessage());
             }
               updateClothesList();
             }
         });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
             dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    private void updateClothesList(){
        Cursor cursor=MainActivity.sqLiteHelper.getData();
        list.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name=cursor.getString(1);
            String price=cursor.getString(2);
            String details=cursor.getString(3);
            byte[] image=cursor.getBlob(4);
            list.add(new Clothes(id,name,price,details,image));



        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==888){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent((Intent.ACTION_PICK));
                intent.setType("image/*" );
                startActivityForResult(intent,888);
            }
            else{
                Toast.makeText(getApplicationContext(),"You don't have permission to access file location",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {

        if (requestCode==888 && data!=null){

            Uri uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

                imageViewClothes.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode,resultCode, data);
    }
    public void showNotificationUpdate(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.bell);
        builder.setContentTitle("Post Added");
        builder.setContentText("Some changes happened to a post.");
        Intent intent=new Intent(this,ClothesList.class);
        TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ClothesList.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager NM= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(0,builder.build());
    }

}
