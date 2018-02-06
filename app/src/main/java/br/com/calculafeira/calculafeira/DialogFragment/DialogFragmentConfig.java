package br.com.calculafeira.calculafeira.DialogFragment;

/**
 * Created by Vinithius on 24/01/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.calculafeira.calculafeira.R;

/**
 * Created by Administrador on 25/02/2015.
 */
public class DialogFragmentConfig extends DialogFragment {

    private TextView versionApp, about;
    private ImageButton personal_email, personal_linkedin, app_facebook, app_instagran, app_youtube;
    private PackageInfo packageInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        try {
            packageInfo = getActivity().getApplication().getPackageManager().getPackageInfo(
                    getActivity().getApplication().getPackageName(), 0
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null);
        setInitialization(view);
        personal_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenEmail();
            }
        });
        personal_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenLinkedin();
            }
        });
        app_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenFacebook(v);
            }
        });
        app_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenYoutube(v);
            }
        });

        //Ainda por criar contas...
        app_instagran.setVisibility(View.GONE);
        app_youtube.setVisibility(View.GONE);

        builder.setView(view);
        return builder.create();
    }

    private void setInitialization(View view){
        versionApp = view.findViewById(R.id.txt_version_app_input);
        about = view.findViewById(R.id.txt_about_input);
        personal_email = view.findViewById(R.id.btn_gmail_pessoal);
        personal_linkedin = view.findViewById(R.id.btn_linkedin_pessoal);
        app_facebook = view.findViewById(R.id.btn_facebook_app);
        app_instagran = view.findViewById(R.id.btn_instagran_app);
        app_youtube = view.findViewById(R.id.btn_youtube_app);
        versionApp.setText(packageInfo.versionName);
        about.setText(getResources().getText(R.string.about_text_app));
    }

    private void getOpenFacebook(View v){
        try {
            v.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    String.valueOf(getResources().getText(R.string.app_uri_facebook)))
            );
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            String uri = String.valueOf(getResources().getText(R.string.url_uri_facebook));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
        }
    }

    private void getOpenLinkedin(){
        String uri = String.valueOf(getResources().getText(R.string.personal_uri_linkedin));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    private void getOpenYoutube(View v){
        Snackbar.make(v, getResources().getString(R.string.em_breve), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void getOpenEmail(){
        String email = String.valueOf(getResources().getText(R.string.personal_email));
        String subject = String.valueOf(getResources().getText(R.string.subject_email));
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ email });
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(i, "Send email"));
    }
}
