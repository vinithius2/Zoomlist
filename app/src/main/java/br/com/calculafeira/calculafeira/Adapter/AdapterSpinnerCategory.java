package br.com.calculafeira.calculafeira.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.R;
import br.com.calculafeira.calculafeira.Util.ItemData;

/**
 * Created by Vinithius on 26/01/2018.
 */

public class AdapterSpinnerCategory extends BaseAdapter {

    private int resourceId;
    private Activity context;
    private ArrayList<ItemData> list;
    private LayoutInflater inflater;
    private int textViewId;

    public AdapterSpinnerCategory(Activity context, int resourceId, int textViewId, ArrayList<ItemData> list){
        this.list = list;
        this.resourceId = resourceId;
        this.textViewId = textViewId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View view = inflater.inflate(R.layout.adapter_spinner_category, null);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_spinner_category);
        TextView textView = (TextView)convertView.findViewById(R.id.textView_spinner_category);

        imageView.setImageResource(list.get(position).getImageId());
        textView.setText(list.get(position).getText());

        return view;
    }

}
