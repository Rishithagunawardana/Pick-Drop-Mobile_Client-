package com.example.pickdrop;

public class requests {
    String NAME;
    String TELNO;
    String  DATE;
    String PASSENGERS;
    String DESTINATION;
    String ACTYPE;



    public String getNAME() {
        return NAME;
    }

    public String getTELNO() {
        return TELNO;
    }

    public String getDATE() {
        return DATE;
    }

    public String getPASSENGERS() {
        return PASSENGERS;
    }

    public String getDESTINATION() {
        return DESTINATION;
    }

    public String getACTYPE() {
        return ACTYPE;
    }

    public requests(String NAME, String TELNO,String DATE,String PASSENGERS,String DESTINATION, String ACTYPE ) {
        this.NAME = NAME;
        this.TELNO = TELNO;
        this.DATE = DATE;
        this.PASSENGERS = PASSENGERS;
        this.DESTINATION = DESTINATION;
        this.ACTYPE = ACTYPE;


    }
}
