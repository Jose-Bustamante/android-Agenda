package com.example.jose_user.agendadeacoso;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GaleriaFotos extends ListActivity {

    public static AdaptadorGaleria a;
    public static ArrayList<Foto> arrayList = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fotos);
        arrayList = MainActivity.bd.recargarArrayFotos();
        a = new AdaptadorGaleria(this, arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);


    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList = MainActivity.bd.recargarArrayFotos();
        a = new AdaptadorGaleria(this, arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);

    }

    @Override
    protected void onListItemClick (ListView l, View v,int position, long id){
        super.onListItemClick(l, v, position, id);
        Log.e("click List", "entra");
    }

    public void funcion(){
        onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeria_fotos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onResume();

        return super.onOptionsItemSelected(item);
    }


}
