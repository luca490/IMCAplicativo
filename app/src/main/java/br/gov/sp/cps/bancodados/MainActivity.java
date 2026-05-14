package br.gov.sp.cps.bancodados;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textNome, textIdade, textAltura, textPeso;
    Button btnGravar, btnConsultar, btnAtualizar, btnDeletar, btnVerCadastros;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        textNome    = findViewById(R.id.textNome);
        textIdade   = findViewById(R.id.textIdade);
        textAltura  = findViewById(R.id.textAltura);
        textPeso    = findViewById(R.id.textPeso);
        btnGravar       = findViewById(R.id.btnGravar);
        btnConsultar    = findViewById(R.id.btnConsultar);
        btnAtualizar    = findViewById(R.id.btnAtualizar);
        btnDeletar      = findViewById(R.id.btnDeletar);
        btnVerCadastros = findViewById(R.id.btnVerCadastros);

        // GRAVAR
        btnGravar.setOnClickListener(v -> {
            String nome    = textNome.getText().toString().trim();
            String sIdade  = textIdade.getText().toString().trim();
            String sAltura = textAltura.getText().toString().trim();
            String sPeso   = textPeso.getText().toString().trim();

            if (nome.isEmpty() || sIdade.isEmpty() || sAltura.isEmpty() || sPeso.isEmpty()) {
                Toast.makeText(this, "Preencha todos os dados", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                int    idade  = Integer.parseInt(sIdade);
                double altura = Double.parseDouble(sAltura);
                double peso   = Double.parseDouble(sPeso);

                if (altura > 3.0) {
                    Toast.makeText(this, "Altura deve ser em metros (ex: 1.75)", Toast.LENGTH_LONG).show();
                    return;
                }

                double imc = peso / (altura * altura);

                if (dbHelper.inserirDados(nome, idade, altura, peso, imc)) {
                    Toast.makeText(this, "Dados inseridos com sucesso!", Toast.LENGTH_LONG).show();
                    limparCampos();
                } else {
                    Toast.makeText(this, "Erro ao inserir dados.", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valores inválidos.", Toast.LENGTH_LONG).show();
            }
        });

        // CONSULTAR — abre tela de detalhes
        btnConsultar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome", Toast.LENGTH_LONG).show();
                return;
            }

            Cursor cursor = dbHelper.obterDadosPorNome(nome);
            if (cursor != null && cursor.moveToFirst()) {
                int    idade  = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IDADE));
                double altura = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALTURA));
                double peso   = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PESO));
                double imc    = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMC));
                cursor.close();

                Intent intent = new Intent(this, DetalhesActivity.class);
                intent.putExtra("nome", nome);
                intent.putExtra("idade", idade);
                intent.putExtra("altura", altura);
                intent.putExtra("peso", peso);
                intent.putExtra("imc", imc);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Nome não encontrado.", Toast.LENGTH_LONG).show();
            }
        });

        // ATUALIZAR
        btnAtualizar.setOnClickListener(v -> {
            String nome    = textNome.getText().toString().trim();
            String sIdade  = textIdade.getText().toString().trim();
            String sAltura = textAltura.getText().toString().trim();
            String sPeso   = textPeso.getText().toString().trim();

            if (nome.isEmpty() || sIdade.isEmpty() || sAltura.isEmpty() || sPeso.isEmpty()) {
                Toast.makeText(this, "Preencha todos os dados", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                int    idade  = Integer.parseInt(sIdade);
                double altura = Double.parseDouble(sAltura);
                double peso   = Double.parseDouble(sPeso);
                double imc    = peso / (altura * altura);

                if (dbHelper.atualizarDados(nome, idade, altura, peso, imc)) {
                    Toast.makeText(this, "Dados atualizados com sucesso.", Toast.LENGTH_LONG).show();
                    limparCampos();
                } else {
                    Toast.makeText(this, "Nome não encontrado.", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valores inválidos.", Toast.LENGTH_LONG).show();
            }
        });

        // DELETAR
        btnDeletar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome.", Toast.LENGTH_LONG).show();
                return;
            }
            if (dbHelper.deletaDados(nome)) {
                Toast.makeText(this, "Dados deletados com sucesso!", Toast.LENGTH_LONG).show();
                limparCampos();
            } else {
                Toast.makeText(this, "Nome não encontrado.", Toast.LENGTH_LONG).show();
            }
        });

        // VER TODOS OS CADASTROS
        btnVerCadastros.setOnClickListener(v ->
                startActivity(new Intent(this, ListaActivity.class)));
    }

    private void limparCampos() {
        textNome.setText("");
        textIdade.setText("");
        textAltura.setText("");
        textPeso.setText("");
    }
}
