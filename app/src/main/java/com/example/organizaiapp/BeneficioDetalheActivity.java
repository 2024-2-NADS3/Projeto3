package com.example.organizaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BeneficioDetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beneficio_detalhe);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        TextView btnVoltar = findViewById(R.id.btn_voltar_beneficios_detalhe);
        TextView txtNomeBeneficio = findViewById(R.id.txt_nome_beneficio_detalhe);
        txtNomeBeneficio.setText(extras.getString("nomeBeneficio"));
        TextView txtDescBeneficio = findViewById(R.id.txt_descricao_beneficio);
        txtDescBeneficio.setText(extras.getString("descBeneficio"));

        btnVoltar.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}