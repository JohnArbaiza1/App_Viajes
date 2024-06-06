package com.example.destinos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.destinos.Adapter.CommentsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallesComentariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesComentariosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //-----------------------------------------------
    //Atributos de la clase
    private ListView listView;
    private CommentsAdapter adapter;
    private ArrayList<Comentarios> listaComentarios;
    Button btnRegresa;
    Fragment explorar;
    //-----------------------------------------------

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetallesComentariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallesComentariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallesComentariosFragment newInstance(String param1, String param2) {
        DetallesComentariosFragment fragment = new DetallesComentariosFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detalles_comentarios, container, false);
        //-------------------------------------------------------
        //Capturamos los id
        listView = root.findViewById(R.id.listaComentarios);
        btnRegresa = root.findViewById(R.id.btnRegresarExplorar);
        //-------------------------------------------------------
        explorar = new principalFragment();
        //--------------------------------------------------------
        listaComentarios = new ArrayList<>();
        adapter  = new CommentsAdapter(getContext(),listaComentarios);
        listView.setAdapter(adapter);
        String idDestino = getArguments().getString("DESTINO_ID");
        System.out.println("datos"+ idDestino);
        //--------------------------------------------------------------------------------------------------
        //Hacemos referencia al nodo de Destinos
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Destinos");
        //Definimos una query para buscar aquellos comentarios que esten relacionados por el id de un
        //destino en especifico
        Query query = reference.child("Comentario").orderByChild("idDestino").equalTo(idDestino);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Limpiamos la lista
                listaComentarios.clear();
                //Iteramos a través de los hijos del snapshot buscando los comentarios relacionados con el destino específico.
                for(DataSnapshot comentarioSnap: snapshot.getChildren()){
                    String comentario = comentarioSnap.child("comment").getValue(String.class);
                    String puntos = comentarioSnap.child("puntuacion").getValue(String.class);
                    String nameUs = comentarioSnap.child("nameUsuario").getValue(String.class);
                    String nameDest = comentarioSnap.child("nameDestino").getValue(String.class);

                    //Verificamos que los valores extraídos no sean nulos
                    if(comentario != null && puntos != null && nameUs != null && nameDest != null){
                        Comentarios comentar = new Comentarios(comentario,puntos,nameUs,nameDest);
                        listaComentarios.add(comentar);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //--------------------------------------------------------------------------------------------------
        btnRegresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,explorar).commit();
            }
        });
        //--------------------------------------------------------------------------------------------------

        return root;
    }

    //Definimos  un metodo static para crear una nueva instancia DetallesComentariosFragment
    public static DetallesComentariosFragment newInstance(String destinoId) {
        DetallesComentariosFragment fragment = new DetallesComentariosFragment();
        //bjeto Bundle para almacenar argumentos
        Bundle args = new Bundle();
        //agregamos el valor utilizando una clave  con un valor asociado
        args.putString("DESTINO_ID", destinoId);
        //Asignamos el Bundle de argumentos a la instancia del fragmento utilizando el método setArguments().
        fragment.setArguments(args);
        return fragment;
    }
}