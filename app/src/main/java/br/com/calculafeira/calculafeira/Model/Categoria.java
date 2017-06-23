package br.com.calculafeira.calculafeira.Model;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Categoria {

    private Long idCategoria;
    private String nomeCategoria;

    public Categoria() {
    }

    public Categoria(Long idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @Override
    public String toString() {
        return nomeCategoria;
    }
}
