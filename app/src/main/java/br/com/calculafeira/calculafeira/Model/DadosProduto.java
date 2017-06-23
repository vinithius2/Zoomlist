package br.com.calculafeira.calculafeira.Model;

import java.util.Date;

/**
 * Created by DPGE on 22/06/2017.
 */

public class DadosProduto {

    private Long idDadosProduto;
    private Long fkProduto;
    private Long dataAquisicao;
    private Integer quantidade;
    private Double preco;

    public DadosProduto() {
    }

    public DadosProduto(Long idDadosProduto, Long fkProduto, Long dataAquisicao, Integer quantidade, Double preco) {
        this.idDadosProduto = idDadosProduto;
        this.fkProduto = fkProduto;
        this.dataAquisicao = dataAquisicao;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Long getIdDadosProduto() {
        return idDadosProduto;
    }

    public void setIdDadosProduto(Long idDadosProduto) {
        this.idDadosProduto = idDadosProduto;
    }

    public Long getFkProduto() {
        return fkProduto;
    }

    public void setFkProduto(Long fkProduto) {
        this.fkProduto = fkProduto;
    }

    public Long getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(Long dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return String.valueOf(quantidade * preco);
    }
}
