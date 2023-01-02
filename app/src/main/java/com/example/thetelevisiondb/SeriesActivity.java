package com.example.thetelevisiondb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends AppCompatActivity {

    static final String SERVIDOR = "https://192.168.56.1/nube/";
    ListView ListaSeries;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        ListaSeries = findViewById(R.id.listseries);

        DescargarCSV descargarCSV = new DescargarCSV();
        descargarCSV.execute("listadoCSV.php");

    }

    private class DescargarCSV extends AsyncTask<String, Void, Void>{
        String todo = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(SeriesActivity.this);
            progressDialog.setTitle("Descargando datos ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();*/
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();
            String[] lineas = todo.split("\n");

            for (String linea:lineas){
                String[] campos = linea.split(";");
                String dato="ID: "+campos[0];
                dato+=" PRODUCTO"+campos[1];
                dato+=" MODELO"+campos[2];
                dato+=" PRECIO"+campos[3];
                list.add(dato);
            }

            adapter= new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            ListaSeries.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(SERVIDOR + script);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String linea = "";

                    while ((linea = br.readLine()) != null){
                        todo += linea + "\n";
                    }

                    br.close();
                    inputStream.close();
                }

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                Log.d("EROR", "EROOOOOOOOOOOOOOOOOOOOOOR");
                e.printStackTrace();
            }
            return null;
        }
    }

}