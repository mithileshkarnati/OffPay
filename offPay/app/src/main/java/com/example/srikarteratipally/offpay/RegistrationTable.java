package com.example.srikarteratipally.offpay;

/**
 * Created by Mithilesh on 09-02-2017.
 */

public class RegistrationTable {
    private int id;
    private String UserMobile;
    private String Amount="500";
    private String PIN;
    public RegistrationTable(){}
    public RegistrationTable(String UserMobile,String s, String s1)
    {
        this.UserMobile=UserMobile;
        this.PIN=s;
        this.Amount=s1;
    }
    public RegistrationTable(int id,String UserMobile,String PIN,String Amount)
    {
        this.id=id;
        this.UserMobile=UserMobile;
        this.PIN=PIN;
        this.Amount=Amount;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUserMobile(String UserMobile){this.UserMobile=UserMobile;}
    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }
    public int getId() {
        return id;
    }
    public String getUserMobile() {return UserMobile;}
    public String getAmount() {
        return Amount;
    }
    public String getPIN() {
        return PIN;
    }
}



