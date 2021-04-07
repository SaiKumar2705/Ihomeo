package com.ihome.ihomeo.adapters;

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


import java.util.ArrayList;


public class Doc_ConsultationType_Amt_Adapter extends ArrayAdapter<Appointments_Model> {


    //Doc_ConsultationType_Amt_Adapter
    Context context;
    private LayoutInflater vi;
    public ArrayList<Appointments_Model> consultation_type_amt_list;

    public Doc_ConsultationType_Amt_Adapter(Context context, int textViewResourceId, ArrayList<Appointments_Model> consultation_amt_list) {


        super(context, textViewResourceId, consultation_amt_list);
        this.context = context;
        this.consultation_type_amt_list = consultation_amt_list;

    }

    private class ViewHolder {
        TextView consult_type, consult_amount;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = vi.inflate(R.layout.doc_consultation_amt_type_dialog_row, null, true);

            holder = new ViewHolder();
            holder.consult_type = (TextView) convertView.findViewById(R.id.consult_type_txv_doc_consultation_amt_dialog_row);
            holder.consult_amount = (TextView) convertView.findViewById(R.id.consult_amt_txv_doc_consultation_amt_dialog_row);

            convertView.setTag(holder);
            convertView.setTag(R.id.consult_type_txv_doc_consultation_amt_dialog_row, holder.consult_type);
            convertView.setTag(R.id.consult_amt_txv_doc_consultation_amt_dialog_row, holder.consult_amount);

            // ((Appointments) context).getDoctor_Consultaion_AmtTypes();

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Appointments_Model app = consultation_type_amt_list.get(position);

        Log.e("bhuuuuuuuuuuu", "" + app.getConsultation_Type());
        holder.consult_type.setText(app.getConsultation_Type());
        holder.consult_amount.setText(app.getConsultation_Type_Amount());

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
