package com.example.viikko_9;

import java.util.ArrayList;

public class Teatterit {
    String Teatteri;
    String tID;

    public Teatterit(String teatteri, String TID){
        Teatteri = teatteri;
        tID = TID;
    }

    public String gettID() {
        return tID;
    }
}
