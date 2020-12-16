package com.example.p1pmdm.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.p1pmdm.R;

import org.w3c.dom.Text;

public class EstadisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        TextView metros = findViewById(R.id.metros);
        TextView minutosKm = findViewById(R.id.minutosKm);

        TextView velocidadKMH = findViewById(R.id.velocidadKMH);
        TextView minuntosTiempo = findViewById(R.id.minutosTiempo);

        Intent intent = getIntent();
        int distaciaTotal = intent.getIntExtra("distanciaTotal", 0);
        double minsKmtotales = intent.getDoubleExtra("minsKmtotales", 0.0);
        double tiempoTotalMinutos = intent.getDoubleExtra("tiempoTotalMinutos",0.0);
        double mediaVelocidadesKMH = intent.getDoubleExtra("mediaVelocidadesKMH",0.0);

        metros.setText(""+distaciaTotal);
        minutosKm.setText(""+minsKmtotales);
        velocidadKMH.setText(""+mediaVelocidadesKMH);
        minuntosTiempo.setText(""+tiempoTotalMinutos);

    }
}