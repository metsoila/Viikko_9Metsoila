package com.example.viikko_9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView valittupvm;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Elokuvatiedot> lista;
    Context context = null;
    String spinner, pickTeatteri, TeatteriID, pvm, pvmstring, a1string, a2string, urlString;
    ;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> PaikatSpinner = new ArrayList<String>();
    ArrayList<Teatterit> Teatterit = new ArrayList<Teatterit>();
    TextView aika1, aika2;
    EditText nimihaku;
    int a1hour, a1minute, a2hour, a2minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nimihaku = findViewById(R.id.elokuvahaku);
        aika1 = findViewById(R.id.aika1);
        aika1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        a1hour = hourOfDay;
                        a1minute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, a1hour, a1minute);
                        aika1.setText("Aloita jälkeen " + DateFormat.format("HH:mm", calendar));
                        a1string = (String) DateFormat.format("HH:mm", calendar);
                        kellonajat kello = kellonajat.getInstance();
                        kello.setJalkeen(a1string);
                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(a1hour, a1minute);
                timePickerDialog.show();
            }
        });
        aika2 = findViewById(R.id.aika2);
        aika2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        a2hour = hourOfDay;
                        a2minute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, a2hour, a2minute);
                        aika2.setText("Aloita ennen " + DateFormat.format("HH:mm", calendar));
                        a2string = (String) DateFormat.format("HH:mm", calendar);
                        kellonajat kello = kellonajat.getInstance();
                        kello.setEnnen(a2string);
                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(a2hour, a2minute);
                timePickerDialog.show();
            }
        });


        valittupvm = findViewById(R.id.valittupvm);
        context = MainActivity.this;
        Spinner spinner = findViewById(R.id.spinneri);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Elokuvat
        lista = new ArrayList<Elokuvatiedot>(); //Elokuvat-lista
        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(lista);//Lista viedään RecyclerAdapter-Luokkaan
        recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //Teatterit
        PaikatSpinner.add("");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, PaikatSpinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pickTeatteri = parent.getItemAtPosition(position).toString();
                TeatteriID = getID(pickTeatteri);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc1 = builder.parse(urlString);
            doc1.getDocumentElement().normalize();
            NodeList nList = doc1.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    PaikatSpinner.add(i, String.valueOf(element.getElementsByTagName("Name").item(0).getTextContent()));
                    Teatterit.add(i, new Teatterit(String.valueOf(element.getElementsByTagName("Name").item(0).getTextContent()),
                            String.valueOf(element.getElementsByTagName("ID").item(0).getTextContent())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        findViewById(R.id.valitsepvm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        pvm = ("Näytetään elokuvia päivältä " + day + "." + (month + 1) + "." + year); //+1 koska indeksi
        if (month > 8) {
            pvmstring = day + "." + (month + 1) + "." + year;
        } else {
            pvmstring = day + ".0" + (month + 1) + "." + year; //(esim. 9->09, mutta ei 10->010)
        }
    }


    public void ListMovies(View v) {
        try {
            lista.clear();
            if (pickTeatteri.equals("Valitse alue/teatteri")) {
                Toast.makeText(getApplicationContext(), "Valitse ensin teatteri", Toast.LENGTH_SHORT).show();
            } else {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                System.out.println(urlString);
                urlString = ("https://www.finnkino.fi/xml/Schedule/?area=" + TeatteriID + "&dt=" + pvmstring);
                System.out.println(urlString);
                Document doc = builder.parse(urlString);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
                kellonajat ajat = kellonajat.getInstance();
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String vAika = String.valueOf(element.getElementsByTagName("dttmShowStart").item(0).getTextContent());

                        if (a1string == null && a2string == null) { //JOS AJAT ALUSTETTU NIIN RAJATAAN MITÄ LISTALLE LAITETAAN.
                            lista.add(new Elokuvatiedot(String.valueOf(element.getElementsByTagName("Title").item(0).getTextContent()), String.valueOf(element.getElementsByTagName("dttmShowStart").item(0).getTextContent())));
                        } else {
                            if (ajat.result(vAika) == true) {
                                lista.add(new Elokuvatiedot(String.valueOf(element.getElementsByTagName("Title").item(0).getTextContent()), String.valueOf(element.getElementsByTagName("dttmShowStart").item(0).getTextContent())));
                            }
                        }

                    }
                }
                if (lista.size() == 0) {
                    lista.add(new Elokuvatiedot("Valitsemillanne asetuksilla ei löytynyt yhtään näytöstä", ""));
                }
                recyclerAdapter = new RecyclerAdapter(lista);
                recyclerView.setAdapter(recyclerAdapter);
                valittupvm.setText(pvm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

   public String getID(String teatteri) {
       try {
           DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
           String url = "https://www.finnkino.fi/xml/TheatreAreas/";
           Document doc = builder.parse(url);
           doc.getDocumentElement().normalize();

           NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
           for (int i = 0; i < nList.getLength(); i++) {
               Node node = nList.item(i);
               if (node.getNodeType() == Node.ELEMENT_NODE) {
                   Element element = (Element) node;
                   if (teatteri.equals(String.valueOf(element.getElementsByTagName("Name").item(0).getTextContent()))) {
                       return String.valueOf(element.getElementsByTagName("ID").item(0).getTextContent());
                   }

               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       } catch (SAXException e) {
           e.printStackTrace();
       } catch (ParserConfigurationException e) {
           e.printStackTrace();
       }
       return "Virhe";
   }

    public void searchMovie(View v) {
        lista.clear();
        try {
            lista.clear();
            for (int j = 0; j < Teatterit.size(); j++) {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                urlString = ("https://www.finnkino.fi/xml/Schedule/?area=" + Teatterit.get(j).gettID() + "&dt=" + pvmstring);
                Document doc = builder.parse(urlString);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node1 = nList.item(i);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node1;
                        String movie = String.valueOf(element.getElementsByTagName("Title").item(0).getTextContent());
                        if (movie.equals(nimihaku.getText().toString())) {
                            lista.add(new Elokuvatiedot(String.valueOf(element.getElementsByTagName("Theatre").item(0).getTextContent()), String.valueOf(element.getElementsByTagName("dttmShowStart").item(0).getTextContent())));
                        }
                    }
                }
            }
            if (lista.size() == 0) {
                lista.add(new Elokuvatiedot("Valitsemillanne asetuksilla ei löytynyt yhtään näytöstä", ""));
            }
            recyclerAdapter = new RecyclerAdapter(lista);
            recyclerView.setAdapter(recyclerAdapter);
            valittupvm.setText(pvm);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}