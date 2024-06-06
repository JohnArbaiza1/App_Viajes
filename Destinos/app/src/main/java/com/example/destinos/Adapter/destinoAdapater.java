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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.destinos.Comentarios;
import com.example.destinos.Destinos;
import com.example.destinos.DetallesComentariosFragment;
import com.example.destinos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class destinoAdapater extends BaseAdapter {

    //-----------------------------------------------------------------------
    //Atributos de la clase
    //-----------------------------------------------------------------------
    private Context context;
    private ArrayList<Destinos> dataDestinos;
    private DatabaseReference reference, referenceUser,ref;
    private FirebaseAuth mauth;
    private Comentarios comentario;
    private String destinoId;

    private String nombreDestino;
    Fragment vistas;
    private String idUser;
    //-----------------------------------------------------------------------
    //Cosntructor
    //-----------------------------------------------------------------------
    public destinoAdapater(Context context, ArrayList<Destinos> dataDestinos, String iduser) {
        this.context = context;
        this.dataDestinos = dataDestinos;
        this.idUser= iduser;
        comentario = new Comentarios();
    }
    //-----------------------------------------------------------------------
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
        view = LayoutInflater.from(context).inflate(R.layout.item_list, null);

        Destinos ds = dataDestinos.get(position);
        //-----------------------------------------------------------
        TextView txtnombre = view.findViewById(R.id.lblnameF);
        TextView txtdes = view.findViewById(R.id.lbldesDF);
        TextView txtdire = view.findViewById(R.id.lbldireF);
        TextView lblautor= view.findViewById(R.id.lblautorP);
        ImageView img = view.findViewById(R.id.imageViewF);
        //-----------------------------------------------------------------
        //Parte donde otorgamos un tamaño definidos a las imagenes a cargar
        //-----------------------------------------------------------------
        int widthPixel = context.getResources().getDisplayMetrics().widthPixels;
        //Para definir el ancho deseado
        int imgAncho = widthPixel;
        //Definimos la altura segun la relacion del aspecto que queremos visualizar
        int HeightPixel= (int) (imgAncho * 0.68);

        //Establecemos las dimensiones
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = widthPixel;
        layoutParams.height = HeightPixel;
        img.setLayoutParams(layoutParams);
        //------------------------------------------------------------------------
        //Partde donde se trabaja para poder hacer zoom en las imagenes
        //------------------------------------------------------------------------

        vistas = new DetallesComentariosFragment();
        ref= FirebaseDatabase.getInstance().getReference("Usuarios").child(ds.getIdUser());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    txtnombre.setText(ds.getNombre());
                    txtdes.setText(ds.getDescripcion());
                    txtdire.setText(ds.getDireccion());
                    Picasso.get().load(ds.getURLImagen()).into(img);
                    lblautor.setText(snapshot.child("nameUser").getValue(String.class));

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //------------------------------------------------------------------------
        //Capturando los id
        //----------------------------------------------------
        ImageButton btnComentarios = view.findViewById(R.id.btnComentar);
        ImageButton btnEnviar = view.findViewById(R.id.btnEnviarcoment);
        ImageButton btnFavorites = view.findViewById(R.id.btnFavorite);
        ImageButton btnver = view.findViewById(R.id.idVerComentario);
        EditText cajaComentarios = view.findViewById(R.id.txtComentarios);
        EditText cajaPuntuacion = view.findViewById(R.id.txtPuntuacion);
        //----------------------------------------------------
        // Establecemos el botón de enviar comentario y la caja de comentarios de manera invisible
        btnEnviar.setVisibility(View.INVISIBLE);
        cajaComentarios.setVisibility(View.INVISIBLE);
        cajaPuntuacion.setVisibility(View.INVISIBLE);
        //---------------------------------------------------
        // Eventos
        //---------------------------------------------------
        btnComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnviar.setVisibility(View.VISIBLE);
                cajaComentarios.setVisibility(View.VISIBLE);
                cajaPuntuacion.setVisibility(View.VISIBLE);
                nombreDestino = ds.Nombre;
                // Toast.makeText(context, nombreDestino, Toast.LENGTH_SHORT).show();
            }
        });
        //Fin del primer evento
        //------------------------------------------------------------------------
        referenceUser = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        //------------------------------------------------------------------------
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDestino = ds.getIdDestino();
                DatabaseReference userFavoritesRef = referenceUser.child("Favoritos");
                userFavoritesRef.orderByChild("idDestino").equalTo(idDestino).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) { //Verifica si ya existe

                            Toast.makeText(context, "Este destino ya está en tus favoritos", Toast.LENGTH_SHORT).show();
                        } else {//si no, guarda el id del destino a agregar

                            Map<String, Object> Favoritos = new HashMap<>();
                            Favoritos.put("idDestino", idDestino);

                            userFavoritesRef.push().setValue(Favoritos).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Se agregó a favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Error al verificar los favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Fin del segundo evento
        //------------------------------------------------------------------------

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------------------------------------------------------------------------
                //Obtenemos una referencia del node de Destinos
                reference = FirebaseDatabase.getInstance().getReference().child("Destinos");
                // indicamos que queremos mantener sincronizados los datos en el nodo de “Destinos”
                reference.keepSynced(true);
                //obteniendo una instancia de la autenticación de Firebase.
                mauth = FirebaseAuth.getInstance();
                //----------------------------------------------------------------------------------------
                // Definimos una consulta para encontrar el nombre de un lugar
                // filtrando los resultados para que solo coincidan con el valor de nombreDestino
                Query query = reference.orderByChild("Nombre").equalTo(nombreDestino);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //---------------------------------------------------------------
                        //Iteramos a través de los hijos del snapshot
                        for (DataSnapshot destinoSnapshot : snapshot.getChildren()) {
                            //asignamos la clave del destino encontrado a la variable destinoId
                            destinoId = destinoSnapshot.getKey();
                        }
                        //---------------------------------------------------------------
                        // Validamos que no hayan campos vacíos
                        if (cajaComentarios.getText().toString().isEmpty() || cajaPuntuacion.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Aun no has comentado nada viajero", Toast.LENGTH_SHORT).show();
                        } else {
                            comentario.comment = cajaComentarios.getText().toString();
                            comentario.puntuacion = cajaPuntuacion.getText().toString();
                            System.out.println(destinoId);

                            // Asignamos el id del usuario logueado al comentario
                            FirebaseUser currentUser = mauth.getCurrentUser();
                            //verificamos si el objeto currentUser no es nulo
                            if (currentUser != null) {
                                //obteniendo (UID)
                                String id = currentUser.getUid(); //getUid() devuelve una cadena que representa el UID del usuario.
                                comentario.idUser = id;

                                // Obtenemos el nombre del usuario actual y lo asignamos al comentario
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String nombreUsuario = dataSnapshot.child("nameUser").getValue(String.class);
                                            comentario.nameUsuario = nombreUsuario;

                                            comentario.idDestino = destinoId;
                                            comentario.nameDestino = nombreDestino;

                                            // Guardamos el comentario en la base de datos
                                            reference.child("Comentario").push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Comentario enviado", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            // Si no se encuentra el nombre de usuario, muestra un mensaje de error
                                            Toast.makeText(context, "Error: No se ha encontrado el nombre de usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Manejar error de consulta cancelada
                                        Toast.makeText(context, "Error: La consulta fue cancelada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Si no hay usuario autenticado, muestra un mensaje de error
                                Toast.makeText(context, "Error: No se ha encontrado un usuario autenticado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //--------------------------------------------------------------------------------------------------------------

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar error de consulta cancelada
                        Toast.makeText(context, "Error: La consulta fue cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Fin del tercer evento
        //-------------------------------------------------------------------------------------------------------------

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Obtenemos la posicion y el id del destino donde se encuentra esta
                String ds = dataDestinos.get(position).getIdDestino();
                System.out.println("manda "+ ds);
                //definimos newIntance para mandar el id al fragment
                DetallesComentariosFragment detalle = DetallesComentariosFragment.newInstance(ds);
               // Realizamos la transaccion del fragmento
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, detalle)
                        .addToBackStack(null).commit();// agregamos una  pila de retroceso por si necesarioacaso
            }
        });
        //fin del cuarto evento
        //-------------------------------------------------------------------------------------------------------------

        return view;
    }

}
