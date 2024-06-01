package com.example.destinos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    //---------------------------------------------------------
    //Atributos de la clase
    //---------------------------------------------------------
    public EditText user, email, pass,confirPass;
    public Button btnRegistro, btnCancelar;
    //Objetos a emplear
    FirebaseFirestore mifire;
    FirebaseAuth mauth;
    public DatabaseReference reference;
    Usuarios usuario = new Usuarios();
    //Variables a emplear
    public String confirmacion, iduser;

    @SuppressLint("MissingInflatedId")
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
        user = findViewById(R.id.txtUser);
        email = findViewById(R.id.txtCorreo);
        pass = findViewById(R.id.txtPass);
        confirPass = findViewById(R.id.txtConfirmar);
        btnRegistro = findViewById(R.id.btnresgister);
        btnCancelar = findViewById(R.id.btnCancela);
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
                reference = FirebaseDatabase.getInstance().getReference();
                usuario.nameUser = user.getText().toString();
                usuario.correo = email.getText().toString();
                usuario.password = pass.getText().toString();
                confirmacion = confirPass.getText().toString();
                //Validamos que no existan campos vacios
                if (usuario.nameUser.isEmpty() && usuario.correo.isEmpty() && usuario.password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //Verificamos que el password contenga 6 caracteres o mas
                    if (usuario.password.length() >= 6) {
                        //Llamamos a la funcion
                        resUser();

                    } else {
                        Toast.makeText(RegisterActivity.this, "El password debe contener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //metodo encargada del registro y authentication
    private void resUser(){
        mauth.createUserWithEmailAndPassword(usuario.correo,usuario.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Obtenemos el id del modelo de authentication
                    iduser = mauth.getCurrentUser().getUid();
                    reference.child("Usuarios").child(iduser).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(RegisterActivity.this,InicioActivity.class));
                                finish(); //Evita que el usuario vuelva a la pantlla de registro
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Error al guardar datos de usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}