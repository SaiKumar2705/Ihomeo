package com.ihomeo.ihomeouploading.ihomeoactivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihome.ihomeo.adapters.Appointments_Adapter;
import com.ihome.ihomeo.adapters.Doc_ConsultationType_Amt_Adapter;
import com.ihomeo.ihomeouploading.model.Appointments_Model;
import com.ihomeo.ihomeouploading.model.Dashboard_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;


import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Sai on 1/21/2017.
 */

public class PayClick extends Activity {

    //
    // Appointments_Adapter mAdapter;


    private List<String> doctorName_List = new ArrayList<String>();
    private List<String> hospitalId_List = new ArrayList<String>();
    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();


    private int hospitalID_Constant;

    private List<String> doctorId_List = new ArrayList<String>();
    Appointments_Adapter mAdapter;

    private ProgressDialog pDialog_GetDocAppts, pDialog_GetPatients_CheckMobileNo, pDialog_BookAppointment, pDialog_GetDoc_Consult_AmtTypes;

    ArrayList<Appointments_Model> appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> new_appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> doc_ConsultationType_Amt_List = new ArrayList<Appointments_Model>();
    private String mUserType, mCurrent_Date, selected_ConsultationType, doctor_Name_Global, mobile_Number_edt_Global, patient_Name_edt_Global, from_Time_Global,
            to_Time_Global, selected_NewPatient_Name, mMR_Number_selected_NewPatient, selected_Consultation_TypeAmt,
            mMRNumber_Pay, mMRNumber_pharmacy_paybill, from_DateTime_Global, mConsultation_DefinedFee;

    private EditText calender;
    private DatePicker mDatepicker_Dialog;
    private Button mCancel_btn_Dialog, mSet_btn_Dialog;
    private EditText Cons_Fee,Mobile,Name;

    private int mEMP_Id_Global, doctor_ID_Global, hospital_ID_Global, Appointment_master_id, token_Number_Global, selected_ConsultationType_ID;
    Context context;
    private Spinner Spinner;

    Appointments Appointment;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payclick);


        getIds_Payclick();
        Calender_Click();


        getData();




        mCancel_btn_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayClick.this.finish();
            }
        });


        // spinner_DropDown_Diagnosis();

        Cons_Fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDoctor_Consultaion_AmtTypes();

               /* Appointments in =new Appointments();
                in.getDoctor_Consultaion_AmtTypes();
*/
            }
        });


    }

    private void getData() {
        SharedPreferences userType_pref_pay = getSharedPreferences("userType_MyPrefs", Context.MODE_PRIVATE);
        mUserType = userType_pref_pay.getString("userType_KEY_PREF", "");


        SharedPreferences doctorId_pref = getSharedPreferences("doctorId_MyPrefs", Context.MODE_PRIVATE);
        String doctorIDs_CommaString = doctorId_pref.getString("doctorId_KEY_PREF", "");

        SharedPreferences doctorName_pref = getSharedPreferences("doctorName_MyPrefs", Context.MODE_PRIVATE);
        String doctorNames_CommaString = doctorName_pref.getString("doctorName_KEY_PREF", "");


        SharedPreferences hospitalId_pref = getSharedPreferences("hospitalId_MyPrefs", Context.MODE_PRIVATE);
        String hospitalIDs_CommaString = hospitalId_pref.getString("hospitalId_KEY_PREF", "");

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

    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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

    public void getDoctor_Consultaion_AmtTypes() {

        if (isInternetOn() == true) {


            int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            int mHospital_id = Integer.parseInt(hospitalId_List.get(0));


            if (mUserType.equals("DOCTOR")) {
                StringBuffer sb_Consult_Amts = new StringBuffer();
                sb_Consult_Amts.append(Constants.URL + "GetDoctorConsultationTypes?DoctorId=" + mDoctor_id + "&HospId=" + mHospital_id);
                String url_Consultation_Amt_types = sb_Consult_Amts.toString();
                System.out.println("Get_Consult_Amt_Types---" + url_Consultation_Amt_types);
                new GetDoctorConsultationTypes().execute(url_Consultation_Amt_types);
            } else if (mUserType.equals("USER")) {

                StringBuffer sb_GetDefinedFee = new StringBuffer();
                sb_GetDefinedFee.append(Constants.URL + "GetDefinedFee?MrNo=" + mMRNumber_Pay + "&HospId=" + hospital_ID_Global);
                String url_GetDefinedFee = sb_GetDefinedFee.toString();
                System.out.println("Get_defined fee for users---" + url_GetDefinedFee);
                new GetDefinedFee_User().execute(url_GetDefinedFee);
            }
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }


    private class GetDefinedFee_User extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_GetDefinedFee;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDefinedFee = new ProgressDialog(PayClick.this);
            pDialog_GetDefinedFee.setMessage("Please Wait.");
            pDialog_GetDefinedFee.setCancelable(false);
            pDialog_GetDefinedFee.show();
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
            System.out.println("json string GetDefinedFee>>>>>>>>" + stream);
            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);
                            String mConsultation_fee_Portal = jArrray1.getString(0);
                            if (mConsultation_fee_Portal.equals("NA")) {
                                mConsultation_DefinedFee = "0.00";
                            } else {
                                mConsultation_DefinedFee = mConsultation_fee_Portal;
                            }
                            if (mConsultation_DefinedFee != null && !mConsultation_DefinedFee.isEmpty()) {
                                break;
                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here
                                //   	        	  Toast.makeText(getApplicationContext(), "Existing patients collected", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available at portal", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetDefinedFee.isShowing()) ;
            pDialog_GetDefinedFee.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            userType_PaymentDialog();
        } // onPostExecute() end
    } // ProcessJSON class end

    public void userType_PaymentDialog() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_payment = new Dialog(PayClick.this);
        // hide to default title for Dialog
        dialog_payment.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_payment.setContentView(R.layout.payment_dialog);

        TextView title_Dialog = (TextView) dialog_payment.findViewById(R.id.title_payment_dialog);
        final TextView consultation_fee_txv_Dialog = (TextView) dialog_payment.findViewById(R.id.consultation_fee_txv_payment_dialog);
        final EditText pharmacy_fee_edt = (EditText) dialog_payment.findViewById(R.id.pharmacy_fee_edt_payment_dialog);
        TextView total_Amt_title_txv_Dialog = (TextView) dialog_payment.findViewById(R.id.sum_title_txv_payment_dialog);
        final TextView total_Amt_txv_Dialog = (TextView) dialog_payment.findViewById(R.id.sum_payment_dialog);
        Button cancel_btn = (Button) dialog_payment.findViewById(R.id.cancel_btn_payment_dialog);
        Button confirm_btn = (Button) dialog_payment.findViewById(R.id.confirm_btn_payment_dialog);


        if (mConsultation_DefinedFee != null && !mConsultation_DefinedFee.isEmpty()) {


            ArrayList<Appointments_Model> payData_list = mAdapter.pay_list;
            for (int i = 0; i < payData_list.size(); i++) {
                Appointments_Model state = payData_list.get(i);
                mMRNumber_pharmacy_paybill = state.getMR_Number();
                if (mMRNumber_pharmacy_paybill != null && !mMRNumber_pharmacy_paybill.isEmpty()) {
                    break;

                }
            }
        } else {
            ArrayList<Appointments_Model> payPharmacy_list = mAdapter.pharmacy_paybill_list;
            for (int i = 0; i < payPharmacy_list.size(); i++) {
                Appointments_Model state = payPharmacy_list.get(i);
                mMRNumber_pharmacy_paybill = state.getMR_Number();
                mConsultation_DefinedFee = state.getPay_Consultation();
                if (mMRNumber_pharmacy_paybill != null && !mMRNumber_pharmacy_paybill.isEmpty()) {
                    break;
                }
            }

        }
        consultation_fee_txv_Dialog.setText(mConsultation_DefinedFee);
//		adding Indian Rupees Symbol to amount
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        final String consultation_fee = consultation_fee_txv_Dialog.getText().toString();


        total_Amt_txv_Dialog.setText(formatter.format(Float.parseFloat(consultation_fee) + (Float.parseFloat("0"))));
        pharmacy_fee_edt.addTextChangedListener(new TextWatcher()

        {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                if (pharmacy_fee_edt.getText().length() == 0) {
                    total_Amt_txv_Dialog.setText(formatter.format(Float.parseFloat(consultation_fee) + (Float.parseFloat("0"))));
                } else if (pharmacy_fee_edt.getText().toString() != null) {
                    float total_Amt_float = Float.parseFloat(consultation_fee) + Float.parseFloat(pharmacy_fee_edt.getText().toString());
                    String mTotal_Amt_Rupees = formatter.format(total_Amt_float);
                    total_Amt_txv_Dialog.setText(mTotal_Amt_Rupees);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (pharmacy_fee_edt.getText().length() == 0) {

                } else if (pharmacy_fee_edt.getText().toString() != null) {


                }

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_payment.dismiss();

            }
        });
    }


       /* confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetOn() == true) {
                    String pharmacy_Fee_Entered = null;
                    if (pharmacy_fee_edt.getText().length() == 0) {
                        pharmacy_Fee_Entered = "0.00";
                    } else if (pharmacy_fee_edt.getText().toString() != null) {

                        pharmacy_Fee_Entered = pharmacy_fee_edt.getText().toString();
                    }
                    String date_Time_Now1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String date_Time_Now = date_Time_Now1.replace(" ", "%20");
                    JSONArray jA1 = new JSONArray();
                    jA1.put(mMRNumber_pharmacy_paybill);
                    jA1.put(date_Time_Now);
                    jA1.put(consultation_fee_txv_Dialog.getText().toString());

                    jA1.put(pharmacy_Fee_Entered);
                    jA1.put(doctor_ID_Global);
                    jA1.put(mEMP_Id_Global);
                    jA1.put(hospital_ID_Global);

                    String send_payment = jA1.toString();
                    StringBuffer sb_send_payment = new StringBuffer();
                    sb_send_payment.append(Constants.URL + "PaymentDone?Data=" + send_payment);
                    String url_Payment = sb_send_payment.toString();
                    System.out.println("send final payemnt >>>>>>>>" + url_Payment);
                   // new Send_PaymentDone().execute(url_Payment);
                    dialog_payment.dismiss();
                } else {
                    // Internet connection is not present
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }

            }

        });

        // Display the dialog
        dialog_payment.show();
    }*/


    private void doc_ConsultationTypes_List_Dilaog() {

        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(PayClick.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.consultation_amt_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_consultation_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.consultation_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_consultation_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_consultation_amt_dialog);


        title_Dialog.setText("Consultation types");


        Doc_ConsultationType_Amt_Adapter mAdapter = new Doc_ConsultationType_Amt_Adapter(PayClick.this, R.layout.doc_consultation_amt_type_dialog_row, doc_ConsultationType_Amt_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);

                selected_ConsultationType_ID = _model.getConsultationType_ID();
                selected_ConsultationType = _model.getConsultation_Type();
                selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();


                EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                e_etxt_Et.setText(selected_Consultation_TypeAmt);


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_ConsultationType_Amt_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doc_ConsultationType_Amt_List.size() == 0) {
                    // Close the dialog
                    dialog_ConsultAmt.dismiss();

                } else {

                    EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                    selected_Consultation_TypeAmt = e_etxt_Et.getText().toString();

                    Cons_Fee.setText(selected_Consultation_TypeAmt);


                    if (selected_Consultation_TypeAmt != null && !selected_Consultation_TypeAmt.isEmpty()) {
                        //					  Send Selected Consultation Fee  from listview
                        //send_DefineConsultationFee();

                        // Close the dialog
                        dialog_ConsultAmt.dismiss();
                        doc_ConsultationType_Amt_List.clear();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select consultation amount", Toast.LENGTH_SHORT).show();
                    }
                }


            }


        });

        // Display the dialog
        dialog_ConsultAmt.show();
    }

    /*protected void send_DefineConsultationFee() {

        // TODO Auto-generated method stub
        if (isInternetOn() == true) {
            JSONArray jA1 = new JSONArray();
            jA1.put(mMRNumber_Pay);
            jA1.put(selected_ConsultationType_ID);
            jA1.put(selected_Consultation_TypeAmt);
            jA1.put(doctor_ID_Global);
            jA1.put(mEMP_Id_Global);
            jA1.put(hospital_ID_Global);

            String send_Doc_DefineConsultFee = jA1.toString();
            StringBuffer sb_DefineConsultFee = new StringBuffer();
            sb_DefineConsultFee.append(Constants.URL + "DefineConsultationFee?Data=" + send_Doc_DefineConsultFee);
            String url_DefineConsultationFee = sb_DefineConsultFee.toString();
            System.out.println("send doc consultation amt to server fm Listview>>>>>>>>" + url_DefineConsultationFee);
            new Send_DefineConsultationFeeService().execute(url_DefineConsultationFee);
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }*/

    private class Send_DefineConsultationFeeService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_DefineConsultFee;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_DefineConsultFee = new ProgressDialog(PayClick.this);
            pDialog_DefineConsultFee.setMessage("Please Wait.");
            pDialog_DefineConsultFee.setCancelable(false);
            pDialog_DefineConsultFee.show();
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
            System.out.println("json string defineConsultFee>>>>>>>>" + stream);
            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        String syn_status_reponse = jArray.getString(0);
                        int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);

                        if (syn_status_reponse_result == 1) {

                            // display toast here
                            Toast.makeText(getApplicationContext(), "Consultation fee defined successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            // display toast here
                            Toast.makeText(getApplicationContext(), "Consultation fee not defined", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available at portal", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_DefineConsultFee.isShowing()) ;
            pDialog_DefineConsultFee.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end


   /* private void spinner_DropDown_Diagnosis() {

        // TODO Auto-generated method stub


        // Set the ClickListener for Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Diagnosis_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }*/

    public void onItemSelected(AdapterView<?> adapterView,
                               View view, int i, long l) {
        // TODO Auto-generated method stub

    }

    // If no option selected
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void Calender_Click() {
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
    }

    public void datepicker() {

        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog = new Dialog(PayClick.this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.appointments_datetime_picker_dialog);

        mDatepicker_Dialog = (DatePicker) dialog.findViewById(R.id.date_picker_appointments_dialog);
        mSet_btn_Dialog = (Button) dialog.findViewById(R.id.set_btn_dialog_received_material);
        mCancel_btn_Dialog = (Button) dialog.findViewById(R.id.cancel_btn_datepicker_appointments_dialog);


        mCancel_btn_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        mSet_btn_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);

                String selected_Date = mDatepicker_Dialog.getDayOfMonth() + "-" + (mDatepicker_Dialog.getMonth() + 1) + "-" + mDatepicker_Dialog.getYear();

                int mDay = c.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                // SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("dd");
                SimpleDateFormat output1 = new SimpleDateFormat("MMM");
                SimpleDateFormat output2 = new SimpleDateFormat("yyyy");

                try {

                    Date oneWayDate = input.parse(selected_Date);  // parse input

                    calender.setText(selected_Date);
                  /*  FromDate = input.format(oneWayDate);

                    date1.setText(output.format(oneWayDate));
                    //date2.setText(output3.format(oneWayDate));
                    month1.setText(output1.format(oneWayDate));
                    //month2.setText(output1.format(oneWayDate));
                    year1.setText(output2.format(oneWayDate));
                    //year2.setText(output2.format(oneWayDate));// format output*/


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();


//            	 call webservice

            }
        });

        // Display the dialog
        dialog.show();
    }

    private void getIds_Payclick() {

        calender = (EditText) findViewById(R.id.calender);

        //  Spinner = (Spinner) findViewById(R.id.spinner);

        Cons_Fee = (EditText) findViewById(R.id.cons_fee);



        Mobile = (EditText) findViewById(R.id.mobile);

        Name = (EditText) findViewById(R.id.name);


        mCancel_btn_Dialog = (Button) findViewById(R.id.cancel_btn_payment_dialog);


    }


    @Override
    public void onBackPressed() {


        this.finish();
    }


    private class GetDoctorConsultationTypes extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDoc_Consult_AmtTypes = new ProgressDialog(PayClick.this);
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
                            String mConsultation_ID_response = jArrray1.getString(0);
                            String mConsultation_Type = jArrray1.getString(1);
                            String mConsultation_Amt = jArrray1.getString(2);
                            int mConsultation_ID = Integer.parseInt(mConsultation_ID_response);

                            Appointments_Model appointments_model = new Appointments_Model(mConsultation_ID, mConsultation_Type, mConsultation_Amt);
                            doc_ConsultationType_Amt_List.add(appointments_model);

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
            doc_ConsultationTypes_List_Dilaog();

        } // onPostExecute() end

    } // ProcessJSON class end

}
