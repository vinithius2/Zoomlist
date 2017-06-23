package br.com.calculafeira.calculafeira.Model;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Produto {

    private Long idProduto;
    private String nomeProduto;
    private Long fkCategoria;

    public Produto() {
    }

    public Produto(Long idProduto, String nomeProduto, Long fkCategoria) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.fkCategoria = fkCategoria;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Long getFkCategoria() {
        return fkCategoria;
    }

    public void setFkCategoria(Long fkCategoria) {
        this.fkCategoria = fkCategoria;
    }

    @Override
    public String toString() {
        return nomeProduto;
    }
}
