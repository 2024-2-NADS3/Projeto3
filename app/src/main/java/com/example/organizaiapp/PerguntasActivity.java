package com.example.organizaiapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.blackcat.currencyedittext.CurrencyEditText;

public class PerguntasActivity extends AppCompatActivity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perguntas);
        CurrencyEditText editTextSalario = findViewById(R.id.edtCurrency);
        BigDecimal value = BigDecimal.valueOf(editTextSalario.getRawValue()); // Para obter o valor numérico
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout_perguntas), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void handleProximaPergunta(View view) {

        //Atribuição de uma variavel global para obter a informação de quantas vezes o botao esta sendo clicado
        clickCount++;

        TextView viewById = findViewById(R.id.txt_pergunta);
        CurrencyEditText ediTextRendaMensal = findViewById(R.id.edtCurrency);
        EditText numMoradores = findViewById(R.id.edit_text_moradores);
        Button btnSimIdoso = findViewById(R.id.btn_sim_idoso);
        Button btnNaoIdoso = findViewById(R.id.btn_nao_idoso);
        Button btnProxPer = findViewById(R.id.btn_prox_pergunta);
        Button btnSimRural = findViewById(R.id.btn_sim_rural);
        Button btnNaoRural = findViewById(R.id.btn_nao_rural);

        //condicional para lidar com cada um dos clicks
        switch (clickCount) {
            case 1:
                viewById.setText("Qual a renda mensal da familia?");
                ediTextRendaMensal.setVisibility(View.VISIBLE);
                break;
            case 2:
                ediTextRendaMensal.setVisibility(View.INVISIBLE);
                viewById.setText("Quantas pessoas moram com você?");
                numMoradores.setVisibility(View.VISIBLE);
                break;
            case 3:
                numMoradores.setVisibility(View.INVISIBLE);
                btnProxPer.setVisibility(View.INVISIBLE);
                viewById.setText("Alguém do seu grupo familiar tem 65 anos ou mais?");
                btnSimIdoso.setVisibility(View.VISIBLE);
                btnNaoIdoso.setVisibility(View.VISIBLE);
                break;
            case 4:
                viewById.setText("Você mora em Zona Rural?");
                btnSimRural.setVisibility(View.VISIBLE);
                btnNaoRural.setVisibility(View.VISIBLE);
                break;
            case 5:
                btnSimRural.setVisibility(View.INVISIBLE);
                btnNaoRural.setVisibility(View.INVISIBLE);
                ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout_perguntas);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.setVerticalBias(R.id.txt_pergunta, 0.4f);
                constraintSet.applyTo(constraintLayout);
                viewById.setTextSize(23);
                viewById.setText("Obrigado por responder!\n Iremos traçar quais benefícios do Governo Federal você poderá se inscrever");
                btnProxPer.setVisibility(View.VISIBLE);
                btnProxPer.setText("Ir para tela principal");
                break;
            case 6:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    public void handlerSimCadUni(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_caduni).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_prox_pergunta).setVisibility(View.VISIBLE);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoCadUni(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_caduni).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_prox_pergunta).setVisibility(View.VISIBLE);
        handleProximaPergunta(new View(this));
    }

    public void handlerSimIdoso(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_idoso).setVisibility(View.INVISIBLE);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoIdoso(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_idoso).setVisibility(View.INVISIBLE);
        handleProximaPergunta(new View(this));
    }

    public void handlerSimRural(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_rural).setVisibility(View.INVISIBLE);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoRural(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_rural).setVisibility(View.INVISIBLE);
        handleProximaPergunta(new View(this));
    }


}