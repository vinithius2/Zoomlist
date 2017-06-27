package br.com.calculafeira.calculafeira.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.R;

/**
 * Created by DPGE on 27/06/2017.
 */

public class AdapterProductData extends ArrayAdapter<ProductData> {

    private final int resourceId;
    private ArrayList<ProductData> productDatas;
    private Context context;

    public AdapterProductData(Context context, int resource, ArrayList<ProductData> productDatas) {
        super(context, resource, productDatas);
        this.resourceId = resource;
        Collections.reverse(productDatas);
        this.productDatas = productDatas;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }
        final ProductData productData = productDatas.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.textView_name_product_adapter);
        textView.setText(productData.toString());
        return convertView;
    }
}
