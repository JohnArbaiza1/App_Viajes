package com.example.destinos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.destinos.Adapter.CommentsAdapter;

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
    private Button btnRegresa;
    private ArrayList<Comentarios> comentariosList;

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
        comentariosList = new ArrayList<>();
        adapter = new CommentsAdapter(getContext(),comentariosList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //---------------------------------------------------------
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comentario");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                comentariosList.clear();
//                for (DataSnapshot postSnapshot : snapshot.getChildren()){
//
//                    String comentario = postSnapshot.child("comment").getValue(String.class);
//                    int puntuacion = (int) postSnapshot.child("puntuacion").getValue(int.class);
//                    String idDestino = postSnapshot.child("idDestino").getValue(String.class);
//                    String idUs = postSnapshot.child("idUser").getValue(String.class);
//
//                    if (comentario != null && puntuacion != 0 && idDestino != null  && idUs != null) {
//
//                        Comentarios comentarios= new Comentarios( comentario,idDestino,idUs,puntuacion);
//                        comentariosList.add(comentarios);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return root;
    }
}