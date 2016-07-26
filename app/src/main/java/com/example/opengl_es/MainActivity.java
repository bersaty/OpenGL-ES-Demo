package com.example.opengl_es;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        final String[] mData = getResources().getStringArray(R.array.list_item);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,mData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent localIntent = new Intent();
                String str = (MainActivity.class.getPackage().getName() + '.' + mData[position]).trim();
                ClassLoader localClassLoader = null;
                if (str.length() != 0)
                    localClassLoader = getClass().getClassLoader();
                try {
                    Class localClass = localClassLoader.loadClass(str);
                    localIntent.setClass(MainActivity.this, localClass);
                    localIntent.putExtra("name", mData[position]);
                    MainActivity.this.startActivity(localIntent);
                    return;
                } catch (ClassNotFoundException localClassNotFoundException) {
                    while (true) {
                        localClassNotFoundException.printStackTrace();
                    }
                }
            }
        });
    }
}
