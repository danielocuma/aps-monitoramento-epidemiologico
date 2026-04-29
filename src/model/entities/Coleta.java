package model.entities;

import java.time.LocalDate;

public class Coleta {

    private Integer id;
    private final LocalDate dataColeta;
    private int casos;
    private int obitos;
    private final Cidade cidade;

    // INSERT
    public Coleta(LocalDate dataColeta, int casos, int obitos, Cidade cidade) {
        this.dataColeta = dataColeta;
        this.casos = casos;
        this.obitos = obitos;
        this.cidade = cidade;
    }

    // SELECT / UPDATE
    public Coleta(Integer id, LocalDate dataColeta, int casos, int obitos, Cidade cidade) {
        this.id = id;
        this.dataColeta = dataColeta;
        this.casos = casos;
        this.obitos = obitos;
        this.cidade = cidade;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public int getCasos() {
        return casos;
    }

    public void setCasos(int casos) {
        this.casos = casos;
    }

    public int getObitos() {
        return obitos;
    }

    public void setObitos(int obitos) {
        this.obitos = obitos;
    }

    public Cidade getCidade() {
        return cidade;
    }
}