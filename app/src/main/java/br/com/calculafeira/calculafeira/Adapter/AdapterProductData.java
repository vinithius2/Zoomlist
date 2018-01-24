package br.com.calculafeira.calculafeira.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import br.com.calculafeira.calculafeira.Activity.ProductCreate;
import br.com.calculafeira.calculafeira.Util.Helpers;

import at.markushi.ui.CircleButton;
import br.com.calculafeira.calculafeira.Model.Product;
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
    private TextView totalPrice, totalQuantity, porcentAlimento, porcentBebida, porcentHigiene, porcentLimpeza, estimate, quantity;
    private Toolbar mToolbar;
    private SharedPreferences mySharedPreferences;

    public AdapterProductData(Context context,
                              int resource,
                              ArrayList<ProductData> productDatas,
                              TextView totalPrice,
                              TextView quantity,
                              TextView porcentAlimento,
                              TextView porcentBebida,
                              TextView porcentHigiene,
                              TextView porcentLimpeza,
                              TextView totalQuantity,
                              TextView estimate,
                              Toolbar mToolbar) {

        super(context, resource, productDatas);
        this.resourceId = resource;
        Collections.reverse(productDatas);
        this.productDatas = productDatas;
        this.totalPrice = totalPrice;
        this.porcentAlimento = porcentAlimento;
        this.porcentBebida = porcentBebida;
        this.porcentHigiene = porcentHigiene;
        this.porcentLimpeza = porcentLimpeza;
        this.totalQuantity = totalQuantity;
        this.estimate = estimate;
        this.quantity = quantity;
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
        final ImageButton imageButtonEdit = (ImageButton) convertView.findViewById(R.id.btn_edit);
        final ImageButton imageButtonDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);
        final SwipeLayout swipeLayout =  (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
        final TextView textView_name_product = (TextView) convertView.findViewById(R.id.textView_name_product_adapter);
        final TextView textView_total_price_product = (TextView) convertView.findViewById(R.id.textView_total_price_product_adapter);
        final TextView textView_unit_price_product = (TextView) convertView.findViewById(R.id.textView_unit_price_product_adapter);
        final TextView textView_unit = (TextView) convertView.findViewById(R.id.textView_unit);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_icon_category);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, convertView.findViewById(R.id.bottom_wrapper));


        CircleButton buttonMais = (CircleButton) convertView.findViewById(R.id.button_mais);
        CircleButton buttonMenos = (CircleButton) convertView.findViewById(R.id.button_menos);

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

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.dialog_delete)
                        + " " + productData.getProduct().getNameProduct() + "?");
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DataManager.getInstance().getProductDataDAO().delete(productData);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductCreate.class);
                intent.putExtra("productData", productData);
                context.startActivity(intent);
            }
        });



        return convertView;
    }

    private void setQuantityAndTotalMoney(ArrayList<ProductData> productDatas){
        Double totalValue = 0d;
        int totalProduct = 0;
        float quantityAlimentos = 0f;
        float quantityBebidas = 0f;
        float quantityHigiene = 0f;
        float quantityLimpeza = 0f;
        for(ProductData p : productDatas){
            totalValue += p.getQuantity() * p.getPrice();
            totalProduct += p.getQuantity();
            switch (p.getProduct().getNameCategory()){
                case Product.ALIMENTO:
                    quantityAlimentos += p.getQuantity();
                    break;
                case Product.BEBIDA:
                    quantityBebidas += p.getQuantity();
                    break;
                case Product.HIGIENE:
                    quantityHigiene += p.getQuantity();
                    break;
                case Product.LIMPEZA:
                    quantityLimpeza += p.getQuantity();
                    break;
            }
        }
        mToolbar.setTitle(Helpers.getMonetary(String.valueOf(totalValue)));
        totalPrice.setText(Helpers.getMonetary(String.valueOf(totalValue)));
        DecimalFormat df = new DecimalFormat("0.0");
        if (totalProduct != 0){
            porcentAlimento.setText(df.format(quantityAlimentos*100/totalProduct) + "%");
            porcentBebida.setText(df.format(quantityBebidas*100/totalProduct) + "%");
            porcentHigiene.setText(df.format(quantityHigiene*100/totalProduct) + "%");
            porcentLimpeza.setText(df.format(quantityLimpeza*100/totalProduct) + "%");
        } else {
            porcentAlimento.setText("0.0%");
            porcentBebida.setText("0.0%");
            porcentHigiene.setText("0.0%");
            porcentLimpeza.setText("0.0%");
        }
        String result = context.getResources().getString(R.string.no_products);
        if (totalProduct == 1) {
            result = String.valueOf(totalProduct) + " " + context.getResources().getString(R.string.singular_products);
        } else if (totalProduct > 1){
            result = String.valueOf(totalProduct) + " " + context.getResources().getString(R.string.plural_product);
        }
        totalQuantity.setText(result);
        mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
        String value = mySharedPreferences.getString("estimate", "");
        if (!value.isEmpty() && !value.equals("000")){
            Double calc_estimate = Double.parseDouble(value) - totalValue;
            estimate.setText(Helpers.getMonetary(String.valueOf(calc_estimate)));
            estimate.setTextColor(context.getResources().getColor(R.color.colorWhite));
            estimate.setTypeface(Typeface.DEFAULT);
            estimate.setTextSize(10);
            if (calc_estimate < 0){
                estimate.setTextColor(context.getResources().getColor(R.color.colorRed));
                estimate.setTypeface(Typeface.DEFAULT_BOLD);
                estimate.setTextSize(15);
            }
        } else {
            estimate.setVisibility(View.GONE);
        }
    }

    private void setSave(ProductData productData, int quantity, TextView textView_total_price_product, TextView textView_unit_price_product, TextView textView_unit, ImageView imageView){
        if (quantity >= 0){
            productData.setQuantity(quantity);
            DataManager.getInstance().getProductDataDAO().save(productData);
            setQuantityAndTotalMoney(productDatas);
            String total_price = String.valueOf(productData.getQuantity() * productData.getPrice());
            textView_total_price_product.setText(Helpers.getMonetary(total_price));
            textView_unit_price_product.setText(Helpers.getMonetary(productData.getPrice().toString()));
            textView_unit.setText(String.valueOf(productData.getQuantity()));
            imageView.setImageDrawable(getImageCategory(productData));
        }
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
/*
    public void atualizaLista(List<Livro> lista){
        this.lista = lista;
        notifyDataSetChanged();
    }
*/
}
