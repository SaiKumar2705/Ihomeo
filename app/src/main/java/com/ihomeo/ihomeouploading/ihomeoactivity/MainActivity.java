package com.ihomeo.ihomeouploading.ihomeoactivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ihomeo.ihomeouploading.dbsqlite.dbhelper;
import com.ihomeo.ihomeouploading.model.Dashboard_Model;
import com.ihomeo.ihomeouploading.model.login_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

/*

    Context mContext=MainActivity.this;
    public SharedPreferences appPreferences;
    boolean isAppInstalled = false;
*/


    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{1,250}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9+_.]{4,16}");
    private static String url_LoginValidation, url_getinstalldate;
    ArrayList<login_Model> login_List = new ArrayList<login_Model>();
    private Button mLoginbtn, mRegiterbtn;
    private EditText mUserName_Edt, mPassword_Edt;
    private TextView mversion, iHomeo,NotOnIhomeo;
    private ProgressDialog pDialog;

    private StringBuffer doctorId_SB = new StringBuffer();
    private StringBuffer doctorId_SB1 = new StringBuffer();
    private StringBuffer doctorName_SB = new StringBuffer();
    private StringBuffer hospitalId_SB = new StringBuffer();
    private StringBuffer hospitalId_SB1 = new StringBuffer();

    int badgeCount = 1;


    private CheckBox saveLoginCheckBox;
    private dbhelper db;
    private String mUserType;
    private JSONArray jArray1;
    private Context context;
    private int hospitalID_Constant;
    private List<String> doctorId_List = new ArrayList<String>();
    private List<String> doctorName_List = new ArrayList<String>();
    private List<String> hospitalId_List = new ArrayList<String>();
    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();
    private ProgressDialog pDialog_GetAppInstallDate;
    private int doctor_ID_Global;
    private int mEMP_Id;
    public String recent_token;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.design1);








       /* appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled", false);
        if (isAppInstalled == false) {

            //  create short code

            Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "i-Homeo");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                    .fromContext(getApplicationContext(), R.drawable.ihomeo_logo));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            //Make preference true

            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();*/

        getIds();
        login_BtnClick();
        Register_BtnClick();
        iHomeo_Txtvw_Click();
        //ShortcutIcon();
          getData();
    }

    private void getData() {


        SharedPreferences emp_Id_Pref1 = getSharedPreferences("recent_token", Context.MODE_PRIVATE);
        recent_token = emp_Id_Pref1.getString("recent_token", "");

    }


    private void iHomeo_Txtvw_Click() {
        iHomeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://www.ihomeo.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    /*private void getData() {
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


		int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
		String mDoctor_name = doctorName_List.get(0);
		int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
		Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mHospital_id);
		doctors_list.add(append_model);
	}*/

    private void get_installed_date() {


        if (isInternetOn() == true) {

            getData();
            String MyToken = recent_token;

          //  Integer anInteger = Integer.parseInt(MyToken.toString());




            SharedPreferences doctorId_pref = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
            String doctorIDs_CommaString = doctorId_pref.getString("doctorId_KEY_PREF", "");
            doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
            String AppVersion = mversion.getText().toString();
            //String InstallDate = DateFormat.getDateTimeInstance().format(new Date());
            String date_Time_Now1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String date_Time_Now = date_Time_Now1.replace(" ", "%20");
            //int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            String userName = mUserName_Edt.getText().toString();
            StringBuffer sb_getinstalldate = new StringBuffer();
            sb_getinstalldate.append(Constants.URL + "GetAppInstallDateBest" + "?" + "EmailId=" + userName + "&PartyId=" + doctorId_SB1 + "&EmployeeId=" + mEMP_Id + "&Appversion=" + AppVersion + "&ReleaseDate=" + date_Time_Now + "&Installdate=" + date_Time_Now+"&HospitalId="+hospitalId_SB1  + "&RegTokenId=" + MyToken);
            url_getinstalldate = sb_getinstalldate.toString();
            System.out.println("getinstalldate_url---" + url_getinstalldate);
            new GetAppInstallDate().execute(url_getinstalldate);



        } else {
            // Internet connection is not available
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);/*//***Change Here***
         startActivity(intent);
         finish();
         System.exit(0);*/
        //or


        moveTaskToBack(true);
    }


    private void Register_BtnClick() {

        mRegiterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.ihomeo.com/DrQrFullname.aspx?RegType=APPR"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void login_BtnClick() {
        // TODO Auto-generated method stub
//			click action for login button

        mLoginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                login();
            }
        });
    }

    private void getIds() {
        // TODO Auto-generated method stub
//			getting ids from xml
        mUserName_Edt = (EditText) findViewById(R.id.user_id_edt_activitymain);
        mPassword_Edt = (EditText) findViewById(R.id.password_edt_activitymain);
        mversion = (TextView) findViewById(R.id.version);
        mLoginbtn = (Button) findViewById(R.id.login_btn_activitymain);
        mRegiterbtn = (Button) findViewById(R.id.Register);
        iHomeo = (TextView) findViewById(R.id.ihomeo);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        NotOnIhomeo = (TextView) findViewById(R.id.notonihomeo);

        db = new dbhelper(MainActivity.this);
        String[] CrRowsAvailable = db.FindRememberRows(db, "Y");
        if (CrRowsAvailable[0] == null) {

            String[] RowsAvailableForUName = db.FindRememberRowsUName(db);
            if (RowsAvailableForUName[0] != null) {

                View Registerbutton = findViewById(R.id.Register);
                Registerbutton.setVisibility(View.GONE);
                View NotOnIhomeo = findViewById(R.id.notonihomeo);
                NotOnIhomeo.setVisibility(View.GONE);
                mUserName_Edt.setText(RowsAvailableForUName[0]);
                mUserName_Edt.setEnabled(false);
            }


        } else {
            mUserName_Edt.setText(CrRowsAvailable[0]);
            mPassword_Edt.setText(CrRowsAvailable[1]);
            saveLoginCheckBox.setChecked(true);
            View Registerbutton = findViewById(R.id.Register);
            Registerbutton.setVisibility(View.GONE);
            View NotOnIhomeo = findViewById(R.id.notonihomeo);
            NotOnIhomeo.setVisibility(View.GONE);
            mUserName_Edt.setEnabled(false);

        }

    }

    private void login() {
        // TODO Auto-generated method stub
        String userName = mUserName_Edt.getText().toString();
        String password = mPassword_Edt.getText().toString();
        if (userName.equals("") || password.equals("")) {

            if (userName.equals("")) {
                Toast.makeText(MainActivity.this, "ENTER USER ID", Toast.LENGTH_LONG).show();

            } else if (password.equals("")) {
                Toast.makeText(MainActivity.this, "ENTER PASSWORD", Toast.LENGTH_LONG).show();
            } else if (!CheckuserName(userName)) {
                Toast.makeText(MainActivity.this, "ENTER VALID USERNAME", Toast.LENGTH_LONG).show();
            } else if (!CheckPassword(password)) {
                Toast.makeText(MainActivity.this, "ENTER VALID PASSWORD", Toast.LENGTH_LONG).show();
                //get_installed_date();
            }
        } else {
            isInternetOn();

        }

    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet then setup the app

            StringBuffer sb = new StringBuffer();
            String user_name1 = mUserName_Edt.getText().toString();
            String user_name = user_name1.replace(" ", "%20");
            sb.append(Constants.URL + "LoginValidation?" + "username=" + user_name + "&password=" + mPassword_Edt.getText().toString());
            url_LoginValidation = sb.toString();
            System.out.println("url is...." + url_LoginValidation);

            new GetLoginAccess_JSON().execute(url_LoginValidation);

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return false;


        }
        return false;
    }

    private boolean CheckuserName(String userName) {
        // TODO Auto-generated method stub
        return USERNAME_PATTERN.matcher(userName).matches();
    }

    private boolean CheckPassword(String password) {
        // TODO Auto-generated method stub
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private void shared_Pref_Data() {

        int doctorID = getIntent().getIntExtra("doctorId_Send_DocClass_to_AppointClass", 0);

        // send empid to fee Settings
        SharedPreferences emp_Id_Pref1 = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor emp_Id_Pref_edit1 = emp_Id_Pref1.edit();
        emp_Id_Pref_edit1.putInt("EMP_Id_KEY_PREF", mEMP_Id);
        emp_Id_Pref_edit1.commit();

        //int doctorID = getIntent().getIntExtra("doctorId_Send_DocClass_to_AppointClass", 0);
        /*doctor_ID_Global = doctorID;
        SharedPreferences doctorId_pref1 = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        String doctorIDs_CommaString = doctorId_pref1.getString("doctorId_KEY_PREF", "");
*/
        SharedPreferences emp_Id_Pref = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor emp_Id_Pref_edit = emp_Id_Pref.edit();
        emp_Id_Pref_edit.putInt("EMP_Id_KEY_PREF", mEMP_Id);
        emp_Id_Pref_edit.commit();

		/*doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
        int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
		String mDoctor_name = doctorName_List.get(0);
		int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
		Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mHospital_id);
		doctors_list.add(append_model);*/

        // TODO Auto-generated method stub
        //passing  doctorId_StringBuffer_list  to DoctorsName_List, Dashboard_DoctorsList class
        SharedPreferences doctorId_pref = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor doctorId_edit = doctorId_pref.edit();
        //doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
        doctorId_edit.putString("doctorId_KEY_PREF", doctorId_SB.toString());
        doctorId_edit.commit();
        //passing  doctor_name_StringBuffer_list to DoctorsName_List, Dashboard_DoctorsList  class
        SharedPreferences doctorName_pref = getSharedPreferences("doctorName_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor doctorName_edit = doctorName_pref.edit();
        doctorName_edit.putString("doctorName_KEY_PREF", doctorName_SB.toString());
        doctorName_edit.commit();
        //passing  hospital_id_StringBuffer_list to DoctorsName_List, Dashboard_DoctorsList class
        SharedPreferences hospitalId_pref = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor hospitalId_edit = hospitalId_pref.edit();
        hospitalId_edit.putString("hospitalId_KEY_PREF", hospitalId_SB.toString());
        hospitalId_edit.commit();

        //passing EMP_Id to DoctorsName_List class
        /*SharedPreferences emp_Id_Pref = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor emp_Id_Pref_edit = emp_Id_Pref.edit();
		emp_Id_Pref_edit.putInt("EMP_Id_KEY_PREF", mEMP_Id);
		emp_Id_Pref_edit.commit();
*/
        //passing UserType to Appointments class
        SharedPreferences userType_pref = getSharedPreferences("userType_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor userType_edit = userType_pref.edit();
        userType_edit.putString("userType_KEY_PREF", mUserType);
        userType_edit.commit();

        //Payclick
        SharedPreferences userType_pref_pay_Doc = getSharedPreferences("userType_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor userType_edit_pay_Doc = userType_pref.edit();
        userType_edit.putString("userType_KEY_PREF", mUserType);
        userType_edit.commit();

        SharedPreferences doctorId_pref_Pay = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor doctorId_edit_Pay = doctorId_pref.edit();
        //doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
        doctorId_edit.putString("doctorId_KEY_PREF", doctorId_SB.toString());
        doctorId_edit.commit();


        //sending DoctorId,employeeId and HospitalId to AddFee class

        SharedPreferences doctorId_pref_Addfee = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor doctorId_edit_Addfee = doctorId_pref.edit();
        //doctorId_List = Arrays.asList(doctorIDs_CommaString.split(","));
        doctorId_edit.putString("doctorId_KEY_PREF", doctorId_SB.toString());
        doctorId_edit.commit();

        // Hospital Id

        SharedPreferences hospitalId_pref_Addfee = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor hospitalId_edit_Addfee = hospitalId_pref.edit();
        hospitalId_edit.putString("hospitalId_KEY_PREF", hospitalId_SB.toString());
        hospitalId_edit.commit();

        //Employee Id

        SharedPreferences emp_Id_Pref_Addfee = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor emp_Id_Pref_edit_Addfee = emp_Id_Pref.edit();
        emp_Id_Pref_edit.putInt("EMP_Id_KEY_PREF", mEMP_Id);
        emp_Id_Pref_edit.commit();


    }

    private class GetLoginAccess_JSON extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait.");
            pDialog.setCancelable(false);
            pDialog.show();
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
            /*
                Important in JSON DATA
                -------------------------
                * Square bracket ([) represents a JSON array
                * Curly bracket ({) represents a JSON object
                * JSON object contains key/value pairs
                * Each key is a String and value may be different data types
             */

            //..........Process JSON DATA...............

                System.out.println("json string>>>>>>>>" + stream);

            if (stream != null) {
                try {

                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {


                        for (int i = 0; i < jArray.length(); i++) {
                            jArray1 = jArray.getJSONArray(i);
                            System.out.println("index of array--:" + jArray1.length());

                            if (jArray1.length() == 1) {

                                String login_value = jArray1.getString(0);
                                int login_value_int = Integer.parseInt(login_value);
                                System.out.println("login value" + login_value);

                                if (login_value_int == 0) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // display toast here
                                            Toast.makeText(getApplicationContext(), "UserName or Password are incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (login_value_int == 1) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // display toast here
                                            Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {

                                if (jArray1.getString(1).toString().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Please Setup your Profile on ihomeo.com", Toast.LENGTH_SHORT).show();
                                    Intent ip = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(ip);
                                    finish();
                                    return;
                                }

                                String doctor_id = jArray1.getString(0);
                                String doctor_name = jArray1.getString(1);
                                String doctor_first = jArray1.getString(2);
                                String doctor_last = jArray1.getString(3);
                                String hospital_id = jArray1.getString(4);
                                String emp_id = jArray1.getString(5);
                                String user_Type = jArray1.getString(6);
                                System.out.println("My doctor_name is:" + doctor_name);
                                System.out.println("My doctor_id is:" + doctor_id);

                                doctorId_SB.append(doctor_id + ",");
                                doctorId_SB1.append(doctor_id);
                                doctorName_SB.append(doctor_name + ",");
                                hospitalId_SB.append(hospital_id + ",");

                                hospitalId_SB1.append(hospital_id);


                                mEMP_Id = Integer.parseInt(emp_id);
                                mUserType = user_Type;
                            }


                        }
                        if (jArray1.length() > 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // display toast here
                                    //    Toast.makeText(getApplicationContext(), "Doctor Names Collected", Toast.LENGTH_SHORT).show();
                                    //Inserted by NR
                                    String userName = mUserName_Edt.getText().toString();
                                    String password = mPassword_Edt.getText().toString();
                                    String AppVersion = mversion.getText().toString();
                                    String InstallDate = DateFormat.getDateTimeInstance().format(new Date());
                                    String DoctorId = "1";
                                    String DoctorName = "KARTHIK";
                                    String RememberMe;
                                    if (saveLoginCheckBox.isChecked() == true)
                                        RememberMe = "Y";
                                    else
                                        RememberMe = "N";

                                    db = new dbhelper(MainActivity.this);

                                    boolean tblRowsAvailable = db.FindRows(db, userName, password);

                                    if (tblRowsAvailable == false) {
                                        db.addLogin(db, userName, password, AppVersion, DoctorId, DoctorName, RememberMe, InstallDate);
                                        //shared_Pref_Data();
                                        get_installed_date();
                                        shared_Pref_Data();


                                    } else

                                    {

                                        db.onupdate(db, userName, password, AppVersion, DoctorId, DoctorName, RememberMe, InstallDate);

                                    }
                                    //End - Insert - NR
                                    Intent i_docNameClass = new Intent(MainActivity.this, DoctorsName_List.class);

                                    startActivity(i_docNameClass);
                                    finish();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "Username or password are incorrect", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end
            else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // display toast here
                        Toast.makeText(getApplicationContext(), "Email or Password are incorrect", Toast.LENGTH_SHORT).show();
                    }
                });

            } // else statement end
            super.onPostExecute(stream);
            if (pDialog.isShowing()) ;
            pDialog.dismiss();

        } // onPostExecute() end
    } // ProcessJSON class end

// Activity end

    private class GetAppInstallDate extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetAppInstallDate = new ProgressDialog(MainActivity.this);
            pDialog_GetAppInstallDate.setMessage("Please Wait.");
            pDialog_GetAppInstallDate.setCancelable(false);
            pDialog_GetAppInstallDate.show();

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
            System.out.println("json string>>>>>>>>" + stream);


            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);

                    System.out.println(jArray.length());


                    if (jArray.length() != 0) {

						/*for (int i = 0; i < jArray.length(); i++)*/
                        {

                          /*  JSONObject jObject1 = jArray.getJSONObject(i);*/

                            String userName = mUserName_Edt.getText().toString();
                            //	int partyId = jObject1.getInt("partyId");
                            //	int EmployeeId = jObject1.getInt("EmployeeId");
                            String date_Time_Now1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            String ReleaseDate = date_Time_Now1.replace(" ", "%20");
                            String InstallDate = date_Time_Now1.replace(" ", "%20");
                            String AppVersion = mversion.getText().toString();
                            //String ReleaseDate = DateFormat.getDateTimeInstance().format(new Date());
                            //String InstallDate = DateFormat.getDateTimeInstance().format(new Date());


                            login_Model login_Model = new login_Model(userName, AppVersion, ReleaseDate, InstallDate);
                            login_List.add(login_Model);


                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
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
            if (pDialog_GetAppInstallDate.isShowing()) ;
            pDialog_GetAppInstallDate.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


        } // onPostExecute() end
    } // ProcessJSON class end
}

