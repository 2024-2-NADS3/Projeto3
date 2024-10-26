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
        registros.add(new BeneficioCard(2L, "Cadastro Único", true,
                "O que é?\n" +
                        "Um sistema de coleta de dados e informações que identifica e caracteriza as famílias brasileiras de baixa renda, permitindo que o governo conheça melhor a realidade socioeconômica dessas famílias.\n\n" +
                        "Qual o objetivo?\n" +
                        "Facilitar o acesso das famílias de baixa renda a programas sociais, garantindo que os benefícios sejam destinados a quem mais precisa.\n\n" +
                        "Regras:\n" +
                        " - Famílias com renda mensal de até meio salário mínimo por pessoa; ou\n" +
                        " - Famílias com renda total de até três salários mínimos.",
                R.drawable.cadastro_unico));
        registros.add(new BeneficioCard(3L, "Pé de Meia", false,
                "O que é?\n" +
                        "Um programa de incentivo à poupança, com foco em ajudar pessoas de baixa renda a formarem uma reserva financeira para emergências ou aposentadoria.\n\n" +
                        "Qual o objetivo?\n" +
                        "Promover a educação financeira e incentivar o hábito de poupar, proporcionando maior segurança financeira para os beneficiários a longo prazo.\n\n" +
                        "Regras:\n" +
                        " - Participação limitada a famílias inscritas no Cadastro Único;\n" +
                        " - O valor poupado é incentivado com um pequeno bônus mensal do governo.",
                R.drawable.pe_de_meia_logo));
        registros.add(new BeneficioCard(3L, "Bolsa Família",true, "O que é?\n" +"Um programa de transferência direta de renda, destinado às famílias em situação de pobreza e extrema pobreza em todo o país, de modo que consigam superar a situação de vulnerabilidade social.\n" +"Qual o objetivo?\n"+"\u200BPromover a cidadania com garantia de renda e apoiar, por meio dos benefícios ofertados, a articulação de políticas voltadas aos beneficiários, com vistas à superação das vulnerabilidades sociais das famílias.\n"+"Regras:\n"+"São elegíveis ao Programa Bolsa Família as famílias:\n" + " - inscritas no CadÚnico; e\n"+" - cuja renda familiar per capita mensal seja igual ou inferior a R$ 218,00 (duzentos e dezoito reais).", R.drawable.bolsa_familia_logo));
        registros.add(new BeneficioCard(4L, "Prestração Continuada(BPC)", false,
                "O que é?\n" +
                        "Um benefício assistencial, no valor de um salário mínimo mensal, garantido a idosos acima de 65 anos e pessoas com deficiência que comprovem não possuir meios de prover a própria manutenção nem tê-la provida por sua família.\n\n" +
                        "Qual o objetivo?\n" +
                        "Garantir uma renda mínima para pessoas idosas ou com deficiência que não conseguem se sustentar financeiramente.\n\n" +
                        "Regras:\n" +
                        " - Ser idoso acima de 65 anos ou pessoa com deficiência;\n" +
                        " - A renda familiar per capita deve ser inferior a 1/4 do salário mínimo.",
                R.drawable.bcp_logo));
        registros.add(new BeneficioCard(5L, "Fomento Rural", false,
                "O que é?\n" +
                        "Um programa que oferece apoio técnico e financeiro para famílias de agricultores familiares em situação de vulnerabilidade social e extrema pobreza.\n\n" +
                        "Qual o objetivo?\n" +
                        "Promover a inclusão produtiva e melhoria das condições de vida de famílias rurais, possibilitando a geração de renda e segurança alimentar.\n\n" +
                        "Regras:\n" +
                        " - Famílias inscritas no Cadastro Único;\n" +
                        " - Devem ser agricultores familiares em situação de extrema pobreza;\n" +
                        " - Recebem um apoio financeiro para investir em projetos de produção de alimentos.",
                R.drawable.fomento_rural));

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

