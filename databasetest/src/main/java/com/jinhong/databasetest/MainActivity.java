package com.jinhong.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Drown");
                values.put("pages",454);
                values.put("price",16.94);
                db.insert("Book",null,values);
                values.clear();
                //开始组装第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","DanBrwon");
                values.put("pages",510);
                values.put("price",19.65);
                db.insert("Book",null,values);
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                db.update("Book", values, "name = ?", new String[]{"The Data Vinci Code"});
            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db= dbHelper.getWritableDatabase();
                db.delete("Book", "pages>?", new String[]{"500"});
            }
        });

        Button queryData = (Button) findViewById(R.id.delete_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase  db= dbHelper.getWritableDatabase();
                //查询Book表中所有的数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity","book name is "+name);
                        Log.d("MainAcitivty","book author is "+author);
                        Log.d("MainAcitivty","book pages is "+pages);
                        Log.d("MainActivity","book price is "+price);
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
        });

        Button replaceData = (Button) findViewById(R.id.delete_data);
        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();  //开启事务
                try{
                    db.delete("Book",null,null);
                    if(true){
                        //在这里手动抛出一个异常，让事务失败
                        throw new NullPointerException();
                    }
                    ContentValues values = new ContentValues();
                    values.put("name","Game of Thrones");
                    values.put("author","George Martin");
                    values.put("pages",720);
                    values.put("price", 28.76);
                    db.insert("Book", null, values);
                    db.setTransactionSuccessful(); //事务已经执行成功
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    db.endTransaction();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
