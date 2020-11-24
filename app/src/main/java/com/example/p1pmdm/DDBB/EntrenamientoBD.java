package com.example.p1pmdm.DDBB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.p1pmdm.core.Entrenamiento;

@Database(entities = {Entrenamiento.class}, version = 1)
public abstract class EntrenamientoBD extends RoomDatabase {
    public abstract EntrenamientoDao getEntrenamientoDao();
}
