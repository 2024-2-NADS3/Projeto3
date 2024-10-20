package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
        List<BeneficioCard> registros = new ArrayList<>();
        registros.add(new BeneficioCard(1L, "Cadastro Unico",true, "Descrição do Cadastro Unico.", R.drawable.cadastro_unico));
        registros.add(new BeneficioCard(2L, "Pé de Meia",false, "Descrição do Pé de Meia.", R.drawable.pe_de_meia_logo));
        registros.add(new BeneficioCard(3L, "Bolsa Família",true, "O que é?\n" +"Um programa de transferência direta de renda, destinado às famílias em situação de pobreza e extrema pobreza em todo o país, de modo que consigam superar a situação de vulnerabilidade social.\n" +"Qual o objetivo?\n"+"\u200BPromover a cidadania com garantia de renda e apoiar, por meio dos benefícios ofertados, a articulação de políticas voltadas aos beneficiários, com vistas à superação das vulnerabilidades sociais das famílias.\n"+"Regras:\n"+"São elegíveis ao Programa Bolsa Família as famílias:\n" + " - inscritas no CadÚnico; e\n"+" - cuja renda familiar per capita mensal seja igual ou inferior a R$ 218,00 (duzentos e dezoito reais).", R.drawable.bolsa_familia_logo));
        registros.add(new BeneficioCard(4L, "Prestração Continuada(BPC)",false, "Descrição do Benefico de Prestação Continuada.", R.drawable.bcp_logo));
        registros.add(new BeneficioCard(5L, "Fomento Rural",false, "Descrição do Benefico Fomento Rural.", R.drawable.fomento_rural));

        // Verifica se é elegível ao benefício ou não.
        for (BeneficioCard registro : registros) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.beneficio_card, linearRegistros, false);

            TextView nomeBeneficio = card.findViewById(R.id.txt_nome_beneficio);
            ImageView imgBeneficio = card.findViewById(R.id.registro_icon_beneficio); // Encontre a ImageView no card
            nomeBeneficio.setText(registro.getNome());

            // Define o ícone do benefício na ImageView
            imgBeneficio.setImageResource(registro.getIconBeneficio());

            if (registro.isElegivel()) {
                TextView txtIsElegivel = card.findViewById(R.id.txt_is_elegivel);
                txtIsElegivel.setVisibility(View.VISIBLE);
            } else {
                // Deixe o card visualmente desligado mas ainda clicável
                card.setAlpha(0.5f); // Tornar o card mais transparente para indicar que não é elegível
            }

            // Adicione o card ao LinearLayout
            linearRegistros.addView(card);

            card.setOnClickListener(v -> {
                Intent intent = new Intent(MeusBeneficiosActivity.this, BeneficioDetalheActivity.class);
                intent.putExtra("nomeBeneficio", registro.getNome());
                intent.putExtra("descBeneficio", registro.getDescricao());
                intent.putExtra("iconBeneficio", registro.getIconBeneficio());
                intent.putExtra("isElegivel", registro.isElegivel());
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

