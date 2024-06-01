package com.example.destinos;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btntregistrar;
    public EditText user, email, pass;
    FirebaseFirestore mifire;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mifire= FirebaseFirestore.getInstance();
        mauth= FirebaseAuth.getInstance();

        user= findViewById(R.id.txtuser);
        email= findViewById(R.id.txtcorreoR);
        pass= findViewById(R.id.txtpassR);

        btntregistrar= findViewById(R.id.btnreg);

        btntregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userUser = user.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPass = pass.getText().toString().trim();


                if (userUser.isEmpty() && userEmail.isEmpty() && userPass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    reg(userUser, userEmail, userPass);

                }

            }

        });

    }

    private void reg(String userUser, String userEmail, String userPass) {
mauth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        String id= mauth.getCurrentUser().getUid();

        Map<String, Object> map=new HashMap<>();
        map.put("id", id);
        map.put("Correo", userEmail);
        map.put("Nombre", userUser);
        map.put("Password", userPass);


        mifire.collection("Usuarios").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class
                ));
                Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
    }
});

    }


}
