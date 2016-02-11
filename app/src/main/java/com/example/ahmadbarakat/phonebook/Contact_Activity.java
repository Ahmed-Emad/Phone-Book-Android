package com.example.ahmadbarakat.phonebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Contact_Activity extends Activity {

    private DB_SQLite mydb ;
    int ID = 0;

    String name, phone, email;
    Bitmap photo;

    TextView Name, Number, Email, NumberSt, EmailSt;
    ImageView image;
    Button Call, Send, Share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        /*
        db = new DataBase_SQLite(this);
        Contacts = db.getAllContacts();
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getInt("id");
            contact = db.getContact(id);
        }
*/

        mydb = new DB_SQLite(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0) {
                Cursor rs = mydb.getData(Value);
                ID = Value;
                rs.moveToFirst();
                name = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_NAME));
                phone = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_PHONE));
                email = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_EMAIL));
                photo = BitmapFactory.decodeByteArray(rs.getBlob(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_Photo)), 0, rs.getBlob(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_Photo)).length);
                if (!rs.isClosed()) {
                    rs.close();
                }
            }
        }

        Name = (TextView) findViewById(R.id.TV_name);
        Name.setText(name);

        NumberSt = (TextView) findViewById(R.id.TV_number_Static);
        EmailSt = (TextView) findViewById(R.id.TV_email_Static);

        Number = (TextView) findViewById(R.id.TV_number);
        Email = (TextView) findViewById(R.id.TV_email);

        if (phone.length()==0) {
            NumberSt.setVisibility(View.INVISIBLE);
            Number.setText("");
        }
        else
            Number.setText(phone);

        if (email.length()==0) {
            EmailSt.setVisibility(View.INVISIBLE);
            Email.setText("");
        }
        else
            Email.setText(email);

        image = (ImageView) findViewById(R.id.I_contact);
        image.setImageBitmap(photo);

        Call = (Button) findViewById(R.id.B_call);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.length()!=0) {
                    String uri = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        Send = (Button) findViewById(R.id.B_send);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.length()!=0) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("mailto:" + email));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        Share = (Button) findViewById(R.id.B_Share);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + name + "\n" + "Number: " + phone + "\n" + "Email: " + email + "\n");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, name + " Info");
                startActivity(Intent.createChooser(shareIntent, "Share Contact With"));
            }
        });
    }

    public void Edit(View v)
    {
        Intent intent = new Intent(this, Add_Activity.class);
        intent.putExtra("id", ID);
        startActivity(intent);
        finish();
    }

    public void Delete(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure You Want to Delete This Contact ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydb.deleteContact(ID);
                        Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog d = builder.create();
        d.setTitle("Delete Contact !");
        d.show();
    }

}