package com.example.jose_user.agendadeacoso;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    static Adaptador a;
    static ArrayList<Contacto> arrayList = new ArrayList();
    public static BaseDatosAcoso bd;
    public static String r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BaseDatosAcoso(this);
        arrayList = bd.recargarArrayContactos();
        a = new Adaptador(this, arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);

        r = getExternalFilesDir(null).getAbsolutePath()+"/";

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = bd.recargarArrayContactos();
        a = new Adaptador(this, arrayList);
        a.notifyDataSetChanged();
        setListAdapter(a);

    }

    @Override
        protected void onListItemClick (ListView l, View v,int position, long id){
        super.onListItemClick(l, v, position, id);
            Contacto c1 = (Contacto) l.getAdapter().getItem(position);
            Intent it = new Intent(this, MostrarContacto.class);
            it.putExtra("objeto", c1);
            startActivity(it);
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
        if (id == R.id.nuevo_contacto) {
            Intent i = new Intent(this, CrearContacto.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.configuracion) {
            Intent i = new Intent(this, Preferencias.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void reargarArrayMain(){
        arrayList = bd.recargarArrayContactos();

    }




}
