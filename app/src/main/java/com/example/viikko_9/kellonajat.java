package com.example.viikko_9;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class kellonajat {
    Date d1, d2, d3;
    private static kellonajat kello = new kellonajat();

    public static kellonajat getInstance(){

        return kello;
    }

    public boolean result(String vertaus) throws ParseException {
        SimpleDateFormat formatinput = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        System.out.println(vertaus);
        Date d = formatinput.parse(vertaus);
        SimpleDateFormat formatoutput = new SimpleDateFormat("HH:mm");
        String uusi = formatoutput.format(d);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        if(d1 == null){ //Jos ensimmäistä aikaa ei ole alustettu niin ei unohdetaan se
            try {
                d3 = sdf.parse(uusi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long x = d3.getTime();
            long z = getEnnen().getTime();
            if (x <= z){
                return true;
            } else {
                return false;
            }

        } else if (d2 == null){

            try {
                d3 = sdf.parse(uusi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long y = getJalkeen().getTime();
            long x = d3.getTime();
            if (x >=  y ){
                return true;
            } else {
                return false;
            }
        } else {
            try {
                d3 = sdf.parse(uusi);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long y = getJalkeen().getTime();
            long x = d3.getTime();
            long z = getEnnen().getTime();
            if (x >=  y && x <= z){
                return true;
            } else {
                return false;
            }
        }

    }

    public void setJalkeen(String aika){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            d1 = sdf.parse(aika);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Date getJalkeen(){
        return d1;
    }


    public void setEnnen(String aika){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            d2 = sdf.parse(aika);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Date getEnnen(){
        return d2;
    }
}
