package com.example.jose_user.agendadeacoso;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jose-User on 29/11/2015.
 */
public class AdaptadorGaleria extends BaseAdapter{
    private ArrayList<Foto> lista;
    private final Activity actividad;

    public AdaptadorGaleria(Activity a, ArrayList<Foto> c) {
        super();
        actividad = a;
        lista = c;
    }



    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ly = actividad.getLayoutInflater(); //Creamos el LayoutInflater para luego crear el View
        View view = ly.inflate(R.layout.galery, null, true); //Creamos el View.

        File imgFile = new File(lista.get(position).getFoto(0));

        if(imgFile.exists()){
            ImageView im = (ImageView) view.findViewById(R.id.GaleriaIV);
            im.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            im.setAdjustViewBounds(true);

        }else{
            ImageView im = (ImageView) view.findViewById(R.id.GaleriaIV);
            im.setImageResource(R.drawable.foto);
            im.setAdjustViewBounds(true);
        }
        Log.e("lista: ", ""+lista.get(position).getFoto(0));
        Log.e("contacto: ",""+MostrarContacto.c.getFotoPrimario());

        if(lista.get(position).getFoto(0).equals(MostrarContacto.c.getFotoPrimario())){
            Log.e("entra: ", "si");
            ImageButton ib = (ImageButton) view.findViewById(R.id.imageButBorrar);
            ib.setEnabled(false);
            ImageButton ib2 = (ImageButton) view.findViewById(R.id.imageButEstablecer);

            ib2.setFocusable(true);
        }

        ImageButton ib2 = (ImageButton) view.findViewById(R.id.imageButEstablecer);
        ib2.setTag(position);
        ib2.setFocusable(true);

        ib2.setOnClickListener(botonSeleccionar);


        ImageButton ib = (ImageButton) view.findViewById(R.id.imageButBorrar);
        ib.setTag(position);
        //ib.setEnabled(false);
        ib.setOnClickListener(botonBorrar);

        return view;
    }

    public View.OnClickListener botonBorrar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int posicion = (Integer) v.getTag();
            Log.e("PosicionBoton "," "+posicion);
            String ruta = lista.get(posicion).getFoto(0);
            int id = lista.get(posicion).getId();
            int result = MainActivity.bd.borrarFoto(ruta, id);
            Log.e("ResultadoBorrarFoto "," "+result);

            GaleriaFotos.a.notifyDataSetChanged();


        }
    };

    public View.OnClickListener botonSeleccionar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int posicion = (Integer) v.getTag();
            MainActivity.bd.cambiarFotoPrimaria(lista.get(posicion));
            MostrarContacto.c.setFotoPrimario(lista.get(posicion).getFoto(0));


        }
    };
}
