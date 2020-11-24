package com.example.p1pmdm.DDBB;

import android.content.Context;

import androidx.room.Room;

import com.example.p1pmdm.core.Entrenamiento;

import java.util.ArrayList;
import java.util.List;

public class EntrenamientoLab implements EntrenamientoDao{

    private static EntrenamientoLab mTrainLab;
    private EntrenamientoDao mTrainDao;

    private EntrenamientoLab(Context context){
        Context appContext = context.getApplicationContext();
        EntrenamientoBD database = Room.databaseBuilder(appContext,EntrenamientoBD.class,
                "entrenamiento").allowMainThreadQueries().build();

        mTrainDao = database.getEntrenamientoDao();
    }

    public static EntrenamientoLab get(Context context){
        if(mTrainLab == null){
            mTrainLab = new EntrenamientoLab(context);
        }
        return mTrainLab;
    }


    @Override
    public List<Entrenamiento> getEntrenamientos() {
        return mTrainDao.getEntrenamientos();
    }

    @Override
    public Entrenamiento getEntrenamiento(String uuid) {
        return mTrainDao.getEntrenamiento(uuid);
    }

    @Override
    public void deleteAll() {
        mTrainDao.deleteAll();
    }

    @Override
    public void addTrain(Entrenamiento train) {
        mTrainDao.addTrain(train);
    }

    @Override
    public void delTrain(Entrenamiento train) {
        mTrainDao.delTrain(train);
    }

    @Override
    public void updateTrain(Entrenamiento train) {
        mTrainDao.updateTrain(train);
    }
}
