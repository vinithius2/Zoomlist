package br.com.calculafeira.calculafeira.Model;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Product {

    public static final String ALIMENTO = "Alimento";
    public static final String BEBIDA = "Bebida";
    public static final String HIGIENE = "Higiene";
    public static final String LIMPEZA = "Limpeza";

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
