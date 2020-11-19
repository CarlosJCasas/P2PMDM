package com.example.p1pmdm.core;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity (tableName = "entrenamiento")
public class Entrenamiento {
    @PrimaryKey
    @NonNull
    private String idTrain;
    @ColumnInfo(name = "fecha")
    private String fecha;
    @ColumnInfo(name = "hora")
    private int horas;
    @ColumnInfo(name = "minuto")
    private int minutos;
    @ColumnInfo(name = "segundo")
    private int segundos;
    @ColumnInfo(name = "distancia")
    private int distancia;

    public Entrenamiento(String fecha, int distancia, int horas, int minutos, int segundos) {
        this.fecha = fecha;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.distancia = distancia;

        idTrain = UUID.randomUUID().toString();
    }

    @NonNull
    public String getIdTrain() {
        return idTrain;
    }

    public void setIdTrain(@NonNull String idTrain) {
        this.idTrain = idTrain;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }


    @Override
    public String toString() {
        return   fecha +
                " Distancia: " + distancia+"m" +
                " Tiempo: " + horas + ":" + minutos + ":" + segundos;
    }
}
