package br.com.calculafeira.calculafeira.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.R;
import br.com.calculafeira.calculafeira.Util.ItemData;

/**
 * Created by Vinithius on 26/01/2018.
 */

public class AdapterSpinnerCategory extends ArrayAdapter<ItemData> {

    private int resourceId;
    private Activity context;
    private ArrayList<ItemData> list;
    private LayoutInflater inflater;
    private int textViewId;

    public AdapterSpinnerCategory(Activity context, int resourceId, int textViewId, ArrayList<ItemData> list){
        super(context,textViewId,list);
        this.list = list;
        this.resourceId = resourceId;
        this.textViewId = textViewId;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView_spinner_category);
        TextView textView = (TextView)convertView.findViewById(textViewId);

        imageView.setImageResource(list.get(position).getImageId());
        textView.setText(list.get(position).getText());

        return convertView;
    }

}
