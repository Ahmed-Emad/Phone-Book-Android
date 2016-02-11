package com.example.ahmadbarakat.phonebook;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Ahmad Barakat on 204 / 23 / 14.
 */
public class Contact {
    private String Name, Phone, Email;
    private  int Id;
    Bitmap Bit;

    public String getName()
    {
        return Name;
    }
    public String getPhone()
    {
        return Phone;
    }
    public String getEmail()
    {
        return Email;
    }
    public int getId()
    {
        return Id;
    }
    public Bitmap getBitmap(){
        return Bit;
    }

    public void setName(String name)
    {
        Name = name;
    }
    public void setPhone(String number)
    {
        Phone = number;
    }
    public void setEmail(String email)
    {
        Email = email;
    }
    public void setId(int id)
    {
        Id = id;
    }
    public void setBitmap(Bitmap bitmap) {
        Bit = bitmap;
    }
}
