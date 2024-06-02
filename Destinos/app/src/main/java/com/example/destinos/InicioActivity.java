package com.example.destinos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.security.Principal;

public class InicioActivity extends AppCompatActivity {

    //-----------------------------------------------------------
    //Atributos de la clase
    //-----------------------------------------------------------
    public Fragment home,explorar,favorite;
    BottomNavigationView menu;
    Button btnExit;
    TextView userName;
    private FirebaseAuth mauth;
    //-----------------------------------------------------------


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        //---------------------------------------------------------------
        mauth = FirebaseAuth.getInstance();
        //---------------------------------------------------------------
        //Capturamos los id
        menu = findViewById(R.id.menuApp);
        btnExit = findViewById(R.id.btnSalir);
        userName = findViewById(R.id.lblName);
        //---------------------------------------------------------------
        home = new principalFragment();
        explorar = new ExploprarFragment();
        favorite = new favoriteDestinationFragment();
        //---------------------------------------------------------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //---------------------------------------------------------------
        //Evento
        //---------------------------------------------------------------
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,home).commit();
                    break;

                    case R.id.explo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,explorar).commit();
                    break;

                    case R.id.favorite:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,favorite).commit();
                    break;
                }
                return true;
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                startActivity(new Intent(InicioActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}