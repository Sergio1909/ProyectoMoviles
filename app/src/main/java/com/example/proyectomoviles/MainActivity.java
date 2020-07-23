package com.example.proyectomoviles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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

    }

    private void dameubicacion(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Tenemos permiso", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        ubicacion= LocationServices.getFusedLocationProviderClient(MainActivity.this);

        ubicacion.getLastLocation().addOnSuccessListener(MainActivity.this,new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){
                if(location !=null){
                    Double latitud = location.getLatitude();
                    Double longitud = location.getLongitude();

                    Toast.makeText(MainActivity.this, "Latitud: "+ latitud + "Longitud: " + longitud,Toast.LENGTH_SHORT).show();

                    //UbicacionPj ubi = new UbicacionPj(latitud,longitud); //Descomentar firebase
                    //refubicacion.push().setValue(ubi); // Descomentar Firebase

                    //Toast.makeText(MainActivity.this, "Ubicacion agregada",Toast.LENGTH_SHORT).show(); //Descomentar firebase

                }
            }
        });
    }
}
