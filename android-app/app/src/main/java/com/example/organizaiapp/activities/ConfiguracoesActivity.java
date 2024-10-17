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

        btnvoltar.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

