package com.example.jose_user.agendadeacoso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Jose-User on 24/11/2015.
 */
public class MostrarContacto extends Activity{

    public static Contacto c;
    public Telefono t;
    String cogerFoto;
    public int creacion = 0;
    public String out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        SharedPreferences prefs = getSharedPreferences("com.example.jose_user.agendadeacoso_preferences", MODE_PRIVATE);
        cogerFoto = prefs.getString("foto", "");

        Bundle datoContacto = getIntent().getExtras();
        if (datoContacto == null) {
            Toast.makeText(getApplicationContext(), "No se Pasa el Contacto", Toast.LENGTH_LONG).show();
        } else {
            c = (Contacto) datoContacto.getSerializable("objeto");
            EditText ed1 = (EditText) findViewById(R.id.IDetCrearNombre);
            ed1.setText(c.getNombre());

            EditText ed2 = (EditText) findViewById(R.id.IDetCrearTelefono);
            ed2.setText(Long.toString(c.getTelefonoPrimario()));

            EditText ed3 = (EditText) findViewById(R.id.IDetCrearDireccion);
            ed3.setText(c.getDireccion());

            EditText ed4 = (EditText) findViewById(R.id.IDetCrearEmail);
            ed4.setText(c.getEmail());

            EditText ed5 = (EditText) findViewById(R.id.IDetCrearWeb);
            ed5.setText(c.getWeb());

            EditText ed6 = (EditText) findViewById(R.id.IDetCrearFoto);
            ed6.setText(c.getFotoPrimario());

            ImageView iv = (ImageView) findViewById(R.id.IDcrearIV);
            iv.setImageURI(Uri.fromFile(new File(c.getFotoPrimario())));

            File imgFile = new File(c.getFotoPrimario());

            if (imgFile.exists()) {

                iv.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                iv.setAdjustViewBounds(true);

            } else {

                iv.setImageResource(R.drawable.foto);
                iv.setAdjustViewBounds(true);
            }

            t = MainActivity.bd.cojerTelefonos(c);
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, t.convertirTelf()); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
        }

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                creacion++;
                if (creacion > 1) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:" + spinner.getSelectedItem().toString()));
                    startActivity(i);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        t = MainActivity.bd.cojerTelefonos(c);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, t.convertirTelf()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        creacion = 0;

        EditText ed6 = (EditText) findViewById(R.id.IDetCrearFoto);
        ed6.setText(c.getFotoPrimario());

        ImageView iv = (ImageView) findViewById(R.id.IDcrearIV);
        iv.setImageURI(Uri.fromFile(new File(c.getFotoPrimario())));

        File imgFile = new File(c.getFotoPrimario());

        if (imgFile.exists()) {

            iv.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            iv.setAdjustViewBounds(true);

        } else {

            iv.setImageResource(R.drawable.foto);
            iv.setAdjustViewBounds(true);
        }
    }

    public void btnAddTel(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getResources().getString(R.string.telefono));
        alert.setMessage(getResources().getString(R.string.newtelefono));

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                MainActivity.bd.insertarTel(value, c.getId());
                onResume();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    int ACT_CAMARA2 = 3;
    int ACT_GALERY2 = 4;
    public void btnAddFoto(View v){
        if (cogerFoto.equals("Coger de camara")) {
            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent2, ACT_CAMARA2);

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, ACT_GALERY2);
        }
    }



    public void btnBorrar(View v){
       int result = MainActivity.bd.borrar(c);
        if(result==1){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.contactoBorrado), Toast.LENGTH_LONG).show();
            MainActivity.arrayList = MainActivity.bd.recargarArrayContactos();
            finish();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
        }

    }

    public void btnModificar(View v){
        Contacto contacto = new Contacto();
        contacto.setId(c.getId());
        EditText ed1 = (EditText) findViewById(R.id.IDetCrearNombre);
        contacto.setNombre(ed1.getText().toString());

        EditText ed2 = (EditText) findViewById(R.id.IDetCrearTelefono);
        contacto.setTelefonoPrimario(Long.valueOf(ed2.getText().toString()));

        EditText ed3 = (EditText) findViewById(R.id.IDetCrearDireccion);
        contacto.setDireccion(ed3.getText().toString());

        EditText ed4 = (EditText) findViewById(R.id.IDetCrearEmail);
        contacto.setEmail(ed4.getText().toString());

        EditText ed5 = (EditText) findViewById(R.id.IDetCrearWeb);
        contacto.setWeb(ed5.getText().toString());

        EditText ed6 = (EditText) findViewById(R.id.IDetCrearFoto);
        contacto.setFotoPrimario(ed6.getText().toString());

        int result = MainActivity.bd.actualizar(contacto);
        if(result>0){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.modificadoCorrecto),Toast.LENGTH_LONG).show();
            MainActivity.reargarArrayMain();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();

        }
    }

    public void btnAcciones(View v){
        Intent it = new Intent(this, Acciones.class);
        it.putExtra("objeto", c);
        startActivity(it);
    }

    int ACT_CAMARA = 1;
    int ACT_GALERY = 2;

    public void btnFoto(View v){
        if (cogerFoto.equals("Coger de camara")) {
            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent2, ACT_CAMARA);

        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, ACT_GALERY);
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
            EditText rutaFoto = (EditText) findViewById(R.id.IDetCrearFoto);
            rutaFoto.setText(ruta, TextView.BufferType.EDITABLE);

        }

        if(resultCode == RESULT_OK && requestCode == ACT_GALERY){

            Uri fotogaleria = data.getData();
            try{
                InputStream is = getContentResolver().openInputStream(fotogaleria);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bm = BitmapFactory.decodeStream(is);
                ImageView iv = (ImageView)findViewById(R.id.IDcrearIV);
                iv.setImageBitmap(bm);
            }catch (FileNotFoundException e){}


            File foto = new File(getRealPathFromURI(fotogaleria));
            String ruta = foto.getPath();
            EditText rutaFoto = (EditText) findViewById(R.id.IDetCrearFoto);
            rutaFoto.setText(ruta, TextView.BufferType.EDITABLE);

        }

        if(resultCode == RESULT_OK && requestCode == ACT_CAMARA2){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            Uri fotoUri = getImageUri(this,bm);
            File foto = new File(getRealPathFromURI(fotoUri));
            String ruta = foto.getPath();
           long afectados = MainActivity.bd.insertarFoto(ruta,null,c.getId());
            if(afectados > 0){
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.newFoto), Toast.LENGTH_LONG).show();
            }

        }

        if(resultCode == RESULT_OK && requestCode == ACT_GALERY2){

            Uri fotogaleria = data.getData();
            File foto = new File(getRealPathFromURI(fotogaleria));
            String ruta = foto.getPath();
           long afectados =  MainActivity.bd.insertarFoto(ruta, null, c.getId());
            if(afectados > 0){
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.newFoto), Toast.LENGTH_LONG).show();
            }
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
