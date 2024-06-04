package com.example.destinos;

public class Destinos {



    public String idDestino;
    public String Descripcion;
    public String Direccion;
    public String Nombre;
    public String URLImagen;

    public String idUser;

    public Destinos() {

    }

    public Destinos(String idDestino, String descripcion, String direccion, String nombre, String URLImagen, String idUser) {
        this.idDestino = idDestino;
        this.Descripcion = descripcion;
        this.Direccion = direccion;
        this.Nombre = nombre;
        this.URLImagen = URLImagen;
        this.idUser = idUser;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }
}
