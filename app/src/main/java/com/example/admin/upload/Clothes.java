package com.example.admin.upload;

/**
 * Created by Admin on 18/11/2017.
 */

public class Clothes {
    private int id;
    private String name;
    private String price;
    private String details;
    private byte[] image;

    public Clothes(int id, String name, String price,String details, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.image = image;
    }
    public int getId(){
        return id;
    }
    public void setId(){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price=price;
    }

    public String getDetails(){
        return details;
    }
    public void setDetails(String details){
        this.details=details;
    }
    public byte[] getImage(){
        return image;
    }
    public void setImage(byte[] image){
        this.image=image;
    }
}
