package com.example.destinos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------
    //Atributos de la clase
    //----------------------------------------------------------
    TextInputEditText txtcorreo, txtPassword;
    Button btnInicia, btnRegistrar;
    //Variables a emplear
    public String correoUser;
    public String passwordUser;
    private FirebaseAuth mauth;
    //-----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //---------------------------------------------------------
        //Instancias
         mauth = FirebaseAuth.getInstance();
        //----------------------------------------------------------
        //Capturamos los id
        txtcorreo = findViewById(R.id.correo);
        txtPassword = findViewById (R.id.pass);
        btnRegistrar = findViewById(R.id.btnRegistrarse);
        btnInicia = findViewById(R.id.btnIniciar);
        //----------------------------------------------------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //----------------------------------------------------------
        //Eventos
        //----------------------------------------------------------
        btnInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoUser = txtcorreo.getText().toString();
                passwordUser = txtPassword.getText().toString();

                //Validamos que los campos no esten vacios
                if(correoUser.isEmpty() && passwordUser.isEmpty()){
                    Toast.makeText(MainActivity.this, "Debe llenar los campos requeridos", Toast.LENGTH_SHORT).show();
                }
                else{
                    //llamamos a la funcion para poder ingresar a la app
                    loginApp();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //Metodo que se encarga del incio de session
    private void loginApp(){
        //Nos ayuda a poder ingresar con las credenciales pedidas al usuario
        mauth.signInWithEmailAndPassword(correoUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //validamos si la operacion fue exitosa  obtenemos al usuario autehnticado
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //verificamos que no sea nulo
                    if (user != null) {
                        //Enviamos al usuario al inicio de la app
                        Intent intent= new Intent(MainActivity.this,InicioActivity.class);
                        intent.putExtra("idUser",user.getUid());
                        startActivity(intent);
                        finish(); // para que no puede regresar a esta tarea
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Error al iniciar session:Copruebe sus datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Este metodo nos permite verificar si el usuario sigue logueado
    @Override
    protected void onStart() {
        super.onStart();
        //Verificamos si el usuario ya ha iniciado session
        if(mauth.getCurrentUser() != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent= new Intent(MainActivity.this,InicioActivity.class);
                intent.putExtra("idUser",user.getUid());
                startActivity(intent);
                //Para que no regrese a esta activity
                finish();
            }
        }
    }
}