package com.ihomeo.ihomeouploading.ihomeoactivity;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ihome.ihomeo.adapters.DoctorsName_Adapter;
import com.ihomeo.ihomeouploading.Report.Patient_Attendance;
import com.ihomeo.ihomeouploading.Settings.Settings;
import com.ihomeo.ihomeouploading.dashboard.Dashboard_DoctorsList;
import com.ihomeo.ihomeouploading.model.DoctorsName_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorsName_List extends Activity {

    private ListView mListview;
    private Button mDashboard_Btn, mpracticeAnalytics_Btn;
    private int mEMP_Id, mHospital_id;
    private static String url_Get;
    private ProgressDialog pDialog_DashBoardAccess;
    private String mUserName, mPassword;

    LinearLayout PracticeAnalytics, Logout, Dashboard, Settings1;
    List<String> doctorId_List = new ArrayList<String>();
    List<String> doctorName_List = new ArrayList<String>();
    List<String> hospitalId_List = new ArrayList<String>();
    ArrayList<DoctorsName_Model> doctors_list = new ArrayList<DoctorsName_Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorsname_list);
        android.app.ActionBar bar = getActionBar();
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'></font>"));
//		      creating methods to write code
        getIds();
        getData();
        listview();
        dashboard_Btn_Click();

	/*	Dashboard.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				dashboard_Btn_Click();
			}
		});
*/
        Settings1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_home12 = new Intent(DoctorsName_List.this, Settings.class);
                startActivity(i_home12);


            }
        });
        Logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You are Logged Out!", Toast.LENGTH_SHORT).show();
                Intent i_home1 = new Intent(DoctorsName_List.this, MainActivity.class);
                startActivity(i_home1);

            }
        });

        PracticeAnalytics.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (isInternetOn() == true) {

                    StringBuffer sb_DashboardAccess = new StringBuffer();
                    sb_DashboardAccess.append(Constants.URL + "DashBoardAccess" + "?" + "EmpId=" + mEMP_Id + "&HospId=" + mHospital_id);
                    String url_DashboardAccess = sb_DashboardAccess.toString();
                    System.out.println("Dashboard url----" + url_DashboardAccess);
                    new DashBoardAccess1().execute(url_DashboardAccess);
                } else {
                    // Internet connection is not present
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //patitentAnalytics_Btn_Click();
    }



	/*private void patitentAnalytics_Btn_Click() {

		mpracticeAnalytics_Btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i = new Intent(DoctorsName_List.this, Patient_Attendance.class);
				startActivity(i);
				finish();
				return;

			}
		});
	}*/


    @Override
    public void onBackPressed() {
        // do something on back.
//	    			Toast.makeText(getApplicationContext(), "go back", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(DoctorsName_List.this, MainActivity.class);
        startActivity(i);
        finish();
        return;
    }

    private void listview() {
        // TODO Auto-generated method stub
        LinearLayout relative = (LinearLayout) findViewById(R.id.listview_linearlayout_doctor_name_list);
        if (doctors_list.size() == 0) {

            relative.setBackgroundResource(0);
        } else {
            relative.setBackgroundResource(R.drawable.boxborder);
        }

        DoctorsName_Adapter adapter = new DoctorsName_Adapter(DoctorsName_List.this, R.layout.doctorsname_list_row, doctors_list);

        mListview.setAdapter(adapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                DoctorsName_Model doctorName_model = (DoctorsName_Model) parent.getItemAtPosition(position);
                int doctor_id = doctorName_model.getDoctor_ID();
                String doctor_name = doctorName_model.getDoctor_Name();
                int hospital_id = doctorName_model.getHospital_Id();

                Intent i = new Intent(DoctorsName_List.this, Appointments.class);

                i.putExtra("doctorId_Send_DocClass_to_AppointClass", doctor_id);
                i.putExtra("doctorName_Send_DocClass_to_AppointClass", doctor_name);
                i.putExtra("hospitalId_Send_DocClass_to_AppointClass", hospital_id);
                startActivity(i);
                finish();
            }
        });
    }

    private void getIds() {
        // TODO Auto-generated method stub
        mListview = (ListView) findViewById(R.id.doctors_namelistview_doc_name_list);
        PracticeAnalytics = (LinearLayout) findViewById(R.id.Practice);
        Settings1 = (LinearLayout) findViewById(R.id.Settings);
        Logout = (LinearLayout) findViewById(R.id.Log);
        Dashboard = (LinearLayout) findViewById(R.id.Dash);

        //mDashboard_Btn = (Button) findViewById(R.id.mydashboard_doc_name_list);
        //	mpracticeAnalytics_Btn=(Button) findViewById(R.id.practiceAnalytics);
    }


    private void getData() {
        // TODO Auto-generated method stub

//			get data for doctorId from MainActivity class
        SharedPreferences doctorId_pref = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        String doctorIDs_CommaString = doctorId_pref.getString("doctorId_KEY_PREF", "");

//			get data for doctorName from MainActivity class
        SharedPreferences doctorName_pref = getSharedPreferences("doctorName_MyPrefs", Context.MODE_PRIVATE);
        String doctorNames_CommaString = doctorName_pref.getString("doctorName_KEY_PREF", "");

//			get data for hospitalId from MainActivity class
        SharedPreferences hospitalId_pref = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        String hospitalIDs_CommaString = hospitalId_pref.getString("hospitalId_KEY_PREF", "");

//			get EMP_Id from MainActivity class
        SharedPreferences emp_Id_pref = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        mEMP_Id = emp_Id_pref.getInt("EMP_Id_KEY_PREF", 0);
//			split the string with reference comma(,) and keep in list
        doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
        doctorName_List = Arrays.asList(doctorNames_CommaString.split(","));
        hospitalId_List = Arrays.asList(hospitalIDs_CommaString.split(","));

        for (int i = 0; i < doctorId_List.size(); i++) {
            int mDoctor_id = Integer.parseInt(doctorId_List.get(i));
            String mDoctor_name = doctorName_List.get(i);
            mHospital_id = Integer.parseInt(hospitalId_List.get(i));
            DoctorsName_Model append_model = new DoctorsName_Model(mDoctor_id, mDoctor_name, mHospital_id);
            doctors_list.add(append_model);
        }
    }


    private void dashboard_Btn_Click() {
        // TODO Auto-generated method stub
        Dashboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isInternetOn() == true) {

                    StringBuffer sb_DashboardAccess = new StringBuffer();
                    sb_DashboardAccess.append(Constants.URL + "DashBoardAccess" + "?" + "EmpId=" + mEMP_Id + "&HospId=" + mHospital_id);
                    String url_DashboardAccess = sb_DashboardAccess.toString();
                    System.out.println("Dashboard url----" + url_DashboardAccess);
                    new DashBoardAccess().execute(url_DashboardAccess);
                } else {
                    // Internet connection is not present
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DashBoardAccess extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_DashBoardAccess = new ProgressDialog(DoctorsName_List.this);
            pDialog_DashBoardAccess.setMessage("Please Wait.");
            pDialog_DashBoardAccess.setCancelable(false);
            pDialog_DashBoardAccess.show();
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

                        String mConstant_String = jArray.getString(0);
                        int mConstant = Integer.parseInt(mConstant_String);
                        if (mConstant == 1) {

                            Intent i = new Intent(DoctorsName_List.this, Dashboard_DoctorsList.class);
                            i.putExtra("Hospital_ID", mHospital_id);
                            startActivity(i);
                        } else if (mConstant == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // display toast here
                                    Toast.makeText(getApplicationContext(), "You are not authorised", Toast.LENGTH_SHORT).show();
                                }
                            });
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
            if (pDialog_DashBoardAccess.isShowing()) ;
            pDialog_DashBoardAccess.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end

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

//		            	 Toast.makeText(getApplicationContext(), "Please connect to internet for latest data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }


    private class DashBoardAccess1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_DashBoardAccess = new ProgressDialog(DoctorsName_List.this);
            pDialog_DashBoardAccess.setMessage("Please Wait.");
            pDialog_DashBoardAccess.setCancelable(false);
            pDialog_DashBoardAccess.show();
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

                        String mConstant_String = jArray.getString(0);
                        int mConstant = Integer.parseInt(mConstant_String);
                        if (mConstant == 1) {

                            Intent i = new Intent(DoctorsName_List.this, Patient_Attendance.class);
                            i.putExtra("Hospital_ID", mHospital_id);
                            startActivity(i);
                        } else if (mConstant == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // display toast here
                                    Toast.makeText(getApplicationContext(), "You are not authorised", Toast.LENGTH_SHORT).show();
                                }
                            });
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
            if (pDialog_DashBoardAccess.isShowing()) ;
            pDialog_DashBoardAccess.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end

}