package com.example.admin.upload;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    EditText editName,editPrice,editDetails;
    Button btnChoose,btnAdd,btnList;
    ImageView imageView;
    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY=999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sqLiteHelper=new SQLiteHelper(this,"PostAdDB.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS ClothesAd(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,price VARCHAR, details VARCHAR,image BLOG)");
    btnChoose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
        }
    });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if (editName.getText().toString().equals("")||editPrice.getText().toString().equals("")|| editDetails.getText().toString().equals("")) {
    Toast.makeText(getApplicationContext(),"Some information missing!",Toast.LENGTH_SHORT).show();
}else {
                try {

                    sqLiteHelper.insertData(
                            editName.getText().toString().trim(),
                            editPrice.getText().toString().trim(),
                            editDetails.getText().toString().trim(),
                            imageViewToByte(imageView));
                    Toast.makeText(getApplicationContext(), "Your post added successfully", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editPrice.setText("");
                    editDetails.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                showNotification();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent=new Intent(MainActivity.this,ClothesList.class);
                    startActivity(intent);
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Msg:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode==REQUEST_CODE_GALLERY){
           if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
               Intent intent=new Intent((Intent.ACTION_PICK));
               intent.setType("image/*" );
               startActivityForResult(intent,REQUEST_CODE_GALLERY);
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

       if (requestCode==REQUEST_CODE_GALLERY && data!=null){

           Uri uri=data.getData();
           try {
               InputStream inputStream=getContentResolver().openInputStream(uri);
               Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

               imageView.setImageBitmap(bitmap);

           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
       }
        super.onActivityResult(requestCode,resultCode, data);
    }

    private void init(){
        editName=(EditText)findViewById(R.id.editName);
        editPrice=(EditText)findViewById(R.id.editPrice);
        editDetails=(EditText)findViewById(R.id.editDetails);
        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnList=(Button)findViewById(R.id.btnList);
        imageView=(ImageView)findViewById(R.id.imageView);

    }


    public void showNotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.bell);
        builder.setContentTitle("Post Added");
        builder.setContentText("There ia a new offer posted");
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
