package com.example.p1pmdm.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.p1pmdm.core.Entrenamiento;

import java.util.List;

@Dao
interface EntrenamientoDao {
    @Query("SELECT * FROM entrenamiento")
    List<Entrenamiento> getEntrenamientos();

    @Query("SELECT * FROM entrenamiento WHERE idTrain LIKE :uuid")
    Entrenamiento getEntrenamiento(String uuid);

    @Query("DELETE  FROM entrenamiento")
    void deleteAll();

    @Insert
    void addTrain(Entrenamiento train);

    @Delete
    void delTrain(Entrenamiento train);

    @Update
    void updateTrain(Entrenamiento train);
}
