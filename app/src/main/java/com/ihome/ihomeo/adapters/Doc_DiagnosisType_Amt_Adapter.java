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

import com.ihomeo.ihomeouploading.model.Diagnosis_Model;

import java.util.ArrayList;

/**
 * Created by Android on 2/17/2017.
 */

public class Doc_DiagnosisType_Amt_Adapter extends ArrayAdapter<Diagnosis_Model> {


    //Doc_ConsultationType_Amt_Adapter
    Context context;
    private LayoutInflater vi;
    public ArrayList<Diagnosis_Model> diagnosis_type_amt_list;

    public Doc_DiagnosisType_Amt_Adapter(Context context, int textViewResourceId, ArrayList<Diagnosis_Model> diagnosis_amt_list) {


        super(context, textViewResourceId, diagnosis_amt_list);
        this.context = context;
        this.diagnosis_type_amt_list = diagnosis_amt_list;

    }

    private class ViewHolder {
        TextView diagnosis_ID, diagnosis_Type;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Doc_DiagnosisType_Amt_Adapter.ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = vi.inflate(R.layout.doc_diagnosis_amt_type_dialog_row, null, true);

            holder = new Doc_DiagnosisType_Amt_Adapter.ViewHolder();
            holder.diagnosis_ID = (TextView) convertView.findViewById(R.id.diagnosis_type_txv_doc_consultation_amt_dialog_row);
            //holder.diagnosis_Type = (TextView) convertView.findViewById(R.id.diagnosis_amt_txv_doc_consultation_amt_dialog_row);

            convertView.setTag(holder);
            convertView.setTag(R.id.diagnosis_type_txv_doc_consultation_amt_dialog_row, holder.diagnosis_ID);
           // convertView.setTag(R.id.diagnosis_amt_txv_doc_consultation_amt_dialog_row, holder.diagnosis_Type);

            // ((Appointments) context).getDoctor_Consultaion_AmtTypes();

        } else {
            holder = (Doc_DiagnosisType_Amt_Adapter.ViewHolder) convertView.getTag();
        }
        Diagnosis_Model app = diagnosis_type_amt_list.get(position);

        Log.e("bhuuuuuuuuuuu", "" + app.getDiagnosisType_ID());

      //  holder.diagnosis_Type.setText("125");
        holder.diagnosis_ID.setText(app.getDiagnosisType_ID());

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
