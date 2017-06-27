package br.com.calculafeira.calculafeira.Model;

/**
 * Created by DPGE on 22/06/2017.
 */

public class Category {

    private Long idCategory;
    private String nameCategory;

    public Category() {
    }

    public Category(Long idCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    @Override
    public String toString() {
        return nameCategory;
    }
}
