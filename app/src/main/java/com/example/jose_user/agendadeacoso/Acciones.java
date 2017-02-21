package com.example.jose_user.agendadeacoso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Acciones extends Activity {

    public Contacto c;

    public Boolean localizacion;
    public String cogerFoto;
    public Uri fotogaleria;
    int ACT_CAMARA = 1;
    int ACT_GALERY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        Bundle datoContacto = getIntent().getExtras();
        c = (Contacto) datoContacto.getSerializable("objeto");

        SharedPreferences prefs = getSharedPreferences("com.example.jose_user.agendadeacoso_preferences",MODE_PRIVATE);
         localizacion = prefs.getBoolean("localizacion", true);
        cogerFoto = prefs.getString("foto", "");

    }

    public void btnLLamar(View v){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + c.getTelefonoPrimario()));
        startActivity(i);

    }
    public void btnEmail(View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, ""+c.getEmail());//En Gmail no pone el destinatario
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(intent);

    }
    public void btnLocalizar(View v){
        if (localizacion == false){
            Toast.makeText(getApplicationContext(),"Localizacion Desactivada", Toast.LENGTH_LONG).show();
        }else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("geo:0,0?q=" + c.getDireccion()));
            startActivity(i);
        }

    }
    public void btnWeb(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://"+c.getWeb()));
        startActivity(i);
    }
    public void btnFoto(View v) {

            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent2, ACT_CAMARA);


    }

    public void btnGaleria(View v){
        Intent it = new Intent(this, GaleriaFotos.class);
        startActivity(it);
        Log.e("Pasos 1: ", "hecho ");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ACT_CAMARA){
           Bitmap bm = (Bitmap) data.getExtras().get("data");
            Uri fotoUri = getImageUri(this,bm);
            File foto = new File(getRealPathFromURI(fotoUri));
            String ruta = foto.getPath();
            Log.e("ruta Contenido: ", "RESULTADO " + ruta);
            c.setFotoPrimario(ruta);
            MainActivity.bd.insertarFoto(ruta,null,c.getId());

        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
