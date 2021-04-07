package com.ihomeo.ihomeouploading.ihomeoactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sai on 1/13/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";


    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, recent_token);


            Shared_Preferences();
    }



    private void Shared_Preferences() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);
        SharedPreferences emp_Id_Pref1 = getSharedPreferences("recent_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor emp_Id_Pref_edit1 = emp_Id_Pref1.edit();
        emp_Id_Pref_edit1.putString("recent_token", recent_token);
        emp_Id_Pref_edit1.commit();

    }
}
