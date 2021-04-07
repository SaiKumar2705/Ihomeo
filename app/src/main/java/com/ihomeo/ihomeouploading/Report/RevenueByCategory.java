package com.ihomeo.ihomeouploading.Report;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ihomeo.ihomeouploading.ihomeoactivity.R;
import com.ihomeo.ihomeouploading.model.Appointments_Model;
import com.ihomeo.ihomeouploading.model.Dashboard_Model;
import com.ihomeo.ihomeouploading.webservices.Constants;
import com.ihomeo.ihomeouploading.webservices.HTTPDataHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sai on 10/25/2016.
 */
public class RevenueByCategory extends Activity {


    public int ConsAmt, PharAmount, TotalAmount, PerConsAmt, PerPharAmt;

    private TextView Total, Revenuebycategory;

    String mDate_OfChart;
    private List<String> doctorId_List = new ArrayList<String>();
    private List<String> doctorName_List = new ArrayList<String>();
    private List<String> hospitalId_List = new ArrayList<String>();
    private ArrayList<Dashboard_Model> doctors_list = new ArrayList<Dashboard_Model>();
    ArrayList<Appointments_Model> appointments_List = new ArrayList<Appointments_Model>();

    private ProgressDialog pDialog_GetRevenueByCategory;


    private static String url_Revenuebycategory;

    private int hospitalID_Constant;

    // ArrayList<Appointments_Model> appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> new_appointments_List = new ArrayList<Appointments_Model>();
    ArrayList<Appointments_Model> doc_ConsultationType_Amt_List = new ArrayList<Appointments_Model>();

    private int mDoctor_id, mHospital_id;

    private DatePicker mDatepicker_Dialog;


    private SimpleDateFormat mDateFormat;

    String FromDate, ToDate;

    LinearLayout calender1, calender2;

    private Button Report_btn, mCancel_btn_Dialog, mSet_btn_Dialog;

    TextView date1, date2, month1, month2, year1, year2;

    PieChart mChart;
    // we're going to display pie chart for Patients arrival

    private String[] xValues = {"Consultation", "Pharmacy"};


    private int[] yValues = {ConsAmt, PharAmount};


    //private static float[] yValues= {"sai_Global"};
    //private static String[] xValues;


    // colors for different sections in pieChart
    public static final int[] MY_COLORS = {
            Color.rgb(0, 128, 255), Color.rgb(176, 0, 0

    ), Color.rgb(153, 204, 255),
            Color.rgb(38, 40, 53), Color.rgb(215, 60, 55)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revenuebycategory);
        android.app.ActionBar bar = getActionBar();
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'></font>"));

        mChart = (PieChart) findViewById(R.id.chart2);
        mChart.setTransparentCircleAlpha(0);
        mChart.setHoleRadius(0f);

        getIds();
        calender_view();
        calender_view1();
        getData();
        Report_btn_click();
        //create_Chart();

        get_revenuebycategory_Data();

        calender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datepicker1();

            }
        });
        calender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();

            }
        });


        //mChart.setUsePercentValues(true);
        mChart.setDescription("");

        mChart.setRotationEnabled(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(RevenueByCategory.this, xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void Report_btn_click() {
        Report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_revenuebycategory_Data();
                setDataForPieChart();
            }
        });
    }

    private void get_revenuebycategory_Data() {

        if (isInternetOn() == true) {

            int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
            int mHospital_id = Integer.parseInt(hospitalId_List.get(0));

            StringBuffer sb_revenuebycategory = new StringBuffer();
            sb_revenuebycategory.append(Constants.URL + "RevenueReport" + "?" + "DoctorId=" + mDoctor_id + "&FromDate=" + FromDate + "&ToDate=" + ToDate + "&HospitalId=" + mHospital_id);
            url_Revenuebycategory = sb_revenuebycategory.toString();
            System.out.println("GetRevenueCategory_url---" + url_Revenuebycategory);
            new RevenueReport().execute(url_Revenuebycategory);
        } else {
            // Internet connection is not available
            Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    // Checking for the connectivity of Internet
    private boolean isInternetOn() {
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


        int mDoctor_id = Integer.parseInt(doctorId_List.get(0));
        String mDoctor_name = doctorName_List.get(0);
        int mHospital_id = Integer.parseInt(hospitalId_List.get(0));
        Dashboard_Model append_model = new Dashboard_Model(mDoctor_id, mHospital_id);
        doctors_list.add(append_model);
    }


    private void calender_view1() {
        mDateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy");


        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        SimpleDateFormat month_date1 = new SimpleDateFormat("MM");
        String month_name = month_date.format(c.getTime());
        String month_name1 = month_date1.format(c.getTime());
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        ToDate = mYear + "-" + month_name1 + "-" + (mDay);


        date2.setText((String.valueOf(mDay)));
        month2.setText((String.valueOf(month_name)));
        year2.setText((String.valueOf(mYear)));


    }

    private void datepicker1() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog = new Dialog(RevenueByCategory.this);
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


                String selected_Date = mDatepicker_Dialog.getYear() + "-" + (mDatepicker_Dialog.getMonth() + 1) + "-" + mDatepicker_Dialog.getDayOfMonth();
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                // SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("dd");
                SimpleDateFormat output1 = new SimpleDateFormat("MMM");
                SimpleDateFormat output2 = new SimpleDateFormat("yyyy");
                SimpleDateFormat output3 = new SimpleDateFormat("dd");

                try {

                    Date oneWayDate = input.parse(selected_Date);  // parse input
                    ToDate = input.format(oneWayDate);

                   /* ToDate = input.parse(selected_Date).toString();
                    Date oneWayDate = input.parse(selected_Date);  // parse input*/

                    //date1.setText(output.format(oneWayDate));
                    date2.setText(output3.format(oneWayDate));
                    //month1.setText(output1.format(oneWayDate));
                    month2.setText(output1.format(oneWayDate));
                    // year1.setText(output2.format(oneWayDate));
                    year2.setText(output2.format(oneWayDate));// format output


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


    private void datepicker() {
        // TODO Auto-generated method stub
        // Create custom dialog object
        final Dialog dialog = new Dialog(RevenueByCategory.this);
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

                String selected_Date = mDatepicker_Dialog.getYear() + "-" + (mDatepicker_Dialog.getMonth() + 1) + "-" + mDatepicker_Dialog.getDayOfMonth();

                int mDay = c.get(Calendar.DAY_OF_MONTH);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                // SimpleDateFormat input = new SimpleDateFormat("dd-M-yyyy");
                SimpleDateFormat output = new SimpleDateFormat("dd");
                SimpleDateFormat output1 = new SimpleDateFormat("MMM");
                SimpleDateFormat output2 = new SimpleDateFormat("yyyy");

                try {

                    Date oneWayDate = input.parse(selected_Date);  // parse input
                    FromDate = input.format(oneWayDate);

                    date1.setText(output.format(oneWayDate));
                    //date2.setText(output3.format(oneWayDate));
                    month1.setText(output1.format(oneWayDate));
                    //month2.setText(output1.format(oneWayDate));
                    year1.setText(output2.format(oneWayDate));
                    //year2.setText(output2.format(oneWayDate));// format output


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


    private void calender_view() {

        mDateFormat = new SimpleDateFormat("EEE, d-MMM-yyyy");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        int mYear = c.get(Calendar.YEAR);
        SimpleDateFormat month_date1 = new SimpleDateFormat("MM");
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(c.getTime());
        String month_name1 = month_date1.format(c.getTime());
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        FromDate = mYear + "-" + month_name1 + "-" + mDay;
        date1.setText((String.valueOf(mDay)));
        month1.setText((String.valueOf(month_name)));
        year1.setText((String.valueOf(mYear)));

    }

    private void getIds() {


        Revenuebycategory = (TextView) findViewById(R.id.revenuebycategory);
        SpannableString content = new SpannableString("R e v e n u e   B y   C a t e g o r y");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        Revenuebycategory.setText(content);
        Report_btn = (Button) findViewById(R.id.Report);
        Total = (TextView) findViewById(R.id.Total2);
        calender1 = (LinearLayout) findViewById(R.id.calender1);
        calender2 = (LinearLayout) findViewById(R.id.calender2);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        month1 = (TextView) findViewById(R.id.month1);
        month2 = (TextView) findViewById(R.id.month2);
        year1 = (TextView) findViewById(R.id.year1);
        year2 = (TextView) findViewById(R.id.year2);

    }


    public void setDataForPieChart() {


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }


    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }


    @Override
    public void onBackPressed() {
        // do something on back.
//	    			Toast.makeText(getApplicationContext(), "go back", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(RevenueByCategory.this, Patient_Attendance.class);
        startActivity(i);
        finish();
        // return;
    }

    private class RevenueReport extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_GetRevenueByCategory = new ProgressDialog(RevenueByCategory.this);
            pDialog_GetRevenueByCategory.setMessage("Please Wait.");
            pDialog_GetRevenueByCategory.setCancelable(false);
            pDialog_GetRevenueByCategory.show();
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

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject1 = jArray.getJSONObject(i);

                            ConsAmt = jObject1.getInt("ConsAmt");
                            PharAmount = jObject1.getInt("PharAmount");
                            TotalAmount = jObject1.getInt("TotalAmount");
                            PerConsAmt = jObject1.getInt("PerConsAmt");
                            PerPharAmt = jObject1.getInt("PerPharAmt");


                            yValues[0] = ConsAmt;
                            yValues[1] = PharAmount;
                            // yValues[1]= Integer.valueOf(String.valueOf(PharAmount)+String.valueOf(PerPharAmt));
                            int i1 = TotalAmount;
                            Total.setText(String.valueOf(i1));



                        /*    yValues[2]=TotalAmount;
                            yValues[3]=PerConsAmt;
                            yValues[4]=PerPharAmt;
*/
                            Appointments_Model appointments_model = new Appointments_Model(ConsAmt, PharAmount, TotalAmount, PerConsAmt, PerPharAmt);
                            appointments_List.add(appointments_model);
                            setDataForPieChart();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // display toast here
                                Toast.makeText(getApplicationContext(), "Revenue Details Collected", Toast.LENGTH_SHORT).show();
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
            if (pDialog_GetRevenueByCategory.isShowing()) ;
            pDialog_GetRevenueByCategory.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


        } // onPostExecute() end
    } // ProcessJSON class end

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctorsname_list_menu1, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.power_doctorsname_list_menu:


                Intent i_home = new Intent(RevenueByCategory.this, Patient_Attendance.class);
                startActivity(i_home);
                finish();
                break;

        }

        return true;

    }
} // Activity end


