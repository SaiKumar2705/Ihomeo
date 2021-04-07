package com.ihome.ihomeo.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ihomeo.ihomeouploading.ihomeoactivity.R;
import com.ihomeo.ihomeouploading.model.Appointments_Model;

public class New_AppointmentsDialog_Adapter extends ArrayAdapter<Appointments_Model> {

    Context context;
    private LayoutInflater vi;
    public ArrayList<Appointments_Model> newPatients_list;

    public New_AppointmentsDialog_Adapter(Context context, int textViewResourceId,
                                          ArrayList<Appointments_Model> new_Appontments_list) {


        super(context, textViewResourceId, new_Appontments_list);
        this.context = context;
        this.newPatients_list = new_Appontments_list;

    }

    private class ViewHolder {
        TextView new_patientName, mr_Number;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = vi.inflate(R.layout.new_appointments_dialog_row, null, true);

            holder = new ViewHolder();
            holder.new_patientName = (TextView) convertView.findViewById(R.id.new_patient_txv_new_appointments_dialog_row);
            //
            // holder.mr_Number=(TextView) convertView.findViewById(R.id.exsistingmrnumber);

            convertView.setTag(holder);
            convertView.setTag(R.id.new_patient_txv_new_appointments_dialog_row, holder.new_patientName);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Appointments_Model app = newPatients_list.get(position);

        Log.e("bhuuuuuuuuuuu", "" + app.getNew_PatientName());
        holder.new_patientName.setText(app.getMR_Number() + "/ " + app.getNew_PatientName());
        //holder.mr_Number.setText(app.getMR_Number());


        convertView.setBackgroundResource(R.color.white);
//if ( position % 2 == 0)
//{
//	convertView.setBackgroundResource(R.color.gray);
//}else{
//	convertView.setBackgroundResource(R.color.white);
//}
        return convertView;
    }


}