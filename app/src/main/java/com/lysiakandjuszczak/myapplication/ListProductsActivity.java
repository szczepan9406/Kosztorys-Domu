package com.lysiakandjuszczak.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


//Klasa aktywności listy produktów

public class ListProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textViewAllPrize;
    ListView listViewProducts;
    List<String> productsNames;
    List<Product>  products = new ArrayList<Product>();

    List<Currency> currencys;
    Map<String,Double> values = new HashMap<String,Double>();
    Button buttonShare;
    DBManager dbManager;

    double allPrize = 0;

    //tworzenie widoku
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textViewAllPrize = (TextView) findViewById(R.id.textViewAllPrize);
        listViewProducts = (ListView) findViewById(R.id.list_viewProducts);
        buttonShare = (Button) findViewById(R.id.buttonShare);

        productsNames = new ArrayList<String>();

        //otwieranie bazy danych
        dbManager =new DBManager(getApplicationContext());
        dbManager.openDataBase();

        //pobieranie produktóþw z bazy danych
        Cursor productsCursor = dbManager.getAllProduct();
        updateCurrencyList(productsCursor);

        //inicjalizowanie  widoku
        populateListview();


        //obsługa eksportu po kliknięciu przycisku eksportuj
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productsJson = "";
                ObjectMapper mapper = new ObjectMapper();
                try {
                    productsJson = mapper.writeValueAsString(products);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                intentShareFile.setType("application/json");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, productsJson);

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Kosztorys ");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, productsJson);

                startActivity(Intent.createChooser(intentShareFile, "Udostępnij Kosztorys"));

            }
        });

        //obsługa usuwanie produktu
        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                String title = "Usuwanie";
                String message = "Czy chcesz usunąć produckt";
                String button = "Usuń";

                AlertDialog.Builder dialog = new AlertDialog.Builder(ListProductsActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle(title);
                dialog.setMessage(message);

                dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Product product = products.get(position);
                        dbManager.deleteProduct(product.getId());
                        populateListview();
                    }
                })
                        .setNegativeButton("Anuluj ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                final AlertDialog alert = dialog.create();
                alert.show();
                return true;
            }
        });
    }
//inicjalizowanie widoku i odświeżanie po usunięciu
    private void populateListview() {
        allPrize=0;
        products.clear();
        productsNames.clear();
        Cursor productsCursor = dbManager.getAllProduct();
        updateCurrencyList(productsCursor);
        generateAllPrize();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, productsNames);
        listViewProducts.setAdapter(adapter);
        listViewProducts.invalidateViews();
    }
    //zapisywanie pobranych  produktów z bazy danych do listy
    private void updateCurrencyList(Cursor productCursor) {
        if (productCursor != null && productCursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(productCursor.getLong(0));
                product.setName(productCursor.getString(1));
                product.setCategory(productCursor.getString(2));
                product.setPrize(productCursor.getDouble(3));
                product.setCount(productCursor.getInt(4));
                product.setCurrency(productCursor.getString(5));

                products.add(product);
                productsNames.add(product.getName() + "  " + product.getPrize() + product.getCurrency() +"  Ilość:" + product.getCount() + "   Kategoria:" + product.getCategory());
            } while ((productCursor.moveToNext()));
        }
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

    //pobieranie kursów walut za opmocą biblioteki Volley
    private void  generateAllPrize(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://www.mycurrency.net/service/rates";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    //jeśli się udało pobrać
                    @Override
                    public void onResponse(JSONArray response) {

                        parseCurrencyFromJSON(response.toString());
                        for(Product product: products){
                            double pln =getCounter("PLN");
                            double counter =pln/getCounter(product.getCurrency());
                            allPrize += product.getPrize() * product.getCount() * counter;
                        }
                        textViewAllPrize.setText("  Całość " + round(allPrize,"##.##") + "PLN");

                    }

                },
                //jeśli błąð
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Problem z pobraniem aktualnych walut",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(getRequest);
    }

    //zaokrąglanie
    public static String round(Double number, String pattern){
        NumberFormat format=new DecimalFormat(pattern);

        return format.format(number).replace(',','.');
    }

    //pobieranie przelicznika z aktualnej listy walut
    private Double getCounter(String key) {

        for(Currency currrency: currencys){
            if (key.equals(currrency.getCurrency_code())){
                return currrency.getRate();
            }
        }
        return  1.00;
    }

    //parsowanie JSONA na listę walut
    public List<Currency> parseCurrencyFromJSON(String json){
        currencys = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            currencys = mapper.readValue(json, new TypeReference<List<Currency>>(){});
        }
        catch (JsonParseException e){
            e.printStackTrace();
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
            System.out.print(e);
        }
        catch (IOException e) { e.printStackTrace(); }
        return currencys;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.list_products, menu);
//        return true;
//    }

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

        if (id == R.id.nav_addProduct) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
