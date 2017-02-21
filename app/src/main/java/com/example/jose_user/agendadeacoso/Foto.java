package com.example.jose_user.agendadeacoso;

import java.io.Serializable;

/**
 * Created by Jose-User on 18/11/2015.
 */
public class Foto implements Serializable{
    int id;
    int tamano;
    String[] fotos;
    String[] obsevaciones;

    public Foto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String[] getFotos() {
        return fotos;
    }

    public void setFotos(String[] fotos) {
        this.fotos = fotos;
    }

    public String getFoto(int pos){
        return fotos[pos];
    }

    public void setFoto(String ruta){
        fotos = new String [5];
        fotos[0] = ruta;
    }

    public String[] getObsevaciones() {
        return obsevaciones;
    }

    public void setObsevaciones(String[] obsevaciones) {
        this.obsevaciones = obsevaciones;
    }
}
