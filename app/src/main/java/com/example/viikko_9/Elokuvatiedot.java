package com.example.viikko_9;

import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Elokuvatiedot {
    String Elokuva;
    String Aika;


    public Elokuvatiedot(String elokuva, String aika) throws ParseException {
        Elokuva = elokuva;
        Aika = aika;
        if (Aika.equals("") ){
            System.out.println("Tyhjä päivämäärä");
        } else {
            SimpleDateFormat formatinput = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date d = formatinput.parse(Aika);
            SimpleDateFormat formatoutput = new SimpleDateFormat("HH:mm");
            String uAika = formatoutput.format(d);
            Aika = uAika;
        }

    }


}