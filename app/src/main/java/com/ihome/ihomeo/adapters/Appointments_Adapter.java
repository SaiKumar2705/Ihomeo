package com.ihome.ihomeo.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ihomeo.ihomeouploading.ihomeoactivity.Appointments;
import com.ihomeo.ihomeouploading.ihomeoactivity.R;
import com.ihomeo.ihomeouploading.model.Appointments_Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Appointments_Adapter extends ArrayAdapter<Appointments_Model> {

    private static final int VIEW_TYPE_ROW_NOTBOOK = 0;
    private static final int VIEW_TYPE_ROW_NOTARRIVED = 1;
    private static final int VIEW_TYPE_ROW_NOTPAY = 2;
    private static final int VIEW_TYPE_ROW_CONSULTATION_PAY = 3;
    private static final int VIEW_TYPE_ROW_PHARMACY_PAY = 4;
    private static final int VIEW_TYPE_ROW_APPOINTMENT_DONE = 5;
    private static final int VIEW_TYPE_ROW_TIMEUP = 6;
    private static final int VIEW_TYPE_ROW_DATE_DIFFRENCE = 7;
    private static final int DETAILSCLOSE = 8;

    Context context;
    private LayoutInflater vi;
    public ArrayList<Appointments_Model> appoints_list;
    public ArrayList<Appointments_Model> new_list = new ArrayList<Appointments_Model>();
    public ArrayList<Appointments_Model> new_list_DET = new ArrayList<>();
    public ArrayList<Appointments_Model> arrived_list = new ArrayList<Appointments_Model>();
    public ArrayList<Appointments_Model> pay_list = new ArrayList<Appointments_Model>();
    public ArrayList<Appointments_Model> pharmacy_paybill_list = new ArrayList<Appointments_Model>();

    private String appointment_date, userType;
    private TextView time_txtTitle, date_txtTitle, name_txv, mobile_txv, phone, MobCal, Name;
    private EditText mobile_edt, MR_NOTARRIVED;

    private Switch mDc_Invoice_Switch;

    private String doc_type;


    public static EditText MR_NOT_ARRIVED, Gender_NotPaid, Gender_NotArrived, Phar_Charge_Not_Paid, name_edt_Not_Paid, mobile_Not_Paid, MR_NOT_PAID, Age_Not_Paid, Phar_Charge_NotArrived, MobileNum_NotArrived, Cons_Fee_NotArrived, Cons_Fee_NotPaid, name_edt_NotArrived, Age_NotArrived, Cons_Fee, Phar_Charge, Diagnosis_Types, calender, MobileNum, sk, name_edt, Age, MR_Number, Cons_Id, Cons_Id1, Cons_Id2, Diag_id, Diag_id1, Diag_id2;

    public static String K;

    private Button back_btn, confirm_btn;

    public Appointments_Adapter(Context context, int textViewResourceId, ArrayList<Appointments_Model> appoints_list, String appointment_date, String mUserType) {
        super(context, textViewResourceId, appoints_list);
        this.context = context;
        this.appoints_list = appoints_list;
        this.appointment_date = appointment_date;
        this.userType = mUserType;
    }


    //count number of row layouts
    @Override
    public int getViewTypeCount() {
        return 14000;
    }

    //pass required row_layout position where book name  is in listview
    @Override
    public int getItemViewType(int position) {

        String patient_name = appoints_list.get(position).getPatient_Name();
        // Sai

        String ph = appoints_list.get(position).getMobile_numberCall();

        // String Mobile = appoints_list.get(position).getMobile_Numb();
        String book = appoints_list.get(position).getBook();
        String arrived = appoints_list.get(position).getArrived();
        String pay_Consult = appoints_list.get(position).getPay_Consultation();
        String pay_Pharmacy = appoints_list.get(position).getPay_Pharmacy();

//	 allow user to book appointment should book_datetime>current_datetime
        String now_date_time = new SimpleDateFormat("EEE,d-MMM-yyyy h:mm aa").format(new Date());
        String book_date_time = appointment_date + " " + appoints_list.get(position).getFrom_Time();
        String book_date = appointment_date;
        String now_date1 = new SimpleDateFormat("EEE, d-MMM-yyyy").format(new Date());

        String now_date = new SimpleDateFormat("EEE, d-MMM-yyyy").format(new Date());


        Date book_DateTimeFormat = null, now_DateTimeFormat = null, X = null, Y = null;
        Date book_DateFormat = null, now_DateFormat = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("EEE,d-MMM-yyyy h:mm aa");
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE,d-MMM-yyyy");

            book_DateTimeFormat = sdf.parse(book_date_time);

            X = sdf1.parse(book_date);
            Y = sdf1.parse(now_date1);


            //bookFormat=
            now_DateTimeFormat = sdf.parse(now_date_time);


        } catch (ParseException ex) {
            ex.printStackTrace();
        }


        if (book.equals("NA") && arrived.equals("NA") && pay_Consult.equals("NA") && pay_Pharmacy.equals("NA")) {
            if (book_DateTimeFormat.after(now_DateTimeFormat)) {
                System.out.println("book_DateTimeFormat is after now_DateTimeFormat");
                position = VIEW_TYPE_ROW_NOTBOOK;
            } else {
                position = VIEW_TYPE_ROW_TIMEUP;
            }

        } else if (!book.equals("NA") && arrived.equals("NA") && pay_Consult.equals("NA") && pay_Pharmacy.equals("NA")) {

            if (!book_date.equals(now_date))
                position = VIEW_TYPE_ROW_DATE_DIFFRENCE;
            else
                position = VIEW_TYPE_ROW_NOTARRIVED;

        } else if (!book.equals("NA") && !arrived.equals("NA") && pay_Consult.equals("NA") && pay_Pharmacy.equals("NA")) {

            position = VIEW_TYPE_ROW_NOTPAY;

        } else if (!book.equals("NA") && !arrived.equals("NA") && !pay_Consult.equals("NA") && !pay_Pharmacy.equals("NA")) {

            position = VIEW_TYPE_ROW_APPOINTMENT_DONE;

        } else if (!book.equals("NA") && !arrived.equals("NA") && !pay_Consult.equals("NA") || !pay_Pharmacy.equals("NA")) {
            if (userType.equals("USER")) {
                position = VIEW_TYPE_ROW_PHARMACY_PAY;
            } else {
                position = VIEW_TYPE_ROW_CONSULTATION_PAY;
            }


        }

        if (X.after(Y)) {
            System.out.println("book_DateTimeFormat is after now_DateTimeFormat");
            position = DETAILSCLOSE;
        }
        if (X.after(Y) && !book.equals("NA")) {
            System.out.println("book_DateTimeFormat is after now_DateTimeFormat");
            position = VIEW_TYPE_ROW_DATE_DIFFRENCE;
        }


        return position;

    }


    private class ViewHolder {
        TextView time, patient_name, book, arrived, pay_consult_amt, pay_pharcy_amt, ph;

        ImageView call;


        EditText ConsFee;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

      /*  String s = values[position];*/

//call view type method
        int type = getItemViewType(position);

        System.out.println("u get type is" + type);
        if (convertView == null) {
            //which type of view u want set here
            if (type == 0) {
//region Type=0
                convertView = vi.inflate(R.layout.appointments_notbook_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notbook_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_notbook_row);
                holder.book = (TextView) convertView.findViewById(R.id.book_appointment_notbook_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived_appointment_notbook_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notbook_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_appointment_notbook_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call_book);
                convertView.setTag(holder);

                holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

/*

                        Intent intent = new Intent(context, PayClick.class);
                        //intent.putextra("your_extra","your_class_value");
                        context.startActivity(intent);


                        final TextView book_Txv = (TextView) v;
//   				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();
                        Appointments_Model model = new Appointments_Model();

                        model.setMR_Number(appointments_model.getBook());
                        pay_list.add(model);
                        ((Appointments) context).getDoctor_Consultaion_AmtTypes();

d
                        */
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.payclick_dialog);


                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);

                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);

                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);

                        Age = (EditText) dialog.findViewById(R.id.age);

                        MR_Number = (EditText) dialog.findViewById(R.id.mrnumber);


                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        calender = (EditText) dialog.findViewById(R.id.calender);
                        MobileNum = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);

                        Phar_Charge = (EditText) dialog.findViewById(R.id.pcharge);


                        MobileNum.requestFocus();

                        MobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    // Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                                } else {

                                    String Mobi = MobileNum.getText().toString();
                                    ((Appointments) context).check_MobileNo_getPatients_New();

                                }
                            }

                        });


                        //  mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch1);


                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_payment_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_payment_dialog);
                        Cons_Fee = (EditText) dialog.findViewById(R.id.cons_fee);
                        Cons_Id = (EditText) dialog.findViewById(R.id.cons_id);
                        Diag_id = (EditText) dialog.findViewById(R.id.dia_id);
                        Diagnosis_Types = (EditText) dialog.findViewById(R.id.diagnosis_Types);
                        sk = (EditText) dialog.findViewById(R.id.sk);
                        mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch);


                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);







                        /*MobileNum.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).check_MobileNo_getPatients();

                            }
                        });

*/
                        Diagnosis_Types.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).Diagnosis_Tpes();


                            }
                        });


                        calender.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).datePicker1();


                            }
                        });

                        // time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        // date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        Cons_Fee.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final TextView book_Txv = (TextView) v;
//   				getting tag

                                ((Appointments) context).getDoctor_Consultaion_AmtTypes();


                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (MobileNum.getText().length() == 0) {
                                    MobileNum.setError("Please fill the field");
                                } else if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else {


                                    Appointments_Model model = new Appointments_Model();
                                    //model.setPatient_Name(name_edt.getText().toString());
                                    // model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    // model.setToken_Number(appointments_model.getToken_Number());

                                    new_list_DET.add(model);
                                    if (mDc_Invoice_Switch.isChecked()) {

                                        doc_type = "female";

                                    } else {

                                        doc_type = "male";
                                    }


                                    String Sai = Cons_Fee.getText().toString();


                                    K = appointments_model.getFrom_Time();

                                    sk.setText(doc_type);


                                    String Veda = sk.getText().toString();


                                    String MR = MR_Number.getText().toString();


                                    String ID = Cons_Id.getText().toString();

/*
                                if (MR.equals("NEW") || MR.contains("APP")) {


                                    ((Appointments) context).InsertPatRecord();
                                } else {


                                    ((Appointments) context).InsertPatRecord2();


                                }*/


                                    ((Appointments) context).InsertPatRecord();


                                    //model.setPay_Consultation(appointments_model.getPay_Consultation());


                                    dialog.dismiss();
                                }
                            }

                        });

                        // Display the dialog
                        dialog.show();
                    }


                });

                holder.book.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.appointment_dialog);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.back_btn_appointments_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_appointments_dialog);


                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNumber();


                                    // Close the dialog
                                    dialog.dismiss();
                                }

                            }
                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending

//endregion Type=0
            } else if (type == 1) {

                convertView = vi.inflate(R.layout.appointments_notarrived_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notarrived_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_notarrived_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_appointment_notarrived_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived_appointment_notarrived_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notarrived_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_amt_appointment_notarrived_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call);


                convertView.setTag(holder);


                Appointments_Model app = appoints_list.get(position);
                //final String PatMbleNo = app.getMobile_numberCall();

                holder.ph.setText(app.getMobile_numberCall());


                holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.payclick_dialog);


                        mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch);

                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);

                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        calender = (EditText) dialog.findViewById(R.id.calender);
                        // mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch1);

                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);


                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_payment_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_payment_dialog);
                        Cons_Fee_NotArrived = (EditText) dialog.findViewById(R.id.cons_fee);
                        Diagnosis_Types = (EditText) dialog.findViewById(R.id.diagnosis_Types);
                        Cons_Id1 = (EditText) dialog.findViewById(R.id.cons_id);
                        Diag_id1 = (EditText) dialog.findViewById(R.id.dia_id);
                        Phar_Charge_NotArrived = (EditText) dialog.findViewById(R.id.pcharge);
                        Gender_NotArrived = (EditText) dialog.findViewById(R.id.sk);

                        MR_NOT_ARRIVED = (EditText) dialog.findViewById(R.id.mrnumber);
                        Age_NotArrived = (EditText) dialog.findViewById(R.id.age);
                        name_edt_NotArrived = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        Cons_Fee_NotArrived = (EditText) dialog.findViewById(R.id.cons_fee);
                        MobileNum_NotArrived = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);


                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);


                        Age_NotArrived.requestFocus();


                        Appointments_Model app = appoints_list.get(position);


                        K = appointments_model.getFrom_Time();


                        name_edt.setText(appointments_model.getPatient_Name());


                        MR_NOT_ARRIVED.setText(appointments_model.getBook());


                        mobile_edt.setText(appointments_model.getMobile_numberCall());

                        Diagnosis_Types.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                ((Appointments) context).Diagnosis_Tpes1();


                            }
                        });


                        calender.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Appointments) context).datePicker1();


                            }
                        });

                        // time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        // date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        Cons_Fee_NotArrived.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final TextView book_Txv = (TextView) v;
//   				getting tag

                                ((Appointments) context).getDoctor_Consultaion_AmtTypes1();


                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (mDc_Invoice_Switch.isChecked()) {

                                    doc_type = "Female";

                                } else {

                                    doc_type = "Male";
                                }


                                String MR = MR_NOT_ARRIVED.getText().toString();

                                String ID = Cons_Id1.getText().toString();

                                String age = Age_NotArrived.getText().toString();

                                Gender_NotArrived.setText(doc_type);

                                Appointments_Model model = new Appointments_Model();
                                //model.setPatient_Name(name_edt.getText().toString());
                                // model.setMobile_Numb(mobile_edt.getText().toString());
                                model.setFrom_Time(appointments_model.getFrom_Time());
                                model.setTo_Time(appointments_model.getTo_Time());
                                // model.setToken_Number(appointments_model.getToken_Number());

                                new_list_DET.add(model);


                                ((Appointments) context).InsertPatRecord_NotArrived();




                               /* String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNumber();*/

                                // Close the dialog
                                dialog.dismiss();
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                holder.call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

// TODO Auto-generated method stub
                        final ImageView book_Txv = (ImageView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.call_user);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        phone = (TextView) dialog.findViewById(R.id.phonenumber);
                        Name = (TextView) dialog.findViewById(R.id.patname);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.btncall);


                        Appointments_Model app = appoints_list.get(position);
                        phone.setText(appointments_model.getMobile_numberCall());
                        Name.setText(appointments_model.getPatient_Name());

                        //time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        //date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Appointments_Model app = appoints_list.get(position);

                                Uri uri = Uri.parse("tel:" + app.getMobile_numberCall());

                                Intent i = new Intent(Intent.ACTION_DIAL, uri);

                                context.startActivity(i);

                               /* Appointments_Model app = appoints_list.get(position);
                                phone.setText(app.getMobile_numberCall());

                                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse(app.getMobile_numberCall()));


                                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                context.startActivity(sIntent);*/
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                holder.arrived.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//   				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();
                        // Create custom dialog object
                        final Dialog dialog_arrived = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog_arrived.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_arrived.setContentView(R.layout.appointment_arrived_dialog);
                        TextView time_txtTitle = (TextView) dialog_arrived.findViewById(R.id.title_appointment_arrived_dialog);
                        TextView message_txv = (TextView) dialog_arrived.findViewById(R.id.message_txv_appointment_arrived_dialog);

                        Button cancel_btn = (Button) dialog_arrived.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        Button confirm_btn = (Button) dialog_arrived.findViewById(R.id.confirm_btn_appointment_arrived_dialog);
                        String Arrived_time = new SimpleDateFormat("EEE, d-MMM-yyyy h:mm aa").format(new Date());
                        message_txv.setText("Are you sure patient arrived at " + Arrived_time);

                        cancel_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog_arrived.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Appointments_Model model = new Appointments_Model();

                                model.setMR_Number(appointments_model.getBook());

                                arrived_list.add(model);
                                ((Appointments) context).PatientArrived();

                                // Close the dialog
                                dialog_arrived.dismiss();


                            }
                        });


                        // Display the dialog
                        dialog_arrived.show();

                    }
                });  //arrived btn click ending

            } else if (type == 2) {

                convertView = vi.inflate(R.layout.appointments_notpaid_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notpaid_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_notpaid_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_notpaid_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrivedtime_appointment_notpaid_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notpaid_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_appointment_notpaid_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call);

                convertView.setTag(holder);

                holder.call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ImageView book_Txv = (ImageView) v;


//          				getting tag

                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.setContentView(R.layout.call_user);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.call_title);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        phone = (TextView) dialog.findViewById(R.id.phonenumber);
                        Name = (TextView) dialog.findViewById(R.id.patname);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.btncall);


                        Appointments_Model app = appoints_list.get(position);


                        // / time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());

                        phone.setText(appointments_model.getMobile_numberCall());
                        Name.setText(appointments_model.getPatient_Name());


                        //time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        //date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Appointments_Model app = appoints_list.get(position);
                                Uri uri = Uri.parse("tel:" + app.getMobile_numberCall());
                                Intent i = new Intent(Intent.ACTION_DIAL, uri);

                                context.startActivity(i);

                               /* Appointments_Model app = appoints_list.get(position);
                                phone.setText(app.getMobile_numberCall());

                                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse(app.getMobile_numberCall()));


                                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                context.startActivity(sIntent);*/
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                convertView.setTag(holder);
                holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*// TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//   				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();
                        Appointments_Model model = new Appointments_Model();

                        model.setMR_Number(appointments_model.getBook());
                        pay_list.add(model);
                        ((Appointments) context).getDoctor_Consultaion_AmtTypes();



*/
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.payclick_dialog);


                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);

                        Age_Not_Paid = (EditText) dialog.findViewById(R.id.age);

                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        calender = (EditText) dialog.findViewById(R.id.calender);
                        Cons_Id2 = (EditText) dialog.findViewById(R.id.cons_id);
                        Diag_id2 = (EditText) dialog.findViewById(R.id.dia_id);
                        MR_NOT_PAID = (EditText) dialog.findViewById(R.id.mrnumber1);
                        Gender_NotPaid = (EditText) dialog.findViewById(R.id.sk);

                        Phar_Charge_Not_Paid = (EditText) dialog.findViewById(R.id.pcharge);

                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);


                        mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch);


                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt_Not_Paid = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_Not_Paid = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_payment_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_payment_dialog);
                        Cons_Fee_NotPaid = (EditText) dialog.findViewById(R.id.cons_fee);
                        Diagnosis_Types = (EditText) dialog.findViewById(R.id.diagnosis_Types);

                        Appointments_Model app = appoints_list.get(position);

                        K = appointments_model.getFrom_Time();


                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);


                        name_edt.setText(appointments_model.getPatient_Name());


                        mobile_edt.setText(appointments_model.getMobile_numberCall());

                        Age_Not_Paid.requestFocus();


                        // Appointments_Model app = appoints_list.get(position);
                        Diagnosis_Types.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).Diagnosis_Tpes2();

                            }
                        });


                        // Appointments_Model app = appoints_list.get(position);


                        MR_NOT_PAID.setText(appointments_model.getBook());


                        calender.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Appointments) context).datePicker1();


                            }
                        });

                        // time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        // date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        Cons_Fee_NotPaid.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final TextView book_Txv = (TextView) v;
//   				getting tag

                                ((Appointments) context).getDoctor_Consultaion_AmtTypes2();


                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Appointments_Model model = new Appointments_Model();
                                //model.setPatient_Name(name_edt.getText().toString());
                                // model.setMobile_Numb(mobile_edt.getText().toString());
                                model.setFrom_Time(appointments_model.getFrom_Time());
                                model.setTo_Time(appointments_model.getTo_Time());
                                // model.setToken_Number(appointments_model.getToken_Number());

                                K = appointments_model.getFrom_Time();

                                new_list_DET.add(model);


                                if (mDc_Invoice_Switch.isChecked()) {

                                    doc_type = "Female";

                                } else {

                                    doc_type = "Male";
                                }


                                Gender_NotPaid.setText(doc_type);

                                String MR = MR_NOT_PAID.getText().toString();

                                String age = Age_Not_Paid.getText().toString();
                                String ID = Cons_Id2.getText().toString();


                                ((Appointments) context).InsertPat_Not_Paid();





                               /* String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNumber();*/

                                // Close the dialog
                                dialog.dismiss();
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                //pay btn click ending

            } else if (type == 3) {

                convertView = vi.inflate(R.layout.appointments_consultation_pay_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_consultation_pay_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_consultation_pay_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_consultation_pay_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrivedtime_consultation_pay_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_consultation_pay_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_consultation_pay_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call);

                holder.call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ImageView book_Txv = (ImageView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.call_user);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        phone = (TextView) dialog.findViewById(R.id.phonenumber);
                        Name = (TextView) dialog.findViewById(R.id.patname);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.btncall);


                        Appointments_Model app = appoints_list.get(position);
                        phone.setText(app.getMobile_numberCall());
                        Name.setText(app.getPatient_Name());

                        //time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        //date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Appointments_Model app = appoints_list.get(position);
                                Uri uri = Uri.parse("tel:" + app.getMobile_numberCall());
                                Intent i = new Intent(Intent.ACTION_DIAL, uri);

                                context.startActivity(i);

                               /* Appointments_Model app = appoints_list.get(position);
                                phone.setText(app.getMobile_numberCall());

                                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse(app.getMobile_numberCall()));


                                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                context.startActivity(sIntent);*/
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                convertView.setTag(holder);


            } else if (type == 4) {

                convertView = vi.inflate(R.layout.appointments_pharmacy_pay_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_pharmacy_pay_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_pharmacy_pay_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_pharmacy_pay_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrivedtime_pharmacy_pay_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_pharmacy_pay_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_pharmacy_pay_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call);


                holder.call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView book_Txv = (ImageView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.call_user);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        phone = (TextView) dialog.findViewById(R.id.phonenumber);
                        Name = (TextView) dialog.findViewById(R.id.patname);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.btncall);


                        Appointments_Model app = appoints_list.get(position);
                        phone.setText(app.getMobile_numberCall());
                        Name.setText(app.getPatient_Name());

                        //time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        //date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Appointments_Model app = appoints_list.get(position);
                                Uri uri = Uri.parse("tel:" + app.getMobile_numberCall());
                                Intent i = new Intent(Intent.ACTION_DIAL, uri);

                                context.startActivity(i);

                               /* Appointments_Model app = appoints_list.get(position);
                                phone.setText(app.getMobile_numberCall());

                                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse(app.getMobile_numberCall()));


                                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                context.startActivity(sIntent);*/
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                convertView.setTag(holder);
                holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();
                        Appointments_Model model = new Appointments_Model();
                        model.setPay_Consultation(appointments_model.getPay_Consultation());
                        model.setMR_Number(appointments_model.getBook());
                        pharmacy_paybill_list.add(model);
                        ((Appointments) context).userType_PaymentDialog();

                    }
                });  //pay_consult_amt  btn click ending

            } else if (type == 5) {

                convertView = vi.inflate(R.layout.appointments_appointment_done_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_appointment_done_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_done_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_appointment_done_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrivedtime_appointment_appointment_done_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_consultation_amt_appointment_appointment_done_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_pharmacy_amt_appointment_appointment_done_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call);
                holder.call.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final ImageView book_Txv = (ImageView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.call_user);

                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        phone = (TextView) dialog.findViewById(R.id.phonenumber);
                        Name = (TextView) dialog.findViewById(R.id.patname);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_appointment_arrived_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.btncall);


                        Appointments_Model app = appoints_list.get(position);
                        phone.setText(appointments_model.getMobile_numberCall());
                        Name.setText(appointments_model.getPatient_Name());



                      /*  Appointments_Model app = appoints_list.get(position);
                        phone.setText(app.getMobile_numberCall());
                        Name.setText(app.getPatient_Name());*/

                        //time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        //date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Appointments_Model app = appoints_list.get(position);
                                Uri uri = Uri.parse("tel:" + app.getMobile_numberCall());
                                Intent i = new Intent(Intent.ACTION_DIAL, uri);

                                context.startActivity(i);

                               /* Appointments_Model app = appoints_list.get(position);
                                phone.setText(app.getMobile_numberCall());

                                Intent sIntent = new Intent(Intent.ACTION_CALL, Uri.parse(app.getMobile_numberCall()));


                                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                context.startActivity(sIntent);*/
                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending


                convertView.setTag(holder);


            } else if (type == 6) {


                convertView = vi.inflate(R.layout.appointments_time_up_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notbook_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_notbook_row);
                holder.book = (TextView) convertView.findViewById(R.id.book_appointment_notbook_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived_appointment_notbook_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notbook_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_appointment_notbook_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call_book);
                convertView.setTag(holder);

                holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.payclick_dialog);


                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);

                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);

                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);

                        Age = (EditText) dialog.findViewById(R.id.age);

                        MR_Number = (EditText) dialog.findViewById(R.id.mrnumber);


                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        calender = (EditText) dialog.findViewById(R.id.calender);
                        MobileNum = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);

                        Phar_Charge = (EditText) dialog.findViewById(R.id.pcharge);


                        MobileNum.requestFocus();

                        MobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    // Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                                } else {

                                    String Mobi = MobileNum.getText().toString();
                                    ((Appointments) context).check_MobileNo_getPatients_New();

                                }
                            }

                        });


                        //  mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch1);


                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_payment_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_payment_dialog);
                        Cons_Fee = (EditText) dialog.findViewById(R.id.cons_fee);
                        Cons_Id = (EditText) dialog.findViewById(R.id.cons_id);
                        Diag_id = (EditText) dialog.findViewById(R.id.dia_id);
                        Diagnosis_Types = (EditText) dialog.findViewById(R.id.diagnosis_Types);
                        sk = (EditText) dialog.findViewById(R.id.sk);
                        mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch);


                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        Appointments_Model app = appoints_list.get(position);






                        /*MobileNum.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).check_MobileNo_getPatients();

                            }
                        });

*/
                        Diagnosis_Types.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).Diagnosis_Tpes();


                            }
                        });


                        calender.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).datePicker1();


                            }
                        });

                        // time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        // date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        Cons_Fee.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final TextView book_Txv = (TextView) v;
//   				getting tag

                                ((Appointments) context).getDoctor_Consultaion_AmtTypes();


                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else {


                                    Appointments_Model model = new Appointments_Model();
                                    //model.setPatient_Name(name_edt.getText().toString());
                                    // model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    // model.setToken_Number(appointments_model.getToken_Number());

                                    new_list_DET.add(model);
                                    if (mDc_Invoice_Switch.isChecked()) {

                                        doc_type = "female";

                                    } else {

                                        doc_type = "male";
                                    }


                                    String Sai = Cons_Fee.getText().toString();


                                    K = appointments_model.getFrom_Time();

                                    sk.setText(doc_type);


                                    String Veda = sk.getText().toString();


                                    String MR = MR_Number.getText().toString();


                                    String ID = Cons_Id.getText().toString();

/*
                                if (MR.equals("NEW") || MR.contains("APP")) {


                                    ((Appointments) context).InsertPatRecord();
                                } else {


                                    ((Appointments) context).InsertPatRecord2();


                                }*/


                                    ((Appointments) context).InsertPatRecord();


                                    //model.setPay_Consultation(appointments_model.getPay_Consultation());


                                    dialog.dismiss();
                                }


                            }


                        });

                        // Display the dialog
                        dialog.show();
                    }


                });

                holder.book.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.appointment_dialog);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.back_btn_appointments_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_appointments_dialog);


                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNumber();


                                    // Close the dialog
                                    dialog.dismiss();
                                }

                            }
                        });

                        // Display the dialog
                        dialog.show();
                    }
                });  //patient_name btn click ending

//endregion Type=0

               /* holder.pay_consult_amt.setOnClickListener(new OnClickListener() {
                    @Override


                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.payclick_dialog);


                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);

                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);

                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);

                        Age = (EditText) dialog.findViewById(R.id.age);

                        MR_Number = (EditText) dialog.findViewById(R.id.mrnumber);


                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        calender = (EditText) dialog.findViewById(R.id.calender);
                        MobileNum = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);

                        Phar_Charge = (EditText) dialog.findViewById(R.id.pcharge);


                        MobileNum.requestFocus();

                        MobileNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    // Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                                } else {

                                    String Mobi = MobileNum.getText().toString();
                                    ((Appointments) context).check_MobileNo_getPatients_New();

                                }
                            }

                        });


                        //  mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch1);


                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.cancel_btn_payment_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_payment_dialog);
                        Cons_Fee = (EditText) dialog.findViewById(R.id.cons_fee);
                        Cons_Id = (EditText) dialog.findViewById(R.id.cons_id);
                        Diag_id = (EditText) dialog.findViewById(R.id.dia_id);
                        Diagnosis_Types = (EditText) dialog.findViewById(R.id.diagnosis_Types);
                        sk = (EditText) dialog.findViewById(R.id.sk);
                        mDc_Invoice_Switch = (Switch) dialog.findViewById(R.id.gender_switch);


                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        Appointments_Model app = appoints_list.get(position);






                        *//*MobileNum.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).check_MobileNo_getPatients();

                            }
                        });

*//*
                        Diagnosis_Types.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).Diagnosis_Tpes();


                            }
                        });


                        calender.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ((Appointments) context).datePicker1();


                            }
                        });

                        // time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        // date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        Cons_Fee.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final TextView book_Txv = (TextView) v;
//   				getting tag

                                ((Appointments) context).getDoctor_Consultaion_AmtTypes();


                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else {


                                    Appointments_Model model = new Appointments_Model();
                                    //model.setPatient_Name(name_edt.getText().toString());
                                    // model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    // model.setToken_Number(appointments_model.getToken_Number());

                                    new_list_DET.add(model);
                                    if (mDc_Invoice_Switch.isChecked()) {

                                        doc_type = "female";

                                    } else {

                                        doc_type = "male";
                                    }


                                    String Sai = Cons_Fee.getText().toString();


                                    K = appointments_model.getFrom_Time();

                                    sk.setText(doc_type);


                                    String Veda = sk.getText().toString();


                                    String MR = MR_Number.getText().toString();


                                    String ID = Cons_Id.getText().toString();

*//*
                                if (MR.equals("NEW") || MR.contains("APP")) {


                                    ((Appointments) context).InsertPatRecord();
                                } else {


                                    ((Appointments) context).InsertPatRecord2();


                                }*//*


                                    ((Appointments) context).InsertPatRecord();


                                    //model.setPay_Consultation(appointments_model.getPay_Consultation());


                                    dialog.dismiss();
                                }


                            }


                        });

                        // Display the dialog
                        dialog.show();*/





                /*holder.book.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.appointment_dialog);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.back_btn_appointments_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_appointments_dialog);


                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNumber();


                                    // Close the dialog
                                    dialog.dismiss();
                                }

                            }
                        });

                        // Display the dialog
                        dialog.show();*/

                //patient_name btn click ending

//endregion Type=0


            } else if (type == 7) {

                convertView = vi.inflate(R.layout.appointmentbooked_date_difference, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notarrived_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_notarrived_row);
                holder.book = (TextView) convertView.findViewById(R.id.mr_number_appointment_notarrived_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived_appointment_date_notarrived_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notarrived_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_amt_appointment_notarrived_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call_book);
                convertView.setTag(holder);
            } else if (type == 8) {
                convertView = vi.inflate(R.layout.appointments_notbook_row, parent, false);
                holder = new ViewHolder();
                holder.time = (TextView) convertView.findViewById(R.id.time_appointment_notbook_row);
                holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_notbook_row);
                holder.book = (TextView) convertView.findViewById(R.id.book_appointment_notbook_row);
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived_appointment_notbook_row);
                holder.pay_consult_amt = (TextView) convertView.findViewById(R.id.pay_appointment_notbook_row);
                holder.pay_pharcy_amt = (TextView) convertView.findViewById(R.id.pay_dummy_appointment_notbook_row);
                holder.ph = (TextView) convertView.findViewById(R.id.Mobile_sai);
                holder.call = (ImageView) convertView.findViewById(R.id.call_book);
                convertView.setTag(holder);


                holder.book.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final TextView book_Txv = (TextView) v;
//          				getting tag
                        final Appointments_Model appointments_model = (Appointments_Model) book_Txv.getTag();

                        // Create custom dialog object
                        final Dialog dialog = new Dialog(v.getContext());
                        // hide to default title for Dialog
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.appointment_dialog);
                        time_txtTitle = (TextView) dialog.findViewById(R.id.time_title_appointments_dialog);
                        date_txtTitle = (TextView) dialog.findViewById(R.id.date_title_appointments_dialog);
                        name_txv = (TextView) dialog.findViewById(R.id.name_txv_appointments_dialog);
                        mobile_txv = (TextView) dialog.findViewById(R.id.mobile_txv_appointments_dialog);
                        name_edt = (EditText) dialog.findViewById(R.id.name_edt_appointments_dialog);
                        mobile_edt = (EditText) dialog.findViewById(R.id.mobile_edt_appointments_dialog);
                        back_btn = (Button) dialog.findViewById(R.id.back_btn_appointments_dialog);
                        confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn_appointments_dialog);


                        time_txtTitle.setText(appointments_model.getFrom_Time() + "-" + appointments_model.getTo_Time());
                        date_txtTitle.setText(appointment_date);
                        back_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        confirm_btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String time = time_txtTitle.getText().toString();

                                if (name_edt.getText().length() == 0) {
                                    name_edt.setError("Please fill the field");
                                } else if (mobile_edt.getText().length() == 0) {
                                    mobile_edt.setError("Please fill the field");
                                } else {

                                    Appointments_Model model = new Appointments_Model();
                                    model.setPatient_Name(name_edt.getText().toString());
                                    model.setMobile_Numb(mobile_edt.getText().toString());
                                    model.setFrom_Time(appointments_model.getFrom_Time());
                                    model.setTo_Time(appointments_model.getTo_Time());
                                    model.setToken_Number(appointments_model.getToken_Number());

                                    new_list.add(model);
                                    ((Appointments) context).check_MobileNo_getPatients();

                                    // Close the dialog
                                    dialog.dismiss();
                                }


                            }


                        });


                        // Display the dialog
                        dialog.show();

                    }


                });

                convertView.setTag(holder);
            }


/*holder = new ViewHolder();
holder.time = (TextView) convertView.findViewById(R.id.time_appointment_appointment_done_row);
holder.patient_name = (TextView) convertView.findViewById(R.id.patient_name_appointment_done_row);
holder.book = (TextView) convertView.findViewById(R.id.mr_number_appointment_done_row);
holder.arrived = (TextView) convertView.findViewById(R.id.arrivedtime_appointment_appointment_done_row);
holder.pay = (TextView) convertView.findViewById(R.id.pay_amt_appointment_appointment_done_row);

convertView.setTag(holder);
convertView.setTag(R.id.time_appointment_appointment_done_row,     holder.time);
convertView.setTag(R.id.patient_name_appointment_done_row,     holder.patient_name);
convertView.setTag(R.id.mr_number_appointment_done_row,     holder.book);
convertView.setTag(R.id.arrivedtime_appointment_appointment_done_row,     holder.arrived);
convertView.setTag(R.id.pay_amt_appointment_appointment_done_row,     holder.pay);*/

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Appointments_Model app = appoints_list.get(position);
        holder.time.setText(app.getFrom_Time() + "-" + app.getTo_Time());
        System.out.println("get patient " + app.getPatient_Name() + "-" + app.getTo_Time());


        if (app.getMobile_numberCall().equals("NA")) {
            holder.ph.setText("");
        } else {
            holder.ph.setText(app.getMobile_numberCall());
        }
        if (app.getPatient_Name().equals("NA")) {
            holder.patient_name.setText("");
        } else {
            holder.patient_name.setText(app.getPatient_Name());
        }
        if (app.getBook().equals("NA")) {
            holder.book.setText("BOOK");
        } else {

            holder.book.setText(app.getBook());
        }
        if (app.getArrived().equals("NA")) {
            holder.arrived.setText("ARRIVED");
        } else {
            holder.arrived.setText(app.getArrived());
        }
        if ((app.getPay_Consultation().equals("NA")) && (app.getPay_Pharmacy().equals("NA"))) {
            holder.pay_consult_amt.setText("DET");
            holder.pay_pharcy_amt.setText("DET");
        } else if (!(app.getPay_Consultation()).equals("NA") && !(app.getPay_Pharmacy()).equals("NA")) {
            holder.pay_consult_amt.setText("Rs. " + app.getPay_Consultation() + " -C");
            holder.pay_pharcy_amt.setText("Rs. " + app.getPay_Pharmacy() + " -P");
        } else if ((app.getPay_Consultation().equals("NA")) || (app.getPay_Pharmacy().equals("NA"))) {
            if (!app.getPay_Consultation().equals("NA")) {
                holder.pay_consult_amt.setText(app.getPay_Consultation());
            }
            if (!app.getPay_Pharmacy().equals("NA")) {
                holder.pay_pharcy_amt.setText(app.getPay_Pharmacy());
            }

        }

//setting tag is important
        holder.time.setTag(app);
        holder.patient_name.setTag(app);
        holder.book.setTag(app);
        holder.book.setTag(app);
        holder.arrived.setTag(app);
        holder.pay_consult_amt.setTag(app);
        holder.pay_pharcy_amt.setTag(app);
        holder.ph.setTag(app);
        holder.call.setTag(app);


        if (type == 0 || type == 1 || type == 2 || type == 5 || type == 6) {
            convertView.setSelected(true);
            convertView.setBackgroundResource(R.color.white);
        } else if (type == 3 || type == 4) {
            convertView.setSelected(true);
            convertView.setBackgroundResource(R.color.graywhite);
        }


        return convertView;
    }
}
