package com.example.organizaiapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.organizaiapp.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageView btnvoltar;
    private TextView TextViewResetarSenha;
    private TextView TextViewLogout;
    private TextView TextViewExcluirConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        btnvoltar = findViewById(R.id.imageBtnVoltar);
        TextViewLogout = findViewById(R.id.TextViewLogout);
        TextViewResetarSenha = findViewById(R.id.TextViewResetarSenha);


        btnvoltar.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        TextViewLogout.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        TextViewResetarSenha.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, RedefinirSenhaActivity.class);
            startActivity(intent);
        });





    }
}

