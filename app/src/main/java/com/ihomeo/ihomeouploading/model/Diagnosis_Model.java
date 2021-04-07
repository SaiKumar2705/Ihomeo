package com.ihomeo.ihomeouploading.model;

/**
 * Created by Android on 2/17/2017.
 */

public class Diagnosis_Model {


        private int mDiagnosis_ID;

        private String mDiagnosis_Type = null;


        public Diagnosis_Model(int mDiagnosis_ID, String mDiagnosis_Type) {

            this.mDiagnosis_ID = mDiagnosis_ID;
            this.mDiagnosis_Type = mDiagnosis_Type;

        }

       // public void setRegType(int mRegtype) {
           // this.RegType = mRegtype;


        public int getDiagnosis_Type() {
            return this.mDiagnosis_ID;
        }




      /* // public void setTillDate(String mTillDate) {
            this.TillDate = mTillDate;
        }*/

        public String getDiagnosisType_ID() {
            return this.mDiagnosis_Type;
        }



}



