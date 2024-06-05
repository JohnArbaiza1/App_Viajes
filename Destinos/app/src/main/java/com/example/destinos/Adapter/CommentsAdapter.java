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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {

    //Atributos de la clase
    Context context;
    public ArrayList<Comentarios> dataComments;

    DatabaseReference reference,referenceComentario,referenceDestino;

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

        //------------------------------------------------------------------------
        TextView txtDestino = convertView.findViewById(R.id.lblDestinos);
        TextView txtName = convertView.findViewById(R.id.lblNameUserComment);
        TextView txtComentario = convertView.findViewById(R.id.lblComentarioUser);
        TextView txtPuntuacion = convertView.findViewById(R.id.lblPuntuacion);
        //----------------------------------------------------------------------------
        reference = FirebaseDatabase.getInstance().getReference().child("Comentario");

        //Consulta para encontrar los comentarios relacionados por el id al destino
        Query query = reference.orderByChild("idDestino").equalTo(comen.idDestino);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        String name = commentSnapshot.getKey();
                        String comentario = commentSnapshot.child("comment").getValue(String.class);
                        String puntos = commentSnapshot.child("puntuacion").getValue(String.class);
                        String nameUs = commentSnapshot.child("nameUsuario").getValue(String.class);
                        String nameDest = commentSnapshot.child("nameDestino").getValue(String.class);

//                        txtDestino.setText(nameDest);
//                        txtName.setText(nameUs);
//                        txtComentario.setText(comentario);

                    }

                }

                //Mostramos los datos
                txtDestino.setText(comen.getNameDestino());
                txtName.setText(comen.getNameUsuario());
                txtComentario.setText(comen.getComment());
                txtPuntuacion.setText(comen.getPuntuacion());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        return convertView;
    }
}
