package com.example.ahmadbarakat.phonebook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ahmadbarakat.phonebook.R;

import java.io.ByteArrayOutputStream;

public class Add_Activity extends Activity {

    private DB_SQLite mydb ;
    int ID = 0;

    String name, phone, email;
    Bitmap photo;

    Button Ok, Picture;
    EditText Name, Number, Email;
    Spinner Mail, Network;
    Bitmap pic;

    byte[] pic2;
    ImageView image;
    Boolean update = false, chooseimage = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mydb = new DB_SQLite(this);

        Name = (EditText) findViewById(R.id.ET_name);
        Number = (EditText) findViewById(R.id.ET_number);
        Email = (EditText) findViewById(R.id.ET_email);

        Mail = (Spinner) findViewById(R.id.S_mail);
        Network = (Spinner) findViewById(R.id.S_network);
        image = (ImageView) findViewById(R.id.I_contactPr);

        ArrayAdapter<CharSequence> mail_adapter = ArrayAdapter.createFromResource(this, R.array.mail_array, android.R.layout.simple_spinner_item);
        mail_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Mail.setAdapter(mail_adapter);

        ArrayAdapter<CharSequence> network_adapter = ArrayAdapter.createFromResource(this, R.array.network_array, android.R.layout.simple_spinner_item);
        network_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Network.setAdapter(network_adapter);

        if (getIntent().getExtras() != null)
        {
            update = true;
            ID = getIntent().getIntExtra("id", -1);
            Cursor rs = mydb.getData(ID);
            rs.moveToFirst();
            name = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_NAME));
            phone = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_PHONE));
            email = rs.getString(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_EMAIL));
            photo = BitmapFactory.decodeByteArray(rs.getBlob(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_Photo)), 0, rs.getBlob(rs.getColumnIndex(DB_SQLite.CONTACTS_COLUMN_Photo)).length);
            if (!rs.isClosed()) {
                rs.close();
            }

            Name.setText(name);
            Number.setText(phone);

            if (email.length() != 0) {
                String email2 = email;
                String mail = email2.substring(email2.indexOf("@") + 1, email2.indexOf("."));
                String network = email2.substring(email2.indexOf(".") + 1, email2.length());
                Email.setText(email2.split("@")[0]);
                Mail.setSelection(mail_adapter.getPosition(mail));
                Network.setSelection(network_adapter.getPosition(network));
            }

            image.setImageBitmap(photo);

            pic = photo;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
            pic2 = stream.toByteArray();
        }



        Picture = (Button) findViewById(R.id.B_image);
        Picture.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                      chooseimage = true;
                                      Intent intent_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                      startActivityForResult(intent_gallery, 1);
                                     }
                                   }
        );


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                pic = BitmapFactory.decodeFile(filePath);
                pic = getRoundedShape(pic);
                image.setImageBitmap(pic);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                pic2 = stream.toByteArray();
            }
            else
            {
                pic = BitmapFactory.decodeResource(getResources(), R.drawable.contact);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                pic2 = stream.toByteArray();
            }
        }
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 250;
        int targetHeight = 250;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_, menu);
        return true;
    }

    public void Done(MenuItem item)
    {
        name = Name.getText().toString();
        phone = Number.getText().toString();
        email = Email.getText().toString();
        if (email.length()!=0)
            email = email + "@" + Mail.getSelectedItem().toString() + "." + Network.getSelectedItem().toString();

        if ((name.length() != 0)) {

            if (!chooseimage && !update) {
                pic = BitmapFactory.decodeResource(getResources(), R.drawable.contact);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                pic2 = stream.toByteArray();
            }

            if (update) {
                mydb.updateContact(ID, name, phone, email, pic2);
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mydb.addContact(name, phone, email, pic2);
            }
            finish();
        }
    }
}