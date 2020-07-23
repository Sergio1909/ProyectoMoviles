package com.example.proyectomoviles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.Entidades.Usuario;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NuevaIncidenciaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    //fStore = FirebaseFirestore.getIns;
    //fStorage = FirebaseStorage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_incidencia);

        // RECUPERAR EL USUARIO LOGUEADO
        final Usuario usuario = new Usuario();

        final Incidencia incidencia = new Incidencia();
        // Nombre Incidencia
        EditText editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        String nombreIncidencia = editTextNombre.getText().toString();
        incidencia.setNombre(nombreIncidencia);
        // Descripcion Incidencia
        EditText editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        String descripcionIncidencia = editTextDescripcion.getText().toString();
        incidencia.setDescripcion(descripcionIncidencia);
        // Ubicacion Incidencia
        Spinner spinner = (Spinner) findViewById(R.id.spinnerUbicacion);
        String ubicacionIncidencia = spinner.getSelectedItem().toString();
        incidencia.setLugar(ubicacionIncidencia);
        // Autor Incidencia
        String autorIncidencia = usuario.getNombre(); // !!!!!!!!!!!
        incidencia.setUsuarioAutor(autorIncidencia);
        // Estado Incidencia
        incidencia.setEstado("Por Atender");
        // Fecha Incidencia (ESTA EN GMT CAMBIAR!!!!)
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String fechaActual = formatter.format(date);
        incidencia.setFecha(fechaActual);

        // FALTA LODE LA FOTO

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Incidencias").push().setValue(incidencia);

    }








}
