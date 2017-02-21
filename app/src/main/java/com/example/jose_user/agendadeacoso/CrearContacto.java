package com.example.jose_user.agendadeacoso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CrearContacto extends Activity {

    public EditText nombre, telefono, direccion, email, web, rutaFoto;
    public String cogerFoto;
    int ACT_CAMARA = 1;
    int ACT_GALERY = 2;
    public Uri fotogaleria;
    public boolean imgSelec = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevocontacto);

        SharedPreferences prefs = getSharedPreferences("com.example.jose_user.agendadeacoso_preferences", MODE_PRIVATE);
        cogerFoto = prefs.getString("foto", "");
    }

    public long resultado;

    public void btnFoto(View v){
        if (cogerFoto.equals("Coger de camara")) {
            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent2, ACT_CAMARA);

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, ACT_GALERY);
        }
    }

    public void btnCrear(View v){
        nombre = (EditText) findViewById(R.id.IDetCrearNombre);
        telefono = (EditText) findViewById(R.id.IDetCrearTelefono);
        direccion = (EditText) findViewById(R.id.IDetCrearDireccion);
        email = (EditText) findViewById(R.id.IDetCrearEmail);
        web = (EditText) findViewById(R.id.IDetCrearWeb);
        rutaFoto = (EditText) findViewById(R.id.IDetCrearFoto);

        String contenidoTelefono = telefono.getText().toString();

        if(contenidoTelefono.matches("") || imgSelec ==false){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.telfyfoto), Toast.LENGTH_LONG).show();
        }else{
           Contacto contacto = new Contacto(nombre.getText().toString(), Integer.parseInt(telefono.getText().toString()),
                    direccion.getText().toString(),email.getText().toString(), web.getText().toString(), rutaFoto.getText().toString());

            resultado = MainActivity.bd.insertar(contacto);
        }

        if(resultado == 1){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.contactoInsertado), Toast.LENGTH_LONG).show();
           MainActivity.arrayList = MainActivity.bd.recargarArrayContactos();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ACT_CAMARA){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            ImageView iv = (ImageView)findViewById(R.id.IDcrearIV);
            iv.setImageBitmap(bm);
            Uri fotoUri = getImageUri(this,bm);
            File foto = new File(getRealPathFromURI(fotoUri));
            String ruta = foto.getPath();
            rutaFoto = (EditText) findViewById(R.id.IDetCrearFoto);
            rutaFoto.setText(ruta, TextView.BufferType.EDITABLE);
            imgSelec = true;

        }

        if(resultCode == RESULT_OK && requestCode == ACT_GALERY){

            fotogaleria = data.getData();
            try{
                InputStream is = getContentResolver().openInputStream(fotogaleria);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bm = BitmapFactory.decodeStream(is);
                ImageView iv = (ImageView)findViewById(R.id.IDcrearIV);
                iv.setImageBitmap(bm);
            }catch (FileNotFoundException e){}


            File foto = new File(getRealPathFromURI(fotogaleria));
            String ruta = foto.getPath();
            rutaFoto = (EditText) findViewById(R.id.IDetCrearFoto);
            rutaFoto.setText(ruta, TextView.BufferType.EDITABLE);
            imgSelec = true;

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
