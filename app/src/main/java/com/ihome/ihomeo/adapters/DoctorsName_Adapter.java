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
import com.ihomeo.ihomeouploading.model.DoctorsName_Model;


public class DoctorsName_Adapter extends ArrayAdapter<DoctorsName_Model> {

    Context context;
    private LayoutInflater vi;
    public ArrayList<DoctorsName_Model> doctor_list;

    public DoctorsName_Adapter(Context context, int textViewResourceId,
                               ArrayList<DoctorsName_Model> buildman_list) {


        super(context, textViewResourceId, buildman_list);
        this.context = context;
        this.doctor_list = buildman_list;

    }

    private class ViewHolder {
        TextView doctor_name;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vi = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = vi.inflate(R.layout.doctorsname_list_row, null, true);

            holder = new ViewHolder();
            holder.doctor_name = (TextView) convertView.findViewById(R.id.names_txv_doctorsname_row);


            convertView.setTag(holder);
            convertView.setTag(R.id.names_txv_doctorsname_row, holder.doctor_name);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DoctorsName_Model app = doctor_list.get(position);


        holder.doctor_name.setText(app.getDoctor_Name());

//convertView.setBackgroundResource(R.color.white);
//if ( position % 2 == 0)
//{
//	convertView.setBackgroundResource(R.color.gray);
//}else{
//	convertView.setBackgroundResource(R.color.white);
//}
        return convertView;
    }


}