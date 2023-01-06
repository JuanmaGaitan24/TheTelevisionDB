package com.example.thetelevisiondb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SeriesActivity extends AppCompatActivity {

    static final String SERVIDOR = "http://192.168.0.107/nube/";
    ListView ListaSeries;
    TextView txtNombreSerie, txtAnnoSerie, txtCadEmisora, txtNumTemporadas, txtUrlPortada;
    Button btnAgregar, btnModificar, btnEliminar;

    Serie[] ArraySeries;

    int cont;
    String nom_ant = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        ListaSeries = findViewById(R.id.listseries);
        txtNombreSerie = findViewById(R.id.editTextNomSerie);
        txtAnnoSerie = findViewById(R.id.editTextAnnoEstreno);
        txtCadEmisora = findViewById(R.id.editTextCadEmisora);
        txtNumTemporadas = findViewById(R.id.editTextNtemporadas);
        txtUrlPortada = findViewById(R.id.editTextUrlPortada);
        btnAgregar = findViewById(R.id.buttonAgregar);
        btnModificar = findViewById(R.id.buttonModificar);
        btnEliminar = findViewById(R.id.buttonBorrar);

        MostrarSeries();

        ListaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txtNombreSerie.setText(ArraySeries[i].getNombre());
                txtAnnoSerie.setText(String.valueOf(ArraySeries[i].getAnno()));
                txtCadEmisora.setText(ArraySeries[i].getCad_emisora());
                txtNumTemporadas.setText(String.valueOf(ArraySeries[i].getNum_temp()));
                txtUrlPortada.setText(ArraySeries[i].getUrl_portada());
                nom_ant = ArraySeries[i].getNombre();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre, emisora, urlPort, anno, temps;

                nombre = txtNombreSerie.getText().toString();
                anno = txtAnnoSerie.getText().toString();
                emisora = txtCadEmisora.getText().toString();
                temps = txtNumTemporadas.getText().toString();
                urlPort = txtUrlPortada.getText().toString();

                if (!"".equals(nombre) && !"".equals(anno) && !"".equals(emisora) && !"".equals(temps) && !"".equals(urlPort)){

                    Insertar insertar = new Insertar();
                    insertar.execute(nombre, anno, emisora, temps, urlPort);

                } else {
                    Toast.makeText(SeriesActivity.this, R.string.RellenarCampos, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre, emisora, urlPort, anno, temps;

                nombre = txtNombreSerie.getText().toString();
                anno = txtAnnoSerie.getText().toString();
                emisora = txtCadEmisora.getText().toString();
                temps = txtNumTemporadas.getText().toString();
                urlPort = txtUrlPortada.getText().toString();

                if (!"".equals(nom_ant)){
                    if (!"".equals(nombre) && !"".equals(anno) && !"".equals(emisora) && !"".equals(temps) && !"".equals(urlPort)){

                        Modificar modificar = new Modificar();
                        modificar.execute(nom_ant, nombre, anno, emisora, temps, urlPort);

                    } else {
                        Toast.makeText(SeriesActivity.this, R.string.RellenarCampos, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SeriesActivity.this, R.string.SeleccionarSerie, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre;

                nombre = txtNombreSerie.getText().toString();

                if (!"".equals(nombre)){

                    Borrar borrar = new Borrar();
                    borrar.execute(nombre);

                } else {
                    Toast.makeText(SeriesActivity.this, R.string.RellenarNombre, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void MostrarSeries() {
        DescargarCSV descargarCSV = new DescargarCSV();
        descargarCSV.execute("listadoCSV.php");
        /*DescargarXML descargarXML = new DescargarXML();
        descargarXML.execute("listadoXML.php");*/
        /*DescargarJSON descargarJSON = new DescargarJSON();
        descargarJSON.execute("listadoJSON.php");*/
    }

    private class Borrar extends  AsyncTask<String, Void, Void>{
        boolean todocorrecto = false;

        @Override
        protected Void doInBackground(String... strings) {
            String nombre = strings[0];

            URL url;
            String token = "6633445";
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(SERVIDOR + "eliminar.php?nombre=" + nombre + "&token=" + token);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;

                while ((linea = in.readLine()) != null){
                    Log.d("estado", "" + linea);
                    if ("Sentencia ejecutada correctamente".equals(linea)){
                        todocorrecto = true;
                    }
                }

                in.close();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            super.onPostExecute(unused);
            if (todocorrecto){
                Toast.makeText(SeriesActivity.this, R.string.OperacionExisto, Toast.LENGTH_SHORT).show();
                MostrarSeries();
            } else {
                Toast.makeText(SeriesActivity.this, R.string.OperacionFallida, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Modificar extends AsyncTask<String, Void, Void>{
        boolean todocorrecto = false;

        @Override
        protected Void doInBackground(String... strings) {
            String nom_ant = strings[0];
            String nombre = strings[1];
            String anno = strings[2];
            String emisora = strings[3];
            String temporadas = strings[4];
            String urlportada = strings[5];

            URL url;
            String token = "6633445";
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(SERVIDOR + "modificar.php?nombre=" + nombre + "&anno=" + anno + "&emisora=" + emisora + "&temporadas=" + temporadas + "&url=" + urlportada + "&token=" + token + "&nom_ant=" + nom_ant);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;

                while ((linea = in.readLine()) != null){
                    Log.d("estado", "" + linea);
                    if ("Sentencia ejecutada correctamente".equals(linea)){
                        todocorrecto = true;
                    }
                }

                in.close();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (todocorrecto){
                Toast.makeText(SeriesActivity.this, R.string.OperacionExisto, Toast.LENGTH_SHORT).show();
                MostrarSeries();
            } else {
                Toast.makeText(SeriesActivity.this, R.string.OperacionFallida, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Insertar extends AsyncTask<String, Void, Void>{
        boolean todocorrecto = false;

        @Override
        protected Void doInBackground(String... strings) {
            String nombre = strings[0];
            String anno = strings[1];
            String emisora = strings[2];
            String temporadas = strings[3];
            String urlportada = strings[4];

            URL url;
            String token = "6633445";
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(SERVIDOR + "insertar.php?nombre=" + nombre + "&anno=" + anno + "&emisora=" + emisora + "&temporadas=" + temporadas + "&url=" + urlportada + "&token=" + token);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String linea;

                while ((linea=in.readLine())!=null){
                    Log.d("estado", "" + linea);
                    if ("Sentencia ejecutada correctamente".equals(linea)){
                        todocorrecto = true;
                    }
                }

                in.close();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (todocorrecto){
                Toast.makeText(SeriesActivity.this, R.string.OperacionExisto, Toast.LENGTH_SHORT).show();
                MostrarSeries();
            } else {
                Toast.makeText(SeriesActivity.this, R.string.OperacionFallida, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DescargarCSV extends AsyncTask<String, Void, Void>{
        String todo = "";

        @Override
        protected void onPreExecute() {super.onPreExecute();}

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayAdapter<String> adapter;
            List<String> list = new ArrayList<String>();
            String[] lineas = todo.split("\n");
            ArraySeries = new Serie[lineas.length];
            cont = 0;

            for (String linea:lineas){
                String[] campos = linea.split(";");
                Serie serie1 = new Serie(campos[0], Integer.valueOf(campos[1]), campos[2], Integer.valueOf(campos[3]), campos[4]);
                ArraySeries[cont] = serie1;
                cont++;
                /*String dato="Nombre: "+campos[0];
                dato+=" Anno_estreno:"+campos[1];
                dato+=" cad_emisora"+campos[2];
                dato+=" num_temp:"+campos[3];
                list.add(dato);*/
            }

            /*adapter= new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            ListaSeries.setAdapter(adapter);*/

            AdaptadorParaSeries adaptadorParaSeries = new AdaptadorParaSeries(SeriesActivity.this, R.layout.layoutseries, ArraySeries);
            ListaSeries.setAdapter(adaptadorParaSeries);
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

    private class DescargarXML extends AsyncTask<String, Void, Void>{

        ArrayList<String>fila = new ArrayList<String>();

        @Override
        protected Void doInBackground(String... strings) {

            String script = strings[0];
            URL url;
            HttpURLConnection httpURLConnection;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();
                url = new URL(SERVIDOR + script);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("serie");
                    ArraySeries = new Serie[items.getLength()];

                    for (int i = 0; i < items.getLength(); i++){
                        Node item = items.item(i);
                        NodeList elementos = item.getChildNodes();

                        /*for (int j = 0; j < elementos.getLength(); j++){
                            if (elementos.item(j) instanceof Element){
                                fila.add(elementos.item(j).getTextContent());
                            }
                        }*/
                        String nom = elementos.item(0).getTextContent();
                        int anno = Integer.valueOf(elementos.item(1).getTextContent());
                        String cad = elementos.item(2).getTextContent();
                        int temp = Integer.valueOf(elementos.item(3).getTextContent());
                        String url_p = elementos.item(4).getTextContent();

                        Serie serie1 = new Serie(nom, anno, cad, temp, url_p);
                        ArraySeries[i] = serie1;
                    }

                }

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (ParserConfigurationException e){
                e.printStackTrace();
            } catch (SAXException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            AdaptadorParaSeries adaptadorParaSeries = new AdaptadorParaSeries(SeriesActivity.this, R.layout.layoutseries, ArraySeries);
            ListaSeries.setAdapter(adaptadorParaSeries);
        }
    }

    private class DescargarJSON extends AsyncTask<String, Void, Void>{

        String todo = "";
        JSONArray ArrayJson;

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
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String linea = "";

                    while ((linea = br.readLine()) != null){
                        todo += linea + "\n";
                    }

                    ArrayJson = new JSONArray(todo);

                    br.close();
                    inputStream.close();
                }
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            ArraySeries = new Serie[ArrayJson.length()];

            for (int i = 0; i < ArrayJson.length(); i++){

                JSONObject jsonObject = null;

                try {
                    jsonObject = ArrayJson.getJSONObject(i);
                    String nom = jsonObject.getString("nombre");
                    int anno = jsonObject.getInt("anno_estreno");
                    String cad = jsonObject.getString("cadena_emisora");
                    int temp = jsonObject.getInt("num_temporadas");
                    String url_p = jsonObject.getString("url_portada");

                    Serie serie1 = new Serie(nom, anno, cad, temp, url_p);
                    ArraySeries[i] = serie1;
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
            AdaptadorParaSeries adaptadorParaSeries = new AdaptadorParaSeries(SeriesActivity.this, R.layout.layoutseries, ArraySeries);
            ListaSeries.setAdapter(adaptadorParaSeries);
        }
    }

    private class AdaptadorParaSeries extends ArrayAdapter<Serie>{
        public AdaptadorParaSeries(@NonNull Context context, int resource, @NonNull Serie[] objects){
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return rellenarFila(position, convertView, parent);
        }

        private View rellenarFila(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View mifila = inflater.inflate(R.layout.layoutseries, parent, false);

            TextView txtTitulo = mifila.findViewById(R.id.textViewTitulo);
            TextView txtAnno = mifila.findViewById(R.id.textViewAnno);
            TextView txtEmisora = mifila.findViewById(R.id.textViewCadena);
            TextView txtNumTemp = mifila.findViewById(R.id.textViewTemp);
            ImageView imgPortada = mifila.findViewById(R.id.imageViewPortada);

            txtTitulo.setText(ArraySeries[position].getNombre());
            txtAnno.setText(String.valueOf(ArraySeries[position].getAnno()));
            txtEmisora.setText(ArraySeries[position].getCad_emisora());
            txtNumTemp.setText(String.valueOf(ArraySeries[position].getNum_temp()));

            Picasso.get()
                    .load(String.valueOf(ArraySeries[position].getUrl_portada()))
                    .error(R.drawable.notfound)
                    .into(imgPortada);

            return mifila;
        }

    }

    public static class Serie{
        String nombre, cad_emisora, url_portada;
        int anno, num_temp;

        public Serie(String nombre, int anno, String cad_emisora, int num_temp, String url_portada) {
            this.nombre = nombre;
            this.anno = anno;
            this.cad_emisora = cad_emisora;
            this.num_temp = num_temp;
            this.url_portada = url_portada;
        }

        public String getUrl_portada() {return url_portada;}

        public void setUrl_portada(String url_portada) {this.url_portada = url_portada;}

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCad_emisora() {
            return cad_emisora;
        }

        public void setCad_emisora(String cad_emisora) {
            this.cad_emisora = cad_emisora;
        }

        public int getAnno() {
            return anno;
        }

        public void setAnno(int anno) {
            this.anno = anno;
        }

        public int getNum_temp() {
            return num_temp;
        }

        public void setNum_temp(int num_temp) {
            this.num_temp = num_temp;
        }
    }

}