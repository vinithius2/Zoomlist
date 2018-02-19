package br.com.calculafeira.calculafeira.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import br.com.calculafeira.calculafeira.Util.Helpers;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Objects;

import br.com.calculafeira.calculafeira.R;


public class ConfigActivity extends AppCompatActivity {

    private static final String CHECK_BOX_ESTIMATE = "checkBoxEstimate";
    private static final String CHECK_BOX_QUANTITY = "checkBoxQuantity";
    private static final String CHECK_BOX_PORCENT = "checkBoxPorcent";
    private CheckBox checkBoxEstimate, checkBoxQuantity, checkBoxPorcent;
    private SharedPreferences mySharedPreferences;
    private FloatingActionButton fab;
    private EditText editTextEstimate;
    private Context context = this;
    private String monetarySymbol;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setInitialization();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTextEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editTextEstimate.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[" + monetarySymbol + ",.]", "");
                    double parsed = Double.parseDouble(Helpers.getClearBlank(cleanString));
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
                    current = formatted;
                    editTextEstimate.setText(formatted);
                    editTextEstimate.setSelection(formatted.length());
                    editTextEstimate.addTextChangedListener(this);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextEstimate.getText().toString();
                if (!Objects.equals(value, "")){
                    mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();

                    String cleanString = Helpers.getClearBlank(
                            value.replaceAll("[" + monetarySymbol + ",.]", "")
                    );

                    editor.putString("estimate", cleanString);
                    editor.apply();

                    addValueCheckBox(CHECK_BOX_ESTIMATE, checkBoxEstimate);
                    addValueCheckBox(CHECK_BOX_QUANTITY, checkBoxQuantity);
                    addValueCheckBox(CHECK_BOX_PORCENT, checkBoxPorcent);

                    Intent intent = new Intent(ConfigActivity.this, MainList.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void addValueCheckBox(String nameCheckBox, View v){
        mySharedPreferences = context.getSharedPreferences(nameCheckBox, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorCheckBox = mySharedPreferences.edit();
        if (((CheckBox) v).isChecked()) {
            editorCheckBox.putBoolean(nameCheckBox, true);
            editorCheckBox.apply();
        } else {
            editorCheckBox.putBoolean(nameCheckBox, false);
            editorCheckBox.apply();
        }
    }

    private void setInitialization(){
        monetarySymbol = Helpers.getCurrencySymbol(
                Currency.getInstance(getResources().getConfiguration().locale).getCurrencyCode()
        );
        editTextEstimate = (EditText)findViewById(R.id.edit_text_estimate);
        editTextEstimate.setText(Helpers.getMonetary("000"));
        mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
        if (!mySharedPreferences.getString("estimate", "").isEmpty()){
            editTextEstimate.setText(Helpers.getMonetary(String.valueOf(
                    Double.parseDouble(mySharedPreferences.getString("estimate", "000"))
            )));
        }
        editTextEstimate.setSelection(editTextEstimate.getText().length());
        fab = (FloatingActionButton) findViewById(R.id.fab);
        checkBoxEstimate = (CheckBox)findViewById(R.id.checkBoxEstimate);
        checkBoxPorcent = (CheckBox)findViewById(R.id.checkBoxPorcent);
        checkBoxQuantity = (CheckBox)findViewById(R.id.checkBoxQuantity);
        atualizarCheckBoxs(checkBoxEstimate, checkBoxQuantity, checkBoxPorcent);
    }

    private void atualizarCheckBoxs(CheckBox checkBoxEstimate, CheckBox checkBoxQuantity, CheckBox checkBoxPorcent){
        mySharedPreferences = context.getSharedPreferences("checkBoxEstimate", Context.MODE_PRIVATE);
        Boolean value01 = mySharedPreferences.getBoolean("checkBoxEstimate", true);
        checkBoxEstimate.setChecked(value01);

        mySharedPreferences = context.getSharedPreferences("checkBoxQuantity", Context.MODE_PRIVATE);
        Boolean value02 = mySharedPreferences.getBoolean("checkBoxQuantity", true);
        checkBoxQuantity.setChecked(value02);

        mySharedPreferences = context.getSharedPreferences("checkBoxPorcent", Context.MODE_PRIVATE);
        Boolean value03 = mySharedPreferences.getBoolean("checkBoxPorcent", true);
        checkBoxPorcent.setChecked(value03);
    }

    public void onBackPressed() {
        Intent intent = new Intent(ConfigActivity.this, MainList.class);
        startActivity(intent);
    }
}
