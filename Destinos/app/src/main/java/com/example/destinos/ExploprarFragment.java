package com.example.destinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.destinos.Adapter.destinoAdapater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    private String idUser;
    public ExploprarFragment() {
        // Required empty public constructor
    }
    public ExploprarFragment(String iduser) {

        this.idUser= iduser;
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
        adapter = new destinoAdapater(getContext(), destinoList,idUser);
        listView.setAdapter(adapter);

//Crea una referencia a la ubicación específica en la base de datos de Firebase donde se encuentran almacenados los destinos.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Destinos");



        //DatabaseReference refejemplo = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser).child("Favoritos");

        //creamos el  evento para manejar las actualizaciones de la informacion
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                destinoList.clear();  //limpiamos para evitar duplicidad de informacion

                for (DataSnapshot postSnapshot : snapshot.getChildren()) { //iteramos en cada hijo de destinos es decir los que vamos agregando en el nodo

                    String descripcion = postSnapshot.child("Descripcion").getValue(String.class); //obtenemos los valores desde firebase
                    String nombre = postSnapshot.child("Nombre").getValue(String.class);
                    String direccion = postSnapshot.child("Direccion").getValue(String.class);
                    String urlImagen = postSnapshot.child("URLImagen").getValue(String.class);

                    //verificamos que ningun valor sea nulo
                    if (descripcion != null && nombre != null && direccion != null && urlImagen != null ) {

                        // creamos el objeto con todos los datos
                        Destinos destino = new Destinos(postSnapshot.getKey(),descripcion, direccion,nombre, urlImagen ,postSnapshot.child("iduser").getValue(String.class) );
                        destinoList.add(destino); //agregamos ese destino a la lista
                    }
                }
                adapter.notifyDataSetChanged(); //mandamos cambios al adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}