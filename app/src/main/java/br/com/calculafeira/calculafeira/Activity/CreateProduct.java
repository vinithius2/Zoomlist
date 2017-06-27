package br.com.calculafeira.calculafeira.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.calculafeira.calculafeira.Model.Category;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.Model.Product;
import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.R;

public class CreateProduct extends AppCompatActivity {

    EditText nameProduct, priceProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameProduct = (EditText)findViewById(R.id.edit_text_name_product);
        priceProduct = (EditText)findViewById(R.id.edit_text_price_product);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProductData productData = new ProductData();
                Product product = new Product();
                Category category = new Category();
                if (DataManager.getInstance().getCategoryDAO().getCount() == 0) {
                    category.setNameCategory("TESTE");
                    Long id = DataManager.getInstance().getCategoryDAO().save(category);
                }

                product.setFkCategory(Long.parseLong("1"));
                product.setNameProduct(nameProduct.getText().toString());
                long id_product = DataManager.getInstance().getProductDAO().save(product);
                productData.setFkProduct(id_product);
                productData.setPrice(Double.parseDouble(priceProduct.getText().toString()));
                productData.setQuantity(1);
                productData.setPurchaseDate(Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime())));
                long id_product_data = DataManager.getInstance().getProductDataDAO().save(productData);

                Snackbar.make(view, product.toString() + " " + productData.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
