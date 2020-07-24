package com.example.proyectomoviles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.Entidades.Usuario;
import com.example.proyectomoviles.Entidades.uploadinfo;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.FusedLocationProviderClient;

//import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NuevaIncidenciaActivity extends AppCompatActivity {
    private FusedLocationProviderClient ubicacion;
    Button btn_dameubi;
    //FirebaseDatabase database; //Descomentar para firebase
    //DatabaseReference refubicacion; //Descomentar firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Button btnbrowse, btnupload;
    EditText txtdata ;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    //fStore = FirebaseFirestore.getIns;
    //fStorage = FirebaseStorage.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_incidencia);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnupload= (Button)findViewById(R.id.btnupload);
        txtdata = (EditText)findViewById(R.id.txtdata);
        imgview = (ImageView)findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(NuevaIncidenciaActivity.this);// Nombre del contexto

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

        //Mapa

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

        // FALTA LODE LA FOTO

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Incidencias").push().setValue(incidencia);

        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });

    }

    private void dameubicacion() {
        if(ContextCompat.checkSelfPermission(NuevaIncidenciaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Tenemos permiso", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(NuevaIncidenciaActivity.this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        ubicacion = LocationServices.getFusedLocationProviderClient(NuevaIncidenciaActivity.this);

        ubicacion.getLastLocation().addOnSuccessListener(NuevaIncidenciaActivity.this,new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location){
                if(location !=null){
                    Double latitud = location.getLatitude();
                    Double longitud = location.getLongitude();

                    Toast.makeText(NuevaIncidenciaActivity.this, "Latitud: "+ latitud + "Longitud: " + longitud,Toast.LENGTH_SHORT).show();

                    //UbicacionPj ubi = new UbicacionPj(latitud,longitud); //Descomentar firebase
                    //refubicacion.push().setValue(ubi); // Descomentar Firebase

                    //Toast.makeText(MainActivity.this, "Ubicacion agregada",Toast.LENGTH_SHORT).show(); //Descomentar firebase

                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Subiendo imagen...");
            progressDialog.show();
            //Linea anterior
            // StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            String nombrefoto = getSaltString();
            StorageReference storageReference2 = storageReference.child(nombrefoto + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = txtdata.getText().toString().trim();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Imagen subida exitosamente ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(NuevaIncidenciaActivity.this, "Por favor seleccione una imagen o añada un nombre", Toast.LENGTH_LONG).show();

        }


    }








}
