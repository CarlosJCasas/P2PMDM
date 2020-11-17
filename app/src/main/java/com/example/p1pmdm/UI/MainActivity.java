package com.example.p1pmdm.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.p1pmdm.R;
import com.example.p1pmdm.core.Entrenamiento;
import com.example.p1pmdm.core.InputFilterMinMax;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> itemList;
    private ArrayAdapter<String> listAdapter;
    private ArrayAdapter<String> adaptadorBorrar;
    private ArrayList<Entrenamiento> entrenamientos;
    private int posicion;
    private EditText fechaEd;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.entrenamientos = new ArrayList<>();
        this.itemList = new ArrayList<>();
        ListView listView = this.findViewById(R.id.viewList1);
        FloatingActionButton floatButton = this.findViewById(R.id.floatingActionButton2);
        listView.setLongClickable(true);
        listAdapter = new ArrayAdapter<>(this.getApplicationContext(),android.R.layout.simple_list_item_1,this.itemList);
        adaptadorBorrar = new ArrayAdapter<>(this.getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,this.itemList);
        listView.setAdapter(this.listAdapter);
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

    private void addTraining(){
        final Entrenamiento[] train = new Entrenamiento[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        String pattern = "dd-MM-yyyy";
//        final String fecha =new SimpleDateFormat(pattern).format(new Date());
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
                int horas=0;
                if(!horasEd.getText().toString().isEmpty()){
                    horas = Integer.parseInt(horasEd.getText().toString());
                }
                int mins = 0;
                if(!minsEd.getText().toString().isEmpty()){
                    mins = Integer.parseInt(minsEd.getText().toString());
                }
                int segs = 0;
                if(!segsEd.getText().toString().isEmpty()){
                    segs = Integer.parseInt(segsEd.getText().toString());
                }
                int dist = 0;
                if(!distEd.getText().toString().isEmpty()){
                    dist = Integer.parseInt(distEd.getText().toString());
                }
                train[0] = new Entrenamiento(fecha,dist,horas,mins,segs);
                String texto = train[0].toString();
                MainActivity.this.listAdapter.add(texto);
                MainActivity.this.listAdapter.notifyDataSetChanged();
                MainActivity.this.entrenamientos.add(train[0]);
            }
        });
        builder.setNegativeButton(R.string.alDiag_canButton, null);
        builder.create().show();
    }

    public void mostrar(int position){
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
        int minutosHora = MainActivity.this.entrenamientos.get(position).getHoras()*60+MainActivity.this.entrenamientos
                .get(position).getMinutos();
        double minutosKm = 0;
        if(MainActivity.this.entrenamientos.get(position).getDistancia()!=0) {
            minutosKm = (minutosHora * 1000) / MainActivity.this.entrenamientos.get(position).getDistancia();
        }
        TextView minsxKm = customLayout.findViewById(R.id.textViewMinsKm);
        minsxKm.setText(getString(R.string.minsKm)+String.valueOf(minutosKm));
        double velMedia = 0;
        if(minutosHora!=0) {
            velMedia = MainActivity.this.entrenamientos.get(position).getDistancia() / minutosHora;
        }
        TextView velMed = customLayout.findViewById(R.id.textViewVelMedia);
        velMed.setText(getString(R.string.velMed)+String.valueOf(velMedia));
        dialogBuilder.create().show();
    }

    public void modificar(){
        final Entrenamiento[] train = new Entrenamiento[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        String pattern = "dd-MM-yyyy";
//        final String[] fecha = {new SimpleDateFormat(pattern).format(new Date())};
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
                train[0] = new Entrenamiento(fechaAntigua,dist,horas,mins,segs);
                String texto = train[0].toString();
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
            if(contadorMins==0 || finalContadorKm==0){
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