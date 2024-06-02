package com.example.destinos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.destinos.Adapter.destinoAdapater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploprarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploprarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ListView listView;
    private destinoAdapater adapter;
    private ArrayList<Destinos> destinoList;
    public ExploprarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploprarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploprarFragment newInstance(String param1, String param2) {
        ExploprarFragment fragment = new ExploprarFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exploprar, container, false);
        listView = view.findViewById(R.id.listView);
        destinoList = new ArrayList<>();
        adapter = new destinoAdapater(getContext(), destinoList);
        listView.setAdapter(adapter);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Destinos");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                destinoList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    String descripcion = postSnapshot.child("Descripcion").getValue(String.class);
                    String nombre = postSnapshot.child("Nombre").getValue(String.class);
                    String direccion = postSnapshot.child("Direccion").getValue(String.class);
                    String urlImagen = postSnapshot.child("URLImagen").getValue(String.class);


                    if (descripcion != null && nombre != null && direccion != null && urlImagen != null ) {

                        Destinos destino = new Destinos(descripcion, nombre, direccion, urlImagen);
                        destinoList.add(destino);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}