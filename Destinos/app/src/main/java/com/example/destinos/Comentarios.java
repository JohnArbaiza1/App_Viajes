package com.example.destinos;

public class Comentarios {

    InicioActivity inicioActivity = new InicioActivity();
    public String idUser;
    public String idDestino;
    public String comment;
    public int puntuacion;

    public String nameUsuario;
    public String nameDestino;

    //Constructor
    public Comentarios(String idUser, String idDestino, String comment, int puntuacion, String nameUsuario, String nameDestino) {
        this.idUser = idUser;
        this.idDestino = idDestino;
        this.comment = comment;
        this.puntuacion = puntuacion;
        this.nameUsuario = nameUsuario;
        this.nameDestino = nameDestino;
    }

    public Comentarios() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNameUsuario() {
        return nameUsuario;
    }

    public void setNameUsuario(String nameUsuario) {
        this.nameUsuario = nameUsuario;
    }

    public String getNameDestino() {
        return nameDestino;
    }

    public void setNameDestino(String nameDestino) {
        this.nameDestino = nameDestino;
    }
}


