package com.lysiakandjuszczak.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Szczepan on 25.05.17.
 */


//Klasa do obsługi bazy danych
public class DBManager extends SQLiteOpenHelper {

    private static String DB_PATCH ;
    private static String DB_NAME="Produkt.db";
    private final String DATABASE_FOLDER = "/databases/";
    private final String DB_APP_PATCH = "/data/data/";
    private final String TABLE_NAME="lista_produktow";

    public final String KEY_NAME ="nazwa_produktu";
    public final String KEY_PRIZE ="cena";
    public final String KEY_COUNT ="ilosc";
    public final String KEY_CATEGORY ="kategoria";

    private final String[] columns ={KEY_NAME, KEY_PRIZE, KEY_COUNT, KEY_CATEGORY};

    private SQLiteDatabase db;
    private final Context myContext;

    public DBManager (Context context){
        super(context,DB_NAME,null,1);
        DB_PATCH =  DB_APP_PATCH + context.getPackageName() + DATABASE_FOLDER;
        this.myContext=context;
    }


    public void createDataBase(){
        boolean dbExist=checkDataBase();

        if(!dbExist){
            this.getReadableDatabase();
        }
    }

    //Sprawdzenie czy istnieje  baza
    private boolean checkDataBase() {
        SQLiteDatabase checkDB =null;
        try{
            String myPatch=DB_PATCH+DB_NAME;
            checkDB=SQLiteDatabase.openDatabase(myPatch,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            Log.d("Check Exist Database","db not exist");
        }
        return checkDB != null;

    }

    public  void openDataBase() throws SQLiteException{
        String myPath =DB_PATCH+DB_NAME;
        db =SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized  void close (){
        if(db !=null){
            db.close();
        }
        super.close();
    }

    //tworzenie tabeli
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCT_TABLE = "CREATE TABLE Product( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "category TEXT, "+
                "prize REAL, "+
                "count INT," +
                "currency TEXT)";

        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //pobieranie wszystkich produktów
    public Cursor getAllProduct(){
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Product",null);
        return  cursor;
    }
        //dodawanie do bazy danych
    public long insertProduct(Product product){

        ContentValues contentValues=new ContentValues();
        contentValues.put("name",product.getName());
        contentValues.put("prize",product.getPrize());
        contentValues.put("count",product.getCount());
        contentValues.put("category",product.getCategory());
        contentValues.put("currency",product.getCurrency());

        return db.insert("Product", null,contentValues);
    }

    //usuwanie produktu
    public boolean deleteProduct(Long id)
    {
        return db.delete("Product", "id=" + id, null) > 0;
    }


}
