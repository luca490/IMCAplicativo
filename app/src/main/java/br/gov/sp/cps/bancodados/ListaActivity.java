package br.gov.sp.cps.bancodados;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista);

        ListView listView = findViewById(R.id.listView);
        TextView tvVazio  = findViewById(R.id.tvVazio);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.obterTodos();

        ArrayList<String> itens = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nome   = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOME));
                int    idade  = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IDADE));
                double altura = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ALTURA));
                double peso   = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PESO));
                double imc    = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMC));

                String item = String.format(
                        "Nome: %s\nIdade: %d anos  |  Altura: %.2f m  |  Peso: %.1f kg\nIMC: %.2f — %s",
                        nome, idade, altura, peso, imc, classificarIMC(imc));
                itens.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (itens.isEmpty()) {
            tvVazio.setText("Nenhum cadastro encontrado.");
        } else {
            tvVazio.setText("");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, itens);
        listView.setAdapter(adapter);

        // Clique em item abre detalhes
        listView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(this, itens.get(position), Toast.LENGTH_LONG).show());

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
    }

    private String classificarIMC(double imc) {
        if (imc < 16.0)       return "Magreza Grave";
        else if (imc < 17.0)  return "Magreza Moderada";
        else if (imc < 18.5)  return "Magreza Leve";
        else if (imc < 25.0)  return "Peso Normal";
        else if (imc < 30.0)  return "Sobrepeso";
        else if (imc < 35.0)  return "Obesidade Grau I";
        else if (imc < 40.0)  return "Obesidade Grau II";
        else                   return "Obesidade Grau III";
    }
}
