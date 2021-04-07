package com.ihomeo.ihomeouploading.ihomeoactivity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ihome.ihomeo.adapters.Appointments_Adapter;
import com.ihome.ihomeo.adapters.Doc_ConsultationType_Amt_Adapter;
import com.ihome.ihomeo.adapters.Doc_DiagnosisType_Amt_Adapter;
import com.ihome.ihomeo.adapters.New_AppointmentsDialog_Adapter;
import com.ihomeo.ihomeouploading.model.Appointments_Model;
import com.ihomeo.ihomeouploading.model.Diagnosis_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.ihome.ihomeo.adapters.Appointments_Adapter.Age;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Age_NotArrived;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Age_Not_Paid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Fee;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Fee_NotArrived;

import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Fee_NotPaid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Id;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Id1;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Cons_Id2;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Diag_id;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Diag_id1;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Diag_id2;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Diagnosis_Types;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Gender_NotArrived;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Gender_NotPaid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.K;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.MR_NOT_ARRIVED;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.MR_NOT_PAID;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.MR_Number;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.MobileNum;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.MobileNum_NotArrived;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Phar_Charge;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Phar_Charge_NotArrived;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.Phar_Charge_Not_Paid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.calender;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.mobile_Not_Paid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.name_edt;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.name_edt_NotArrived;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.name_edt_Not_Paid;
import static com.ihome.ihomeo.adapters.Appointments_Adapter.sk;

public class Appointments extends Activity {


    private TextView mCurt_date_Txv, mDoctorName_Txv;
    private ImageView mPrevous_date_Img, mRefersh_Img, mNext_date_Img;
    private Button mClose_Btn, mCancel_btn_Dialog, mSet_btn_Dialog;
    private ListView mListview;

    private String mUserType, mCurrent_Date, selected_ConsultationType, doctor_Name_Global, mobile_Number_edt_Global, from_Time_Global1, patient_Name_edt_Global, from_Time_Global,
            to_Time_Global, selected_NewPatient_Name, mMR_Number_selected_NewPatient, selected_DiagnosisType, selected_Consultation_TypeAmt,
            mMRNumber_Pay, mMRNumber_pharmacy_paybill, from_DateTime_Global, mConsultation_DefinedFee;
    private int mEMP_Id_Global, doctor_ID_Global, hospital_ID_Global, Appointment_master_id, token_Number_Global, selected_ConsultationType_ID, selected_DiagnosisType_ID;
    private ProgressDialog pDialog_GetDocAppts, pDialog_Insert, pDialog_Insert1, pDialog_GetDiagnosis, pDialog_GetPatients_CheckMobileNo, pDialog_BookAppointment, pDialog_GetDoc_Consult_AmtTypes;
    private static String url_GetDocAppointments, url_CheckMobileNo_GetbyMobileNo, url_BookAppointment_New,
            url_BookAppointment, url_GetDocDiagnosis, url_InsertPatRecord;
    private Calendar mCalender;
    private SimpleDateFormat mDateFormat;

    String next_date_result_Global;

    Dialog dialog_BookAppointment;
    private DatePicker mDatepicker_Dialog;
    ArrayList<Appointments_Model> appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> new_appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> doc_ConsultationType_Amt_List = new ArrayList<Appointments_Model>();
    ArrayList<Diagnosis_Model> doc_Diagnosis_List = new ArrayList<Diagnosis_Model>();

    public ArrayList<Appointments_Model> new_list = new ArrayList<Appointments_Model>();
    //ArrayList<DoctorCount_model> DoctorCount_List = new ArrayList<DoctorCount_model>();

    Appointments_Adapter mAdapter;


    public int selected_ConsultationType_ID_S;


    private EditText MR_NotArrived;


    public String MOBILE, mMR_Number, mNewPatient_Name, correct_Date, MRNO;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);


        //	      creating methods to write code
        getIds();
        current_Date_ArrowClick();
        getData();
        getDoctor_Appointments_Portal();
        close_Btn_Click();
        refresh_Img_Click();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onBackPressed() {
        // do something on back.
//	    			Toast.makeText(getApplicationContext(), "go back", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Appointments.this, DoctorsName_List.class);
        startActivity(i);
        finish();
        return;
    }

    //adapter class methods
    public void PatientArrived() {
        if (isInternetOn() == true) {
            send_ArrivedWebservices();
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void refresh_Img_Click() {
        // TODO Auto-generated method stub


        mRefersh_Img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                getDoctor_Appointments_Portal();
            }
        });
    }

    private void send_ArrivedWebservices() {
        // TODO Auto-generated method stub
        ArrayList<Appointments_Model> arrivedData_list = mAdapter.arrived_list;
        String arrivedPatient_MRNumber = null;
        for (int i = 0; i < arrivedData_list.size(); i++) {
            Appointments_Model state = arrivedData_list.get(i);
            arrivedPatient_MRNumber = state.getMR_Number();
        }
        String date_Time_Now1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String date_Time_Now = date_Time_Now1.replace(" ", "%20");
//			create json string
        JSONArray jA1 = new JSONArray();
        jA1.put(arrivedPatient_MRNumber);
        jA1.put(date_Time_Now);
        jA1.put(doctor_ID_Global);
        jA1.put(mEMP_Id_Global);

        jA1.put(hospital_ID_Global);
        jA1.put(Appointment_master_id);
        String send_ArrivedData = jA1.toString();
        StringBuffer sb_PatientArrived = new StringBuffer();
        sb_PatientArrived.append(Constants.URL + "PatientArrived?Data=" + send_ArrivedData);
        String url_PatientArrived = sb_PatientArrived.toString();
        System.out.println("url_PatientArrived" + url_PatientArrived);

        new PatientArrived_Service().execute(url_PatientArrived);
    }

    //	adapter class methods
    public void check_MobileNumber() {
        // TODO Auto-generated method stub
        if (isInternetOn() == true) {
            check_MobileNo_getPatients();
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void getDoctor_Consultaion_AmtTypes() {
        if (isInternetOn() == true) {
            ArrayList<Appointments_Model> payData_list = mAdapter.pay_list;

            for (int i = 0; i < payData_list.size(); i++) {
                Appointments_Model state = payData_list.get(i);
                mMRNumber_Pay = state.getMR_Number();
                if (mMRNumber_Pay != null && !mMRNumber_Pay.isEmpty()) {
                    break;
                }
            }
            if (mUserType.equals("DOCTOR")) {
                StringBuffer sb_Consult_Amts = new StringBuffer();
                sb_Consult_Amts.append(Constants.URL + "GetDoctorConsultationTypes?DoctorId=" + doctor_ID_Global + "&HospId=" + hospital_ID_Global);
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


    public void check_MobileNo_getPatients() {
        // TODO Auto-generated method stub
        edittextDataFM_Appointments_Adapter();

        StringBuffer sb_CheckMobile = new StringBuffer();
        sb_CheckMobile.append(Constants.URL + "GetbyMobileNo" + "?" + "MobileNo=" + mobile_Number_edt_Global + "&HospitalId=" + hospital_ID_Global);
        url_CheckMobileNo_GetbyMobileNo = sb_CheckMobile.toString();
        System.out.println("Checking_Mobile_Number" + url_CheckMobileNo_GetbyMobileNo);
        new GetPatients_CheckMobileNo().execute(url_CheckMobileNo_GetbyMobileNo);
    }

    private void edittextDataFM_Appointments_Adapter() {
        // TODO Auto-generated method stub
        ArrayList<Appointments_Model> new_list = mAdapter.new_list;

        for (int i = 0; i < new_list.size(); i++) {
            Appointments_Model state = new_list.get(i);
            patient_Name_edt_Global = state.getPatient_Name();
            mobile_Number_edt_Global = state.getMobile_Numb();
            from_Time_Global = state.getFrom_Time();
            to_Time_Global = state.getTo_Time();
            token_Number_Global = state.getToken_Number();

        }
    }

    private void current_Date_ArrowClick() {
        // TODO Auto-generated method stub
        mCurrent_Date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        mCalender = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy");

        String mCurrent_Date_Display = mDateFormat.format(mCalender.getTime());
        mCurt_date_Txv.setText(mCurrent_Date_Display);


        mPrevous_date_Img.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

//		            	set date from mCurt_date_Txv(selected date from datepicker) to Calender
                setDateTo_Calender();

//	                   minus 1 day to calender
                mCalender.add(Calendar.DATE, -1);
                String prev_date_result = mDateFormat.format(mCalender.getTime());

                Date today_Date = null, previous_Date = null;
                try {
                    today_Date = mDateFormat.parse(new SimpleDateFormat("EEE, d-MMM-yyyy").format(new Date()));
                    previous_Date = mDateFormat.parse(prev_date_result);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (mUserType.equals("USER") && previous_Date.before(today_Date)) {

                    Toast.makeText(getApplicationContext(), "You are not authorised", Toast.LENGTH_SHORT).show();
                } else {

                    mCurt_date_Txv.setText(prev_date_result);
                    change_DateFormat();
//			            	 call webservice
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();

                    getDoctor_Appointments_Portal();
                }

            }


        });

        mNext_date_Img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();

//	            	set date from mCurt_date_Txv(selected date from datepicker) to Calender
                setDateTo_Calender();

//                   add 1 day to calender
                mCalender.add(Calendar.DATE, 1);
                String next_date_result = mDateFormat.format(mCalender.getTime());
                next_date_result_Global = next_date_result;
                mCurt_date_Txv.setText(next_date_result);

//	       		 changing  EEE, d-MMM-yyyy  to  dd-MM-yyyy (to send date as parameter in webservice)
                change_DateFormat();
//	            	 call webservice
                getDoctor_Appointments_Portal();

            }
        });
        mCurt_date_Txv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datePicker();
            }
        });
    }

    private void setDateTo_Calender() {
        // TODO Auto-generated method stub
        try {
            mCalender.setTime(mDateFormat.parse(mCurt_date_Txv.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void change_DateFormat() {
        // TODO Auto-generated method stub
//		 changing  EEE, d-MMM-yyyy  to  dd-MM-yyyy

        String date = mCurt_date_Txv.getText().toString();
        SimpleDateFormat input = new SimpleDateFormat("EEE, d-MMM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date appointments_Date = input.parse(date);                 // parse input

            mCurrent_Date = output.format(appointments_Date);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void datePicker1() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.appointments_datetime_picker_dialog);

        mDatepicker_Dialog = (DatePicker) dialog.findViewById(R.id.date_picker_appointments_dialog);
        mSet_btn_Dialog = (Button) dialog.findViewById(R.id.set_btn_dialog_received_material);
        mCancel_btn_Dialog = (Button) dialog.findViewById(R.id.cancel_btn_datepicker_appointments_dialog);


        mCancel_btn_Dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        mSet_btn_Dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_Date = mDatepicker_Dialog.getDayOfMonth() + "-" + (mDatepicker_Dialog.getMonth() + 1)
                        + "-" + mDatepicker_Dialog.getYear();
                SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("EEE, d-MMM-yyyy");
                try {
                    Date oneWayDate = input.parse(selected_Date);                 // parse input

                    // mCurt_date_Txv.setText(output.format(oneWayDate));        // format output

                    calender.setText(selected_Date);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
//	       		 changing  EEE, d-MMM-yyyy  to  dd-MM-yyyy (to send date as parameter in webservice)
                change_DateFormat();
//            	 call webservice

                getDoctor_Appointments_Portal();
            }
        });

        // Display the dialog
        dialog.show();

    }

    public void datePicker() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.appointments_datetime_picker_dialog);

        mDatepicker_Dialog = (DatePicker) dialog.findViewById(R.id.date_picker_appointments_dialog);
        mSet_btn_Dialog = (Button) dialog.findViewById(R.id.set_btn_dialog_received_material);
        mCancel_btn_Dialog = (Button) dialog.findViewById(R.id.cancel_btn_datepicker_appointments_dialog);


        mCancel_btn_Dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        mSet_btn_Dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_Date = mDatepicker_Dialog.getDayOfMonth() + "-" + (mDatepicker_Dialog.getMonth() + 1)
                        + "-" + mDatepicker_Dialog.getYear();
                SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("EEE, d-MMM-yyyy");
                try {
                    Date oneWayDate = input.parse(selected_Date);                 // parse input

                    mCurt_date_Txv.setText(output.format(oneWayDate));        // format output

                    calender.setText(output.format(oneWayDate));


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
//	       		 changing  EEE, d-MMM-yyyy  to  dd-MM-yyyy (to send date as parameter in webservice)
                change_DateFormat();
//            	 call webservice
                getDoctor_Appointments_Portal();
            }
        });

        // Display the dialog
        dialog.show();

    }

    private void close_Btn_Click() {
        // TODO Auto-generated method stub
        mClose_Btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Appointments.this, DoctorsName_List.class);
                startActivity(i);
                finish();
                return;
            }
        });
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {


            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private void getDoctor_Appointments_Portal() {
        // TODO Auto-generated method stub
        if (isInternetOn() == true) {
            StringBuffer sb_DocAppts = new StringBuffer();
            sb_DocAppts.append(Constants.URL + "GetDocAppointments" + "?" + "DoctorId=" + doctor_ID_Global + "&HospitalId=" + hospital_ID_Global + "&DateofAppointment=" + mCurrent_Date);
            url_GetDocAppointments = sb_DocAppts.toString();
            System.out.println("GetDocAppointments_url---" + url_GetDocAppointments);
            new GetDocAppointments().execute(url_GetDocAppointments);


        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void getData() {


        // TODO Auto-generated method stub
        int doctorID = getIntent().getIntExtra("doctorId_Send_DocClass_to_AppointClass", 0);
        String doctorName = getIntent().getStringExtra("doctorName_Send_DocClass_to_AppointClass");
        int hospitalID = getIntent().getIntExtra("hospitalId_Send_DocClass_to_AppointClass", 0);


//		get EMP_Id from MainActivity class
        SharedPreferences emp_Id_pref = getSharedPreferences("EMP_Id_MyPrefs", Context.MODE_PRIVATE);
        mEMP_Id_Global = emp_Id_pref.getInt("EMP_Id_KEY_PREF", 0);

//		get UserType from MainActivity class
        SharedPreferences userType_pref = getSharedPreferences("userType_MyPrefs", Context.MODE_PRIVATE);
        mUserType = userType_pref.getString("userType_KEY_PREF", "");

        mDoctorName_Txv.setText(doctorName);
        doctor_ID_Global = doctorID;
        doctor_Name_Global = doctorName;
        hospital_ID_Global = hospitalID;


    }

    private void getIds() {
        // TODO Auto-generated method stub

        mDoctorName_Txv = (TextView) findViewById(R.id.doctorname_txv_header_tdayAppointments);
        mPrevous_date_Img = (ImageView) findViewById(R.id.prev_date_tdayAppointments);
        mRefersh_Img = (ImageView) findViewById(R.id.refresh_img_tdayAppointments);
        mCurt_date_Txv = (TextView) findViewById(R.id.currentdate_tdayApointments);
        mNext_date_Img = (ImageView) findViewById(R.id.next_date_tdayAppointments);
        mListview = (ListView) findViewById(R.id.appointmentslistview);

        mClose_Btn = (Button) findViewById(R.id.close_appointments);
    }


    public void InsertPatRecord() {

        // sb_insert.append(Constants.URL + "Web_InsertPatRecord" + "?" + "DoctorId=" + doctor_ID_Global + "&HospitalId=" + hospital_ID_Global + "&EmpId=" + mEMP_Id_Global + "&TokenNo=" + 0 + "&AppDate=" + Time + "&MobileNo=" + Mble + "&MrNo=" + MR_No + "&Name=" + names + "&Age=" + age + "&Gender=" + XXX + "&DiagnosisType=" + Diag_Id + "&ReviewDate=" + R_Date + "&ConsultTypeId=" + ID + "&ConsultationFee=" + Fee + "&PharmacyAmount=" + P_amount);


       /* Time_Date_Data();
        formate_fromTime_withDate();

*/

        Time_Date_Data();
        formate_fromTime_withDate();

        String P_amount = Phar_Charge.getText().toString();

        String age = Age.getText().toString();

        String names = name_edt.getText().toString();


        String Time = from_DateTime_Global;

        String Fee = Cons_Fee.getText().toString();

        String Mble = MobileNum.getText().toString();

        String gender = sk.getText().toString();

        String ID = Cons_Id.getText().toString();


        String MR_No = MR_Number.getText().toString();


        String Diag_Id = Diag_id.getText().toString();


        if (isInternetOn() == true) {


            JSONArray jA1 = new JSONArray();

            String patient_Name_edt_Global1 = Time.replace(" ", "%20");
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put(mEMP_Id_Global);
            jA1.put(0);
            jA1.put(patient_Name_edt_Global1);
            jA1.put(Mble);
            jA1.put(MR_No);
            jA1.put(names);
            jA1.put(age);
            jA1.put(gender);
            jA1.put(Diag_Id);
            //jA1.put(correct_Date);
            // jA1.put(ID);
            //jA1.put(Fee);
            // jA1.put(P_amount);
            //jA1.put(Appointment_master_id);
            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();

            sb_Book_new.append(Constants.URL + "Web_InsertPatRecord" + "?" + "Data=" + send_BookData_New);
            url_InsertPatRecord = sb_Book_new.toString();
            System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


            new InsertRecord().execute(url_InsertPatRecord);
        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void Time_Date_Data() {

        //ArrayList<Appointments_Model> new_list_DET = mAdapter.new_list_DET;


        from_Time_Global = K;
    }

    public void book_Appointment_New_Patient_New() {
        // TODO Auto-generated method stub
        edittextDataFM_Appointments_Adapter();
//	modifing from_time (from_time in 24h and with date)
        formate_fromTime_withDate();
        if (isInternetOn() == true) {
            JSONArray jA1 = new JSONArray();
            String patient_Name_edt_Global1 = patient_Name_edt_Global.replace(" ", "%20");
            jA1.put(patient_Name_edt_Global1);
            jA1.put(mobile_Number_edt_Global);
            String from_DateTime_Global1 = from_DateTime_Global.replace(" ", "%20");
            jA1.put(from_DateTime_Global1);
            jA1.put(token_Number_Global);
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put("NEW");
            jA1.put(mEMP_Id_Global);
            //jA1.put(Appointment_master_id);
            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();
            sb_Book_new.append(Constants.URL + "BookAppointment" + "?" + "Data=" + send_BookData_New);
            url_BookAppointment_New = sb_Book_new.toString();
            System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);

            new Send_BookAppointment().execute(url_BookAppointment_New);

        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }

    public void Diagnosis_Tpes() {


        StringBuffer sb_Diagnosis = new StringBuffer();
        sb_Diagnosis.append(Constants.URL + "Web_GetDiagnosis" + "?" + "HospitalId=" + 0);
        url_GetDocDiagnosis = sb_Diagnosis.toString();
        System.out.println("GetDocAppointments_url---" + url_GetDocDiagnosis);
        new GetDiagnosis().execute(url_GetDocDiagnosis);


    }

    public void check_MobileNo_getPatients_New() {
        edittextDataFM_Appointments_Adapter_New();
        StringBuffer sb_CheckMobile = new StringBuffer();
        sb_CheckMobile.append(Constants.URL + "GetbyMobileNo" + "?" + "MobileNo=" + MOBILE + "&HospitalId=" + hospital_ID_Global);
        url_CheckMobileNo_GetbyMobileNo = sb_CheckMobile.toString();
        System.out.println("Checking_Mobile_Number" + url_CheckMobileNo_GetbyMobileNo);
        new GetPatients_CheckMobileNo_New().execute(url_CheckMobileNo_GetbyMobileNo);

    }


    private void edittextDataFM_Appointments_Adapter_New() {


        // TODO Auto-generated method stub

        ArrayList<Appointments_Model> new_list = mAdapter.new_list;
        for (int i = 0; i < new_list.size(); i++) {
            Appointments_Model state = new_list.get(i);
            patient_Name_edt_Global = state.getPatient_Name();
            mobile_Number_edt_Global = state.getMobile_Numb();
            from_Time_Global = state.getFrom_Time();
            to_Time_Global = state.getTo_Time();
            token_Number_Global = state.getToken_Number();

        }
        MOBILE = MobileNum.getText().toString();


    }

    public void InsertPatRecord1() {
/*

        Time_Date_Data();
        formate_fromTime_withDate();
*/


        String MR_No = MR_Number.getText().toString();

        String P_amount = Phar_Charge.getText().toString();

        String age = Age.getText().toString();

        String names = name_edt.getText().toString();

        String Time = from_DateTime_Global;

        String Fee = Cons_Fee.getText().toString();

        String Mble = MobileNum.getText().toString();

        String gender = sk.getText().toString();

        String ID = Cons_Id.getText().toString();


        // String Per_MR_Number = MR_Number.getText().toString();


        JSONArray jA1 = new JSONArray();


        jA1.put(MRNO);
        jA1.put(doctor_ID_Global);
        jA1.put(hospital_ID_Global);
        jA1.put(mEMP_Id_Global);
        jA1.put(Fee);
        jA1.put(P_amount);
        jA1.put(Mble);
        jA1.put(correct_Date);
        jA1.put(ID);
        jA1.put(names);


        String send_BookData_New = jA1.toString();
        StringBuffer sb_Book_new = new StringBuffer();

        sb_Book_new.append(Constants.URL + "Web_InsertPatRecord1" + "?" + "Data=" + send_BookData_New);
        url_InsertPatRecord = sb_Book_new.toString();
        System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


        new InsertRecord1().execute(url_InsertPatRecord);


    }

    public void InsertPatRecord2() {


        Time_Date_Data();
        formate_fromTime_withDate();


        String MR_No = MR_Number.getText().toString();

        String P_amount = Phar_Charge.getText().toString();


        String Date_1 = calender.getText().toString();

        String age = Age.getText().toString();

        String names = name_edt.getText().toString();

        String Time = from_DateTime_Global;

        String Fee = Cons_Fee.getText().toString();

        String Mble = MobileNum.getText().toString();

        String gender = sk.getText().toString();

        String ID = Cons_Id.getText().toString();


        // String Per_MR_Number = MR_Number.getText().toString();


        JSONArray jA1 = new JSONArray();


        jA1.put(MR_No);
        jA1.put(doctor_ID_Global);
        jA1.put(hospital_ID_Global);
        jA1.put(mEMP_Id_Global);
        jA1.put(Fee);
        jA1.put(P_amount);
        jA1.put(Mble);
        jA1.put(Date_1);
        jA1.put(ID);
        jA1.put(names);


        String send_BookData_New = jA1.toString();
        StringBuffer sb_Book_new = new StringBuffer();

        sb_Book_new.append(Constants.URL + "Web_InsertPatRecord1" + "?" + "Data=" + send_BookData_New);
        url_InsertPatRecord = sb_Book_new.toString();
        System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


        new InsertRecord1().execute(url_InsertPatRecord);


    }

    public void Diagnosis_Tpes1() {


        StringBuffer sb_Diagnosis = new StringBuffer();
        sb_Diagnosis.append(Constants.URL + "Web_GetDiagnosis" + "?" + "HospitalId=" + 0);
        url_GetDocDiagnosis = sb_Diagnosis.toString();
        System.out.println("GetDocAppointments_url---" + url_GetDocDiagnosis);
        new GetDiagnosis1().execute(url_GetDocDiagnosis);


    }

    public void InsertPatRecord_NotArrived() {


        Time_Date_Data1();
        formate_fromTime_withDate();

        String P_amount = Phar_Charge_NotArrived.getText().toString();

        String age = Age_NotArrived.getText().toString();

        String names = name_edt_NotArrived.getText().toString();


        String Time = from_DateTime_Global;


        String Fee = Cons_Fee_NotArrived.getText().toString();

        String Mble = MobileNum_NotArrived.getText().toString();

        String gender = Gender_NotArrived.getText().toString();

        String ID = Cons_Id1.getText().toString();

        String MR_No = MR_NOT_ARRIVED.getText().toString();


        String Diag_Id = Diag_id1.getText().toString();


        if (isInternetOn() == true) {


            Time_Date_Data();
            formate_fromTime_withDate();


            JSONArray jA1 = new JSONArray();

            String patient_Name_edt_Global1 = Time.replace(" ", "%20");
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put(mEMP_Id_Global);
            jA1.put(0);
            jA1.put(patient_Name_edt_Global1);
            jA1.put(Mble);
            jA1.put(MR_No);
            jA1.put(names);
            jA1.put(age);
            jA1.put(gender);
            jA1.put(Diag_Id);
            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();

            sb_Book_new.append(Constants.URL + "Web_InsertPatRecord" + "?" + "Data=" + send_BookData_New);
            url_InsertPatRecord = sb_Book_new.toString();
            System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


            new InsertRecord_New_NotArrived().execute(url_InsertPatRecord);
        }

    }

    private void Time_Date_Data1() {


        from_Time_Global = K;


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Appointments Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void InsertPat_Not_Paid() {


        Time_Date_Data1();
        formate_fromTime_withDate();

        String P_amount = Phar_Charge_Not_Paid.getText().toString();

        String age = Age_Not_Paid.getText().toString();

        String names = name_edt_Not_Paid.getText().toString();


        String Time = from_DateTime_Global;


        String Fee = Cons_Fee_NotPaid.getText().toString();

        String Mble = mobile_Not_Paid.getText().toString();

        String gender = Gender_NotPaid.getText().toString();

        String ID = Cons_Id2.getText().toString();

        String MR_No = MR_NOT_PAID.getText().toString();


        String Diag_Id = Diag_id2.getText().toString();


        if (isInternetOn() == true) {


          /*  Time_Date_Data();
            formate_fromTime_withDate();
*/

            JSONArray jA1 = new JSONArray();

            String patient_Name_edt_Global1 = Time.replace(" ", "%20");
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put(mEMP_Id_Global);
            jA1.put(0);
            jA1.put(patient_Name_edt_Global1);
            jA1.put(Mble);
            jA1.put(MR_No);
            jA1.put(names);
            jA1.put(age);
            jA1.put(gender);
            jA1.put(Diag_Id);
            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();

            sb_Book_new.append(Constants.URL + "Web_InsertPatRecord" + "?" + "Data=" + send_BookData_New);
            url_InsertPatRecord = sb_Book_new.toString();
            System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


            new InsertRecord_New_Not_Paid().execute(url_InsertPatRecord);
        }


    }

    public void Diagnosis_Tpes2() {

        StringBuffer sb_Diagnosis = new StringBuffer();
        sb_Diagnosis.append(Constants.URL + "Web_GetDiagnosis" + "?" + "HospitalId=" + 0);
        url_GetDocDiagnosis = sb_Diagnosis.toString();
        System.out.println("GetDocAppointments_url---" + url_GetDocDiagnosis);
        new GetDiagnosis2().execute(url_GetDocDiagnosis);


    }

    public void getDoctor_Consultaion_AmtTypes1() {

        if (isInternetOn() == true) {
            ArrayList<Appointments_Model> payData_list = mAdapter.pay_list;

            for (int i = 0; i < payData_list.size(); i++) {
                Appointments_Model state = payData_list.get(i);
                mMRNumber_Pay = state.getMR_Number();
                if (mMRNumber_Pay != null && !mMRNumber_Pay.isEmpty()) {
                    break;
                }
            }
            if (mUserType.equals("DOCTOR")) {
                StringBuffer sb_Consult_Amts = new StringBuffer();
                sb_Consult_Amts.append(Constants.URL + "GetDoctorConsultationTypes?DoctorId=" + doctor_ID_Global + "&HospId=" + hospital_ID_Global);
                String url_Consultation_Amt_types = sb_Consult_Amts.toString();
                System.out.println("Get_Consult_Amt_Types---" + url_Consultation_Amt_types);
                new GetDoctorConsultationTypes1().execute(url_Consultation_Amt_types);
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

    public void getDoctor_Consultaion_AmtTypes2() {

        if (isInternetOn() == true) {
            ArrayList<Appointments_Model> payData_list = mAdapter.pay_list;

            for (int i = 0; i < payData_list.size(); i++) {
                Appointments_Model state = payData_list.get(i);
                mMRNumber_Pay = state.getMR_Number();
                if (mMRNumber_Pay != null && !mMRNumber_Pay.isEmpty()) {
                    break;
                }
            }
            if (mUserType.equals("DOCTOR")) {
                StringBuffer sb_Consult_Amts = new StringBuffer();
                sb_Consult_Amts.append(Constants.URL + "GetDoctorConsultationTypes?DoctorId=" + doctor_ID_Global + "&HospId=" + hospital_ID_Global);
                String url_Consultation_Amt_types = sb_Consult_Amts.toString();
                System.out.println("Get_Consult_Amt_Types---" + url_Consultation_Amt_types);
                new GetDoctorConsultationTypes2().execute(url_Consultation_Amt_types);
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


    private class GetDocAppointments extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDocAppts = new ProgressDialog(Appointments.this);
            pDialog_GetDocAppts.setMessage("Please Wait.");
            pDialog_GetDocAppts.setCancelable(false);
            pDialog_GetDocAppts.show();
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

                            String from_Time = jObject1.getString("TIME");
                            String to_Time = jObject1.getString("TOTIME");
                            String patient_name = jObject1.getString("PATNAME");
                            String book = jObject1.getString("BOOK");
                            String arrived = jObject1.getString("ARR");
                            String pay_consultation = jObject1.getString("PAY");
                            String pay_pharmacy = jObject1.getString("PHAR");
                            String Mobile = jObject1.getString("MOBILE");
                            int App_Master_Id = 0;
                            final int token_number = i + 1;
                            System.out.println("token" + token_number);
                            Appointments_Model appointments_model = new Appointments_Model(from_Time, to_Time, patient_name,
                                    book, arrived, pay_consultation, pay_pharmacy, token_number, App_Master_Id, Mobile);
                            appointments_List.add(appointments_model);

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here

                                ;

                                Toast.makeText(getApplicationContext(), "Appointments collected", Toast.LENGTH_SHORT).show();

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
            if (pDialog_GetDocAppts.isShowing()) ;
            pDialog_GetDocAppts.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            String mAppointment_Date = mCurt_date_Txv.getText().toString();
            mAdapter = new Appointments_Adapter(Appointments.this, R.layout.appointments_notbook_row, appointments_List, mAppointment_Date, mUserType);

            mListview.setAdapter(mAdapter);
        } // onPostExecute() end
    } // ProcessJSON class end

    private class GetPatients_CheckMobileNo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetPatients_CheckMobileNo = new ProgressDialog(Appointments.this);
            pDialog_GetPatients_CheckMobileNo.setMessage("Please Wait.");
            pDialog_GetPatients_CheckMobileNo.setCancelable(false);
            pDialog_GetPatients_CheckMobileNo.show();
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

                        new_appointments_List.clear();

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);

                            mMR_Number = jArrray1.getString(0);
                            mNewPatient_Name = jArrray1.getString(1);

                            Appointments_Model appointments_model = new Appointments_Model(mNewPatient_Name, mMR_Number);
                            new_appointments_List.add(appointments_model);

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
                                new_appointments_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetPatients_CheckMobileNo.isShowing()) ;
            pDialog_GetPatients_CheckMobileNo.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            newPatient_List_Dilaog();

        } // onPostExecute() end

    } // ProcessJSON class end

    public void newPatient_List_Dilaog() {
        // TODO Auto-generated method stub
        // Create custom dialog object

        dialog_BookAppointment = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_BookAppointment.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_BookAppointment.setContentView(R.layout.new_appointments_dialog);

        TextView title_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.title_new_appointments_dialog);
        ListView listview_Dialog = (ListView) dialog_BookAppointment.findViewById(R.id.new_appointmentslistview);
        TextView message_title_txv_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.message_title_txv_new_appointments_dialog);
        TextView message_txv_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.message_txv_new_appointments_dialog);
        Button cancel_btn = (Button) dialog_BookAppointment.findViewById(R.id.cancel_btn_new_appointments_dialog);
        Button confirm_btn = (Button) dialog_BookAppointment.findViewById(R.id.confirm_btn_new_appointments_dialog);
        Button create_as_new_btn = (Button) dialog_BookAppointment.findViewById(R.id.create_as_new_btn_new_appointments_dialog);
        LinearLayout rl1 = (LinearLayout) dialog_BookAppointment.findViewById(R.id.hide_show_message_layout_new_appointments_dialog);
        if (new_appointments_List.size() == 0) {
            title_Dialog.setText("");
            message_title_txv_Dialog.setText("Do you want to book at " + from_Time_Global + "-" + to_Time_Global);
            message_txv_Dialog.setText("Patient Name    : " + patient_Name_edt_Global + "\n" + "Mobile Number : " + mobile_Number_edt_Global);
            rl1.setVisibility(View.VISIBLE);
            create_as_new_btn.setVisibility(View.GONE);
        } else {
            title_Dialog.setText("Existing patients");
            rl1.setVisibility(View.GONE);
            create_as_new_btn.setVisibility(View.VISIBLE);
        }

        New_AppointmentsDialog_Adapter mAdapter = new
                New_AppointmentsDialog_Adapter(Appointments.this, R.layout.new_appointments_dialog_row, new_appointments_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);
                selected_NewPatient_Name = _model.getNew_PatientName();
                mMR_Number_selected_NewPatient = _model.getMR_Number();
                view.setSelected(true);
                System.out.println("selected patient" + selected_NewPatient_Name);


            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_BookAppointment.dismiss();
                new_appointments_List.clear();
            }
        });
        create_as_new_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                book_Appointment_New_Patient();
                // Close the dialog
                dialog_BookAppointment.dismiss();
                new_appointments_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                name_edt.setText(selected_NewPatient_Name);

                dialog_BookAppointment.dismiss();*/


//			modifing from_time (from_time in 24h and with date)
                formate_fromTime_withDate();


                if (new_appointments_List.size() == 0) {
                    book_Appointment_New_Patient();
                    // Close the dialog
                    dialog_BookAppointment.dismiss();

                } else {
                    if (selected_NewPatient_Name != null && !selected_NewPatient_Name.isEmpty()) {
                        //					  book appointment for selected_patient from listview
                        book_Appointment_SelectedPatient();

                        // Close the dialog
                        dialog_BookAppointment.dismiss();
                        new_appointments_List.clear();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select patient", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });

        // Display the dialog
        dialog_BookAppointment.show();
    }

    public void book_Appointment_New_Patient() {
        // TODO Auto-generated method stub
        edittextDataFM_Appointments_Adapter();
//	modifing from_time (from_time in 24h and with date)
        formate_fromTime_withDate();
        if (isInternetOn() == true) {
            JSONArray jA1 = new JSONArray();
            String patient_Name_edt_Global1 = patient_Name_edt_Global.replace(" ", "%20");
            jA1.put(patient_Name_edt_Global1);
            jA1.put(mobile_Number_edt_Global);
            String from_DateTime_Global1 = from_DateTime_Global.replace(" ", "%20");
            jA1.put(from_DateTime_Global1);
            jA1.put(token_Number_Global);
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put("NEW");
            jA1.put(mEMP_Id_Global);
            //jA1.put(Appointment_master_id);
            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();
            sb_Book_new.append(Constants.URL + "BookAppointment" + "?" + "Data=" + send_BookData_New);
            url_BookAppointment_New = sb_Book_new.toString();
            System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);

            new Send_BookAppointment().execute(url_BookAppointment_New);

        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void book_Appointment_SelectedPatient() {
        // TODO Auto-generated method stub
        if (isInternetOn() == true) {
            JSONArray jA1 = new JSONArray();
            String patient_Name_edt_Global1 = selected_NewPatient_Name.replace(" ", "%20");
            jA1.put(patient_Name_edt_Global1);
            jA1.put(mobile_Number_edt_Global);
            String from_DateTime_Global1 = from_DateTime_Global.replace(" ", "%20");
            jA1.put(from_DateTime_Global1);
            jA1.put(token_Number_Global);
            jA1.put(doctor_ID_Global);
            jA1.put(hospital_ID_Global);
            jA1.put(mMR_Number_selected_NewPatient);
            jA1.put(mEMP_Id_Global);
            jA1.put(Appointment_master_id);

            String send_BookData_New = jA1.toString();
            StringBuffer sb_Book_new = new StringBuffer();
            sb_Book_new.append(Constants.URL + "BookAppointment" + "?" + "Data=" + send_BookData_New);
            url_BookAppointment_New = sb_Book_new.toString();
            System.out.println("send to server to book appointment for selectedPatient fm Listview>>>>>>>>" + url_BookAppointment_New);

            new Send_BookAppointment().execute(url_BookAppointment_New);


        } else {
            // Internet connection is not present
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void formate_fromTime_withDate() {
        // TODO Auto-generated method stub
        SimpleDateFormat correctFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = parseFormat.parse(from_Time_Global);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String correct_Time = (correctFormat.format(date)) + ":00";


        String book_date_input = mCurt_date_Txv.getText().toString();
        SimpleDateFormat dateFormat_input = new SimpleDateFormat("EEE, d-MMM-yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat_input.parse(book_date_input);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat_output = new SimpleDateFormat("yyyy-MM-dd");
        correct_Date = dateFormat_output.format(myDate);

        StringBuilder sb_fmDateTime = new StringBuilder();
        from_DateTime_Global = (sb_fmDateTime.append(correct_Date + " " + correct_Time)).toString();
        System.out.println("formated from_datetime" + from_DateTime_Global);
    }

    private class Send_BookAppointment extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_BookAppointment = new ProgressDialog(Appointments.this);
            pDialog_BookAppointment.setMessage("Please Wait.");
            pDialog_BookAppointment.setCancelable(false);
            pDialog_BookAppointment.show();
        }

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
            System.out.println("json string>>>>>>>>" + stream);
            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());


                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {

                            String syn_status_reponse = jArray.getString(0);

                            int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);


                            if (syn_status_reponse_result == 1) {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "Appointment Booked", Toast.LENGTH_SHORT).show();
                                // Close the dialog
//                     			  dialog_BookAppointment.dismiss();
                                finish();
                                startActivity(getIntent());
                            } else {
                                // display toast here
                                Toast.makeText(getApplicationContext(), " Appointment not Booked", Toast.LENGTH_SHORT).show();
                            }

                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_BookAppointment.isShowing()) ;
            pDialog_BookAppointment.dismiss();

        } // onPostExecute() end
    } // ProcessJSON class end

    private class PatientArrived_Service extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_PatArrived;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_PatArrived = new ProgressDialog(Appointments.this);
            pDialog_PatArrived.setMessage("Please Wait.");
            pDialog_PatArrived.setCancelable(false);
            pDialog_PatArrived.show();
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

            System.out.println("json string arrived>>>>>>>>" + stream);
            if (stream != null) {
                try {
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        String syn_status_reponse = jArray.getString(0);
                        int syn_status_reponse_result = Integer.parseInt(syn_status_reponse);


                        if (syn_status_reponse_result == 1) {// display toast here
                            Toast.makeText(getApplicationContext(), "Patient Arrived", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            // display toast here
                            Toast.makeText(getApplicationContext(), " Patient not Arrived", Toast.LENGTH_SHORT).show();
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
            if (pDialog_PatArrived.isShowing()) ;
            pDialog_PatArrived.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end

    private class GetDoctorConsultationTypes extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDoc_Consult_AmtTypes = new ProgressDialog(Appointments.this);
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

    public void doc_ConsultationTypes_List_Dilaog() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.consultation_amt_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_consultation_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.consultation_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_consultation_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_consultation_amt_dialog);


        if (doc_ConsultationType_Amt_List.size() == 0) {
            title_Dialog.setText("Consultation types does not exists");

        } else {
            title_Dialog.setText("Consultation types");

        }

        Doc_ConsultationType_Amt_Adapter mAdapter = new
                Doc_ConsultationType_Amt_Adapter(Appointments.this, R.layout.doc_consultation_amt_type_dialog_row, doc_ConsultationType_Amt_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);

                selected_ConsultationType_ID = _model.getConsultationType_ID();
                selected_ConsultationType = _model.getConsultation_Type();
                selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();

                int i1 = selected_ConsultationType_ID;


                Cons_Fee.setText(selected_Consultation_TypeAmt);


                Cons_Id.setText(String.valueOf(i1));


                EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                e_etxt_Et.setText(selected_Consultation_TypeAmt);


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_ConsultationType_Amt_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doc_ConsultationType_Amt_List.size() == 0) {
                    // Close the dialog
                    dialog_ConsultAmt.dismiss();

                } else {

                    EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                    selected_Consultation_TypeAmt = e_etxt_Et.getText().toString();


                    if (selected_Consultation_TypeAmt != null && !selected_Consultation_TypeAmt.isEmpty()) {
                        //					  Send Selected Consultation Fee  from listview


                        Cons_Fee.setText(selected_Consultation_TypeAmt);

                        // Cons_Id.setText(selected_ConsultationType_ID_S);


                        //     send_DefineConsultationFee();

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

    protected void send_DefineConsultationFee() {
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
    }

    private class Send_DefineConsultationFeeService extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_DefineConsultFee;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_DefineConsultFee = new ProgressDialog(Appointments.this);
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
                            //Toast.makeText(getApplicationContext(), "Consultation fee not defined", Toast.LENGTH_SHORT).show();
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

    private class GetDefinedFee_User extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_GetDefinedFee;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDefinedFee = new ProgressDialog(Appointments.this);
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
        final Dialog dialog_payment = new Dialog(Appointments.this);
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

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_payment.dismiss();

            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
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
                    new Send_PaymentDone().execute(url_Payment);
                    dialog_payment.dismiss();
                } else {
                    // Internet connection is not present
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                }

            }

        });

        // Display the dialog
        dialog_payment.show();
    }

    private class Send_PaymentDone extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog_PayDone;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_PayDone = new ProgressDialog(Appointments.this);
            pDialog_PayDone.setMessage("Please Wait.");
            pDialog_PayDone.setCancelable(false);
            pDialog_PayDone.show();
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
            System.out.println("json string Payment_Done>>>>>>>>" + stream);
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
                            Toast.makeText(getApplicationContext(), "Payment done successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            // display toast here
                            Toast.makeText(getApplicationContext(), "Payment not done", Toast.LENGTH_SHORT).show();
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
            if (pDialog_PayDone.isShowing()) ;
            pDialog_PayDone.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

        } // onPostExecute() end
    } // ProcessJSON class end


    private class GetDiagnosis extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDiagnosis = new ProgressDialog(Appointments.this);
            pDialog_GetDiagnosis.setMessage("Please Wait.");
            pDialog_GetDiagnosis.setCancelable(false);
            pDialog_GetDiagnosis.show();
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
                    doc_Diagnosis_List.clear();
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject1 = jArray.getJSONObject(i);
                            int mDiagnosis_ID = jObject1.getInt("DiagnosisId");
                            String mDiagnosis_Type = jObject1.getString("DiagnosisName");





                      /*  for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);
                            String mDiagnosis_ID_response = jArrray1.getString(0);
                            String mDiagnosis_Type = jArrray1.getString(1);
                           // String mConsultation_Amt = jArrray1.getString(2);
                            int mDiagnosis_ID = Integer.parseInt(mDiagnosis_ID_response);
*/
                            Diagnosis_Model xsai = new Diagnosis_Model(mDiagnosis_ID, mDiagnosis_Type);
                            doc_Diagnosis_List.add(xsai);
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
                                doc_Diagnosis_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetDiagnosis.isShowing()) ;
            pDialog_GetDiagnosis.dismiss();

            doc_DIagnosisTypes_List_Dilaog();

            /**
             * Updating parsed JSON data into ListView
             * */
            //doc_ConsultationTypes_List_Dilaog();

        } // onPostExecute() end

    } // ProcessJSON class end

    private void doc_DIagnosisTypes_List_Dilaog() {

        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.diagnosislist_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_diagnosis_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.diagnosis_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_diagnosis_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_diagnosis_amt_dialog);


        if (doc_Diagnosis_List.size() == 0) {
            title_Dialog.setText("Diagnosis types does not exists");

        } else {
            title_Dialog.setText("Diagnosis types");

        }

        Doc_DiagnosisType_Amt_Adapter mAdapter = new Doc_DiagnosisType_Amt_Adapter(Appointments.this, R.layout.doc_diagnosis_amt_type_dialog_row, doc_Diagnosis_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Diagnosis_Model _model = (Diagnosis_Model) parent.getItemAtPosition(position);


                selected_DiagnosisType_ID = _model.getDiagnosis_Type();
                selected_DiagnosisType = _model.getDiagnosisType_ID();

                Diagnosis_Types.setText(selected_DiagnosisType);

                int i2 = selected_DiagnosisType_ID;
                Diag_id.setText(String.valueOf(i2));
                dialog_ConsultAmt.dismiss();

                //  selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();



                /*
                selected_DiagnosisType = _model.getDiagnosis_Type();*/
                //selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_Diagnosis_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Diagnosis_Types.setText(selected_DiagnosisType);

                dialog_ConsultAmt.dismiss();


            }


        });

        // Display the dialog
        dialog_ConsultAmt.show();
    }


    private class GetPatients_CheckMobileNo_New extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetPatients_CheckMobileNo = new ProgressDialog(Appointments.this);
            pDialog_GetPatients_CheckMobileNo.setMessage("Please Wait.");
            pDialog_GetPatients_CheckMobileNo.setCancelable(false);
            pDialog_GetPatients_CheckMobileNo.show();
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

                        new_appointments_List.clear();

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);

                            mMR_Number = jArrray1.getString(0);
                            mNewPatient_Name = jArrray1.getString(1);

                            Appointments_Model appointments_model = new Appointments_Model(mNewPatient_Name, mMR_Number);
                            new_appointments_List.add(appointments_model);

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
                                new_appointments_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetPatients_CheckMobileNo.isShowing()) ;
            pDialog_GetPatients_CheckMobileNo.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            newPatient_List_Dilaog1();

        } // onPostExecute() end
    }

    private void newPatient_List_Dilaog1() {

        // TODO Auto-generated method stub
        // Create custom dialog object

        dialog_BookAppointment = new Dialog(Appointments.this);
        // hide to default title for Dialog

        if (new_appointments_List.size() == 0) {


            MR_Number.setText("NEW");


        } else {
            dialog_BookAppointment.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog_BookAppointment.setContentView(R.layout.new_appointments_dialog1);

            TextView title_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.title_new_appointments_dialog);
            ListView listview_Dialog = (ListView) dialog_BookAppointment.findViewById(R.id.new_appointmentslistview);
            TextView message_title_txv_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.message_title_txv_new_appointments_dialog);
            TextView message_txv_Dialog = (TextView) dialog_BookAppointment.findViewById(R.id.message_txv_new_appointments_dialog);
            Button cancel_btn = (Button) dialog_BookAppointment.findViewById(R.id.cancel_btn_new_appointments_dialog);
            Button confirm_btn = (Button) dialog_BookAppointment.findViewById(R.id.confirm_btn_new_appointments_dialog);
            Button create_as_new_btn = (Button) dialog_BookAppointment.findViewById(R.id.create_as_new_btn_new_appointments_dialog);
            LinearLayout rl1 = (LinearLayout) dialog_BookAppointment.findViewById(R.id.hide_show_message_layout_new_appointments_dialog);
            title_Dialog.setText("Existing patients");
            rl1.setVisibility(View.GONE);
            create_as_new_btn.setVisibility(View.VISIBLE);


            New_AppointmentsDialog_Adapter mAdapter = new
                    New_AppointmentsDialog_Adapter(Appointments.this, R.layout.new_appointments_dialog_row, new_appointments_List);

            listview_Dialog.setAdapter(mAdapter);
            listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);
                    selected_NewPatient_Name = _model.getNew_PatientName();
                    mMR_Number_selected_NewPatient = _model.getMR_Number();
                    view.setSelected(true);
                    System.out.println("selected patient" + selected_NewPatient_Name);


                }
            });

            cancel_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_BookAppointment.dismiss();
                    new_appointments_List.clear();
                }
            });
            create_as_new_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog_BookAppointment.dismiss();
                    new_appointments_List.clear();
                }
            });


            confirm_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    name_edt.setText(selected_NewPatient_Name);

                    MR_Number.setText(mMR_Number_selected_NewPatient);

                    Age.requestFocus();

                    dialog_BookAppointment.dismiss();


//			modifing from_time (from_time in 24h and with date)
                    //formate_fromTime_withDate();



               /* if (new_appointments_List.size() == 0) {
                    book_Appointment_New_Patient();
                    // Close the dialog
                    dialog_BookAppointment.dismiss();

                } else {
                    if (selected_NewPatient_Name != null && !selected_NewPatient_Name.isEmpty()) {
                        //					  book appointment for selected_patient from listview
                            book_Appointment_SelectedPatient();

                        // Close the dialog
                        dialog_BookAppointment.dismiss();
                        new_appointments_List.clear();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select patient", Toast.LENGTH_SHORT).show();
                    }*/
                }


            });

            // Display the dialog
            dialog_BookAppointment.show();
        }


    }

    public class InsertRecord extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_Insert = new ProgressDialog(Appointments.this);
            pDialog_Insert.setMessage("Please Wait.");
            pDialog_Insert.setCancelable(false);
            pDialog_Insert.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler1 hh = new HTTPDataHandler1();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);


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

                            MRNO = jObject1.getString("MRNO");

                            InsertPatRecord1();


                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_Insert.isShowing()) ;
            pDialog_Insert.dismiss();


            //  getDoctor_Appointments_Portal();

        } // onPostExecute() end
    } // ProcessJSON class end{

    private class InsertRecord1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_Insert1 = new ProgressDialog(Appointments.this);
            pDialog_Insert1.setMessage("Please Wait.");
            pDialog_Insert1.setCancelable(false);
            pDialog_Insert1.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler1 hh = new HTTPDataHandler1();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);


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
/*
                            MRNO = jObject1.getString("MRNO");

                            InsertPatRecord1();*/


                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_Insert1.isShowing()) ;
            pDialog_Insert1.dismiss();

            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            getDoctor_Appointments_Portal();

            // getDoctor_Appointments_Portal();


            //  getDoctor_Appointments_Portal();

        } // onPostExecute() end
    } // ProcessJSON class end{

    private class GetDiagnosis1 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDiagnosis = new ProgressDialog(Appointments.this);
            pDialog_GetDiagnosis.setMessage("Please Wait.");
            pDialog_GetDiagnosis.setCancelable(false);
            pDialog_GetDiagnosis.show();
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
                    doc_Diagnosis_List.clear();
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject1 = jArray.getJSONObject(i);
                            int mDiagnosis_ID = jObject1.getInt("DiagnosisId");
                            String mDiagnosis_Type = jObject1.getString("DiagnosisName");





                      /*  for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);
                            String mDiagnosis_ID_response = jArrray1.getString(0);
                            String mDiagnosis_Type = jArrray1.getString(1);
                           // String mConsultation_Amt = jArrray1.getString(2);
                            int mDiagnosis_ID = Integer.parseInt(mDiagnosis_ID_response);
*/
                            Diagnosis_Model xsai = new Diagnosis_Model(mDiagnosis_ID, mDiagnosis_Type);
                            doc_Diagnosis_List.add(xsai);
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
                                doc_Diagnosis_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetDiagnosis.isShowing()) ;
            pDialog_GetDiagnosis.dismiss();

            doc_DIagnosisTypes_List_Dilaog1();

            /**
             * Updating parsed JSON data into ListView
             * */
            //doc_ConsultationTypes_List_Dilaog();

        } // onPostExecute() end
    }

    private void doc_DIagnosisTypes_List_Dilaog1() {

        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.diagnosislist_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_diagnosis_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.diagnosis_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_diagnosis_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_diagnosis_amt_dialog);


        if (doc_Diagnosis_List.size() == 0) {
            title_Dialog.setText("Diagnosis types does not exists");

        } else {
            title_Dialog.setText("Diagnosis types");

        }

        Doc_DiagnosisType_Amt_Adapter mAdapter = new Doc_DiagnosisType_Amt_Adapter(Appointments.this, R.layout.doc_diagnosis_amt_type_dialog_row, doc_Diagnosis_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Diagnosis_Model _model = (Diagnosis_Model) parent.getItemAtPosition(position);


                selected_DiagnosisType_ID = _model.getDiagnosis_Type();
                selected_DiagnosisType = _model.getDiagnosisType_ID();

                Diagnosis_Types.setText(selected_DiagnosisType);

                int i2 = selected_DiagnosisType_ID;
                Diag_id1.setText(String.valueOf(i2));
                dialog_ConsultAmt.dismiss();

                //  selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();



                /*
                selected_DiagnosisType = _model.getDiagnosis_Type();*/
                //selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_Diagnosis_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Diagnosis_Types.setText(selected_DiagnosisType);

                dialog_ConsultAmt.dismiss();


            }


        });

        // Display the dialog
        dialog_ConsultAmt.show();


    }

    private class InsertRecord_New_NotArrived extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_Insert = new ProgressDialog(Appointments.this);
            pDialog_Insert.setMessage("Please Wait.");
            pDialog_Insert.setCancelable(false);
            pDialog_Insert.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler1 hh = new HTTPDataHandler1();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);


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

                            MRNO = jObject1.getString("MRNO");

                            InsertPatRecord_Not_Arrived();


                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_Insert.isShowing()) ;
            pDialog_Insert.dismiss();


            //  getDoctor_Appointments_Portal();

        } // onPostExecute() end
    }

    private void InsertPatRecord_Not_Arrived() {


        /*

        Time_Date_Data();
        formate_fromTime_withDate();
*/


        String P_amount = Phar_Charge_NotArrived.getText().toString();

        String age = Age_NotArrived.getText().toString();

        String names = name_edt_NotArrived.getText().toString();


        String Time = from_DateTime_Global;


        String Fee = Cons_Fee_NotArrived.getText().toString();

        String Mble = MobileNum_NotArrived.getText().toString();

        String gender = Gender_NotArrived.getText().toString();

        String ID = Cons_Id1.getText().toString();

        String MR_No = MR_NOT_ARRIVED.getText().toString();


        String Diag_Id = Diag_id1.getText().toString();


        // String Per_MR_Number = MR_Number.getText().toString();


        JSONArray jA1 = new JSONArray();


        jA1.put(MRNO);
        jA1.put(doctor_ID_Global);
        jA1.put(hospital_ID_Global);
        jA1.put(mEMP_Id_Global);
        jA1.put(Fee);
        jA1.put(P_amount);
        jA1.put(Mble);
        jA1.put(correct_Date);
        jA1.put(ID);
        jA1.put(names);


        String send_BookData_New = jA1.toString();
        StringBuffer sb_Book_new = new StringBuffer();

        sb_Book_new.append(Constants.URL + "Web_InsertPatRecord1" + "?" + "Data=" + send_BookData_New);
        url_InsertPatRecord = sb_Book_new.toString();
        System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


        new InsertRecord_X().execute(url_InsertPatRecord);


    }

    private class InsertRecord_X extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_Insert1 = new ProgressDialog(Appointments.this);
            pDialog_Insert1.setMessage("Please Wait.");
            pDialog_Insert1.setCancelable(false);
            pDialog_Insert1.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler1 hh = new HTTPDataHandler1();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);


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
/*
                            MRNO = jObject1.getString("MRNO");

                            InsertPatRecord1();*/


                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_Insert1.isShowing()) ;
            pDialog_Insert1.dismiss();

            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            getDoctor_Appointments_Portal();

            // getDoctor_Appointments_Portal();


            //  getDoctor_Appointments_Portal();

        } // onPostExecute() end
    } // ProcessJSON class end{

    private class GetDiagnosis2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDiagnosis = new ProgressDialog(Appointments.this);
            pDialog_GetDiagnosis.setMessage("Please Wait.");
            pDialog_GetDiagnosis.setCancelable(false);
            pDialog_GetDiagnosis.show();
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
                    doc_Diagnosis_List.clear();
                    // Extract JSON array from the response
                    JSONArray jArray = new JSONArray(stream);
                    System.out.println(jArray.length());
                    if (jArray.length() != 0) {

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject1 = jArray.getJSONObject(i);
                            int mDiagnosis_ID = jObject1.getInt("DiagnosisId");
                            String mDiagnosis_Type = jObject1.getString("DiagnosisName");




                      /*  for (int i = 0; i < jArray.length(); i++) {
                            JSONArray jArrray1 = jArray.getJSONArray(i);
                            String mDiagnosis_ID_response = jArrray1.getString(0);
                            String mDiagnosis_Type = jArrray1.getString(1);
                           // String mConsultation_Amt = jArrray1.getString(2);
                            int mDiagnosis_ID = Integer.parseInt(mDiagnosis_ID_response);
*/
                            Diagnosis_Model xsai = new Diagnosis_Model(mDiagnosis_ID, mDiagnosis_Type);
                            doc_Diagnosis_List.add(xsai);
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
                                doc_Diagnosis_List.clear();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_GetDiagnosis.isShowing()) ;
            pDialog_GetDiagnosis.dismiss();

            doc_DIagnosisTypes_List_Dilaog2();

            /**
             * Updating parsed JSON data into ListView
             * */
            //doc_ConsultationTypes_List_Dilaog();
        }
    }

    private void doc_DIagnosisTypes_List_Dilaog2() {


        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.diagnosislist_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_diagnosis_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.diagnosis_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_diagnosis_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_diagnosis_amt_dialog);


        if (doc_Diagnosis_List.size() == 0) {
            title_Dialog.setText("Diagnosis types does not exists");

        } else {
            title_Dialog.setText("Diagnosis types");

        }

        Doc_DiagnosisType_Amt_Adapter mAdapter = new Doc_DiagnosisType_Amt_Adapter(Appointments.this, R.layout.doc_diagnosis_amt_type_dialog_row, doc_Diagnosis_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Diagnosis_Model _model = (Diagnosis_Model) parent.getItemAtPosition(position);


                selected_DiagnosisType_ID = _model.getDiagnosis_Type();
                selected_DiagnosisType = _model.getDiagnosisType_ID();

                Diagnosis_Types.setText(selected_DiagnosisType);

                int i2 = selected_DiagnosisType_ID;
                Diag_id2.setText(String.valueOf(i2));
                dialog_ConsultAmt.dismiss();

                //  selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();



                /*
                selected_DiagnosisType = _model.getDiagnosis_Type();*/
                //selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_Diagnosis_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Diagnosis_Types.setText(selected_DiagnosisType);

                dialog_ConsultAmt.dismiss();


            }


        });

        // Display the dialog
        dialog_ConsultAmt.show();

    }

    private class InsertRecord_New_Not_Paid extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_Insert = new ProgressDialog(Appointments.this);
            pDialog_Insert.setMessage("Please Wait.");
            pDialog_Insert.setCancelable(false);
            pDialog_Insert.show();
        }

        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler1 hh = new HTTPDataHandler1();
            stream = hh.GetHTTPData(urlString);
            System.out.println("json string>>>>>>>>" + stream);


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

                            MRNO = jObject1.getString("MRNO");

                            InsertPatRecord_Not_Paid();


                        } //for loop end


                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // display toast here
                                Toast.makeText(getApplicationContext(), "No data available to book appointment", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    // process other data as this way..............

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end

            super.onPostExecute(stream);
            if (pDialog_Insert.isShowing()) ;
            pDialog_Insert.dismiss();


            //  getDoctor_Appointments_Portal();

        } // onPostExecute() end

    }

    private void InsertPatRecord_Not_Paid() {

        String P_amount = Phar_Charge_Not_Paid.getText().toString();

        String age = Age_Not_Paid.getText().toString();

        String names = name_edt_Not_Paid.getText().toString();


        String Time = from_DateTime_Global;


        String Fee = Cons_Fee_NotPaid.getText().toString();

        String Mble = mobile_Not_Paid.getText().toString();

        String gender = Gender_NotPaid.getText().toString();

        String ID = Cons_Id2.getText().toString();

        String MR_No = MR_NOT_PAID.getText().toString();


        String Diag_Id = Diag_id2.getText().toString();


        // String Per_MR_Number = MR_Number.getText().toString();


        JSONArray jA1 = new JSONArray();

        jA1.put(MRNO);
        jA1.put(doctor_ID_Global);
        jA1.put(hospital_ID_Global);
        jA1.put(mEMP_Id_Global);
        jA1.put(Fee);
        jA1.put(P_amount);
        jA1.put(Mble);
        jA1.put(correct_Date);
        jA1.put(ID);
        jA1.put(names);


        String send_BookData_New = jA1.toString();
        StringBuffer sb_Book_new = new StringBuffer();

        sb_Book_new.append(Constants.URL + "Web_InsertPatRecord1" + "?" + "Data=" + send_BookData_New);
        url_InsertPatRecord = sb_Book_new.toString();
        System.out.println("send to server to book appointment for new >>>>>>>>" + url_BookAppointment_New);


        new InsertRecord_X().execute(url_InsertPatRecord);


    }

    private class GetDoctorConsultationTypes1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDoc_Consult_AmtTypes = new ProgressDialog(Appointments.this);
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
            doc_ConsultationTypes_List_Dilaog1();

        } // onPostExecute() end

    }

    private void doc_ConsultationTypes_List_Dilaog1() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.consultation_amt_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_consultation_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.consultation_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_consultation_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_consultation_amt_dialog);


        if (doc_ConsultationType_Amt_List.size() == 0) {
            title_Dialog.setText("Consultation types does not exists");

        } else {
            title_Dialog.setText("Consultation types");

        }

        Doc_ConsultationType_Amt_Adapter mAdapter = new
                Doc_ConsultationType_Amt_Adapter(Appointments.this, R.layout.doc_consultation_amt_type_dialog_row, doc_ConsultationType_Amt_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);

                selected_ConsultationType_ID = _model.getConsultationType_ID();
                selected_ConsultationType = _model.getConsultation_Type();
                selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();

                int i1 = selected_ConsultationType_ID;


                Cons_Fee_NotArrived.setText(selected_Consultation_TypeAmt);


                Cons_Id1.setText(String.valueOf(i1));


                EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                e_etxt_Et.setText(selected_Consultation_TypeAmt);


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_ConsultationType_Amt_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doc_ConsultationType_Amt_List.size() == 0) {
                    // Close the dialog
                    dialog_ConsultAmt.dismiss();

                } else {

                    EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                    selected_Consultation_TypeAmt = e_etxt_Et.getText().toString();


                    if (selected_Consultation_TypeAmt != null && !selected_Consultation_TypeAmt.isEmpty()) {
                        //					  Send Selected Consultation Fee  from listview


                        Cons_Fee_NotArrived.setText(selected_Consultation_TypeAmt);

                        // Cons_Id.setText(selected_ConsultationType_ID_S);


                        //     send_DefineConsultationFee();

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

    private class GetDoctorConsultationTypes2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetDoc_Consult_AmtTypes = new ProgressDialog(Appointments.this);
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
            doc_ConsultationTypes_List_Dilaog2();

        } // onPostExecute() end
    }

    private void doc_ConsultationTypes_List_Dilaog2() {

        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog_ConsultAmt = new Dialog(Appointments.this);
        // hide to default title for Dialog
        dialog_ConsultAmt.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_ConsultAmt.setContentView(R.layout.consultation_amt_dialog);

        TextView title_Dialog = (TextView) dialog_ConsultAmt.findViewById(R.id.title_consultation_amt_dialog);
        ListView listview_Dialog = (ListView) dialog_ConsultAmt.findViewById(R.id.consultation_amt_typelistview);
        Button cancel_btn = (Button) dialog_ConsultAmt.findViewById(R.id.cancel_btn_consultation_amt_dialog);
        Button confirm_btn = (Button) dialog_ConsultAmt.findViewById(R.id.confirm_btn_consultation_amt_dialog);


        if (doc_ConsultationType_Amt_List.size() == 0) {
            title_Dialog.setText("Consultation types does not exists");

        } else {
            title_Dialog.setText("Consultation types");

        }

        Doc_ConsultationType_Amt_Adapter mAdapter = new
                Doc_ConsultationType_Amt_Adapter(Appointments.this, R.layout.doc_consultation_amt_type_dialog_row, doc_ConsultationType_Amt_List);

        listview_Dialog.setAdapter(mAdapter);
        listview_Dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Appointments_Model _model = (Appointments_Model) parent.getItemAtPosition(position);

                selected_ConsultationType_ID = _model.getConsultationType_ID();
                selected_ConsultationType = _model.getConsultation_Type();
                selected_Consultation_TypeAmt = _model.getConsultation_Type_Amount();

                int i1 = selected_ConsultationType_ID;


                Cons_Fee_NotPaid.setText(selected_Consultation_TypeAmt);


                Cons_Id2.setText(String.valueOf(i1));


                EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                e_etxt_Et.setText(selected_Consultation_TypeAmt);


                view.setSelected(true);
                System.out.println("selected_Consultation_TypeAmt" + selected_Consultation_TypeAmt);

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_ConsultAmt.dismiss();
                doc_ConsultationType_Amt_List.clear();
            }
        });


        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doc_ConsultationType_Amt_List.size() == 0) {
                    // Close the dialog
                    dialog_ConsultAmt.dismiss();

                } else {

                    EditText e_etxt_Et = (EditText) dialog_ConsultAmt.findViewById(R.id.e_others);
                    selected_Consultation_TypeAmt = e_etxt_Et.getText().toString();


                    if (selected_Consultation_TypeAmt != null && !selected_Consultation_TypeAmt.isEmpty()) {
                        //					  Send Selected Consultation Fee  from listview


                        Cons_Fee_NotPaid.setText(selected_Consultation_TypeAmt);

                        // Cons_Id.setText(selected_ConsultationType_ID_S);


                        //     send_DefineConsultationFee();

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
}
