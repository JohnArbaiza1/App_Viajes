package com.example.destinos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    //---------------------------------------------------------
    //Atributos de la clase
    //---------------------------------------------------------
    public EditText user, email, pass,confirPass;
    public Button btnRegistro;
    //Objetos a emplear
    FirebaseFirestore mifire;
    FirebaseAuth mauth;
    public DatabaseReference reference;
    Usuarios usuario = new Usuarios();
    //Variables a emplear
    public String confirmacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //------------------------------------------
        mifire= FirebaseFirestore.getInstance();
        mauth= FirebaseAuth.getInstance();
        //--------------------------------------------
        //Capturando el id de los elementos
        user= findViewById(R.id.txtUser);
        email= findViewById(R.id.txtCorreo);
        pass= findViewById(R.id.txtPass);
        confirPass = findViewById(R.id.txtConfirmar);
        btnRegistro = findViewById(R.id.btnresgister);
        //---------------------------------------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //--------------------------------------------------
        //Eventos de la activity
        //--------------------------------------------------
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtenemos los datos ingresados
                usuario.nameUser = user.getText().toString();
                usuario.correo = email.getText().toString();
                usuario.password = pass.getText().toString();
                confirmacion = confirPass.getText().toString();
                reference.child("Usuarios").push().setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}