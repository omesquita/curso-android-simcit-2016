package br.com.simcit.filmes.model;

import java.io.Serializable;

/**
 * Classe filme, o implements Serializable é necessário sempre que for necessário passar
 * Objetos entre activity atraves de Intents
 * */

public class Filme implements Serializable{
    private Integer id;
    private String titulo;
    private Integer ano;
    private Integer categoria;
    private String classificacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public String toString() {
        return titulo + " - " + ano;
    }
}
