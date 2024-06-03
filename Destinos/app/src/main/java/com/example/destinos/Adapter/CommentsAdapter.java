package com.example.destinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.destinos.Comentarios;
import com.example.destinos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {

    //Atributos de la clase
    Context context;
    public ArrayList<Comentarios> dataComments;
    public String nombreEncontrado;
    public String idUser;

    //Constructor
    public CommentsAdapter(Context context, ArrayList<Comentarios> dataComments) {
        this.context = context;
        this.dataComments = dataComments;
    }

    @Override
    public int getCount() {
        return dataComments.size();
    }

    @Override
    public Object getItem(int position) {
        return dataComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_comentarios,null);
        Comentarios comen = dataComments.get(position);

        TextView txtDestino = convertView.findViewById(R.id.lblDestinos);
        TextView txtName = convertView.findViewById(R.id.lblNameUserComment);
        TextView txtComentario = convertView.findViewById(R.id.lblComentarioUser);
        TextView txtPuntuacion = convertView.findViewById(R.id.lblPuntuacion);

        //----------------------------------------------------------------------------
        //Hacemos referencia al node de destino
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Destinos");
        String destinoId = comen.idDestino;
        // Consulta que nos permitira encontrar el destino por su ID
        reference.child(destinoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Obtén el nombre del destino encontrado
                    nombreEncontrado = snapshot.child("Nombre").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //----------------------------------------------------------------------------
        //Buscamos el nombre del usuario
        idUser = comen.idUser;
        reference.child("Usuarios").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("nameUser").getValue().toString();
                    txtName.setText(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Mostramos los datos
        txtDestino.setText(nombreEncontrado);
        txtComentario.setText(comen.comment);
        txtPuntuacion.setText(comen.puntuacion);

        return convertView;
    }
}
