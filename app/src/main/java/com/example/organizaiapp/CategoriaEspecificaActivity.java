package com.example.organizaiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.domain.CategoriaCard;
import com.example.organizaiapp.domain.CategoriaEspecificaCard;

import java.util.ArrayList;
import java.util.List;

public class CategoriaEspecificaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categoria_especifica);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        TextView btnVoltar = findViewById(R.id.btnVoltar);
        TextView txtNomeCategoria = findViewById(R.id.txt_nome_categoria);
        txtNomeCategoria.setText(extras.getString("nomeCategoria"));


        LinearLayout linearRegistros = findViewById(R.id.linear_registros_especifica);
        List<CategoriaEspecificaCard> registros = new ArrayList<CategoriaEspecificaCard>();
        registros.add(new CategoriaEspecificaCard("Dia 10", "Compras da semana", 200.00));
        registros.add(new CategoriaEspecificaCard("Dia 20", "Compras de fraudas",300.00));

        for (CategoriaEspecificaCard registro : registros){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.categoria_especifica_card, linearRegistros, false);

            TextView dataRegistro = card.findViewById(R.id.data_registro);
            TextView descGasto = card.findViewById(R.id.descricao_gasto);
            TextView valorGasto = card.findViewById(R.id.valor_do_gasto);

            dataRegistro.setText(registro.getDataDoGasto());
            descGasto.setText(registro.getDescricaoDoGasto());
            valorGasto.setText("R$ " + registro.getValor());

            // Adicione o card ao LinearLayout
            linearRegistros.addView(card);
        }

        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}