package com.example.android.payup4;

/**
 * Created by hp 15-ab032tx on 23-03-2017.
 */

public class Personal {

    private String reason;
    private String account;
    private String sent;
    private String me;
    private String amount;
    private String sent_rec;

    public Personal() {
    }

    public Personal(String reason1,String mee, String account1,String amount1, String sent)
    {
        reason=reason1;
        me=mee;
        account = account1;
        amount=amount1;
        sent_rec=sent;
    }

    public String getReason(){
        return reason;
    }
    public String getAccount()
    {
        return account;
    }
    public String getSent()
    {
        return sent_rec;
    }
    public String getUser(){
        return me;
    }

    public String getAmount(){
        return amount;
    }


    public void setReason(String r){
        reason=r;
    }
    public void setAccount(String a)
    {
        account=a;
    }
    public void setSent(String se)
    {
        sent_rec=se;
    }
    public void setUser(String name){
        me=name;
    }

    public void setAmount(String amt){
        amount=amt;
    }

}
