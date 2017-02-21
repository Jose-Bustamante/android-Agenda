package com.example.jose_user.agendadeacoso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jose-User on 18/11/2015.
 */
public class BaseDatosAcoso extends SQLiteOpenHelper{

    private static final int VERSION_BASEDATOS = 3;
    private static final String NOMBRE_BASEDATOS = "BD_Acoso.db";
    private static final String NOMBRE_TABLA = "Contactos";

    private static final String insContactos = "CREATE TABLE Contactos (idContacto INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(50), direccion VARCHAR(50), webBlog VARCHAR(50), email VARCHAR(50))";
    private static final String insTelefonos = "CREATE TABLE Telefonos (idTelefonos INTEGER PRIMARY KEY AUTOINCREMENT, telefono VARCHAR(45), contactos_idContacto INT)";
    private static final String insFotos = "CREATE TABLE Fotos (idFoto INTEGER PRIMARY KEY AUTOINCREMENT, nomFichero VARCHAR(50), observFoto VARCHAR(255), contactos_idContacto INT)";

    static ArrayList<Contacto> arrayList = new ArrayList();
    static ArrayList<Foto> arrayListFotos = new ArrayList();


    public BaseDatosAcoso(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(insContactos);
        db.execSQL(insTelefonos);
        db.execSQL(insFotos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+NOMBRE_TABLA+";");
        db.execSQL("DROP TABLE "+"Telefonos;");
        db.execSQL("DROP TABLE "+"Fotos;");
        db.execSQL("CREATE TABLE Contactos (idContacto INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(50), direccion VARCHAR(50), webBlog VARCHAR(50), email VARCHAR(50))");
        db.execSQL("CREATE TABLE Telefonos (idTelefonos INTEGER PRIMARY KEY AUTOINCREMENT, telefono VARCHAR(45), contactos_idContacto INT)");
        db.execSQL("CREATE TABLE Fotos (idFoto INTEGER PRIMARY KEY AUTOINCREMENT, nomFichero VARCHAR(50), observFoto VARCHAR(255), contactos_idContacto INT)");

    }

    public void insertarTel(String tel, int id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("telefono", tel);
        valores.put("contactos_idContacto", id);
        Long nreg_afectados = db.insert("Telefonos", null, valores);
        db.close();

    }

    public long insertarFoto(String ruta, String observ, int idc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("NomFichero", ruta);
        valores.put("ObservFoto", observ);
        valores.put("contactos_idContacto", idc);
        Long nreg_afectados = db.insert("Fotos", null, valores);
        db.close();
        return  nreg_afectados;
    }

    public Telefono cojerTelefonos(Contacto c){
        SQLiteDatabase db = getWritableDatabase();
        int cont = 0;
        Cursor cursor = db.rawQuery("SELECT Telefono FROM Telefonos WHERE contactos_idContacto=" + c.getId(), null);
        cursor.moveToFirst();
        int tamano = cursor.getCount();
        long[] telefonos = new long[tamano];
        Telefono t = new Telefono();
        t.setIdContacto(c.getId());
        t.setnTelefonos(tamano);
        do{
            telefonos[cont] = cursor.getLong(0);
            cont++;
        }while (cursor.moveToNext());
        t.setTelefonos(telefonos);
        return t;
    }

    public int borrarFoto(String ruta, int id){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete("Fotos", "contactos_idContacto="+id+" AND NomFichero='"+ruta+"'", null);
        db.close();
        return result;
    }

    public int borrar(Contacto c){
        SQLiteDatabase db = getWritableDatabase();
        if(db == null){
            db.close();
            return 0;
        }else{
           int result =  db.delete(NOMBRE_TABLA, "idContacto="+c.getId(), null);
            Log.e("Result Contenido: ", "RESULTADO " +result);
            int result2 = db.delete("Telefonos", "contactos_idContacto="+c.getId(), null);
            Log.e("Result2 Contenido: ", "RESULTADO " +result2);
            int result3 = db.delete("Fotos", "contactos_idContacto="+c.getId(), null);
            Log.e("Result3 Contenido: ", "RESULTADO " +result3);
            if(result>0&&result2>0&&result3>0){
                db.close();
                return 1;
            }else{db.close();return 0;}
        }
    }

    public int actualizar(Contacto c){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contenido = new ContentValues();
        //idContacto INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(50), direccion VARCHAR(50), webBlog VARCHAR(50), email VARCHAR(50))
        contenido.put("nombre", c.getNombre());
        contenido.put("direccion", c.getDireccion());
        contenido.put("webBlog", c.getWeb());
        contenido.put("email", c.getEmail());
        int result1 = db.update(NOMBRE_TABLA, contenido, "idContacto=" + c.getId(), null);
        Log.e("Result1: ", "RESULTADO " + result1);

        ContentValues contenido2 = new ContentValues();
        contenido2.put("Telefono", c.getTelefonoPrimario());
        int result2 = db.update("Telefonos", contenido2, "Contactos_idContacto="+c.getId()+" AND telefono="+MostrarContacto.c.getTelefonoPrimario(), null);
        Log.e("Result2: ", "RESULTADO " + result2);


        Cursor c1 = db.rawQuery("SELECT idFoto FROM Fotos WHERE contactos_idContacto=" + MostrarContacto.c.getId() + " AND NomFichero='" + MostrarContacto.c.getFotoPrimario() + "'", null);
        c1.moveToFirst();
        ContentValues contenido3 = new ContentValues();
        contenido3.put("NomFichero", c.getFotoPrimario());
        Log.e("c.Foto: ", "RESULTADO " + c.getFotoPrimario());
        int result = db.update("Fotos", contenido3, "idFoto="+c1.getInt(0), null);

        db.close();
        return 1;
    }

    public long insertar(Contacto c){
//idContacto INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), direccion VARCHAR(50), webBlog VARCHAR(50)
        long nreg_afectados =-1;
        long nreg_afectados2 =-1;
        long nreg_afectados3 = -1;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("nombre", c.getNombre());
            valores.put("direccion", c.getDireccion());
            valores.put("webBlog", c.getWeb());
            valores.put("email", c.getEmail());
            nreg_afectados = db.insert(NOMBRE_TABLA, null, valores); //ESto no va asi hay que cambiar. primero insertar el Contacto, hacer una consulta con el ID y pasarla.
            String[] args = new String[]{c.getNombre(), c.getDireccion()};
            Cursor c4 = db.rawQuery("SELECT idContacto FROM Contactos WHERE nombre=? AND direccion=?", args);
            c4.moveToFirst();
            int idCon = c4.getInt(0);



            //idTelefonos INT AUTO_INCREMENT PRIMARY KEY, telefono VARCHAR(45), contactos_idContacto INT
            ContentValues valores2 = new ContentValues();
            valores2.put("telefono", c.getTelefonoPrimario());
            valores2.put("contactos_idContacto", idCon);
            nreg_afectados2 = db.insert("Telefonos", null, valores2);

           // idFoto INT AUTO_INCREMENT PRIMARY KEY, nomFichero VARCHAR(50), observFoto VARCHAR(255), contactos_idContacto INT
            ContentValues valores3 = new ContentValues();
            valores3.put("nomFichero", c.getFotoPrimario());
            valores3.put("observFoto", "ghkgkhhk");
            valores3.put("contactos_idContacto", idCon);
            nreg_afectados3 = db.insert("Fotos", null, valores3);

        }
        db.close();

        if(nreg_afectados != -1 && nreg_afectados2 != -1 && nreg_afectados3 != -1){
            return 1; //Inserccion correcta
        }else {
            return 0; //Error en la inserccion
        }

    }

    public void cambiarFotoPrimaria(Foto f){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c1 = db.rawQuery("SELECT idFoto FROM Fotos WHERE contactos_idContacto=" + MostrarContacto.c.getId() + " AND NomFichero='" + MostrarContacto.c.getFotoPrimario() + "'", null);
        c1.moveToFirst();
        Cursor c2 = db.rawQuery("SELECT idFoto FROM Fotos WHERE contactos_idContacto=" + f.getId()+" AND NomFichero='"+f.getFoto(0)+"'", null);
        c2.moveToFirst();

        ContentValues contenido = new ContentValues();
        contenido.put("NomFichero", f.getFoto(0));
        db.update("Fotos", contenido, "idFoto=" + c1.getInt(0), null);

        ContentValues contenido2 = new ContentValues();
        contenido2.put("NomFichero", MostrarContacto.c.getFotoPrimario());
        db.update("Fotos", contenido2, "idFoto=" + c2.getInt(0), null);
        MostrarContacto.c.setFotoPrimario(f.getFoto(0));
                db.close();
    }

    public ArrayList<Foto> recargarArrayFotos(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Fotos WHERE contactos_idContacto=" + MostrarContacto.c.getId(), null);
        Log.e("Pasos 3: ", "query "+c.getCount());
        String[] fotos = new String[c.getCount()];

        if(c.moveToFirst()){
            Log.e("Entra if: ", "hecho ");
            arrayListFotos.clear();
            do{
                Log.e("Do: ", "hecho ");
                //db.execSQL("CREATE TABLE Fotos (idFoto INTEGER PRIMARY KEY AUTOINCREMENT, nomFichero VARCHAR(50), observFoto VARCHAR(255), contactos_idContacto INT)");
                Foto f = new Foto();
                f.setId(c.getInt(3));
                Log.e("parte if: ", "id " + c.getInt(3));
                Log.e("2parte if: ", "hecho1 " + c.getString(1));
                Log.e("2parte if: ", "hecho2 " + c.getString(2));
                f.setFoto(c.getString(1));


                arrayListFotos.add(f);

            }while (c.moveToNext());
        }
        db.close();
        return arrayListFotos;
    }

    public ArrayList<Contacto> recargarArrayContactos(){
        SQLiteDatabase db = getWritableDatabase();
        String[] campos = {"idContacto", "Nombre", "Direccion", "WebBlog", "email"};
        String[] campos2 = {"idTelefonos", "Telefono", "Contactos_idContacto"};
        String[] campos3 = {"idFoto", "NomFichero", "ObservFoto", "Contactos_idContacto"};
        Cursor c = db.query(NOMBRE_TABLA, campos, null, null, null, null, null);
        ;
        ContentValues valores2 = new ContentValues();
        valores2.put("NomFichero", "1233323");
        valores2.put("ObservFoto", "asdsadas");
        valores2.put("contactos_idContacto", "0");
        long nreg_afectados3 = db.insert("Fotos", null, valores2);

        //String nomb, int tel, String dire, String ema, String we, String rutaFoto
        if(c.moveToFirst()){
            arrayList.clear();


            do{
                Log.e("Query id: ", "RESULTADO "+c.getInt(0));
                Log.e("Query Cantidad: ", "RESULTADO "+c.getCount());
                Cursor c2 = db.rawQuery("SELECT * FROM Telefonos WHERE contactos_idContacto=" + c.getInt(0), null);
                c2.moveToFirst();
                Log.e("Query2 Contenido: ", "RESULTADO " + c2.getCount());
                Cursor c3 = db.rawQuery("SELECT * FROM Fotos WHERE contactos_idContacto=" + c.getInt(0), null);
                Log.e("Query3 Contenido: ", "RESULTADO " + c3.getCount());
                c3.moveToFirst();
                Contacto contacto = new Contacto();
                contacto.setNombre(c.getString(1));
                if(c2.getCount()==0){
                    contacto.setTelefonoPrimario(123456789);
                }else{
                    contacto.setTelefonoPrimario(Long.valueOf(c2.getString(1)));
                }
                if(c3.getCount()==0){
                    contacto.setFotoPrimario("");
                }else{
                    contacto.setFotoPrimario(c3.getString(1));
                }
                    contacto.setDireccion(c.getString(2));
                    contacto.setEmail(c.getString(4));
                    contacto.setWeb(c.getString(3));
                    contacto.setId(c.getInt(0));


                arrayList.add(contacto);
            }while (c.moveToNext());
        }


        db.close();
        return arrayList;
    }
}
