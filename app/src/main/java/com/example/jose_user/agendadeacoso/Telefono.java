package com.example.jose_user.agendadeacoso;

import java.io.Serializable;

/**
 * Created by Jose-User on 18/11/2015.
 */
public class Telefono implements Serializable{

    long idContacto;
    int nTelefonos;
    long [] telefonos;

    public Telefono() {
    }

    public Telefono(long idContacto, int nTelefonos) {
        this.idContacto = idContacto;
        this.nTelefonos = nTelefonos;
        this.telefonos = new long[nTelefonos];
    }

    public long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(long idContacto) {
        this.idContacto = idContacto;
    }

    public long getnTelefonos() {
        return nTelefonos;
    }

    public void setnTelefonos(int nTelefonos) {
        this.nTelefonos = nTelefonos;
    }

    public long[] getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(long[] telefonos) {
        this.telefonos = telefonos;
    }

    public void setTelefono(long telefono, int pos) {
        this.telefonos[pos] = telefono;
    }

    public long getTelefono(int pos) {
        return telefonos[pos];
    }

    public String[] convertirTelf(){
        String[] telfs = new String[nTelefonos];
        for(int i = 0; i<nTelefonos;i++){
            telfs[i] = Long.toString(telefonos[i]);
        }
        return telfs;
    }
}
