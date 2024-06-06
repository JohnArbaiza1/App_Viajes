package com.example.destinos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AgregarDestinosFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;
    private EditText descripcionField, direccionField, nombreField;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarDestinosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarDestinosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarDestinosFragment newInstance(String param1, String param2) {
        AgregarDestinosFragment fragment = new AgregarDestinosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_destinos, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); //referencias para conocer usuario actual y guardado de imagen
        mStorage = FirebaseStorage.getInstance().getReference();

        descripcionField = view.findViewById(R.id.txtdescriD);
        direccionField = view.findViewById(R.id.txtdireD);
        nombreField = view.findViewById(R.id.txtnombreD);
        imageView = view.findViewById(R.id.imageView2);
        Button subirImagenButton = view.findViewById(R.id.btngaleria);
        Button guardarButton = view.findViewById(R.id.btnguardardestino);

        subirImagenButton.setOnClickListener(v -> abrirGaleria()); //eventos de los botones
        guardarButton.setOnClickListener(v -> guardarDatos());

        return view;
    }
//funcion del primer evento
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST); //realizamos un intent para poder abrir y la galeria y capturar los elementos que seleccionemos
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //verificamos si la solicitud se realizo con exito y no devolvio ninguna valor nulo
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // obtenemos la url
            imageView.setImageURI(imageUri); // la mandamos
        }
    }

    private void guardarDatos() {
        FirebaseUser currentUser = mAuth.getCurrentUser(); //obtenemos el usuario autentificado
        if (currentUser == null) {

            Toast.makeText(getContext(), "Por favor, inicie sesión para continuar", Toast.LENGTH_SHORT).show();
            return;
        }//si es nulo manda un mensaje

        String descripcion = descripcionField.getText().toString().trim(); //  y si no obtenemos la info de los editText
        String direccion = direccionField.getText().toString().trim();
        String nombre = nombreField.getText().toString().trim();

        //verificamos que no vaya ninguno vacio
        if (descripcion.isEmpty() || direccion.isEmpty() || nombre.isEmpty() || imageUri == null) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos y seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        //creamos la referencia donde indicamos la carpeta y la extension que tendran los elementos a guardar

        StorageReference fileRef = mStorage.child("images/" + System.currentTimeMillis() + ".jpg");


        fileRef.putFile(imageUri) //subimos la imagen
                .addOnSuccessListener(taskSnapshot -> {
                    //obtenenemos la url
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString(); //lo convertimos a una cadena de texto

                        //llamamos al metodo que manejara el envio de los datos y sus parametros
                        guardarEnFirebase(currentUser.getUid(), descripcion, direccion, nombre, imageUrl);
                    });
                })
                //por si falla la subida de la imagen
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    //metodo para guardar
    private void guardarEnFirebase(String userId, String descripcion, String direccion, String nombre, String imageUrl) {

        DatabaseReference destinosRef = mDatabase.child("Destinos"); // referencia a nuestro nodo Destinos


        String key = destinosRef.push().getKey(); // generamos una key  principal que representara los id de los destinos


        //creamos un mapa de datos
        Map<String, Object> destinoMap = new HashMap<>(); // y con el metodo push vamos ingresando los valores a rellenar en nuestro nodo
        destinoMap.put("iduser", userId);
        destinoMap.put("Descripcion", descripcion);
        destinoMap.put("Direccion", direccion);
        destinoMap.put("Nombre", nombre);
        destinoMap.put("URLImagen", imageUrl);


        destinosRef.child(key).setValue(destinoMap) // luego le pasamos la key unica en este caso su id  y luego el mapeo de informacion
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                    
                    descripcionField.setText("");
                    direccionField.setText("");       //si se guardo correctamente mandamos un mensaje y limpiamos los campos
                    nombreField.setText("");
                    imageView.setImageDrawable(null);
                    imageUri = null;
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }}
