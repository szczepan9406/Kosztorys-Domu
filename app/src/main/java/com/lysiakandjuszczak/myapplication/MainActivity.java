package com.lysiakandjuszczak.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
>>>>>>> dawid

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText  editTextProductName;
    EditText  editTextPrize;
    EditText  editTextProductCount;
    EditText  editTextProductCategory;
    Spinner   spinnerCurrency;
    Button    buttonAddProduct;
    List<String> currnecysName;

    double usdValue = 0;
    double plnValue = 0;
    double EuroValue = 0;
    double GbpValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editTextProductName = (EditText ) findViewById(R.id.editTextProductName);
        editTextPrize = (EditText ) findViewById(R.id.editTextPrize);
        editTextProductCount = (EditText ) findViewById(R.id.editTextCount);
        editTextProductCategory = (EditText ) findViewById(R.id.editTextCategory);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);

        currnecysName = new ArrayList<String>();
        currnecysName.add("PLN");
        currnecysName.add("USD");
        currnecysName.add("EURO");
        currnecysName.add("GBP");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,currnecysName);
        spinnerCurrency.setAdapter(adapter);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setName(editTextProductName.getText().toString());
                product.setCategory(editTextProductCategory.getText().toString());
                product.setCurrency(currnecysName.get(spinnerCurrency.getSelectedItemPosition()));
                product.setCount(Integer.parseInt(editTextProductCount.getText().toString()));
                product.setPrize(Double.parseDouble(editTextPrize.getText().toString()));

                DBManager dbManager;
                dbManager =new DBManager(getApplicationContext());
                dbManager.createDataBase();
                dbManager.openDataBase();
                dbManager.insertProduct(product);
                Toast.makeText(getApplicationContext(),"Dodano Pozycję",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listProducts) {
            Intent intent = new Intent(getApplicationContext(),ListProductsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
