package br.gov.sp.cps.bancodados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dados.db";
    public static final String TABLE_NAME    = "pessoas";
    public static final String COL_ID        = "ID";
    public static final String COL_NOME      = "NOME";
    public static final String COL_IDADE     = "IDADE";
    public static final String COL_ALTURA    = "ALTURA";
    public static final String COL_PESO      = "PESO";
    public static final String COL_IMC       = "IMC";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NOME TEXT," +
                " IDADE INTEGER," +
                " ALTURA REAL," +
                " PESO REAL," +
                " IMC REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean inserirDados(String nome, int idade, double altura, double peso, double imc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOME, nome);
        cv.put(COL_IDADE, idade);
        cv.put(COL_ALTURA, altura);
        cv.put(COL_PESO, peso);
        cv.put(COL_IMC, imc);
        long res = db.insert(TABLE_NAME, null, cv);
        db.close();
        return res != -1;
    }

    public Cursor obterDadosPorNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COL_NOME + " = ?",
                new String[]{nome}, null, null, null);
    }

    public Cursor obterTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, COL_ID + " DESC");
    }

    public boolean atualizarDados(String nome, int novaIdade, double novaAltura, double novoPeso, double novoImc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_IDADE, novaIdade);
        valores.put(COL_ALTURA, novaAltura);
        valores.put(COL_PESO, novoPeso);
        valores.put(COL_IMC, novoImc);
        int rows = db.update(TABLE_NAME, valores, COL_NOME + " = ?", new String[]{nome});
        db.close();
        return rows > 0;
    }

    public boolean deletaDados(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, COL_NOME + " = ?", new String[]{nome});
        db.close();
        return rows > 0;
    }
}
