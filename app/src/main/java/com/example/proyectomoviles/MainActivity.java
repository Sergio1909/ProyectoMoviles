package com.example.proyectomoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.proyectomoviles.Entidades.IncidenciaNoFuncaDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    IncidenciaNoFuncaDTO incidenciaNoFuncaDTO = new IncidenciaNoFuncaDTO();

}
