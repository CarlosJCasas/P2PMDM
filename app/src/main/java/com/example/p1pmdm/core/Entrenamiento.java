package com.example.p1pmdm.core;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
//@Entity (tableName = "tarea")
public class Entrenamiento {
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
    private int id;
//    @ColumnInfo(name = "fecha")
    private String fecha;

    private int horas;

    private int minutos;

    private int segundos;

    private int distancia;

    private double minsxkm;

    private double velmed;

    public Entrenamiento(String fecha, int distancia, int horas, int minutos, int segundos) {
        this.fecha = fecha;
        this.horas = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        this.distancia = distancia;
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

    public double getMinsxkm() {
        return minsxkm;
    }

    public void setMinsxkm(double minsxkm) {
        this.minsxkm = minsxkm;
    }

    public double getVelmed() {
        return velmed;
    }

    public void setVelmed(double velmed) {
        this.velmed = velmed;
    }

    @Override
    public String toString() {
        return   fecha +
                " Distancia: " + distancia+"m" +
                " Tiempo: " + horas + ":" + minutos + ":" + segundos;
    }
}
