package com.example.ahmadbarakat.phonebook;

/**
 * Created by ahmadbarakat on 247 / 4 / 14.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.LogRecord;


public class CustomContactAdapter extends BaseAdapter {


    private List<Contact> Contactslist = null;
    private ArrayList<Contact> arraylist;

    LayoutInflater inflater;
    Context mcontext;

    public CustomContactAdapter(Context cntx, ArrayList<Contact> contacts) {
        //super(cntx, contacts);
        mcontext = cntx;
        inflater = LayoutInflater.from(mcontext);

        Contactslist = contacts;

        arraylist = new ArrayList<Contact>();
        arraylist.addAll(contacts);
    }

    @Override
    public int getCount() {
        return Contactslist.size();
    }

    @Override
    public Contact getItem(int position) {

        return Contactslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Contactslist.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Contact item = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(R.layout.list_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        ImageView image = (ImageView) convertView.findViewById(R.id.I_contactList);

        name.setText(item.getName());
        number.setText(item.getPhone());
        image.setImageBitmap(item.getBitmap());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Contactslist.clear();
        if (charText.length() == 0) {
            Contactslist.addAll(arraylist);
        } else {
            for (Contact ct : arraylist) {
                if (ct.getName().toLowerCase(Locale.getDefault()).contains(charText) || ct.getEmail().toLowerCase(Locale.getDefault()).contains(charText) || ct.getPhone().toLowerCase(Locale.getDefault()).contains(charText)) {
                    Contactslist.add(ct);
                }
            }
        }
        notifyDataSetChanged();
    }

}
