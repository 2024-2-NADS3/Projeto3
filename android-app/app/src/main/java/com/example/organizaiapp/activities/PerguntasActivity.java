package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.organizaiapp.R;
import com.example.organizaiapp.dto.QuizRequest;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerguntasActivity extends AppCompatActivity {

    private int clickCount = 0;

    private ApiService apiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perguntas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_layout_perguntas), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    QuizRequest quizRequest = new QuizRequest();
    public void handleProximaPergunta(View view) {

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        int userId = sessionManager.getUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("PT-BR"));
        String formattedDate = sdf.format(new Date());

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

        quizRequest.setUserId(userId);
        quizRequest.setDataCriacao(formattedDate);

        //condicional para lidar com cada um dos clicks
        switch (clickCount) {
            case 1:
                viewById.setText("Qual a renda mensal da familia?");
                ediTextRendaMensal.setVisibility(View.VISIBLE);
                break;
            case 2:
                ediTextRendaMensal.setVisibility(View.INVISIBLE);
                quizRequest.setRendaMensal(BigDecimal.valueOf(ediTextRendaMensal.getRawValue()).divide(BigDecimal.valueOf(100), RoundingMode.CEILING));
                viewById.setText("Quantas pessoas moram com você?");
                numMoradores.setVisibility(View.VISIBLE);
                break;
            case 3:
                numMoradores.setVisibility(View.INVISIBLE);
                btnProxPer.setVisibility(View.INVISIBLE);
                quizRequest.setQtnPorFamilia(Integer.valueOf(numMoradores.getText().toString()));
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
                quizRequest.setAnswered(true);
                criaQuizCompleto(quizRequest);
        }
    }

    private void criaQuizCompleto(QuizRequest quizRequest) {
        Call<ResponseBody> call = apiService.criaQuizCompleto(quizRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PerguntasActivity.this, "Quiz atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PerguntasActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(PerguntasActivity.this, "Erro ao criar as perguntas completas!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PerguntasActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PerguntasActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void handlerSimCadUni(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_caduni).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_prox_pergunta).setVisibility(View.VISIBLE);
        quizRequest.setCadUni(true);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoCadUni(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_caduni).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_prox_pergunta).setVisibility(View.VISIBLE);
        quizRequest.setCadUni(false);
        handleProximaPergunta(new View(this));
    }

    public void handlerSimIdoso(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_idoso).setVisibility(View.INVISIBLE);
        quizRequest.setOlder(true);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoIdoso(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_idoso).setVisibility(View.INVISIBLE);
        quizRequest.setOlder(false);
        handleProximaPergunta(new View(this));
    }

    public void handlerSimRural(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_nao_rural).setVisibility(View.INVISIBLE);
        quizRequest.setRural(true);
        handleProximaPergunta(new View(this));
    }

    public void handlerNãoRural(View view) {
        view.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_sim_rural).setVisibility(View.INVISIBLE);
        quizRequest.setRural(false);
        handleProximaPergunta(new View(this));
    }


}