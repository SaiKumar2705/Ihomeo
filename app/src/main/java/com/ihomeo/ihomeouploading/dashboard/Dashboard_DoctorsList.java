package com.ihomeo.ihomeouploading.dashboard;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihomeo.ihomeouploading.ihomeoactivity.DoctorsName_List;
import com.ihomeo.ihomeouploading.ihomeoactivity.R;
import com.ihomeo.ihomeouploading.model.Dashboard_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;

public class Dashboard_DoctorsList extends Activity {

    private Spinner mSpinnerDropDown_DoctorsName;

    private Button mGet_btn, mBack;

    private TextView mOP_Today_Number_of_bills_Txv, mOP_Today_Amount_Txv, mPharmacy_Today_Number_of_bills_Txv,
            mPharmacy_Today_Amount_Txv, mToday_Sum_Amount_Txv, mOP_Month_Number_of_bills_Txv, mOP_Month_Amount_Txv,
            mPharmacy_Month_Number_of_bills_Txv, mPharmacy_Month_Amount_Txv, mMonth_Sum_Amount_Txv;

    private int hospitalID_Constant;

    private List<String> doctorId_List = new ArrayList<String>();
    private List<String> doctorName_List = new ArrayList<String>();
    private List<String> hospitalId_List = new ArrayList<String>();

    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();

    private ProgressDialog pDialog_GetDashBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_doctorslist);
        android.app.ActionBar bar = getActionBar();
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>i-Homeo</font>"));
//		      creating methods to write code
        getIds();
        getData();
        spinner_DropDown_DoctorNames();
        getBtn_Click();
        get_Dashboard_Data();

        BackBtn_Click();
    }

    private void BackBtn_Click() {

        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dashboard_DoctorsList.this, DoctorsName_List.class);
                startActivity(i);
                finish();
                return;

            }
        });
    }


    @Override
    public void onBackPressed() {
        // do something on back.
//	    			Toast.makeText(getApplicationContext(), "go back", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Dashboard_DoctorsList.this, DoctorsName_List.class);
        startActivity(i);
        finish();
        return;
    }


    private void spinner_DropDown_DoctorNames() {
        // TODO Auto-generated method stub
        ArrayAdapter<Dashboard_Model> myAdapter = new ArrayAdapter<Dashboard_Model>(this, android.R.layout.simple_spinner_item, doctors_list);
        // Drop down layout style - list view with radio button
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDropDown_DoctorsName.setAdapter(myAdapter);


        // Set the ClickListener for Spinner
        mSpinnerDropDown_DoctorsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
    }


    private void getBtn_Click() {
        // TODO Auto-generated method stub
        mGet_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                get_Dashboard_Data();


            }
        });
    }

    private void get_Dashboard_Data() {
        // TODO Auto-generated method stub
        Dashboard_Model model = (Dashboard_Model) mSpinnerDropDown_DoctorsName.getSelectedItem();

        String mCurrent_Date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        int mDoctor_Id = model.getDoctor_ID();
        int mHospital_Id = model.getHospital_Id();

        if (isInternetOn() == true) {

            StringBuffer sb_GetDashboard = new StringBuffer();
            sb_GetDashboard.append(Constants.URL + "GetDashBoard" + "?" + "Date=" + mCurrent_Date + "&DocId=" + mDoctor_Id + "&HospId=" + mHospital_Id);
            String url_DashboardAccess = sb_GetDashboard.toString();
            new GetDashBoard().execute(url_DashboardAccess);
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetDashBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDashBoard = new ProgressDialog(Dashboard_DoctorsList.this);
            pDialog_GetDashBoard.setMessage("Please Wait.");
            pDialog_GetDashBoard.setCancelable(false);
            pDialog_GetDashBoard.show();
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
                            JSONObject jObject1 = jArray.getJSONObject(i);

                            String today_OP_BillCount = jObject1.getString("TimeTillDateOPCount");
                            String today_OP_Amount = jObject1.getString("TimeTillDateOPCollection");
                            String today_Pharmacy_BillCount = jObject1.getString("TimeTillDatePharCount");
                            String today_Pharmacy_Amount = jObject1.getString("TimeTillDatePharCollection");

                            String month_OP_BillCount = jObject1.getString("MonthTillDateOPCount");
                            String month_OP_Amount = jObject1.getString("MonthTillDateOPCollection");
                            String month_Pharmacy_BillCount = jObject1.getString("MonthTillDatePharCount");
                            String month_Pharmacy_Amount = jObject1.getString("MonthTillDatePharCollection");

                            mOP_Today_Number_of_bills_Txv.setText(today_OP_BillCount);
                            mOP_Today_Amount_Txv.setText(today_OP_Amount);
                            mPharmacy_Today_Number_of_bills_Txv.setText(today_Pharmacy_BillCount);
                            mPharmacy_Today_Amount_Txv.setText(today_Pharmacy_Amount);

                            mOP_Month_Number_of_bills_Txv.setText(month_OP_BillCount);
                            mOP_Month_Amount_Txv.setText(month_OP_Amount);
                            mPharmacy_Month_Number_of_bills_Txv.setText(month_Pharmacy_BillCount);
                            mPharmacy_Month_Amount_Txv.setText(month_Pharmacy_Amount);

//		            	    		adding Indian Rupees Symbol to amount 
                            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//		            	    	        Today_sum_Amt(op+pharmacy)
                            float sum_Today_Amt_float = Float.parseFloat(today_OP_Amount) + Float.parseFloat(today_Pharmacy_Amount);
                            String mSum_Today_Amt_Rupees = formatter.format(sum_Today_Amt_float);
                            mToday_Sum_Amount_Txv.setText(mSum_Today_Amt_Rupees);
                            System.out.println("today sum_amt" + mSum_Today_Amt_Rupees);
//		            	    	        month_Sum_Amt(op+pharmacy)
                            float sum_Month_Amt_float = Float.parseFloat(month_OP_Amount) + Float.parseFloat(month_Pharmacy_Amount);
                            String mSum_Month_Amt_Rupees = formatter.format(sum_Month_Amt_float);
                            mMonth_Sum_Amount_Txv.setText(mSum_Month_Amt_Rupees);
                            System.out.println("month sum_amt" + mSum_Month_Amt_Rupees);

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Collections collected", Toast.LENGTH_SHORT).show();
                            }
                        });


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
            if (pDialog_GetDashBoard.isShowing()) ;
            pDialog_GetDashBoard.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end

    private void getData() {
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
        doctorName_List = Arrays.asList(doctorNames_CommaString.split(","));
        hospitalId_List = Arrays.asList(hospitalIDs_CommaString.split(","));

//				adding All to dropdown
        doctors_list.add(new Dashboard_Model(0, "All", hospitalID_Constant));

        for (int i = 0; i < doctorId_List.size(); i++) {
            int mDoctor_id = Integer.parseInt(doctorId_List.get(i));
            String mDoctor_name = doctorName_List.get(i);
            int mHospital_id = Integer.parseInt(hospitalId_List.get(i));
            Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mDoctor_name, mHospital_id);
            doctors_list.add(append_model);
        }
    }

    private void getIds() {
        // TODO Auto-generated method stub
        mBack = (Button) findViewById(R.id.goback);
        mSpinnerDropDown_DoctorsName = (Spinner) findViewById(R.id.spinner_Dashboard);
        mGet_btn = (Button) findViewById(R.id.Get_Btn_Dashboard);
        mOP_Today_Number_of_bills_Txv = (TextView) findViewById(R.id.today_opd_no_of_bills_Dashboard);
        mOP_Today_Amount_Txv = (TextView) findViewById(R.id.today_opd_amount_Dashboard);
        mPharmacy_Today_Number_of_bills_Txv = (TextView) findViewById(R.id.today_pharmacy_no_of_bills_Dashboard);
        mPharmacy_Today_Amount_Txv = (TextView) findViewById(R.id.today_pharmacy_amount_Dashboard);
        mToday_Sum_Amount_Txv = (TextView) findViewById(R.id.today_sum_amount_Dashboard);

        mOP_Month_Number_of_bills_Txv = (TextView) findViewById(R.id.month_opd_no_of_bills_Dashboard);
        mOP_Month_Amount_Txv = (TextView) findViewById(R.id.month_opd_amount_Dashboard);
        mPharmacy_Month_Number_of_bills_Txv = (TextView) findViewById(R.id.month_pharmacy_no_of_bills_Dashboard);
        mPharmacy_Month_Amount_Txv = (TextView) findViewById(R.id.month_pharmacy_amount_Dashboard);
        mMonth_Sum_Amount_Txv = (TextView) findViewById(R.id.month_sum_amount_Dashboard);
    }

    public final boolean isInternetOn() {
        // TODO Auto-generated method stub
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet then setup the app

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

//	 Toast.makeText(getApplicationContext(), "Please connect to internet for latest data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

}
