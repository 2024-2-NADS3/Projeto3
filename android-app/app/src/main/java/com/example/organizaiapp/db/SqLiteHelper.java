//package com.example.organizaiapp.db;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//
//public class SqLiteHelper extends SQLiteOpenHelper {
//
//    // Nome do banco de dados
//    private static final String DATABASE_NAME = "organizai.db";
//    // Versão do banco de dados
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String CREATE_TABLE_USER = "CREATE TABLE user (" +
//            "UserId INTEGER  PRIMARY KEY AUTOINCREMENT, " +
//            "nome TEXT NOT NULL, " +
//            "sobrenome TEXT NOT NULL, " +
//            "cpf TEXT, " +
//            "email TEXT NOT NULL, " +
//            "telefone TEXT NOT NULL, " +
//            "senha TEXT NOT NULL" +
//            ");";
//
//    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE quiz (" +
//            "UserId INTEGER NOT NULL, " +
//            "isAnswered INTEGER NOT NULL, " +
//            "isCadUni INTEGER NOT NULL, " +
//            "rendaMensal REAL NOT NULL, " +
//            "qtnPorFamilia INTEGER NOT NULL, " +
//            "isOlder INTEGER NOT NULL, " +
//            "isRural INTEGER NOT NULL, " +
//            "dataCriacao DATE NOT NULL, " +
//            "FOREIGN KEY (UserId) REFERENCES user(UserId)" +
//            ");";
//
//    private static final String CREATE_TABLE_CATEGORIA = "CREATE TABLE categoria (" +
//            "CategoriaId INTEGER NOT NULL, " +
//            "nomeCat TEXT NOT NULL, " +
//            "tipo INTEGER NOT NULL, " +
//            "PRIMARY KEY (CategoriaId)" +
//            ");";
//
//    private static final String CREATE_TABLE_USUARIOCATEGORIA = "CREATE TABLE UsuarioCategoria (" +
//            "UserId INTEGER NOT NULL, " +
//            "CategoriaId INTEGER NOT NULL, " +
//            "FOREIGN KEY (UserId) REFERENCES user(UserId), " +
//            "FOREIGN KEY (CategoriaId) REFERENCES categoria(CategoriaId)" +
//            ");";
//
//    private static final String CREATE_TABLE_TRANSACAO = "CREATE TABLE transacao (" +
//            "TransacaoId INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Autoincrement para o TransacaoId
//            "UsuarioId INTEGER NOT NULL, " +
//            "CategoriaId INTEGER NOT NULL, " +
//            "isReceita INTEGER NOT NULL, " +
//            "valor REAL NOT NULL, " +
//            "descricao TEXT NOT NULL, " +
//            "data DATE NOT NULL, " +
//            "FOREIGN KEY (UsuarioId) REFERENCES user(UserId), " +
//            "FOREIGN KEY (CategoriaId) REFERENCES categoria(CategoriaId)" +
//            ");";
//
//    private static final String CREATE_TABLE_BANCO = "CREATE TABLE banco (" +
//            "BancoId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            "nomeBanco TEXT NOT NULL, " +
//            "PRIMARY KEY (BancoId)" +
//            ");";
//
//    private static final String CREATE_TABLE_USUARIOBANCO = "CREATE TABLE UsuarioBanco (" +
//            "usuarioBancoID INTEGER NOT NULL, " +
//            "UserId INTEGER NOT NULL, " +
//            "BancoId INTEGER NOT NULL, " +
//            "valor REAL NOT NULL, " +
//            "PRIMARY KEY (usuarioBancoID), " +
//            "FOREIGN KEY (UserId) REFERENCES user(UserId), " +
//            "FOREIGN KEY (BancoId) REFERENCES banco(BancoId)" +
//            ");";
//
//    // Índice
//    private static final String CREATE_INDEX_USER_NOME = "CREATE INDEX idx_user_nome ON user(nome);";
//
//    public SqLiteHelper(Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Criação das tabelas
//        db.execSQL(CREATE_TABLE_USER);
//        db.execSQL(CREATE_TABLE_USUARIOCATEGORIA);
//        db.execSQL(CREATE_TABLE_QUIZ);
//        db.execSQL(CREATE_TABLE_CATEGORIA);
//        db.execSQL(CREATE_TABLE_TRANSACAO);
//        db.execSQL(CREATE_TABLE_BANCO);
//        db.execSQL(CREATE_TABLE_USUARIOBANCO);
//
//        // Criação do índice
//        db.execSQL(CREATE_INDEX_USER_NOME);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//    // Atualização do banco de dados (se necessário)
//        db.execSQL("DROP TABLE IF EXISTS UsuarioBanco");
//        db.execSQL("DROP TABLE IF EXISTS banco");
//        db.execSQL("DROP TABLE IF EXISTS transacao");
//        db.execSQL("DROP TABLE IF EXISTS quiz");
//        db.execSQL("DROP TABLE IF EXISTS user");
//        db.execSQL("DROP TABLE IF EXISTS categoria");
//        onCreate(db);
//    }
//}
