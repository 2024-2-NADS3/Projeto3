package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.R;
import com.example.organizaiapp.domain.CategoriaEspecificaCard;
import com.example.organizaiapp.dto.TransacaoDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // Recebe a lista de transações específicas da categoria selecionada
        ArrayList<TransacaoDto> transacoesCategoria = intent.getParcelableArrayListExtra("especificos");

        LinearLayout linearRegistros = findViewById(R.id.linear_registros_especifica);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

        assert transacoesCategoria != null;
        for (TransacaoDto registro : transacoesCategoria){
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.categoria_especifica_card, linearRegistros, false);

            TextView dataRegistro = card.findViewById(R.id.data_registro);
            TextView descGasto = card.findViewById(R.id.descricao_gasto);
            TextView valorGasto = card.findViewById(R.id.valor_do_gasto);

            // Formatação da data
            String dataFormatada;
            try {
                Date date = inputFormat.parse(registro.getData());
                dataFormatada = outputFormat.format(date);
            } catch (ParseException e) {
                dataFormatada = registro.getData(); // Se der erro, usa a data original
                e.printStackTrace();
            }

            dataRegistro.setText(dataFormatada);
            descGasto.setText(registro.getDescricao());
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