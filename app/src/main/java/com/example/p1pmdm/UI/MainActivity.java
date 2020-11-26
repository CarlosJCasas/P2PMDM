package com.example.p1pmdm.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.p1pmdm.DDBB.EntrenamientoLab;
import com.example.p1pmdm.R;
import com.example.p1pmdm.core.Entrenamiento;
import com.example.p1pmdm.core.InputFilterMinMax;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> itemList;
    private ArrayAdapter<String> listAdapter;
    private List<Entrenamiento> entrenamientos;
    private int posicion;
    private EditText fechaEd;
    private String fecha;
    private EntrenamientoLab mTrainLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Crea el archivo para guardar las estadisticas
        File estadisticas = new File(MainActivity.this.getFilesDir(),"EstadisticasGenerales.txt");


        mTrainLab = EntrenamientoLab.get(this);

        //Borrar toda la base de datos
//        mTrainLab.deleteAll();

        this.entrenamientos = mTrainLab.getEntrenamientos();

        this.itemList = new ArrayList<>();

        ListView listView = this.findViewById(R.id.viewList1);
        FloatingActionButton floatButton = this.findViewById(R.id.floatingActionButton2);
        listView.setLongClickable(true);
        listAdapter = new ArrayAdapter<>(this.getApplicationContext(),android.R.layout.simple_list_item_1,this.itemList);
        listView.setAdapter(this.listAdapter);
        
        //Hay que recorrer todos los entrenamientos y meter su toString en itemList que es la que añade a la vista
        if(!entrenamientos.isEmpty()){
            for (Entrenamiento entreno : entrenamientos){
                String texto = entreno.toString();
                MainActivity.this.listAdapter.add(texto);
                MainActivity.this.listAdapter.notifyDataSetChanged();
            }
        }

        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >=0){
                    posicion = position;
                    MainActivity.this.mostrar(posicion);
                }
            }
        });
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MainActivity.this.addTraining();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText nombre, edad, altura, peso;
        nombre = findViewById(R.id.nombreEd);
        edad = findViewById(R.id.edadEd);
        altura = findViewById(R.id.alturaEd);
        peso = findViewById(R.id.pesoEd);

        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.misPreferencias),Context.MODE_PRIVATE);
        nombre.setText(sharedPreferences.getString(getString(R.string.nombre), ""));
        edad.setText(sharedPreferences.getString(getString(R.string.edad), ""));
        altura.setText(sharedPreferences.getString(getString(R.string.altura), ""));
        peso.setText(sharedPreferences.getString(getString(R.string.peso), ""));

    }

    @Override
    protected void onStop() {
        super.onStop();
        int metrosTotales = 0;
        double minsKmTotales = 0;
        double kmTotales;
        double mediaMinsKm;
        double minutosKmEach;

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("EstadisticasGenerales.txt", Context.MODE_PRIVATE);
            for(Entrenamiento ent : entrenamientos){
                metrosTotales = metrosTotales + ent.getDistancia();
                //Calcular los mins por km de cada entrenamiento
                minutosKmEach = (1000*((ent.getHoras()*60)+ent.getMinutos()))/ent.getDistancia();
                minsKmTotales = minsKmTotales + minutosKmEach;
            }
            kmTotales = (double)metrosTotales/1000;
            mediaMinsKm = minsKmTotales/entrenamientos.size();

            String estadisticas = "Estadisticas Generales\n\n" + "Kilometros totales recorridos: " + String.format("%.2f", kmTotales) + "\n\nMedia de minutos por kilometro recorrido: " + String.format("%.2f", mediaMinsKm);
            outputStream.write(estadisticas.getBytes());
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText nombre, edad, altura, peso;
        nombre = findViewById(R.id.nombreEd);
        edad = findViewById(R.id.edadEd);
        altura = findViewById(R.id.alturaEd);
        peso = findViewById(R.id.pesoEd);

        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.misPreferencias),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.nombre), nombre.getText().toString());
        editor.putString(getString(R.string.edad), edad.getText().toString());
        editor.putString(getString(R.string.altura), altura.getText().toString());
        editor.putString(getString(R.string.peso), peso.getText().toString());

        editor.apply();
    }

    public void addTraining(){

        final Entrenamiento[] train = new Entrenamiento[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_train,null);
        builder.setView(customLayout);
        TextView title = new TextView(this);
        title.setText(R.string.alDiag_train);
        title.setTextSize(25);
        title.setPadding(25,25,25,25);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setTextColor(getResources().getColor(R.color.blanco));
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);

        fechaEd = customLayout.findViewById(R.id.fechaEditText);
        fechaEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        final EditText distEd = customLayout.findViewById(R.id.distanciaEditText);
        final EditText horasEd = customLayout.findViewById(R.id.horasEditText);
        final EditText minsEd = customLayout.findViewById(R.id.minutosEditText);
        minsEd.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs),getString(R.string.maxSegs))});
        final EditText segsEd = customLayout.findViewById(R.id.segundosEditText);
        segsEd.setFilters(new InputFilter[]{new InputFilterMinMax(getString(R.string.minSegs),getString(R.string.maxSegs))});
        builder.setPositiveButton(R.string.alDiag_posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

        int dist = 0;
        int horas = 0;
        int mins = 0;
        int segs = 0;
        double minsKm;

        if (horasEd.getText().toString().isEmpty() && minsEd.getText().toString().isEmpty() && segsEd.getText().toString().isEmpty() && distEd.getText().toString().isEmpty()){
                Toast toast = Toast.makeText(MainActivity.this,"No se admiten todos los campos vacíos.", Toast.LENGTH_LONG);
                toast.show();
                addTraining();

        }else if(horasEd.getText().toString().isEmpty() && minsEd.getText().toString().isEmpty() && segsEd.getText().toString().isEmpty() || distEd.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(MainActivity.this, "Es necesario introducir una distancia o alguna unidad de tiempo.", Toast.LENGTH_LONG);
            toast.show();
            addTraining();
        }else {

            if(!horasEd.getText().toString().isEmpty()){
                horas = Integer.parseInt(horasEd.getText().toString());
                String.format("%02d", horas); //Deberia poner los numeros con dos cifras rellenando con 0s
            }
            if(!minsEd.getText().toString().isEmpty()){
                mins = Integer.parseInt(minsEd.getText().toString());
            }
            if(!segsEd.getText().toString().isEmpty()){
                segs = Integer.parseInt(segsEd.getText().toString());
            }
            if(!distEd.getText().toString().isEmpty()){
                dist = Integer.parseInt(distEd.getText().toString());
            }
            if(fechaEd.getText().toString().isEmpty()){
                int day, month, year;
                Calendar calendario  = Calendar.getInstance();
                day = calendario.get(Calendar.DAY_OF_MONTH);
                month = calendario.get(Calendar.MONTH);
                year = calendario.get(Calendar.YEAR);
                String selectedDate = day+"/"+month+"/"+year;
                fecha = selectedDate;
            }
            minsKm = (double)(((horas*60 + mins)*1000)/dist);

            train[0] = new Entrenamiento(fecha, dist, horas, mins, segs, minsKm);
            String texto = train[0].toString();

            //Añadir a la base de datos
            mTrainLab.addTrain(train[0]);

            MainActivity.this.listAdapter.add(texto);
            MainActivity.this.listAdapter.notifyDataSetChanged();
            MainActivity.this.entrenamientos.add(train[0]);
        }

            }
        });

        builder.setNegativeButton(R.string.alDiag_canButton, null);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mostrar(int position){
        String formattedVelMedia = "";
        String formattedMinsKm = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View customLayout = getLayoutInflater().inflate(R.layout.mostrar_lay, null);
        dialogBuilder.setView(customLayout);

        TextView fechaTextView = customLayout.findViewById(R.id.textViewFecha);
        fechaTextView.setText(getString(R.string.fechaTextView)+MainActivity.this.entrenamientos.get(position).getFecha());

        TextView distanciaTextView = customLayout.findViewById(R.id.textViewDistancia);
        distanciaTextView.setText(getString(R.string.distanciaTextView)+String.valueOf(MainActivity.this.entrenamientos.get(position).getDistancia()));

        TextView tiempoTextView = customLayout.findViewById(R.id.textViewTiempo);
        tiempoTextView.setText(getString(R.string.tiempoTextView)+MainActivity.this.entrenamientos.get(position).getHoras()+":"+MainActivity.this.entrenamientos
                .get(position).getMinutos()+":"+MainActivity.this.entrenamientos.get(position).getSegundos());

        int minutosHora = (MainActivity.this.entrenamientos.get(position).getHoras()*60) + MainActivity.this.entrenamientos
                .get(position).getMinutos();

        double minutosKm = 0;
        double kilometros = (double)MainActivity.this.entrenamientos.get(position).getDistancia()/(double)1000;
        if(MainActivity.this.entrenamientos.get(position).getDistancia()!=0) {
            minutosKm = (double)minutosHora / kilometros;
            formattedMinsKm = String.format("%.2f", minutosKm);
        }

        TextView minsxKm = customLayout.findViewById(R.id.textViewMinsKm);
        minsxKm.setText(getString(R.string.minsKm) + formattedMinsKm);
        double velMedia = 0;
        double horas = 0;
        horas = ((double)MainActivity.this.entrenamientos.get(position).getHoras() + ((double)MainActivity.this.entrenamientos.get(position).getMinutos()/(double)60));
        if(horas!=0) {
            velMedia = ((double)MainActivity.this.entrenamientos.get(position).getDistancia()/(double)1000) / horas;
            formattedVelMedia = String.format("%.2f", velMedia);
        }

        TextView velMed = customLayout.findViewById(R.id.textViewVelMedia);
        velMed.setText(getString(R.string.velMed) + formattedVelMedia + getString(R.string.kmH));
        dialogBuilder.create().show();
    }

    public void modificar(){
        final Entrenamiento[] train = new Entrenamiento[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_train,null);
        builder.setView(customLayout);

        TextView title = new TextView(this);
        title.setText(R.string.modificar);
        title.setTextSize(25);
        title.setPadding(25,25,25,25);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setTextColor(getResources().getColor(R.color.blanco));
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);

        fechaEd = customLayout.findViewById(R.id.fechaEditText);
        fechaEd.setHint(entrenamientos.get(posicion).getFecha());
        fechaEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        final EditText distEd = customLayout.findViewById(R.id.distanciaEditText);
        distEd.setHint(String.valueOf(entrenamientos.get(posicion).getDistancia()));
        final EditText horasEd = customLayout.findViewById(R.id.horasEditText);
        horasEd.setHint(String.valueOf(entrenamientos.get(posicion).getHoras()));
        final EditText minsEd = customLayout.findViewById(R.id.minutosEditText);
        minsEd.setHint(String.valueOf(entrenamientos.get(posicion).getMinutos()));
        final EditText segsEd = customLayout.findViewById(R.id.segundosEditText);
        segsEd.setHint(String.valueOf(entrenamientos.get(posicion).getSegundos()));

        builder.setPositiveButton(R.string.alDiag_posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            int horas = entrenamientos.get(posicion).getHoras();
            if(!horasEd.getText().toString().isEmpty()){
                horas = Integer.parseInt(horasEd.getText().toString());
            }
            int mins = entrenamientos.get(posicion).getMinutos();
            if(!minsEd.getText().toString().isEmpty()) {
                mins = Integer.parseInt(minsEd.getText().toString());
            }
            int segs = entrenamientos.get(posicion).getSegundos();
            if(!segsEd.getText().toString().isEmpty()) {
                segs = Integer.parseInt(segsEd.getText().toString());
            }
            int dist = entrenamientos.get(posicion).getDistancia();
            if(!distEd.getText().toString().isEmpty()) {
                dist = Integer.parseInt(distEd.getText().toString());
            }
            String fechaAntigua = entrenamientos.get(posicion).getFecha();
            if(!fechaEd.getText().toString().isEmpty()){
                fechaAntigua = fechaEd.getText().toString();
            }
            double minsKm = entrenamientos.get(posicion).getMinsKm();

            train[0] = new Entrenamiento(fechaAntigua,dist,horas,mins,segs, minsKm);
            String texto = train[0].toString();

            //Modificar en la base de datos
            mTrainLab.updateTrain(train[0]);

            MainActivity.this.listAdapter.remove(itemList.get(posicion));
            MainActivity.this.listAdapter.add(texto);
            MainActivity.this.listAdapter.notifyDataSetChanged();
            MainActivity.this.entrenamientos.remove(posicion);
            MainActivity.this.entrenamientos.add(posicion,train[0]);
            }
        });

        builder.setNegativeButton(R.string.alDiag_canButton,null);
        builder.create().show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mostrar){
            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            posicion = (int) info.id;
            mostrar(posicion);
        }else if(item.getItemId()==R.id.modificar){
            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            posicion = (int) info.id;
            modificar();
        }else if(item.getItemId()==R.id.borrar){
            AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            posicion = (int) info.id;
            itemList.remove(posicion);
            
            //Borrar elemento de la base de datos
            mTrainLab.delTrain(entrenamientos.get(posicion));

            entrenamientos.remove(posicion);
            this.listAdapter.notifyDataSetChanged();
        }else{
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.stats) {
            double contadorKm = 0;
            double contadorMins = 0;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View customLayout = getLayoutInflater().inflate(R.layout.stats,null);
            builder.setView(customLayout);
            final TextView kmTotales = customLayout.findViewById(R.id.resultadoKmTotales);
            final TextView minsKm = customLayout.findViewById(R.id.resultadoMinsKm);
            for(Entrenamiento ent : entrenamientos){
                contadorKm = contadorKm + ent.getDistancia();
                contadorMins = contadorMins + ent.getMinutos() + (ent.getHoras()*60);
            }
            final double finalContadorKm = contadorKm/1000;
            final double minutosXkm = contadorMins/finalContadorKm;
            kmTotales.setText(String.valueOf(finalContadorKm));
            if(contadorMins == 0 || finalContadorKm == 0){
                minsKm.setText(R.string._00);
            }else {
                minsKm.setText(String.format("%.2f", minutosXkm));
            }
            builder.setPositiveButton(R.string.alDiag_posButton, null);
            builder.create().show();
            return true;
        }else if(id==R.id.add){
            addTraining();
            return true;
        }else if (id==R.id.mod){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int checkedItem = -1;
            builder.setSingleChoiceItems(listAdapter, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        posicion=which;
                        }
                    });
            builder.setPositiveButton(R.string.alDiag_posButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    modificar();
                }
            });
            builder.setNegativeButton(R.string.alDiag_canButton,null);
            builder.create().show();
            return true;
        }else if (id == R.id.clear){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final ArrayList<Integer> itemsSelected = new ArrayList<>();
            CharSequence[] cs = itemList.toArray(new CharSequence[itemList.size()]);
            builder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        itemsSelected.add(posicion);
                    }else if (itemsSelected.contains(posicion)){
                        itemsSelected.remove(Integer.valueOf(posicion));
                    }
                }
            });

            builder.setPositiveButton(R.string.alDiag_posButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i :itemsSelected){
                        itemList.remove(i);
                        entrenamientos.remove(i);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            });

            

            builder.setNegativeButton(R.string.alDiag_canButton,null);
            builder.create().show();

            return true;
        }else{
            return false;
        }
    }

    private void showDatePickerDialog(){
        int day, month, year;
        final Calendar calendario = Calendar.getInstance();
        day = calendario.get(Calendar.DAY_OF_MONTH);
        month = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendario.set(Calendar.MONTH, month1);
                calendario.set(Calendar.YEAR, year1);
                String selectedDate = dayOfMonth+"/"+month1+"/"+year1;
                fecha = selectedDate;
                fechaEd.setText(selectedDate);
            }
        },year, month,day);
        datePickerDialog.show();
    }
}