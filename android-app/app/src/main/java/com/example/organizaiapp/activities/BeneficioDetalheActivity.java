package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.R;

public class BeneficioDetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beneficio_detalhe);

        // Recupera a Intent e o Bundle com os dados passados
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        // Recupera os elementos da interface
        TextView btnVoltar = findViewById(R.id.btn_voltar_beneficios_detalhe);
        TextView txtNomeBeneficio = findViewById(R.id.txt_nome_beneficio_detalhe);
        TextView txtDescBeneficio = findViewById(R.id.txt_descricao_beneficio);
        ImageView imgBeneficio = findViewById(R.id.img_beneficio); // Adicionando a ImageView para o ícone

        // Verifica se o Bundle não é nulo antes de acessar os dados
        if (extras != null) {
            // Atualiza o nome e a descrição do benefício
            txtNomeBeneficio.setText(extras.getString("nomeBeneficio"));
            txtDescBeneficio.setText(extras.getString("descBeneficio"));

            // Recupera o ícone do benefício e aplica à ImageView
            int iconBeneficio = extras.getInt("iconBeneficio", R.drawable.cadastro_unico); // Define um ícone padrão caso não seja encontrado
            imgBeneficio.setImageResource(iconBeneficio); // Define a imagem na ImageView
        }

        // Ação de clique para voltar à tela anterior
        btnVoltar.setOnClickListener(v -> finish());

        // Ajusta as margens de acordo com os insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}