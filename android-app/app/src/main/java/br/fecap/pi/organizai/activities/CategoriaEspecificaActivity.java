package br.fecap.pi.organizai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.organizai.R;

import br.fecap.pi.organizai.dto.TransacaoDto;
import br.fecap.pi.organizai.service.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CategoriaEspecificaActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categoria_especifica);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        montaRegistrosEspecificos();

        TextView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void montaRegistrosEspecificos() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


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
            ImageView btnDelete = card.findViewById(R.id.btn_delete);

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
            valorGasto.setText("R$ " + String.format("%.2f", registro.getValor()));

            // Adicione o card ao LinearLayout
            linearRegistros.addView(card);

            btnDelete.setOnClickListener(v -> {
                excluirTransacaoEspecifica(registro.getTransacaoId(), transacoesCategoria);
            });
        }
    }

    private void excluirTransacaoEspecifica(Integer transacaoId, ArrayList<TransacaoDto> transacoesCategoria) {
        Call<ResponseBody> call = apiService.excluirTransacaoEspecifica(transacaoId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Encontrar o registro na lista e removê-lo
                    for (int i = 0; i < transacoesCategoria.size(); i++) {
                        if (transacoesCategoria.get(i).getTransacaoId().equals(transacaoId)) {
                            transacoesCategoria.remove(i);

                            // Remover a visualização do card
                            LinearLayout linearRegistros = findViewById(R.id.linear_registros_especifica);
                            linearRegistros.removeViewAt(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(CategoriaEspecificaActivity.this, "Erro ao excluir a transação!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CategoriaEspecificaActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}