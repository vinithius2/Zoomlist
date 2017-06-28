package br.com.calculafeira.calculafeira.Model;

import java.util.Date;

import br.com.calculafeira.calculafeira.Persistence.DataManager;

/**
 * Created by DPGE on 22/06/2017.
 */

public class ProductData {

    private Long idProductData;
    private Long fkProduct;
    private Long purchaseDate;
    private Integer quantity;
    private Double price;

    public ProductData() {
    }

    public ProductData(Long idProductData, Long fkProduct, Long purchaseDate, Integer quantity, Double price) {
        this.idProductData = idProductData;
        this.fkProduct = fkProduct;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getIdProductData() {
        return idProductData;
    }

    public void setIdProductData(Long idProductData) {
        this.idProductData = idProductData;
    }

    public Long getFkProduct() {
        return fkProduct;
    }

    public void setFkProduct(Long fkProduct) {
        this.fkProduct = fkProduct;
    }

    public Long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        Product product = DataManager.getInstance().getProductDAO().getIdProduct(getFkProduct());
        return String.valueOf(product.getNameProduct().toString());
    }
}
