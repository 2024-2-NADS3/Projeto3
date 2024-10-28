package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.R;
import com.example.organizaiapp.domain.BeneficioCard;
import com.example.organizaiapp.service.ApiService;
import com.example.organizaiapp.manager.UserSessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeusBeneficiosActivity extends AppCompatActivity {

    private ApiService apiService;
    private List<BeneficioCard> registros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_beneficios);

        // Configuração do Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Inicializa a lista de benefícios
        inicializarListaBeneficios();

        // Obtém a elegibilidade do usuário e atualiza os cards
        UserSessionManager sessionManager = new UserSessionManager(this);
        verificarElegibilidade(sessionManager.getUserId());

        TextView btnVoltar = findViewById(R.id.btn_voltar_beneficios);
        btnVoltar.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void inicializarListaBeneficios() {
        registros.add(new BeneficioCard(2L, "Cadastro Único", false,
                "Descrição do Cadastro Único.", R.drawable.cadastro_unico));
        registros.add(new BeneficioCard(3L, "Pé de Meia", false,
                "Descrição do Pé de Meia.", R.drawable.pe_de_meia_logo));
        registros.add(new BeneficioCard(4L, "Bolsa Família", false,
                "Descrição do Bolsa Família.", R.drawable.bolsa_familia_logo));
        registros.add(new BeneficioCard(5L, "Benefício Idoso", false,
                "Descrição do Benefício Idoso.", R.drawable.bcp_logo));
        registros.add(new BeneficioCard(6L, "Fomento Rural", false,
                "Descrição do Fomento Rural.", R.drawable.fomento_rural));
    }

    private void verificarElegibilidade(int userId) {
        apiService.getElegibilidade(userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Atualiza a elegibilidade com base na resposta da API
                        registros.get(0).setElegivel(jsonResponse.getBoolean("cadastroUnico"));
                        registros.get(1).setElegivel(false); // Pé de Meia, por exemplo, não tem verificação
                        registros.get(2).setElegivel(jsonResponse.getBoolean("bolsaFamilia"));
                        registros.get(3).setElegivel(jsonResponse.getBoolean("beneficioIdoso"));
                        registros.get(4).setElegivel(jsonResponse.getBoolean("fomentoRural"));

                        // Exibe os cards de benefícios com a elegibilidade aplicada
                        exibirBeneficios();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MeusBeneficiosActivity.this, "Erro ao obter elegibilidade", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MeusBeneficiosActivity.this, "Erro na conexão com a API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exibirBeneficios() {
        LinearLayout linearRegistros = findViewById(R.id.linear_registros_beneficios);
        linearRegistros.removeAllViews(); // Limpa visualizações anteriores

        for (BeneficioCard registro : registros) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout card = (LinearLayout) inflater.inflate(R.layout.beneficio_card, linearRegistros, false);

            TextView nomeBeneficio = card.findViewById(R.id.txt_nome_beneficio);
            ImageView imgBeneficio = card.findViewById(R.id.registro_icon_beneficio);

            nomeBeneficio.setText(registro.getNome());
            imgBeneficio.setImageResource(registro.getIconBeneficio());

            // Aplica a transparência ao card se o benefício não for elegível
            if (!registro.isElegivel()) {
                card.setAlpha(0.5f);
            }

            // Configura o clique para abrir detalhes do benefício
            card.setOnClickListener(v -> {
                Intent intent = new Intent(MeusBeneficiosActivity.this, BeneficioDetalheActivity.class);
                intent.putExtra("nomeBeneficio", registro.getNome());
                intent.putExtra("descBeneficio", registro.getDescricao());
                intent.putExtra("iconBeneficio", registro.getIconBeneficio());
                intent.putExtra("isElegivel", registro.isElegivel());
                startActivity(intent);
            });

            // Adiciona o card ao layout
            linearRegistros.addView(card);
        }
    }
}
