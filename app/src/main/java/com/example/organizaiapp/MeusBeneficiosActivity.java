package com.example.organizaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.domain.BeneficioCard;
import com.example.organizaiapp.domain.CategoriaEspecificaCard;

import java.util.ArrayList;
import java.util.List;

public class MeusBeneficiosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_beneficios);

        LinearLayout linearRegistros = findViewById(R.id.linear_registros_beneficios);
        List<BeneficioCard> registros = new ArrayList<BeneficioCard>();
        registros.add(new BeneficioCard(1L, "Bolsa Família",true, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. "));
        registros.add(new BeneficioCard(2L, "Pé de Meia",false, "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));

        for (BeneficioCard registro : registros){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.beneficio_card, linearRegistros, false);
            if (registro.isElegivel()){
                TextView txtIsElegivel = card.findViewById(R.id.txt_is_elegivel);
                txtIsElegivel.setVisibility(View.VISIBLE);
            }
            TextView nomeBeneficio = card.findViewById(R.id.txt_nome_beneficio);
            nomeBeneficio.setText(registro.getNome());
            // Adicione o card ao LinearLayout
            linearRegistros.addView(card);

            card.setOnClickListener(v -> {
                Intent i = new Intent(this, BeneficioDetalheActivity.class);
                i.putExtra("nomeBeneficio", registro.getNome());
                i.putExtra("descBeneficio", registro.getDescricao());
                startActivity(i);
            });
        }
        TextView btnVoltar = findViewById(R.id.btn_voltar_beneficios);
        btnVoltar.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}