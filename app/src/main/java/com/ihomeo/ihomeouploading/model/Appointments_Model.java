package com.ihomeo.ihomeouploading.model;

public class Appointments_Model {

    private int appointment_id;
    private String from_time = null;
    private String to_time = null;
    private String patient_name = null;
    private String book = null;
    private String arrived = null;
    private String ForDate = null;
    private String pay_consultation = null;
    private String pay_pharmacy = null;
    private int token_number;
    private String mobile_number = null;
    private String new_patient_name = null;
    private String mr_Number = null;
    private int consultationType_id, App_Master_Id;
    private String consultation_Type = null;
    private String consultation_Type_amt = null;
    private String mMobile_number_Call = null;
    private int TotalCount, NewCount, OldCount, ConsAmt, PharAmount, TotalAmount, PerConsAmt, PerPharAmt;

    public Appointments_Model(String from_Time2, String to_Time2, String patient_name, String mBook, String mArrived, String mPay_Consultation, String mPay_Pharmacy, int token_number, int App_Master_Id, String Mobile) {
        // TODO Auto-generated constructor stub
        this.from_time = from_Time2;
        this.to_time = to_Time2;
        this.patient_name = patient_name;
        this.book = mBook;
        this.arrived = mArrived;
        this.pay_consultation = mPay_Consultation;
        this.pay_pharmacy = mPay_Pharmacy;
        this.token_number = token_number;
        this.App_Master_Id = App_Master_Id;
        this.mMobile_number_Call = Mobile;

    }


    public Appointments_Model(int TotalCount, int NewCount, int OldCount) {


        this.TotalCount = TotalCount;
        this.NewCount = NewCount;
        this.OldCount = OldCount;

    }


    public Appointments_Model() {
        // TODO Auto-generated constructor stub
    }

    public Appointments_Model(String newPatient_Name, String mMR_Number) {
        // TODO Auto-generated constructor stub
        this.new_patient_name = newPatient_Name;
        this.mr_Number = mMR_Number;
    }


    public Appointments_Model(int mConsultation_ID, String mConsultation_Type, String mConsultation_Amt) {
        // TODO Auto-generated constructor stub

        this.consultationType_id = mConsultation_ID;
        this.consultation_Type = mConsultation_Type;
        this.consultation_Type_amt = mConsultation_Amt;
    }

    public void setAppointments_ID(int mAppointment_id) {
        this.appointment_id = mAppointment_id;
    }

    public int getAppointments_ID() {
        return this.appointment_id;
    }

    public void setFrom_Time(String mFrom_time) {
        this.from_time = mFrom_time;
    }

    public String getFrom_Time() {
        return this.from_time;
    }

    public void setTo_Time(String mTo_time) {
        this.to_time = mTo_time;
    }

    public String getTo_Time() {
        return this.to_time;
    }

    public void setPatient_Name(String mPatient_name) {
        this.patient_name = mPatient_name;
    }

    public String getPatient_Name() {
        return this.patient_name;
    }


    public void setBook(String mBook) {
        this.book = mBook;
    }

    public String getBook() {
        return this.book;
    }

    public void setArrived(String mArrived) {
        this.arrived = mArrived;
    }

    public String getArrived() {
        return this.arrived;
    }

    public void setPay_Consultation(String mPay_Consultation) {
        this.pay_consultation = mPay_Consultation;
    }

    public String getPay_Consultation() {
        return this.pay_consultation;
    }

    public void setPay_Pharmacy(String mPay_Pharmacy) {
        this.pay_pharmacy = mPay_Pharmacy;
    }

    public String getPay_Pharmacy() {
        return this.pay_pharmacy;
    }

    public void setToken_Number(int mToken_numb) {
        this.token_number = mToken_numb;
    }

    public int getToken_Number() {
        return this.token_number;
    }

    public void setMobile_Numb(String mMobile_number) {
        this.mobile_number = mMobile_number;
    }

    public String getMobile_Numb() {
        return this.mobile_number;
    }


    public void setMobile_NumbCall(String mMobile_number_Call) {
        this.mMobile_number_Call = mMobile_number_Call;
    }

    public String getMobile_numberCall() {
        return this.mMobile_number_Call;
    }

    public void setNew_PatientName(String mNew_patientName) {
        this.new_patient_name = mNew_patientName;
    }

    public String getNew_PatientName() {
        return this.new_patient_name;
    }

    public void setMR_Number(String mMR_Number) {
        this.mr_Number = mMR_Number;
    }

    public String getMR_Number() {
        return this.mr_Number;
    }

    public void setConsultationType_ID(int mConsultationType_id) {
        this.consultationType_id = mConsultationType_id;
    }

    public int getConsultationType_ID() {
        return this.consultationType_id;
    }

    public int getNewCount() {
        return this.NewCount;
    }

    public int getOldCount() {
        return this.OldCount;
    }


    public int getTotalCount() {
        return this.TotalCount;
    }


    public Appointments_Model(int ConsAmt, int PharAmount, int TotalAmount, int PerConsAmt, int PerPharAmt) {


        this.ConsAmt = ConsAmt;
        this.PharAmount = PharAmount;
        this.TotalAmount = TotalAmount;
        this.PerConsAmt = PerConsAmt;
        this.PerPharAmt = PerPharAmt;
    }


    //revenuebycategory

    public int getConsAmt() {
        return this.ConsAmt;
    }

    public int getPharAmount() {
        return this.PharAmount;
    }

    public int getTotalAmount() {
        return this.TotalAmount;
    }

    public int PerConsAmt() {
        return this.PerConsAmt;
    }

    public int getPerPharAmt() {
        return this.PerPharAmt;
    }


    public void setConsultation_Type(String mConsultation_Type) {
        this.consultation_Type = mConsultation_Type;
    }

    public String getConsultation_Type() {
        return this.consultation_Type;
    }

    public void setConsultation_Type_Amount(String mConsultation_Type_Amount) {
        this.consultation_Type_amt = mConsultation_Type_Amount;
    }

    public String getConsultation_Type_Amount() {
        return this.consultation_Type_amt;
    }

}
