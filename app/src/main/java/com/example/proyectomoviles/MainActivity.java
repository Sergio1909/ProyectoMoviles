package com.example.proyectomoviles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.proyectomoviles.Entidades.IncidenciaDTO;
import com.example.proyectomoviles.Usuarios.IncidenciaUsuarioActivity;
import com.example.proyectomoviles.Usuarios.RegistroActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectomoviles.Entidades.UbicacionPj;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient ubicacion;

    Button btn_dameubi;
    //FirebaseDatabase database; //Descomentar para firebase
    //DatabaseReference refubicacion; //Descomentar firebase



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        IncidenciaDTO incidenciaDTO = new IncidenciaDTO();
        incidenciaDTO.setAutor("Elmo Mazo");
        incidenciaDTO.setDescripcion("Descripcion generica");
        incidenciaDTO.setEstado("sin atender");
        incidenciaDTO.setFecha("hoy dia xd");
        incidenciaDTO.setLugar("Jamaica");
        incidenciaDTO.setNombre("Eventoprueba");
        databaseReference.child("Incidencias").push().setValue(incidenciaDTO).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Log.d("infoApp", "guardado exitosamente");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("infoApp", "onFailure",e.getCause());
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("img");

        File directorio =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File archivo = new File(directorio,"images (23).jpeg");

        try {
            InputStream stream = new FileInputStream(archivo);
            imageRef = storageRef.child("img/images (23).jpeg");
            UploadTask uploadTask = imageRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("infoApp", "subido exitoso");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp","error",e.getCause());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

/*
            //database =FirebaseDatabase.getInstance(); // Descomentar para la conexion
            //refubicacion=database.getReference("ubicacion"); //Descomentar para la conexion
            btn_dameubi=findViewById(R.id.btn_dameubi);
            btn_dameubi.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //
                    dameubicacion();

                    //

                }
            });


*/



    }

    public void iniciarSesion(View view){

        List<AuthUI.IdpConfig> listaProveedores =
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()

                );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(listaProveedores)
                        .build(),
                1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Log.d("infoApp","inicio de sesion exitoso");
                Intent intent = new Intent(this, IncidenciaUsuarioActivity.class);
                startActivity(intent);
            } else {

                Log.d("infoApp","inicio erroneo");
            }

        }
        /*
        //database =FirebaseDatabase.getInstance(); // Descomentar para la conexion
        //refubicacion=database.getReference("ubicacion"); //Descomentar para la conexion
        btn_dameubi=findViewById(R.id.btn_dameubi);
        btn_dameubi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //
                dameubicacion();

                //

            }
        });
        */
    }

/*
    private void dameubicacion(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Tenemos permiso", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        ubicacion= LocationServices.getFusedLocationProviderClient(MainActivity.this);

    }
    /*
        ubicacion.getLastLocation().addOnSuccessListener(MainActivity.this,new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){
                if(location !=null){
                    Double latitud = location.getLatitude();
                    Double longitud = location.getLongitude();

    //Abrir registros
                    Toast.makeText(MainActivity.this, "Latitud: "+ latitud + "Longitud: " + longitud,Toast.LENGTH_SHORT).show();
/*
    public void abrirRegistrosActivity(View view){
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
                    //UbicacionPj ubi = new UbicacionPj(latitud,longitud); //Descomentar firebase
                    //refubicacion.push().setValue(ubi); // Descomentar Firebase

                    //Toast.makeText(MainActivity.this, "Ubicacion agregada",Toast.LENGTH_SHORT).show(); //Descomentar firebase

                }
            }
        });
    }
*/


}
