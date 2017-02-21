package com.example.jose_user.agendadeacoso;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Jose-User on 18/11/2015.
 */
public class Contacto implements Serializable{

    private int id;
    private String nombre;
    private String direccion;
    private String web;
    private String email;
    private long telefonoPrimerio;
    private String fotoPrimaria;


    public Contacto() {
    }

    public Contacto(String nomb, int tel, String dire, String ema, String we, String rutaFoto){
        nombre = nomb;
        telefonoPrimerio = tel;
        direccion = dire;
        email = ema;
        web = we;
        fotoPrimaria = rutaFoto;

    }
    public String getEmail() { return email; }

    public void setEmail(String email) {this.email = email; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }


    public long getTelefonoPrimario(){return telefonoPrimerio;}

    public void setTelefonoPrimario(long tel){telefonoPrimerio = tel;}

    public String getFotoPrimario(){return fotoPrimaria;}

    public void setFotoPrimario(String ruta){this.fotoPrimaria = ruta;}



}
