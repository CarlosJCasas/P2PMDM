package com.example.p1pmdm.DDBB;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.p1pmdm.core.Entrenamiento;

import java.util.List;

@Dao
interface EntrenamientoDao {
    @Query("SELECT * FROM entrenamiento")
    List<Entrenamiento> getEntrenamientos();
}
