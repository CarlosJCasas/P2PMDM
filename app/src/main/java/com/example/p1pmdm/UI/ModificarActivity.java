package com.example.p1pmdm.UI;

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

public class ModificarActivity extends AppCompatActivity {

    Entrenamiento entrenamiento;
    EditText fechaBarras;
    String uuid;
    private EntrenamientoLab mTrainLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_activity);
        mTrainLab = EntrenamientoLab.get(this);
        Intent intent = getIntent();
        uuid =intent.getStringExtra("uuid");

        entrenamiento = mTrainLab.getEntrenamiento(uuid);

        final EditText distanciaRecorridaEd = findViewById(R.id.distanciaRecorridaEditModificar);
        distanciaRecorridaEd.setHint(String.valueOf(entrenamiento.getDistancia()));

        final EditText horasED = findViewById(R.id.horasEDModificar);
        horasED.setHint(String.valueOf(entrenamiento.getHoras()));

        final EditText minsED = findViewById(R.id.minsEDModificar);
        minsED.setHint(String.valueOf(entrenamiento.getMinutos()));

        final EditText segsED = findViewById(R.id.segsEDModificar);
        segsED.setHint(String.valueOf(entrenamiento.getSegundos()));

        minsED.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs), getString(R.string.maxSegs))});
        segsED.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs), getString(R.string.maxSegs))});

        final EditText fechaBarras = findViewById(R.id.fechaBarrasModificar);

        fechaBarras.setHint(entrenamiento.getFecha());
        fechaBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Button botonAceptar = findViewById(R.id.botonAceptarModificar);

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int horas = entrenamiento.getHoras();
                String horasFormateadas = String.format("%02d", horas);
                if (!horasED.getText().toString().isEmpty()) {
                    horas = Integer.parseInt(horasED.getText().toString());
                    horasFormateadas = String.format("%02d", horas);
                }

                int mins = entrenamiento.getMinutos();
                String minsFormateados = String.format("%02d", mins);
                if (!minsED.getText().toString().isEmpty()) {
                    mins = Integer.parseInt(minsED.getText().toString());
                    minsFormateados = String.format("%02d", mins);
                }

                int segs = entrenamiento.getSegundos();
                String segsFormateados = String.format("%02d", segs);
                if (!segsED.getText().toString().isEmpty()) {
                    segs = Integer.parseInt(segsED.getText().toString());
                    segsFormateados = String.format("%02d", segs);
                }

                int dist = entrenamiento.getDistancia();
                if (!distanciaRecorridaEd.getText().toString().isEmpty()) {
                    dist = Integer.parseInt(distanciaRecorridaEd.getText().toString());
                }
                String fechaAntigua = entrenamiento.getFecha();
                if (!fechaBarras.getText().toString().isEmpty()) {
                    fechaAntigua = fechaBarras.getText().toString();
                }

                double minsKm = entrenamiento.getMinsKm();
                entrenamiento.setFecha(fechaAntigua);
                entrenamiento.setDistancia(dist);
                entrenamiento.setHoras(horas);
                entrenamiento.setMinutos(mins);
                entrenamiento.setSegundos(segs);
                entrenamiento.setMinsKm(minsKm);

                if (horas == 0 && mins == 0 && segs == 0) {
                    Toast toast = Toast.makeText(ModificarActivity.this, "El tiempo no puede ser 00:00:00", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    String textoVisible = fechaAntigua +
                            " Distancia: " + entrenamiento.getDistancia() + "m" +
                            " Tiempo: " + horasFormateadas + ":" + minsFormateados + ":" + segsFormateados;
                    //Modificar en la base de datos
                    mTrainLab.updateTrain(entrenamiento);
                    Intent intent = new Intent();
                    intent.putExtra("uuid", entrenamiento.getIdTrain());
                    setResult(3,intent);
                    finish();
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