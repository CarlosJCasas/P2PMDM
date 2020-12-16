package com.example.p1pmdm.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.p1pmdm.R;

public class DatosPersonalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        final EditText nombre = findViewById(R.id.nombreEdit);
        final EditText edad = findViewById(R.id.edadEdit);
        final EditText altura = findViewById(R.id.alturaEdit);
        final EditText peso = findViewById(R.id.pesoEdit);
        Button botonGuardar = findViewById(R.id.botonGuardar);
        final Intent intent = getIntent();
        final String textoNombre = intent.getStringExtra("nombre");
        final String textoEdad = intent.getStringExtra("edad");
        final String textoAltura = intent.getStringExtra("altura");
        final String textoPeso = intent.getStringExtra("peso");
        if(!textoNombre.isEmpty()){
            nombre.setHint(textoNombre);
        }

        if(!textoEdad.isEmpty()){
            edad.setHint(textoEdad);
        }

        if(!textoAltura.isEmpty()){
            altura.setHint(textoAltura);
        }

        if(!textoPeso.isEmpty()){
            peso.setHint(textoPeso);
        }

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String devolverNombre, devolverEdad, devolverAltura, devolverPeso;
                if(!nombre.getText().toString().isEmpty()){
                    devolverNombre = nombre.getText().toString();
                }else{
                    devolverNombre = textoNombre;
                }
                if(!edad.getText().toString().isEmpty()){
                    devolverEdad = edad.getText().toString();
                }else{
                    devolverEdad = textoEdad;
                }
                if(!altura.getText().toString().isEmpty()){
                    devolverAltura = altura.getText().toString();
                }else{
                    devolverAltura = textoAltura;
                }
                if(!peso.getText().toString().isEmpty()){
                    devolverPeso = peso.getText().toString();
                }else{
                    devolverPeso = textoPeso;
                }

                Context context = getApplicationContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.misPreferencias), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.nombre), devolverNombre);
                editor.putString(getString(R.string.edad), devolverEdad);
                editor.putString(getString(R.string.altura), devolverAltura);
                editor.putString(getString(R.string.peso), devolverPeso);
                editor.apply();
                
                intent.putExtra("nombre",devolverNombre)
                        .putExtra("edad", devolverEdad)
                        .putExtra("altura", devolverAltura)
                        .putExtra("peso", devolverPeso);
                setResult(4,intent);
                finish();
            }
        });

    }
}