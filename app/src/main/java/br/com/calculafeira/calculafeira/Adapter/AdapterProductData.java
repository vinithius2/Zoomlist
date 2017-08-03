package br.com.calculafeira.calculafeira.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.R;

/**
 * Created by DPGE on 27/06/2017.
 */

public class AdapterProductData extends ArrayAdapter<ProductData> {

    private final int resourceId;
    private ArrayList<ProductData> productDatas;
    private Context context;
    private TextView totalPrice, totalQuantity;
    private Toolbar mToolbar;

    public AdapterProductData(Context context, int resource, ArrayList<ProductData> productDatas, TextView totalPrice, TextView totalQuantity, Toolbar mToolbar) {
        super(context, resource, productDatas);
        this.resourceId = resource;
        Collections.reverse(productDatas);
        this.productDatas = productDatas;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.context = context;
        this.mToolbar = mToolbar;
        setQuantityAndTotalMoney(productDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }

        final ProductData productData = productDatas.get(position);

        final TextView textView_name_product = (TextView) convertView.findViewById(R.id.textView_name_product_adapter);
        final TextView textView_total_price_product = (TextView) convertView.findViewById(R.id.textView_total_price_product_adapter);
        final TextView textView_unit_price_product = (TextView) convertView.findViewById(R.id.textView_unit_price_product_adapter);
        final TextView textView_unit = (TextView) convertView.findViewById(R.id.textView_unit);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_icon_category);

        Button buttonMais = (Button) convertView.findViewById(R.id.button_mais);
        Button buttonMenos = (Button) convertView.findViewById(R.id.button_menos);

        textView_name_product.setText(productData.toString());

        setSave(productData, productData.getQuantity(), textView_total_price_product, textView_unit_price_product, textView_unit, imageView);

        buttonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productData.getQuantity();
                setSave(productData, ++quantity, textView_total_price_product, textView_unit_price_product, textView_unit, imageView);
            }
        });
        buttonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productData.getQuantity();
                setSave(productData, --quantity, textView_total_price_product, textView_unit_price_product, textView_unit, imageView);
            }
        });
        return convertView;
    }

    private void setQuantityAndTotalMoney(ArrayList<ProductData> productDatas){
        Double totalValue = 0.0;
        int totalProduct = 0;
        for(ProductData p : productDatas){
            totalValue += p.getQuantity() * p.getPrice();
            totalProduct += p.getQuantity();
        }
        mToolbar.setTitle(getMonetary(String.valueOf(totalValue)));
        totalPrice.setText(getMonetary(String.valueOf(totalValue)));
        String result = context.getResources().getString(R.string.no_products);
        if (totalProduct == 1) {
            result = String.valueOf(totalProduct) + " " + context.getResources().getString(R.string.singular_products);
        } else if (totalProduct > 1){
            result = String.valueOf(totalProduct) + " " + context.getResources().getString(R.string.plural_product);
        }
        totalQuantity.setText(result);
    }

    private void setSave(ProductData productData, int quantity, TextView textView_total_price_product, TextView textView_unit_price_product, TextView textView_unit, ImageView imageView){
        if (quantity >= 0){
            productData.setQuantity(quantity);
            DataManager.getInstance().getProductDataDAO().save(productData);
            setQuantityAndTotalMoney(productDatas);
            String total_price = String.valueOf(productData.getQuantity() * productData.getPrice());
            textView_total_price_product.setText(getMonetary(total_price));
            textView_unit_price_product.setText(getMonetary(productData.getPrice().toString()));
            textView_unit.setText(String.valueOf(productData.getQuantity()));
            imageView.setImageDrawable(getImageCategory(productData));
        }
    }

    private String getMonetary(String money){
        double parsed = Double.parseDouble(money);
        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
        return formatted;
    }

    private Drawable getImageCategory(ProductData productData){
        String name = productData.getProduct().getNameCategory();
        Drawable drawable = null;
        switch (name) {
            case "Alimento":
                drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ico_alimento, null);
                break;
            case "Bebida":
                drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ico_bebida, null);
                break;
            case "Limpeza":
                drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ico_limpeza, null);
                break;
            case "Higiene":
                drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ico_higiene, null);
                break;
        }
        return drawable;
    }
}
