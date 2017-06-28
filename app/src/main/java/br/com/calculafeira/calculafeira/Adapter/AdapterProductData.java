package br.com.calculafeira.calculafeira.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
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
    private DecimalFormat maskMoney;

    public AdapterProductData(Context context, int resource, ArrayList<ProductData> productDatas, TextView totalPrice, TextView totalQuantity) {
        super(context, resource, productDatas);
        this.resourceId = resource;
        Collections.reverse(productDatas);
        this.productDatas = productDatas;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.context = context;
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
        Button buttonMais = (Button) convertView.findViewById(R.id.button_mais);
        Button buttonMenos = (Button) convertView.findViewById(R.id.button_menos);
        textView_name_product.setText(productData.toString());
        setSave(productData, textView_total_price_product, textView_unit_price_product);
        buttonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productData.getQuantity();
                productData.setQuantity(++quantity);
                DataManager.getInstance().getProductDataDAO().save(productData);
                setSave(productData, textView_total_price_product, textView_unit_price_product);
            }
        });
        buttonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productData.getQuantity();
                productData.setQuantity(--quantity);
                DataManager.getInstance().getProductDataDAO().save(productData);
                setSave(productData, textView_total_price_product, textView_unit_price_product);
            }
        });
        return convertView;
    }

    private void setQuantityAndTotalMoney(TextView totalPrice, TextView totalQuantity){
        maskMoney = new DecimalFormat("R$ 0.00");
        totalPrice.setText(maskMoney.format(DataManager.getInstance().getProductDataDAO().getTotalPrice()));
        totalQuantity.setText(DataManager.getInstance().getProductDataDAO().getCount());
    }

    private void setSave(ProductData productData, TextView textView_total_price_product, TextView textView_unit_price_product){
        String total_price = String.valueOf(productData.getQuantity() * productData.getPrice());
        textView_total_price_product.setText(total_price);
        textView_unit_price_product.setText(productData.getPrice().toString());
        DataManager.getInstance().getProductDataDAO().save(productData);
//                setQuantityAndTotalMoney(totalPrice, totalQuantity);
    }
}
