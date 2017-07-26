package br.com.calculafeira.calculafeira.Model;

import br.com.calculafeira.calculafeira.Persistence.DataManager;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Product {

    private Long idProduct;
    private String nameProduct;
    private String nameCategory;

    public Product() {
    }

    public Product(Long idProduct, String nameProduct, String nameCategory) {

        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.nameCategory = nameCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
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

    @Override
    public String toString() {
        return nameProduct;
    }
}
