package com.example.srikarteratipally.offpay;

public class Transaction {
    private int id;
    private String amount;
    private String FromUser;
    private String ToUser;
    public Transaction(){}
    public Transaction(String amount,String s, String s1)
    {
        this.amount=amount;
        this.FromUser=s;
        this.ToUser=s1;
    }
    public Transaction(int id,String amount,String FromUser,String ToUser)
    {
        this.id=id;
        this.amount=amount;
        this.FromUser=FromUser;
        this.ToUser=ToUser;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAmount(String amount){this.amount=amount;}
    public void setFromUser(String FromUser) {
        this.FromUser = FromUser;
    }

    public void setToUser(String ToUser) {
        this.ToUser = ToUser;
    }
    public int getId() {
        return id;
    }
    public String getAmount() {return amount;}
    public String getToUser() {
        return ToUser;
    }
    public String getFromUser() {
        return FromUser;
    }
}



