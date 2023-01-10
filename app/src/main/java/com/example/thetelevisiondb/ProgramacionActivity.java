package com.example.thetelevisiondb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ProgramacionActivity extends AppCompatActivity {

    Programa[] ArrayProgramas;
    ListView ListaProgramas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);

        ListaProgramas = findViewById(R.id.listprogramas);

        DescargarProgramacion descargarProgramacion = new DescargarProgramacion();
        descargarProgramacion.execute();
    }

    private class DescargarProgramacion extends AsyncTask<String, Void, Void>{
        ArrayList<Programa> ProgramacionActual = new ArrayList<Programa>();

        @Override
        protected Void doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();
                url = new URL("https://www.tdtchannels.com/epg/TV.xml");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
                Date HoraAhora = new Date();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("programme");

                    for (int i = 0; i < items.getLength(); i++){

                        //Codigo para sacar la hora de Inicio y Fin del Programa
                        Element h = (Element) items.item(i);

                        String hi = h.getAttribute("start");
                        String[] horai = hi.split(" ");
                        Date HoraInicio = formato.parse(horai[0]);

                        String hf = h.getAttribute("stop");
                        String[] horaf = hf.split(" ");
                        Date HoraFin = formato.parse(horaf[0]);

                        if (HoraAhora.after(HoraInicio) && HoraAhora.before(HoraFin)){

                            Node item = items.item(i);
                            NodeList elementos = item.getChildNodes();
                            Element eu = (Element) elementos.item(5);

                            String Canal = h.getAttribute("channel");
                            String Titulo = elementos.item(1).getTextContent();
                            String Descripcion = elementos.item(3).getTextContent();
                            String UrlPortada = eu.getAttribute("src");
                            String Categoria = elementos.item(7).getTextContent();

                            Programa programa = new Programa(Canal, Titulo, Descripcion, Categoria, UrlPortada, HoraInicio, HoraFin);
                            ProgramacionActual.add(programa);

                        }

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
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ArrayProgramas = new Programa[ProgramacionActual.size()];
            ArrayProgramas = ProgramacionActual.toArray(ArrayProgramas);
            AdaptadorProgramas adaptadorProgramas = new AdaptadorProgramas(ProgramacionActivity.this, R.layout.layoutprograma, ArrayProgramas);
            ListaProgramas.setAdapter(adaptadorProgramas);
        }
    }

    private class AdaptadorProgramas extends ArrayAdapter<Programa>{
        public AdaptadorProgramas(@NonNull Context context, int resource, @NonNull Programa[] objects){
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return rellenarFila(position, convertView, parent);
        }

        private View rellenarFila(int position, View convertView, ViewGroup parent){
            SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
            LayoutInflater inflater = getLayoutInflater();
            View mifila = inflater.inflate(R.layout.layoutprograma, parent, false);

            TextView txtHoraInicio = mifila.findViewById(R.id.textViewHoraInicio);
            TextView txtHoraFin = mifila.findViewById(R.id.textViewHoraFin);
            TextView txtTitulo = mifila.findViewById(R.id.textViewTituloPro);
            TextView txtCadena = mifila.findViewById(R.id.textViewCadenaPro);
            TextView txtCategoria = mifila.findViewById(R.id.textViewCategoria);
            TextView txtDescripcion = mifila.findViewById(R.id.textViewDescripcionPro);
            ImageView imgPortada = mifila.findViewById(R.id.imageViewPortadaPro);

            txtHoraInicio.setText(formato.format(ArrayProgramas[position].getHoraInicio()));
            txtHoraFin.setText(formato.format(ArrayProgramas[position].getHoraFin()));
            txtTitulo.setText(ArrayProgramas[position].getTitulo());
            txtCadena.setText(ArrayProgramas[position].getCanal());
            txtCategoria.setText(ArrayProgramas[position].getCategoria());
            txtDescripcion.setText(ArrayProgramas[position].getDescripcion());

            Picasso.get()
                    .load(String.valueOf(ArrayProgramas[position].getUrlPortada()))
                    .error(R.drawable.notfound)
                    .into(imgPortada);

            return mifila;
        }
    }

    private static class Programa{
        String Canal, Titulo, Descripcion, Categoria, UrlPortada;
        Date HoraInicio, HoraFin;

        public Programa(String canal, String titulo, String descripcion, String categoria, String urlPortada, Date horaInicio, Date horaFin) {
            Canal = canal;
            Titulo = titulo;
            Descripcion = descripcion;
            Categoria = categoria;
            UrlPortada = urlPortada;
            HoraInicio = horaInicio;
            HoraFin = horaFin;
        }

        public String getCanal() {
            return Canal;
        }

        public void setCanal(String canal) {
            Canal = canal;
        }

        public String getTitulo() {
            return Titulo;
        }

        public void setTitulo(String titulo) {
            Titulo = titulo;
        }

        public String getDescripcion() {
            return Descripcion;
        }

        public void setDescripcion(String descripcion) {
            Descripcion = descripcion;
        }

        public String getCategoria() {
            return Categoria;
        }

        public void setCategoria(String categoria) {
            Categoria = categoria;
        }

        public String getUrlPortada() {
            return UrlPortada;
        }

        public void setUrlPortada(String urlPortada) {
            UrlPortada = urlPortada;
        }

        public Date getHoraInicio() {
            return HoraInicio;
        }

        public void setHoraInicio(Date horaInicio) {
            HoraInicio = horaInicio;
        }

        public Date getHoraFin() {
            return HoraFin;
        }

        public void setHoraFin(Date horaFin) {
            HoraFin = horaFin;
        }
    }

}