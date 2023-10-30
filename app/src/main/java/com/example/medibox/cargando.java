package com.example.medibox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class cargando extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(cargando.this, bienvenida.class));
                    finish();
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE);
                    if(sharedPreferences.getString("nombre", "").equals("")){
                        startActivity(new Intent(cargando.this, agregarPerfil.class));
                        finish();
                    }else{
                        String nombrePersona = sharedPreferences.getString("nombre", "");
                        startActivity(new Intent(cargando.this, bienvenida.class));
                        finish();
                    }

                }
            }
        }, 3000);
    }
}