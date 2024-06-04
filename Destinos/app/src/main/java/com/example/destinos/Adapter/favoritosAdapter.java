package com.example.destinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.destinos.Destinos;
import com.example.destinos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class favoritosAdapter  extends BaseAdapter {

    public List<String>  listaFav;

    public Context context;

    public String idUser;

    public DatabaseReference destinosref, userref, userfavref;

    public favoritosAdapter(List<String> listaFav, Context context, String idUser) {
        this.listaFav = listaFav;
        this.context = context;
        this.idUser = idUser;
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
        return 0;
    }

    @Override
    public View getView(int position, View root, ViewGroup parent) {



        root= LayoutInflater.from(context).inflate(R.layout.item_fav, parent,false);

        TextView lblnombre= root.findViewById(R.id.lbldireF);
        TextView lbldire= root.findViewById(R.id.lblnameF);
        TextView lbldes= root.findViewById(R.id.lbldesDF);
        TextView lblautor= root.findViewById(R.id.lblautorF);
        ImageView img= root.findViewById(R.id.imageViewF);


        String idDestino=  listaFav.get(position);

        userfavref= FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        destinosref= FirebaseDatabase.getInstance().getReference("Destinos").child(idDestino);

        destinosref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){


                    userref= FirebaseDatabase.getInstance().getReference("Usuarios").child(snapshot.child("iduser").getValue(String.class));

                    userref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {


                            if(snapshotUser.exists()){

                                Destinos dt= new Destinos(snapshot.getKey(),
                                        snapshot.child("Descripcion").getValue(String.class),
                                        snapshot.child("Direccion").getValue(String.class),
                                        snapshot.child("Nombre").getValue(String.class),
                                        snapshot.child("URLImagen").getValue(String.class),
                                        snapshotUser.child("nameUser").getValue(String.class) );

                                lbldes.setText(dt.getDescripcion());
                                lbldire.setText(dt.getDireccion());
                                lblnombre.setText(dt.getNombre());
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
}
