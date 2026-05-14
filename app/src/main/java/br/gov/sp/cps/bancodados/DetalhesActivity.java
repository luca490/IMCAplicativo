package br.gov.sp.cps.bancodados;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);

        // Receber dados enviados pela MainActivity
        String nome   = getIntent().getStringExtra("nome");
        int    idade  = getIntent().getIntExtra("idade", 0);
        double altura = getIntent().getDoubleExtra("altura", 0);
        double peso   = getIntent().getDoubleExtra("peso", 0);
        double imc    = getIntent().getDoubleExtra("imc", 0);

        String classificacao = classificarIMC(imc);

        TextView tvNome          = findViewById(R.id.tvNome);
        TextView tvIdade         = findViewById(R.id.tvIdade);
        TextView tvAltura        = findViewById(R.id.tvAltura);
        TextView tvPeso          = findViewById(R.id.tvPeso);
        TextView tvImc           = findViewById(R.id.tvImc);
        TextView tvClassificacao = findViewById(R.id.tvClassificacao);

        tvNome.setText("Nome: " + nome);
        tvIdade.setText("Idade: " + idade + " anos");
        tvAltura.setText(String.format("Altura: %.2f m", altura));
        tvPeso.setText(String.format("Peso: %.1f kg", peso));
        tvImc.setText(String.format("IMC: %.2f", imc));
        tvClassificacao.setText("Classificação: " + classificacao);

        // Botão Voltar
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    private String classificarIMC(double imc) {
        if (imc < 16.0)       return "Magreza Grave";
        else if (imc < 17.0)  return "Magreza Moderada";
        else if (imc < 18.5)  return "Magreza Leve";
        else if (imc < 25.0)  return "Peso Normal ✓";
        else if (imc < 30.0)  return "Sobrepeso";
        else if (imc < 35.0)  return "Obesidade Grau I";
        else if (imc < 40.0)  return "Obesidade Grau II";
        else                   return "Obesidade Grau III";
    }
}
