package com.example.ahmadbarakat.phonebook;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListAdapter;
        import android.widget.ListView;

        import java.util.ArrayList;
        import java.util.Locale;


public class Main extends Activity {

    ListView List;
    EditText Search;

    ArrayList<Contact> Contacts;
    DB_SQLite mydb;
    CustomContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DB_SQLite(this);

        try {
            Contacts = mydb.getAllContacts();
        }
        catch (NullPointerException e){
            Contacts = new ArrayList<Contact>();
        }

        adapter = new CustomContactAdapter(this, Contacts);

        List = (ListView) findViewById(R.id.LV_list);
        List.setAdapter(adapter);

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                            int id_To_Search = (int) id;
                                            //Log.e("Main:", "ID = " + adapter.getItem(position).getId());
                                            Bundle dataBundle = new Bundle();
                                            dataBundle.putInt("id", id_To_Search);
                                            Intent intent = new Intent(getApplicationContext(),Contact_Activity.class);
                                            intent.putExtras(dataBundle);
                                            startActivity(intent);
                                        }
                                    }
        );

        Search = (EditText) findViewById(R.id.ET_search);
        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i2, int i3) {
                //Log.e("Main", "Search String OnChange: " + text.toString());
                //adapter.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.e("Main", "Search String AfterChange: " + Search.getText().toString().toLowerCase(Locale.getDefault()));
                String text = Search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();
        //adapter.notifyDataSetChanged();

        Search.setText("");

        try {
            Contacts = mydb.getAllContacts();
        }
        catch (NullPointerException e){
            Contacts = new ArrayList<Contact>();
        }

        adapter = new CustomContactAdapter(this, Contacts);

        List = (ListView) findViewById(R.id.LV_list);
        List.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void About(MenuItem item)
    {
        Intent intent = new Intent(this, About_Activity.class);
        startActivity(intent);
    }

    public void Add(MenuItem item)
    {
        Intent intent = new Intent(this, Add_Activity.class);
        startActivity(intent);
    }

}