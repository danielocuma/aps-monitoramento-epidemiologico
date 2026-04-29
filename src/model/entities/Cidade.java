package model.entities;

public class Cidade {

    private Integer id;
    private final String nome;
    private final int populacao;

    public Cidade(Integer id, String nome, int populacao) {
        this.id = id;
        this.nome = nome;
        this.populacao = populacao;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPopulacao() {
        return populacao;
    }
}