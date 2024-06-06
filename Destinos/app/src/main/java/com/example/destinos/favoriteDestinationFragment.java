package com.example.destinos;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.destinos.Adapter.favoritosAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favoriteDestinationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoriteDestinationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER_ID = "userId";

    private String mParam1;
    private String mParam2;
    private String idUser;

    private ListView ls;
    private List<String> dataDestinosfav;
    private DatabaseReference favRef;
    private favoritosAdapter adapter;

    public favoriteDestinationFragment() {
        // Required empty public constructor
    }

    public favoriteDestinationFragment(String idUser) {
        this.idUser = idUser;
    }

    public static favoriteDestinationFragment newInstance(String param1, String param2, String userId) {
        favoriteDestinationFragment fragment = new favoriteDestinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idUser = getArguments().getString(ARG_USER_ID);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_destination, container, false);
        TextView mensaje = root.findViewById(R.id.lblmensaje);
        ls = root.findViewById(R.id.listFav);
        dataDestinosfav = new ArrayList<>();

        //hacemos la referencia de favoritos
        favRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser).child("Favoritos");

        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataDestinosfav.clear(); //limpiamos la lista
                if (snapshot.exists()) {  //verificamos si existe

                    for (DataSnapshot fav : snapshot.getChildren()) { //iteramos en cada nodo
                        String idDestino = fav.child("idDestino").getValue(String.class); //obtenemos el id
                        if (idDestino != null) {
                            dataDestinosfav.add(idDestino); //lo mandamos a la lista
                        }
                    }
                }
                adapter = new favoritosAdapter(dataDestinosfav, getContext(), idUser, mensaje); // parametros del adapter
                ls.setAdapter(adapter);
                // Actualizar visibilidad del mensaje aquí también
                if (dataDestinosfav.isEmpty()) {
                    mensaje.setVisibility(View.VISIBLE);
                } else {
                    mensaje.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores aquí
            }
        });

        return root;
    }
}
