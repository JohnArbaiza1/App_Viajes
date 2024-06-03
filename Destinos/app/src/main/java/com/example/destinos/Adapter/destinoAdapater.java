package com.example.destinos.Adapter;

import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class destinoAdapater extends BaseAdapter {

    public Context context;

    public ArrayList<Destinos> dataDestinos;

    public DatabaseReference reference;
    private FirebaseAuth mauth;
    Comentarios comentario;
    public String destinoId;
    public String nombreDestino;

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

        view = LayoutInflater.from(context).inflate(R.layout.item_list,null);

        Destinos ds = dataDestinos.get(position);

        TextView txtnombre= view.findViewById(R.id.lblnameD);
        TextView txtdes= view.findViewById(R.id.lbldesD);
        TextView txtdire= view.findViewById(R.id.lbldireD);
        ImageView img= view.findViewById(R.id.imageView3);

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
        //establecemos el button de mandar comentario y la caja de comentarios de manera invisible
        btnEnviar.setVisibility(view.INVISIBLE);
        cajaComentarios.setVisibility(view.INVISIBLE);
        cajaPuntuacion.setVisibility(view.INVISIBLE);
        //---------------------------------------------------
        View finalView = view;
        //Eventos
        btnComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnviar.setVisibility(finalView.VISIBLE);
                cajaComentarios.setVisibility(finalView.VISIBLE);
                cajaPuntuacion.setVisibility(finalView.VISIBLE);
                nombreDestino = ds.Direccion;
                //Toast.makeText(context, nombreDestino, Toast.LENGTH_SHORT).show();
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Destinos");
                reference.keepSynced(true);
                mauth = FirebaseAuth.getInstance();

                //Defiimos una consulta para encontrar el nombre de un lugar
                Query query = reference.orderByChild("Nombre").equalTo(nombreDestino);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot destinoSnapshot : snapshot.getChildren()){
                            destinoId = destinoSnapshot.getKey();
                            //Toast.makeText(context, destinoId, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Validamos que no hayan campos vacios
                if(cajaComentarios.getText().toString().isEmpty() && cajaPuntuacion.getText().toString().isEmpty()){
                    Toast.makeText(context, "Aun no has comentado nada viajero", Toast.LENGTH_SHORT).show();
                }
                else{
                    comentario.comment = cajaComentarios.getText().toString();
                    comentario.puntuacion = Integer.parseInt(cajaPuntuacion.getText().toString());
                    //Asigamos el id del usuario loguado al comentario
                    String id = mauth.getCurrentUser().getUid();
                    comentario.idUser = id;
                    comentario.idDestino = destinoId;

                    reference.child("Comentario").push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Comentario enviado", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

        });

        return view;
    }
}
