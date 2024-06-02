package com.example.destinos;

public class Destinos {

    public String Descripcion;
    public String Direccion;
    public String Nombre;
    public String URLImagen;
    public int idUser;

    public Destinos() {

    }

    public Destinos(String descripcion, String direccion, String nombre, String URLImagen) {
        Descripcion = descripcion;
        Direccion = direccion;
        Nombre = nombre;
        this.URLImagen = URLImagen;

    }



    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getURLImagen() {
        return URLImagen;
    }

    public void setURLImagen(String URLImagen) {
        this.URLImagen = URLImagen;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
