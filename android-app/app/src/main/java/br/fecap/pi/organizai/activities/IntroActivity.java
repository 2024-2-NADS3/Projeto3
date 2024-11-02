package br.fecap.pi.organizai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.organizai.R;
import br.fecap.pi.organizai.dto.QuizRequest;
import br.fecap.pi.organizai.manager.UserSessionManager;
import br.fecap.pi.organizai.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntroActivity extends AppCompatActivity {

    private int clickCount = 0;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnPularPerguntas = findViewById(R.id.btn_pular_perguntas);

        btnPularPerguntas.setOnClickListener(v -> {
            criaQuizWithIsAnsweredFalse();
        });
    }

    public void handlerProximoIntro(View view) {
        //Atribuição de uma variavel global para obter a informação de quantas vezes o botao esta sendo clicado
        clickCount++;

        //busca pela textview que iremos alterar
        TextView viewById = findViewById(R.id.txt_pergunta);

        //condicional para lidar com cada um dos clicks
        switch (clickCount){
            case 1:
                viewById.setText("Queremos que você seja capaz de cuidar dos seus gastos de forma adequada, através de dicas e sugestões.");
            break;
            case 2:
                viewById.setText("Com apenas algumas perguntas, vamos elaborar sugestões personalizadas para você.");
            break;
            case 3:
                viewById.setText("Gostaria de montar seu perfil de usuário?");
                findViewById(R.id.btn_pular_perguntas).setVisibility(View.VISIBLE);
                Button btnProximo = (Button) view;
                btnProximo.setText("Sim");
                break;
            case 4:
                Intent i = new Intent(this, PerguntasActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }


    private void criaQuizWithIsAnsweredFalse() {
        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        int userId = sessionManager.getUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("PT-BR"));
        String formattedDate = sdf.format(new Date());
        QuizRequest quizRequest = new QuizRequest(userId,false, formattedDate);

        Call<ResponseBody> call = apiService.criaQuizWithIsAnsweredFalse(quizRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && (response.code() == 201 || response.code() == 202)){
                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(i);
                } else if (response.code() == 204) {
                    //Nesse caso já existe um quiz criado para o usuário, ele só nao foi respondido ainda
                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(IntroActivity.this, "Erro ao pular perguntas!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(IntroActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}