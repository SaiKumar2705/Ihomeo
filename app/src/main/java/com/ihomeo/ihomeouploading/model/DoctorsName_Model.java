package com.ihomeo.ihomeouploading.model;

public class DoctorsName_Model {
	private int doctor_id;
	private String doctor_name = null;
	private int hospital_id;
	
	public DoctorsName_Model(int mDoctor_id, String mDoctor_name, int mHospital_id) {
		// TODO Auto-generated constructor stub
		this.doctor_id=mDoctor_id;
		this.doctor_name=mDoctor_name;
		this.hospital_id=mHospital_id;
	}
	public void setDoctor_Id(int mylogin_id) {
	    this.doctor_id = mylogin_id;
	}
	public int getDoctor_ID() {
	    return this.doctor_id;
	}


	public void setDoctor_Name(String mDoctor_Name) {
	    this.doctor_name = mDoctor_Name;
	}
	public String getDoctor_Name() {
	    return this.doctor_name;
	}
	
	public void setHospital_Id(int hospital_id){
		this.hospital_id=hospital_id;
	}
	public int getHospital_Id() {
		return this.hospital_id;
		
	}
}
