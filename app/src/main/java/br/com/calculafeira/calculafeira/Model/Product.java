package br.com.calculafeira.calculafeira.Model;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Product {

    private Long idProduct;
    private String nameProduct;
    private Long fkCategory;

    public Product() {
    }

    public Product(Long idProduct, String nameProduct, Long fkCategory) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.fkCategory = fkCategory;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Long getFkCategory() {
        return fkCategory;
    }

    public void setFkCategory(Long fkCategory) {
        this.fkCategory = fkCategory;
    }

    @Override
    public String toString() {
        return nameProduct;
    }
}
