package com.example.p1pmdm.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p1pmdm.DDBB.EntrenamientoLab;
import com.example.p1pmdm.R;
import com.example.p1pmdm.core.Entrenamiento;
import com.example.p1pmdm.core.InputFilterMinMax;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    EditText fechaBarras;
    EditText distanciaRecorridaEd;
    EditText horasED;
    EditText minsED;
    EditText segsED;
    String fecha;
    Button botonAceptar;
    private EntrenamientoLab mTrainLab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mTrainLab = EntrenamientoLab.get(this);
        fechaBarras = findViewById(R.id.fechaBarras);
        distanciaRecorridaEd = findViewById(R.id.distanciaRecorridaEdit);
        horasED = findViewById(R.id.horasED);
        minsED = findViewById(R.id.minsED);
        segsED = findViewById(R.id.segsED);
        botonAceptar = findViewById(R.id.botonAceptar);

        minsED.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs), getString(R.string.maxSegs))});
        segsED.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs), getString(R.string.maxSegs))});

        fechaBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dist = 0;
                int horas = 0;
                int mins = 0;
                int segs = 0;
                double minsKm;
                if (horasED.getText().toString().isEmpty() && minsED.getText().toString().isEmpty() && segsED.getText().toString().isEmpty() && distanciaRecorridaEd.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(AddActivity.this, "No se admiten todos los campos vacíos.", Toast.LENGTH_LONG);
                    toast.show();
                } else if (horasED.getText().toString().isEmpty() && minsED.getText().toString().isEmpty() && segsED.getText().toString().isEmpty() || distanciaRecorridaEd.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(AddActivity.this, "Es necesario introducir una distancia o alguna unidad de tiempo.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    if (!horasED.getText().toString().isEmpty()) {
                        horas = Integer.parseInt(horasED.getText().toString());
                    }
                    if (!minsED.getText().toString().isEmpty()) {
                        mins = Integer.parseInt(minsED.getText().toString());
                    }
                    if (!segsED.getText().toString().isEmpty()) {
                        segs = Integer.parseInt(segsED.getText().toString());
                    }
                    if (!distanciaRecorridaEd.getText().toString().isEmpty()) {
                        dist = Integer.parseInt(distanciaRecorridaEd.getText().toString());
                    }
                    if (fechaBarras.getText().toString().isEmpty()) {
                        int day, month, year;
                        Calendar calendario = Calendar.getInstance();
                        day = calendario.get(Calendar.DAY_OF_MONTH);
                        month = calendario.get(Calendar.MONTH);
                        year = calendario.get(Calendar.YEAR);
                        String selectedDate = day + "/" + month + "/" + year;
                        fecha = selectedDate;
                    }else{
                        fecha = fechaBarras.getText().toString();
                    }

                    if (horas == 0 && mins == 0 && segs == 0) {
                        Toast toast = Toast.makeText(AddActivity.this, "El tiempo no puede ser 00:00:00", Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        minsKm = (double) (((horas * 60 + mins) * 1000) / dist);
                        Entrenamiento entrenamiento = new Entrenamiento(fecha, dist, horas, mins, segs, minsKm);
                        String idEntrenamiento = entrenamiento.getIdTrain();
                        mTrainLab.addTrain(entrenamiento);
                        Intent intent = new Intent();
                        intent.putExtra("uuid",idEntrenamiento);
                        setResult(2,intent);
                        finish();
                    }
                }
            }
        });

    }

    private void showDatePickerDialog() {
        int day, month, year;
        final Calendar calendario = Calendar.getInstance();
        day = calendario.get(Calendar.DAY_OF_MONTH);
        month = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendario.set(Calendar.MONTH, month1);
                calendario.set(Calendar.YEAR, year1);
                String selectedDate = dayOfMonth + "/" + month1 + "/" + year1;
                fechaBarras.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}