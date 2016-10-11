package br.com.simcit.filmes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.simcit.filmes.model.Filme;

/**
 * Classe que será resposável por fazer a manipulação do banco de dados;
 * Por padrão utilizamos o banco de dados interno do android, o SQLite;
 * Para utizamos precisamos extender da classe SQLiteOpenHelper
 * Para saber mais sobre Salvar Dados usando SQLite: https://developer.android.com/training/basics/data-storage/databases.html
 */

public class FilmeDao extends SQLiteOpenHelper {

    private static final String DATABASE = "filmes.db";
    private static final int VERSAO = 1;
    private static final String TABELA = "filmes";


    public FilmeDao(Context context) {
        //Definimos o nome e versão do banco.
        super(context, DATABASE, null, VERSAO);
    }


    //Se o banco de dados não existir o metodo onCreate é chamado é a hora certa para criamos o banco de dados.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Pegamos o bancodeDados que foi criado, o parametro db e executamos nossa instrução sql.
        db.execSQL("CREATE TABLE " + TABELA + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "titulo TEXT, " +
                "genero INTEGER, " +
                "classificacao TEXT," +
                "ano INTEGER)");
    }

    //Metodo chamado semrpe que a versão do banco de dados for alterada.
    // Momento perfeito para fazermos alterações no banco de dados.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Método resposavel por inserir o filme no banco de dados
     */
    public void inserir(Filme filme) {
        //Recebemos o objeto filme por parâmetro e preenchemos o nosso ContentValues,
        //que conterá os dados que serão inseridos da tabela.
        //Obs.: A chave deve ser igual ao nome da coluna da tabela.
        ContentValues valores = new ContentValues();
        valores.put("titulo", filme.getTitulo());
        valores.put("genero", filme.getCategoria());
        valores.put("ano", filme.getAno());
        valores.put("classificacao", filme.getClassificacao());
        //Otemos a instância do banco com permissões de escrita e passamos através do metodo
        // insert(tabela que queremos inserir, coluna que será null, valores que serão inseridos)
        getWritableDatabase().insert(TABELA, null, valores);
    }

    /**
     * Método resposavel por alterar um filme no banco de dados
     */
    public void alterar(Filme filme) {
        //Recebemos o filme por parametro
        //Definimos qual é a condição where
        String whereClause = "id = ?";
        //Criamos um Vetor contendo os valores que serão passados por essa condição
        String whereArgs[] = {String.valueOf(filme.getId())};

        //Criamos e preenchemos os ContentValues
        ContentValues valores = new ContentValues();
        valores.put("titulo", filme.getTitulo());
        valores.put("genero", filme.getCategoria());
        valores.put("ano", filme.getAno());
        valores.put("classificacao", filme.getClassificacao());

        //Otemos a instância do banco com permissões de escrita e passamos através do metodo
        // update(tabela que queremos alterar os dados, os valores que serão alterados,
        // condição where, argumentos da condição where);
        getWritableDatabase().update(TABELA, valores, whereClause, whereArgs);
    }

    /**
     * Método resposavel por excluir um filme no banco de dados
     */
    public void excluir(Filme filme) {
        //Recebemos o filme por parametro
        //Definimos qual é a condição where
        String whereClause = "id = ?";
        //Criamos um vetor contendo os valores que serão passados por essa condição
        String whereArgs[] = {String.valueOf(filme.getId())};

        //Otemos a instância do banco com permissões de escrita e passamos através do metodo
        // delete(a tabela que queremos excluir os dados, condição where, argumentos da condição where);
        getWritableDatabase().delete(TABELA, whereClause, whereArgs);
    }

    /**
     * Método resposavel por obter todos os filmes do banco de dados e retornar através de um ArrayList
     */
    public ArrayList<Filme> getFilmes() {
        // Criamos um cursor que irá receber os dados retornados pelo banco.
        // obtemos a instância do banco com permissões de leitura e através do método query(
        // a tabela que queremos consultar, vetor com as colunas que queremos, argumentos de selecao,
        // vetor com os valores do argumento de selecao, groupBy, having, ordenado por)
        Cursor cursor = getReadableDatabase().query(TABELA, null, null, null, null, null, "titulo");

        //Criamos um arrayList que conterá os filmes rertornados pelo banco de dados
        ArrayList<Filme> filmes = new ArrayList<>();

        //Através de um laço, navegamos pelo cursor e a cada interação criamos um filme e
        // armazenamos ele no arrayList
        while (cursor.moveToNext()) {
            Filme filme = new Filme();
            filme.setId(cursor.getInt(cursor.getColumnIndex("id")));
            filme.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
            filme.setCategoria(cursor.getInt(cursor.getColumnIndex("genero")));
            filme.setClassificacao(cursor.getString(cursor.getColumnIndex("classificacao")));
            filme.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
            filmes.add(filme);
        }

        //Fechamos o cursor para liberamrmos a memória
        cursor.close();
        //Retornamos o arrayList com os filmes
        return filmes;
    }
}