package com.example.destinos.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.destinos.Comentarios;
import com.example.destinos.Destinos;
import com.example.destinos.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class destinoAdapater extends BaseAdapter {

    private Context context;
    private ArrayList<Destinos> dataDestinos;
    private DatabaseReference reference;
    private FirebaseAuth mauth;
    private Comentarios comentario;
    private String destinoId;
    private String nombreDestino;

    public destinoAdapater(Context context, ArrayList<Destinos> dataDestinos) {
        this.context = context;
        this.dataDestinos = dataDestinos;
        comentario = new Comentarios();
    }

    @Override
    public int getCount() {
        return this.dataDestinos.size();
    }

    @Override
    public Object getItem(int position) {
        return dataDestinos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_list, null);

        Destinos ds = dataDestinos.get(position);

        TextView txtnombre = view.findViewById(R.id.lblnameD);
        TextView txtdes = view.findViewById(R.id.lbldesD);
        TextView txtdire = view.findViewById(R.id.lbldireD);
        ImageView img = view.findViewById(R.id.imageView3);

        txtnombre.setText(ds.getNombre());
        txtdes.setText(ds.getDescripcion());
        txtdire.setText(ds.getDireccion());
        Picasso.get().load(ds.getURLImagen()).into(img);

        //----------------------------------------------------
        ImageButton btnComentarios = view.findViewById(R.id.btnComentar);
        ImageButton btnEnviar = view.findViewById(R.id.btnEnviarcoment);
        ImageButton btnFavorites = view.findViewById(R.id.btnFavorite);
        EditText cajaComentarios = view.findViewById(R.id.txtComentarios);
        EditText cajaPuntuacion = view.findViewById(R.id.txtPuntuacion);
        //----------------------------------------------------
        // Establecemos el botón de enviar comentario y la caja de comentarios de manera invisible
        btnEnviar.setVisibility(View.INVISIBLE);
        cajaComentarios.setVisibility(View.INVISIBLE);
        cajaPuntuacion.setVisibility(View.INVISIBLE);
        //---------------------------------------------------
        // Eventos
        btnComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnviar.setVisibility(View.VISIBLE);
                cajaComentarios.setVisibility(View.VISIBLE);
                cajaPuntuacion.setVisibility(View.VISIBLE);
                nombreDestino = ds.Direccion;
                // Toast.makeText(context, nombreDestino, Toast.LENGTH_SHORT).show();
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Destinos");
                reference.keepSynced(true);
                mauth = FirebaseAuth.getInstance();

                // Definimos una consulta para encontrar el nombre de un lugar
                Query query = reference.orderByChild("Nombre").equalTo(nombreDestino);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot destinoSnapshot : snapshot.getChildren()) {
                            destinoId = destinoSnapshot.getKey();
                        }

                        // Validamos que no hayan campos vacíos
                        if (cajaComentarios.getText().toString().isEmpty() || cajaPuntuacion.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Aun no has comentado nada viajero", Toast.LENGTH_SHORT).show();
                        } else {
                            comentario.comment = cajaComentarios.getText().toString();
                            comentario.puntuacion = Integer.parseInt(cajaPuntuacion.getText().toString());

                            // Asignamos el id del usuario logueado al comentario
                            FirebaseUser currentUser = mauth.getCurrentUser();
                            if (currentUser != null) {
                                String id = currentUser.getUid();
                                comentario.idUser = id;

                                // Obtenemos el nombre del usuario actual y lo asignamos al comentario
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String nombreUsuario = dataSnapshot.child("nameUser").getValue(String.class);
                                            comentario.nameUsuario = nombreUsuario;

                                            comentario.idDestino = destinoId;
                                            comentario.nameDestino = nombreDestino;

                                            // Guardamos el comentario en la base de datos
                                            reference.child("Comentario").push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Comentario enviado", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            // Si no se encuentra el nombre de usuario, muestra un mensaje de error
                                            Toast.makeText(context, "Error: No se ha encontrado el nombre de usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Manejar error de consulta cancelada
                                        Toast.makeText(context, "Error: La consulta fue cancelada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Si no hay usuario autenticado, muestra un mensaje de error
                                Toast.makeText(context, "Error: No se ha encontrado un usuario autenticado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar error de consulta cancelada
                        Toast.makeText(context, "Error: La consulta fue cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }
}
