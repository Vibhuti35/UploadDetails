package com.example.admin.upload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;



public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
public void queryData(String sql){
    SQLiteDatabase database=getWritableDatabase();
    database.execSQL(sql);
}

public void insertData(String name, String price,String details, byte[] image){
    SQLiteDatabase database=getWritableDatabase();
    String sql="INSERT INTO ClothesAd VALUES(NULL,?,?,?,?)";
    SQLiteStatement statement=database.compileStatement(sql);
    statement.clearBindings();
    statement.bindString(1,name);
    statement.bindString(2,price);
    statement.bindString(3,details);
    statement.bindBlob(4,image);
    statement.executeInsert();
}

public void  updateData(String name,String price,String details,byte[] image,int id){
    SQLiteDatabase database=getWritableDatabase();
    String sql="UPDATE  ClothesAd SET name=?,price=?,details=?,image=? WHERE id=?";
    SQLiteStatement statement=database.compileStatement(sql);
    statement.bindString(1,name);
    statement.bindString(2,price);
    statement.bindString(3,details);
    statement.bindBlob(4,image);
    statement.bindDouble(5,(double)id);
    statement.execute();
    database.close();
}

public void deleteData(int id){
    SQLiteDatabase database=getWritableDatabase();
    String sql="DELETE FROM ClothesAd WHERE id=?";
    SQLiteStatement statement=database.compileStatement(sql);
    statement.clearBindings();
    statement.bindDouble(1,(double)id);
    statement.execute();
    database.close();
}

public Cursor getData(){
    SQLiteDatabase database=getWritableDatabase();
    Cursor cursor = database.rawQuery("SELECT * FROM ClothesAd", null);
   // return database.rawQuery("SELECT * FROM ClothesAd",null);
    return  cursor;
}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
