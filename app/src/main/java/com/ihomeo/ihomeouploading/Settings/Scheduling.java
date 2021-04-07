
package com.ihomeo.ihomeouploading.Settings;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ihomeo.ihomeouploading.ihomeoactivity.R;

import java.util.Calendar;

/**
 * Created by Sai on 1/9/2017.
 */

public class Scheduling extends Activity {

    public String Result, Result1;


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DT1 = "nameKey";
    public static final String DT2 = "phoneKey";
    public static final String DT3 = "emailKey";
    public static final String DT4 = "emailKey";
    public static final String Ed1 = "emailKey";
    public static final String Ed2 = "emailKey";
    public static final String Cb1 = "emailKey";
    public static final String Cb2 = "emailKey";


    TextView AccessTime;
    EditText DisplayTime, DisplayTime2, DisplayTime3, DisplayTime4, ED1, ED2;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private CheckBox MorningCheckBox, EveningCheckBox;

    Button Next, Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling);


        getIds();

        shared_Pref_Data();

        addListenerOnmorningCheckbox();

        addListenerOneveningCheckbox();

        NxtBtnClick();

        CancelBtnClick();


        DisplayTime = (EditText) findViewById(R.id.Edittext1);
        DisplayTime2 = (EditText) findViewById(R.id.Edittext2);
        DisplayTime3 = (EditText) findViewById(R.id.Edittext3);
        DisplayTime4 = (EditText) findViewById(R.id.Edittext4);

        DisplayTime4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(Scheduling.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                DisplayTime4.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();


            }
        });

        DisplayTime3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(Scheduling.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                DisplayTime3.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }

        });


        DisplayTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(Scheduling.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                DisplayTime2.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }


        });

        DisplayTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(Scheduling.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                DisplayTime.setText(hourOfDay + ":" + minute + "" + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });
    }


    private void CancelBtnClick() {
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Scheduling.this, Settings.class);

                startActivity(i);


            }
        });

    }


    private void NxtBtnClick() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (MorningCheckBox.isChecked() == false && EveningCheckBox.isChecked() == false) {

                    Intent i = new Intent(Scheduling.this, Settings.class);

                    startActivity(i);

                }


                if (MorningCheckBox.isChecked() == true) {
                    String userName = DisplayTime.getText().toString();
                    String password = DisplayTime2.getText().toString();


                    if (userName.equals("")) {
                        Toast.makeText(Scheduling.this, "ENTER THE FIELD", Toast.LENGTH_LONG).show();

                    } else if (password.equals("")) {
                        Toast.makeText(Scheduling.this, "ENTER THE FIELD", Toast.LENGTH_LONG).show();


                    }
                    //get_installed_date();

                }
                if (EveningCheckBox.isChecked() == true) {
                    String userName = DisplayTime3.getText().toString();
                    String password = DisplayTime4.getText().toString();

                    if (userName.equals("")) {
                        Toast.makeText(Scheduling.this, "ENTER THE FIELD", Toast.LENGTH_LONG).show();

                    } else if (password.equals("")) {
                        Toast.makeText(Scheduling.this, "ENTER THE FIELD", Toast.LENGTH_LONG).show();


                        // isInternetOn();

                    }
                }


                String n = DisplayTime.getText().toString();
                String ph = DisplayTime2.getText().toString();
                String e = DisplayTime3.getText().toString();
                String p = DisplayTime4.getText().toString();

                String q = ED1.getText().toString();

                String r = ED2.getText().toString();


                String z = Result;

                String a = Result1;


                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(DT1, n);
                editor.putString(DT2, ph);
                editor.putString(DT3, e);
                editor.putString(DT4, p);
                editor.putString(Ed1, q);
                editor.putString(Ed2, r);
                editor.putString(Cb1, z);
                editor.putString(Cb2, a);


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(DisplayTime.getWindowToken(), 0);


                editor.commit();


             /*   Intent i = new Intent(Scheduling.this,Scheduling2.class);
                startActivity(i);

*/

            }
        });
    }


    private void shared_Pref_Data() {


    }

    private void addListenerOneveningCheckbox() {

        EveningCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  String Result1;
                if (EveningCheckBox.isChecked() == true)
                    Result1 = "Y";
                else
                    Result1 = "N";


            }


        });


    }

    private void getIds() {

        MorningCheckBox = (CheckBox) findViewById(R.id.morningCheckBox);
        EveningCheckBox = (CheckBox) findViewById(R.id.eveningCheckBox);

        ED1 = (EditText) findViewById(R.id.EdittextDuration);
        ED2 = (EditText) findViewById(R.id.EdittextDuration2);

        Next = (Button) findViewById(R.id.next);


        Cancel = (Button) findViewById(R.id.cancel);

    }

    private void addListenerOnmorningCheckbox() {


        MorningCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (MorningCheckBox.isChecked() == true)

                    Result = "Y";
                else
                    Result = "N";
            }


        });

    }
}