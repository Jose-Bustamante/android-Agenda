package com.example.jose_user.agendadeacoso;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jose-User on 19/11/2015.
 */
public class Adaptador extends BaseAdapter {

    private ArrayList<Contacto> lista;
    private final Activity actividad;

    public Adaptador(Activity a, ArrayList<Contacto> c) {
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
        View view = ly.inflate(R.layout.item, null, true); //Creamos el View.

        TextView tv1 = (TextView) view.findViewById(R.id.TVNombre);
        tv1.setText("" + lista.get(position).getNombre()); //Cojemos el nombre y lo asignamos.

        TextView tv2 = (TextView) view.findViewById(R.id.TVDireccion);
        tv2.setText("" + lista.get(position).getDireccion());//Cojemos la Direccion

        TextView tv3 = (TextView) view.findViewById(R.id.TVEmail);
        tv3.setText("" + lista.get(position).getEmail()); //Cojemos el Email.

        TextView tv4 = (TextView) view.findViewById(R.id.TVWeb);
        tv4.setText("" + lista.get(position).getWeb()); //Cojemos la Web/Blog

        TextView tv5 = (TextView) view.findViewById(R.id.TVTelefono);
        tv5.setText("" + lista.get(position).getTelefonoPrimario()); //Cojemos el tlf

        File imgFile = new File(lista.get(position).getFotoPrimario());

        if(imgFile.exists()){
            ImageView im = (ImageView) view.findViewById(R.id.itemIView);
            im.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            im.setAdjustViewBounds(true);

        }else{
            ImageView im = (ImageView) view.findViewById(R.id.itemIView);
            im.setImageResource(R.drawable.foto);
            im.setAdjustViewBounds(true);
        }


        return view;
    }
}
