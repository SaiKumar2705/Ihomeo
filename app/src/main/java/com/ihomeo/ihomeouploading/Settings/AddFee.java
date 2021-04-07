package com.ihomeo.ihomeouploading.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ihome.ihomeo.adapters.Doc_ConsultationType_Settings;
import com.ihomeo.ihomeouploading.ihomeoactivity.R;
import com.ihomeo.ihomeouploading.model.Appointments_Model;
import com.ihomeo.ihomeouploading.model.Dashboard_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sai on 11/26/2016.
 */

public class AddFee extends Activity {

    private int mEMP_Id_Global;

    ListView mListview;

    Doc_ConsultationType_Settings mAdapter;

    private ProgressDialog pDialog_InsertConsultation, pDialog_GetDoc_Consult_AmtTypes;

    private static String url_insert;

    private int hospitalID_Constant;
    ArrayList<Appointments_Model> doc_ConsultationType_Amt_List = new ArrayList<Appointments_Model>();

    private List<String> doctorId_List = new ArrayList<String>();
    private List<String> hospitalId_List = new ArrayList<String>();
    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();
    private Button back, confirm;

    private EditText ConsultationType, ConsultationAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_fee);

        getIds();
        getData();
        //getDoctor_Consultaion_AmtTypes();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ct = ConsultationType.getText().toString();
                String cp = ConsultationAmount.getText().toString();

                InsertNewConsultation();
                //  mAdapter.clear();
                // mAdapter.notifyDataSetChanged();

                //getDoctor_Consultaion_AmtTypes();


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddFee.this, Feesettings.class);
                startActivity(i);
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddFee.this, Feesettings.class);
        startActivity(i);
        finish();
        return;
    }


    private void getData() {
        hospitalID_Constant = getIntent().getIntExtra("Hospital_ID", 0);
        SharedPreferences emp_Id_Pref_Addfee = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        mEMP_Id_Global = emp_Id_Pref_Addfee.getInt("EMP_Id_KEY_PREF", 0);

        SharedPreferences doctorId_pref_Addfee = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        String doctorIDs_CommaString = doctorId_pref_Addfee.getString("doctorId_KEY_PREF", "");

        SharedPreferences hospitalId_pref_Addfee = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        String hospitalIDs_CommaString = hospitalId_pref_Addfee.getString("hospitalId_KEY_PREF", "");


        doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));

        hospitalId_List = Arrays.asList(hospitalIDs_CommaString.split(","));

//				adding All to dropdown
        doctors_list.add(new Dashboard_Model(0, "All", hospitalID_Constant));


        int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
        int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
        Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mHospital_id);
        doctors_list.add(append_model);
    }

    private void InsertNewConsultation() {
        if (isInternetOn() == true) {
            int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
            String ct = ConsultationType.getText().toString();
            String ct1 = ct.replace(" ", "%20");
            String cp = ConsultationAmount.getText().toString();
            JSONArray jA1 = new JSONArray();
            jA1.put(mDoctor_id);
            jA1.put(mHospital_id);
            jA1.put(mEMP_Id_Global);
            jA1.put(ct1);
            jA1.put(cp);
            jA1.put("N");


            String sb_inserts = jA1.toString();
            StringBuffer sb_insert = new StringBuffer();
            sb_insert.append(Constants.URL + "WebInsertNewConsultation" + "?" + "Data=" + sb_inserts);
            url_insert = sb_insert.toString();
            System.out.println("getinsert_url---" + url_insert);


            new AddFee.Insert_Consultation().execute(url_insert);

        }
    }

    private class Insert_Consultation extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog_InsertConsultation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_InsertConsultation = new ProgressDialog(AddFee.this);
            pDialog_InsertConsultation.setMessage("Please Wait.");
            pDialog_InsertConsultation.setCancelable(false);
            pDialog_InsertConsultation.show();
        }


        @Override
        protected String doInBackground(String... strings) {


            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);
            // Return the data from specified url
            return stream;


        }

        protected void onPostExecute(String stream) {

            //..........Process JSON DATA................
            System.out.println("json string >>>>>>>>" + stream);


            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray11 = new JSONArray(stream);

                    System.out.println(jArray11.length());


                    if (jArray11.length() != 0) {

						/*for (int i = 0; i < jArray.length(); i++)*/
                        for (int i = 0; i < jArray11.length(); i++) {

                            String syn_status_reponse = jArray11.getString(0);

                            int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);

                            if (syn_status_reponse_result == 1) {

                                // display toast here


                                Intent b = new Intent(AddFee.this, Feesettings.class);
                                startActivity(b);
                                Toast.makeText(getApplicationContext(), "Insert Sucessfull", Toast.LENGTH_SHORT).show();

                                // Close the dialog
//                     			  dialog_BookAppointment.dismiss();

                            } else {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                            }


                          /*  String syn_status_reponse = jArray11.getString(0);

                            int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);


                            if (syn_status_reponse_result == 1) {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "Insert Sucessfull", Toast.LENGTH_SHORT).show();
                                // Close the dialog
//                     			  dialog_BookAppointment.dismiss();
                               *//* finish();
                                startActivity(getIntent());*//*
                            } else {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Insert Failed", Toast.LENGTH_SHORT).show();*/



                          /*  JSONObject jObject1 = jArray.getJSONObject(i);*/


                        }


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_InsertConsultation.isShowing()) ;
            pDialog_InsertConsultation.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


        } // onPostExecute() end
    } // ProcessJSON class end


    private boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }


    private void getIds() {
        back = (Button) findViewById(R.id.back);
        confirm = (Button) findViewById(R.id.confirm);
        ConsultationType = (EditText) findViewById(R.id.consultationType);
        ConsultationAmount = (EditText) findViewById(R.id.consultationAmount1);

    }
}

