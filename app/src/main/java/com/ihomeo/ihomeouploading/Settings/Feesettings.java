
package com.ihomeo.ihomeouploading.Settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * Created by Sai on 11/28/2016.
 */

public class Feesettings extends Activity {


    private ProgressDialog pDialog_UpdateConsultation, pDialog_DeleteConsultation;
    private static String url_consultation;
    private List<String> arrayitems;

    ArrayList<String> arrayList;

    ArrayAdapter<String> arrayAdapter;


    public String mConsultation_Type, mConsultation_Amt;

    Doc_ConsultationType_Settings mAdapter;


    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();

    private List<String> doctorId_List = new ArrayList<String>();

    private List<String> hospitalId_List = new ArrayList<String>();

    private int hospitalID_Constant;

    ListView mListview;

    public int selected_ConsultationType_ID;

    public String selected_ConsultationType, selected_Consultation_TypeAmt;


    private int doctor_ID_Global, hospital_ID_Global, mEMP_Id_Global;

    private String mMRNumber_Pay, mUserType, mConsultation_DefinedFee;

    ArrayList<Appointments_Model> doc_ConsultationType_Amt_List = new ArrayList<Appointments_Model>();

    private FloatingActionButton fab;

    private Button cancel;

    //Appointments_Adapter mAdapter;

    private ProgressDialog pDialog_GetDoc_Consult_AmtTypes;

    private int mDoctor_id, mHospital_id;

    public int mEMP_Id;
    public String ConsultationName, ConsultationName1, ConsultationFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fee_settings);
        // Sai=(TextView) findViewById(R.id.sai);
        cancel = (Button) findViewById(R.id.back_btn_appointments_dialog);
        mListview = (ListView) findViewById(R.id.consultation_amt_typelistview1);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);

        getData();
        getDoctor_Consultaion_AmtTypes();


        // Creating methods to write Code


        // android.app.ActionBar bar = getActionBar();
        //  getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'></font>"));


        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feesettings.this, AddFee.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Feesettings.this, Settings.class);
        startActivity(i);
        finish();
        return;
    }


    private void getData() {


        //get empid

        SharedPreferences emp_Id_pref1 = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        mEMP_Id_Global = emp_Id_pref1.getInt("EMP_Id_KEY_PREF", 0);

        // TODO Auto-generated method stub
//			  	get hospital_id from DoctorsName_List class(hospital_id and emp_id is constant in whole application)
        hospitalID_Constant = getIntent().getIntExtra("Hospital_ID", 0);

//				get data for doctorId from MainActivity class
        SharedPreferences doctorId_pref = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        String doctorIDs_CommaString = doctorId_pref.getString("doctorId_KEY_PREF", "");

//				get data for doctorName from MainActivity class
        SharedPreferences doctorName_pref = getSharedPreferences("doctorName_MyPrefs", Context.MODE_PRIVATE);
        String doctorNames_CommaString = doctorName_pref.getString("doctorName_KEY_PREF", "");

//				get data for hospitalId from MainActivity class
        SharedPreferences hospitalId_pref = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        String hospitalIDs_CommaString = hospitalId_pref.getString("hospitalId_KEY_PREF", "");

//				split the string with reference comma(,) and keep in list
        doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));

        hospitalId_List = Arrays.asList(hospitalIDs_CommaString.split(","));

//				adding All to dropdown
        doctors_list.add(new Dashboard_Model(0, "All", hospitalID_Constant));


        int mDoctor_id = Integer.parseInt(doctorId_List.get(0));

        int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
        Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mHospital_id);
        doctors_list.add(append_model);

    }

    private void getDoctor_Consultaion_AmtTypes() {

        if (isInternetOn() == true) {


            int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
            StringBuffer sb_Consult_Amts = new StringBuffer();
            sb_Consult_Amts.append(Constants.URL + "GetDoctorConsultationTypes?DoctorId=" + mDoctor_id + "&HospId=" + mHospital_id);
            String url_Consultation_Amt_types = sb_Consult_Amts.toString();
            System.out.println("Get_Consult_Amt_Types---" + url_Consultation_Amt_types);
            new GetDoctorConsultationTypes().execute(url_Consultation_Amt_types);

        }
    }

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

    private class GetDoctorConsultationTypes extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDoc_Consult_AmtTypes = new ProgressDialog(Feesettings.this);
            pDialog_GetDoc_Consult_AmtTypes.setMessage("Please Wait.");
            pDialog_GetDoc_Consult_AmtTypes.setCancelable(false);
            pDialog_GetDoc_Consult_AmtTypes.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream) {
            //..........Process JSON DATA................
            System.out.println("json string>>>>>>>>" + stream);
            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);
                            if (jArrray1.getString(1).equals("OTHERS")) {
                            } else {
                                String mConsultation_ID_response = jArrray1.getString(0);
                                String mConsultation_Type = jArrray1.getString(1);
                                String mConsultation_Amt = jArrray1.getString(2);
                                String mConsultation_Amt1 = mConsultation_Amt.replace(".0", ".00");
                                int mConsultation_ID = Integer.parseInt(mConsultation_ID_response);


                                Appointments_Model appointments_model = new Appointments_Model(mConsultation_ID, mConsultation_Type, mConsultation_Amt1);
                                doc_ConsultationType_Amt_List.add(appointments_model);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here
//                   	        	  Toast.makeText(getApplicationContext(), "Existing patients collected", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
//            	        	  Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                                doc_ConsultationType_Amt_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetDoc_Consult_AmtTypes.isShowing()) ;
            pDialog_GetDoc_Consult_AmtTypes.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            mAdapter = new Doc_ConsultationType_Settings(Feesettings.this, R.layout.doc_consultation_amt_type_dialog_settings, doc_ConsultationType_Amt_List);
            mListview.setAdapter(mAdapter);

            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);


                    selected_ConsultationType_ID = _model.getConsultationType_ID();
                    selected_ConsultationType = _model.getConsultation_Type();
                    selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();

                    String[] items = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};


                    arrayitems = java.util.Arrays.<String>asList(items);


                    arrayList = new ArrayList<>(Arrays.asList(items));

                    showInputBox(arrayList.get(position), position);


                }
            });

        }


        public void showInputBox(String oldItem, final int index) {


            final Dialog dialog = new Dialog(Feesettings.this);

            dialog.setContentView(R.layout.input_box);
            // TextView txtMessage = (TextView) dialog.findViewById(R.id.txtmessage);
            // txtMessage.setText("Update");
            SpannableString content = new SpannableString("Update");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            // txtMessage.setText(content);
            //txtMessage.setTextColor(Color.parseColor("#59cdbe"));
            final EditText editText = (EditText) dialog.findViewById(R.id.txtinput);
            final EditText editText1 = (EditText) dialog.findViewById(R.id.txtinput1);
            editText.setText(selected_ConsultationType);
            editText1.setText(selected_Consultation_TypeAmt);
            Button bt = (Button) dialog.findViewById(R.id.btdone);
            Button delete = (Button) dialog.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConsultationName = editText.getText().toString();
                    ConsultationName1 = ConsultationName.replace(" ", "%20");

                    ConsultationFee = editText1.getText().toString();
                    delete_Consultation();
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    getDoctor_Consultaion_AmtTypes();
                    dialog.dismiss();
                }
            });

            Button cancel = (Button) dialog.findViewById(R.id.back_btn_appointments_dialog);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ConsultationName = editText.getText().toString();

                    ConsultationName1 = ConsultationName.replace(" ", "%20");

                    ConsultationFee = editText1.getText().toString();

                    arrayList.set(index, editText.getText().toString());
                    arrayList.set(index, editText1.getText().toString());
                    update_consultation();
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                    getDoctor_Consultaion_AmtTypes();
                    // mAdapter.clear();

                    //getDoctor_Consultaion_AmtTypes();


                    //arrayList.add(editText.getText().toString());

                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        private void delete_Consultation() {
            if (isInternetOn() == true) {
                int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
                int mHospital_id = Integer.parseInt(hospitalId_List.get(0));

                JSONArray jA1 = new JSONArray();
                jA1.put(mDoctor_id);
                jA1.put(mHospital_id);
                jA1.put(mEMP_Id_Global);
                jA1.put(ConsultationName1);
                jA1.put(ConsultationFee);
                jA1.put("N");
                jA1.put(selected_ConsultationType_ID);
                jA1.put("N");
                String sb_Updates = jA1.toString();
                StringBuffer sb_Update = new StringBuffer();
                sb_Update.append(Constants.URL + "WebEditConsultation" + "?" + "Data=" + sb_Updates);
                url_consultation = sb_Update.toString();
                System.out.println("getconsultation_url---" + url_consultation);
                new Feesettings.Delete_Consultation().execute(url_consultation);
            } else {
                // Internet connection is not present
                Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void update_consultation() {
        if (isInternetOn() == true) {
            int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            int mHospital_id = Integer.parseInt(hospitalId_List.get(0));

            JSONArray jA1 = new JSONArray();
            jA1.put(mDoctor_id);
            jA1.put(mHospital_id);
            jA1.put(mEMP_Id_Global);
            jA1.put(ConsultationName1);
            jA1.put(ConsultationFee);
            jA1.put("N");
            jA1.put(selected_ConsultationType_ID);
            jA1.put("Y");
            String sb_Updates = jA1.toString();
            StringBuffer sb_Update = new StringBuffer();
            sb_Update.append(Constants.URL + "WebEditConsultation" + "?" + "Data=" + sb_Updates);
            String url_consultation = sb_Update.toString();
            System.out.println("getconsultation_url---" + url_consultation);
            new Feesettings.Update_Consultation().execute(url_consultation);
        } else {
            // Internet connection is not available
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }


    private class Update_Consultation extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog_UpdateConsultation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_UpdateConsultation = new ProgressDialog(Feesettings.this);
            pDialog_UpdateConsultation.setMessage("Please Wait.");
            pDialog_UpdateConsultation.setCancelable(false);
            pDialog_UpdateConsultation.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream) {

            //..........Process JSON DATA................
            System.out.println("json string >>>>>>>>" + stream);


            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);

                    System.out.println(jArray.length());


                    if (jArray.length() != 0) {
/*for (int i = 0; i < jArray.length(); i++)*/
                        for (int i = 0; i < jArray.length(); i++) {

                            String syn_status_reponse = jArray.getString(0);

                            int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);

                            if (syn_status_reponse_result == 1) {

                                // display toast here


                                Toast.makeText(getApplicationContext(), "Update  Sucessfull", Toast.LENGTH_SHORT).show();

                                // Close the dialog
//                     			  dialog_BookAppointment.dismiss();

                            } else {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
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
            if (pDialog_UpdateConsultation.isShowing()) ;
            pDialog_UpdateConsultation.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


        } // onPostExecute() end
    } // ProcessJSON class end

    public class Delete_Consultation extends AsyncTask<String, Void, String> {


        ProgressDialog pDialog_DeleteConsultation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_DeleteConsultation = new ProgressDialog(Feesettings.this);
            pDialog_DeleteConsultation.setMessage("Please Wait.");
            pDialog_DeleteConsultation.setCancelable(false);
            pDialog_DeleteConsultation.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream) {

            //..........Process JSON DATA................
            System.out.println("json string >>>>>>>>" + stream);


            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);

                    System.out.println(jArray.length());


                    if (jArray.length() != 0) {

						/*for (int i = 0; i < jArray.length(); i++)*/
                        for (int i = 0; i < jArray.length(); i++) {

                            String syn_status_reponse = jArray.getString(0);

                            int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);

                            if (syn_status_reponse_result == 1) {

                                // display toast here


                                Toast.makeText(getApplicationContext(), "Delete Sucessfull", Toast.LENGTH_SHORT).show();

                                // Close the dialog
//                     			  dialog_BookAppointment.dismiss();

                            } else {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
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
            if (pDialog_DeleteConsultation.isShowing()) ;
            pDialog_DeleteConsultation.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


        } // onPostExecute() end
    } // ProcessJSON class end

}


