package com.ihomeo.ihomeouploading.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ihomeo.ihomeouploading.ihomeoactivity.DoctorsName_List;
import com.ihomeo.ihomeouploading.ihomeoactivity.R;

/**
 * Created by Sai on 12/2/2016.
 */

public class Settings extends Activity {


    LinearLayout Feesettings1, User1, Scheduling1, Clinicinfo1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings1);
        android.app.ActionBar bar = getActionBar();
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'></font>"));


        //	      creating methods to write code
        getIds();
        Feesettings1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent iFeesetting = new Intent(Settings.this, Feesettings.class);
                startActivity(iFeesetting);

            }
        });

        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Visit Website For SetUp!", Toast.LENGTH_SHORT).show();
            }
        });

        Scheduling1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Visit Website For SetUp!", Toast.LENGTH_SHORT).show();
                // Intent scheduling = new Intent(Settings.this, Scheduling.class);
                // startActivity(scheduling);
            }
        });

        Clinicinfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Visit Website For SetUp!", Toast.LENGTH_SHORT).show();

            }
        });


        //	      creating methods to write code

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Settings.this, DoctorsName_List.class);
        startActivity(i);
        finish();
        return;
    }


    private void getIds() {

        Feesettings1 = (LinearLayout) findViewById(R.id.Feesettings);
        User1 = (LinearLayout) findViewById(R.id.user);
        Scheduling1 = (LinearLayout) findViewById(R.id.scheduling);
        Clinicinfo1 = (LinearLayout) findViewById(R.id.clinicinfo);
    }


}
