package com.example.android.payup4;

/**
 * Created by hp 15-ab032tx on 12-03-2017.
 */

public class Word {
    private String owed_name;
    private String owed_amount;
    private String owed_type;
    private String description="No description added";
    private String me;

    public Word()
    {

    }

    public Word(String name, String amount, String type, String desc, String mee) {
        owed_name = name;
        owed_amount = amount;
        owed_type= type;
        description=desc;
        me=mee;
    }
    /*
    public Word(String name, String amount, String type, String mee) {
        owed_name = name;
        owed_amount = amount;
        owed_type= type;
        me=mee;
    }

    //only above 2 should remain

    public Word(String name, String amount, String type) {
        owed_name = name;
        owed_amount = amount;
        owed_type= type;
    }

*/

    public String getName() {
        return owed_name;
    }

    public String getAmount() {

        return owed_amount;
    }

    public String getowedType() {
        return owed_type;
    }

    public void setName(String name) {
        owed_name= name;
    }

    public void setAmount(String amt) {

        owed_amount=amt;
    }

    public String getDescription(){ return description; }

    public String getUser(){    return me; }

    public void setowedType(String type) {
        owed_type= type;
    }
}
