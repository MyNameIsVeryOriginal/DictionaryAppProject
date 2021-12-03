package com.perso.DictionaryApp;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextInputLayout textInputLayout;
    String searchData;
    TextView word;
    TextView type;
    TextView definition;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allows connection on Main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Detecting stuffs from layout
        textInputLayout = findViewById(R.id.searchBar);
        word = findViewById(R.id.searchResult);
        type = findViewById(R.id.searchResult2);
        definition = findViewById(R.id.searchResult3);

    }

    // What the button does
    public void actionButtonSearch(View view) throws JSONException {

        searchData = textInputLayout.getEditText().getText().toString().trim();

        try {
            httpConnectionPerso("http://192.168.1.41:8080/crud/webapi/crud/words/" + searchData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ceci est le résultat " + result);

        System.out.println(isJSONValid(result));

        word.setText("Word : " + stringToJson(result).getString("word"));
        type.setText("Word type : " + stringToJson(result).getString("type"));
        definition.setText("Definition : \n" + deleteN(stringToJson(result).getString("definition")));

    }

    // HTTP connection
    public void httpConnectionPerso(String i_str_url) throws Exception {
        URL url = new URL(i_str_url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            ArrayList<String> l_array = fct_creerUneArrayListStringAPartirDunInputStream(in, "UTF8");
            result = l_array.get(0);
        }
        finally {
            urlConnection.disconnect();
        }
    }

    // Make an ArrayList from an InputStream
    public ArrayList<String> fct_creerUneArrayListStringAPartirDunInputStream(InputStream i_obj, String i_str_encodageStreamIN) throws Exception {
        ArrayList<String> o_arrayList_lignes = null;

        BufferedReader l_Reader					= null;
        InputStreamReader l_InputStreamReader_tampon	= null;
        StringBuffer 		l_strBuffer 				= new StringBuffer();
        String              l_str_ligne                 = null;

        if (i_obj==null) {/* EXCEPTION ! */ throw new Exception("Input Stream fourni null pour transformation en ArrayList");}

        l_InputStreamReader_tampon = new InputStreamReader(i_obj, i_str_encodageStreamIN);
        l_Reader = new BufferedReader(l_InputStreamReader_tampon);

        o_arrayList_lignes=new ArrayList<String>();

        while ((l_str_ligne = l_Reader.readLine()) != null) {
            o_arrayList_lignes.add(l_str_ligne);
        }

        l_Reader.close();

        l_InputStreamReader_tampon.close();

        return o_arrayList_lignes;
    }

    // Check if the String param looks like a JSON
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    // Transform String to JSONObject
    public JSONObject stringToJson(String s) throws JSONException {
        return new JSONObject(s);
    }

    // replace some un needed characters sent from database
    public String deleteN(String s){
        s = s.replace("\n  ","");
        s = s.replace("(/) ", "");
        s = s.replace("/ ", "");
        return s;
    }
}



