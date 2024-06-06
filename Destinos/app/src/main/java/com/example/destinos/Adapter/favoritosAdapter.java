package com.example.destinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.destinos.Destinos;
import com.example.destinos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class favoritosAdapter extends BaseAdapter {

    private List<String> listaFav;
    private Context context;
    private String idUser;
    private TextView mensaje;
    private DatabaseReference destinosref, userref, userfavref;

    public favoritosAdapter(List<String> listaFav, Context context, String idUser, TextView mensaje) {
        this.listaFav = listaFav;
        this.context = context;
        this.idUser = idUser;
        this.mensaje = mensaje;
       actualizarmensaje();
    }

    @Override
    public int getCount() {
        return listaFav.size();
    }

    @Override
    public Object getItem(int position) {
        return listaFav.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View root, ViewGroup parent) {

        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false);
        }

        TextView lblnombre = root.findViewById(R.id.lbldireF);
        TextView lbldire = root.findViewById(R.id.lblnameF);
        TextView lbldes = root.findViewById(R.id.lbldesDF);
        TextView lblautor = root.findViewById(R.id.lblautorF);
        ImageView img = root.findViewById(R.id.imageViewF);
        ImageButton deletefav = root.findViewById(R.id.imageButton);

        String idDestino = listaFav.get(position); // obtenemos el id del destino almacenado en nuestra lista

        //referenciamos a nuestro subnodo por medio de su id
        userfavref = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser).child("Favoritos");


        deletefav.setOnClickListener(new View.OnClickListener() { //evento de eliminar
            @Override
            public void onClick(View v) {
                // Ordena y Busca el destino en la lista de favoritos
                userfavref.orderByChild("idDestino").equalTo(idDestino).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) { //si existe
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//itera sobre  los elementos
                                snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() { //lo eliminamos
                                    @Override
                                    public void onSuccess(Void unused) { //mandamos un mensaje que informe al usuario
                                        Toast.makeText(context, "Se eliminó de favoritos", Toast.LENGTH_SHORT).show();
                                        listaFav.remove(position); //eliminamos tambien de la lista
                                        notifyDataSetChanged(); //actualizamos
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(context, "El destino no se encuentra en favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Error al verificar los favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //referencia a destinos  para identificar un destino en especifico
        destinosref = FirebaseDatabase.getInstance().getReference("Destinos").child(idDestino);

        destinosref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { //si existe
                    //hacemos la referencia para obtener el id del usuario que se genera en destinos
                    userref = FirebaseDatabase.getInstance().getReference("Usuarios").child(snapshot.child("iduser").getValue(String.class));

                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            if (snapshotUser.exists()) {//si existe
                                Destinos dt = new Destinos(snapshot.getKey(),  //primero la key unica
                                        snapshot.child("Descripcion").getValue(String.class),
                                        snapshot.child("Direccion").getValue(String.class), // luego traemos la demas informacion
                                        snapshot.child("Nombre").getValue(String.class),
                                        snapshot.child("URLImagen").getValue(String.class),
                                        snapshotUser.child("nameUser").getValue(String.class)); //el id del usuario

                                lbldes.setText(dt.getDescripcion());
                                lbldire.setText(dt.getDireccion());
                                lblnombre.setText(dt.getNombre());  //mandamos la info a donde la queremos mostrar
                                Picasso.get().load(dt.getURLImagen()).into(img);
                                lblautor.setText(dt.getIdUser());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return root;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        actualizarmensaje();
    }

    private void  actualizarmensaje() {
        if (listaFav.isEmpty()) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.GONE);
        }
    }
}
