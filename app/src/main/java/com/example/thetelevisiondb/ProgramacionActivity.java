package com.example.thetelevisiondb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ProgramacionActivity extends AppCompatActivity {

    Programa[] ArrayProgramas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);
        DescargarProgramacion descargarProgramacion = new DescargarProgramacion();
        descargarProgramacion.execute();
    }

    private class DescargarProgramacion extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {

                DocumentBuilder db = dbf.newDocumentBuilder();
                url = new URL("https://www.tdtchannels.com/epg/TV.xml");
                httpURLConnection = (HttpURLConnection) url.openConnection();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Document doc = db.parse(httpURLConnection.getInputStream());
                    Element root = doc.getDocumentElement();
                    NodeList items = root.getElementsByTagName("programme");
                    ArrayProgramas = new Programa[items.getLength()];
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
    }

    private static class Programa{
        String Canal, Titulo, Descripcion, Categoria, UrlPortada;
        int HoraInicio, HoraFin;

        public Programa(String canal, String titulo, String descripcion, String categoria, String urlPortada, int horaInicio, int horaFin) {
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

        public int getHoraInicio() {
            return HoraInicio;
        }

        public void setHoraInicio(int horaInicio) {
            HoraInicio = horaInicio;
        }

        public int getHoraFin() {
            return HoraFin;
        }

        public void setHoraFin(int horaFin) {
            HoraFin = horaFin;
        }
    }

}