package com.example.organizaiapp.activities;

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

import com.example.organizaiapp.R;
import com.example.organizaiapp.domain.BeneficioCard;

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
        registros.add(new BeneficioCard(3L, "Cadastro Unico",true, "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        registros.add(new BeneficioCard(4L, "Beneficio Idoso",false, "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        registros.add(new BeneficioCard(5L, "Cadastro Unico",false, "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));


        // Verifica se é elegivel ao beneficio ou não.
        for (BeneficioCard registro : registros) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.beneficio_card, linearRegistros, false);

            TextView nomeBeneficio = card.findViewById(R.id.txt_nome_beneficio);
            nomeBeneficio.setText(registro.getNome());

            if (registro.isElegivel()) {
                TextView txtIsElegivel = card.findViewById(R.id.txt_is_elegivel);
                txtIsElegivel.setVisibility(View.VISIBLE);
            } else {
                // Deixe o card visualmente "off" mas ainda clicável
                card.setAlpha(0.5f); // Tornar o card mais transparente para indicar que não é elegível
            }

            // Adicione o card ao LinearLayout
            linearRegistros.addView(card);

            // Definir ação de clique no card, mesmo para os que não são elegíveis
            card.setOnClickListener(v -> {
                Intent intent = new Intent(MeusBeneficiosActivity.this, BeneficioDetalheActivity.class);
                intent.putExtra("nomeBeneficio", registro.getNome());
                intent.putExtra("descBeneficio", registro.getDescricao());
                intent.putExtra("isElegivel", registro.isElegivel()); // Passar a elegibilidade para a próxima Activity
                startActivity(intent);
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