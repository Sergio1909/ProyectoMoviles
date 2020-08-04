package com.example.proyectomoviles;

import androidx.annotation.NonNull;
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

import com.example.proyectomoviles.Entidades.Comentario;
import com.example.proyectomoviles.Entidades.Incidencia;
import com.example.proyectomoviles.Entidades.IncidenciaTu;
import com.example.proyectomoviles.Entidades.UbicacionPj;
import com.example.proyectomoviles.Entidades.Usuario;
import com.example.proyectomoviles.Entidades.uploadinfo;
import com.example.proyectomoviles.Usuarios.IncidenciaUsuarioActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.FusedLocationProviderClient;

//import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NuevaIncidenciaActivity extends AppCompatActivity {

    private FusedLocationProviderClient ubicacion;
    Button btn_dameubi;
    //FirebaseDatabase database; //Descomentar para firebase
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Button btnbrowse, btnupload;
  //  EditText txtdata ;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    //fStore = FirebaseFirestore.getIns;
    //fStorage = FirebaseStorage.getInstance();
    String nombrefoto = "nombre_generico";
    double latitud = 0;
    double longitud = 0;
    String nombreIncidencia = "init";
    String descripcionIncidencia = "init";
    String ubicacionIncidencia = "init";
    String autorIncidencia = "init";

    public NuevaIncidenciaActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_incidencia);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        btnbrowse = (Button) findViewById(R.id.btnbrowse);
        btnupload = (Button) findViewById(R.id.btnupload);
        // txtdata = (EditText)findViewById(R.id.txtdata);
        imgview = (ImageView) findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(NuevaIncidenciaActivity.this);// Nombre del contexto

        // RECUPERAR EL USUARIO LOGUEADO
        final Usuario usuario = new Usuario();


        // Ubicacion Incidencia



        // Autor Incidencia
        //autorIncidencia = usuario.getNombre(); // !!!!!!!!!!!

        // Estado Incidencia

        // Fecha Incidencia (ESTA EN GMT CAMBIAR!!!!)
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String fechaActual = formatter.format(date);

        // Foto Incidencia nombre en el metodo upload image

        //Mapa

        //database =FirebaseDatabase.getInstance(); // Descomentar para la conexion
        //refubicacion=database.getReference("ubicacion"); //Descomentar para la conexion
        btn_dameubi = findViewById(R.id.btn_dameubi);

        //Obtener ubicacion
        btn_dameubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                dameubicacion();

                //

            }
        });


        //Seleccionar foto y generacion de nombre aleatorio
        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
                nombrefoto = getSaltString();

            }
        });

        //Obtener autor
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Usuarios").child(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    //   autorIncidencia = snapshot.child("nombre").getValue().toString();
                    autorIncidencia = usuario.getNombre();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Subir Incidencia
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nombre Incidencia
                EditText editTextNombre = (EditText) findViewById(R.id.editTextNombre);
                nombreIncidencia = editTextNombre.getText().toString();

                // Descripcion Incidencia
                EditText editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
                descripcionIncidencia = editTextDescripcion.getText().toString();

                Spinner spinner = (Spinner) findViewById(R.id.spinnerUbicacion);
                ubicacionIncidencia = spinner.getSelectedItem().toString();




                Comentario comentario = new Comentario();
                comentario.setAutorComentario("AdminPrueba");
                comentario.setDescripcionComentario("MensajePrueba");
                comentario.setFechaComentario("Fecha Prueba");
                List<Comentario> comentarios = new ArrayList<>();
                comentarios.add(comentario);
                final IncidenciaTu incidencia = new IncidenciaTu();

                incidencia.setNombre(nombreIncidencia);
                incidencia.setDescripcion(descripcionIncidencia);
                incidencia.setLugar(ubicacionIncidencia);
                incidencia.setUsuarioAutor(autorIncidencia);
                incidencia.setEstado("Por Atender");
                incidencia.setFecha(fechaActual);
                incidencia.setLatitud(latitud);
                incidencia.setLongitud(longitud);
                incidencia.setFoto(nombrefoto);
                incidencia.setComentarios(comentarios);
                incidencia.setAdministrador("");
                UploadImage();

                databaseReference.child("Incidencias").push().setValue(incidencia);
                Intent intent = new Intent(NuevaIncidenciaActivity.this, IncidenciaUsuarioActivity.class);
                startActivity(intent);

            }
        });

    }

    private void dameubicacion() {
        if(ContextCompat.checkSelfPermission(NuevaIncidenciaActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Se tienen permisos para localización, espere...", Toast.LENGTH_SHORT).show();
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
                    latitud = location.getLatitude();
                     longitud = location.getLongitude();

                    Toast.makeText(NuevaIncidenciaActivity.this, "Latitud: "+ latitud + "Longitud: " + longitud,Toast.LENGTH_SHORT).show();

                    UbicacionPj ubi = new UbicacionPj(latitud,longitud); //Descomentar firebase
                    FirebaseDatabase.getInstance().getReference("ubicacion").push().setValue(ubi); // Descomentar Firebase

                    Toast.makeText(NuevaIncidenciaActivity.this, "Ubicacion agregada",Toast.LENGTH_SHORT).show(); //Descomentar firebase

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

    //Generador de nombre aleatorio para las fotos

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string. heroe
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Registrando datos de incidencia, espere...");
            progressDialog.show();
            //Linea anterior
            // StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            StorageReference storageReference2 = storageReference.child(nombrefoto + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            //                String TempImageName = txtdata.getText().toString().trim();
                            String TempImageName = nombrefoto;
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Incidencia reportada exitosamente ", Toast.LENGTH_LONG).show();
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
