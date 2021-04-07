package com.ihomeo.ihomeouploading.model;

/**
 * Created by mr on 07/07/2016.
 */
public class login_Model {

    private int PartyId,EmployeeId,DoctorId;

    private String userName;
    private String password;
    private String AppVersion;
    private String InstallDate;
    private String ReleaseDate;


    public login_Model(String muserName, int PartyId, int EmployeeId ,String mAppVersion,String ReleaseDate,String mInstallDate) {
        // TODO Auto-generated constructor stub
        this.userName = muserName;
        this.PartyId = PartyId;
        this.EmployeeId=EmployeeId;

        this.AppVersion = mAppVersion;
        this.ReleaseDate = ReleaseDate;
        this.InstallDate = mInstallDate;

    }
    public login_Model(String muserName, String mpassword, String mAppVersion,String mInstallDate) {
        // TODO Auto-generated constructor stub
        this.userName=muserName;
        this.password=mpassword;
        this.AppVersion=mAppVersion;
        this.InstallDate=mInstallDate;
    }
    public void setuserName(String myuserName) {this.userName = myuserName;}
    public String getuserName() {
        return this.userName;
    }

    public void setPassword(String myPassword) {
        this.password = myPassword;
    }
    public String getPassword() {
        return this.password;
    }

    public void setAppVersion(String myAppversion) {
        this.AppVersion = myAppversion;
    }
    public String getAppVersion() {
        return this.AppVersion;
    }


    public void setInstallDate(String myInstallDate){
        this.InstallDate=myInstallDate;
    }
    public String getInstallDate() {return this.InstallDate;}
}


