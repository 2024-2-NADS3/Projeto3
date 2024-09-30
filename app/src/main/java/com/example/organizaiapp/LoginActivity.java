package com.example.organizaiapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText inputEmail = findViewById(R.id.textInputEmail);
        TextInputEditText inputSenha = findViewById(R.id.textInputPassword);

        Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(v -> {
            if(inputEmail.getText().toString().equals("admin") && inputSenha.getText().toString().equals("admin")){
                Intent i = new Intent(this, IntroActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Usuario nao autorizado", Toast.LENGTH_LONG).show();
            }
        });
    }
}