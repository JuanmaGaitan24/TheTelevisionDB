package com.example.thetelevisiondb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int COD_SERIE = 111;
    private static final int COD_PROGRAMACION = 101;

    Button btnIrSeries, btnIrProgramacion;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIrSeries = findViewById(R.id.buttonSeries);
        btnIrProgramacion = findViewById(R.id.buttonProgramacion);
        logo = findViewById(R.id.imageViewLogo);

        logo.setImageResource(R.drawable.app_icon);

        btnIrSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SeriesActivity.class);
                startActivityForResult(intent, COD_SERIE);
            }
        });

        btnIrProgramacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProgramacionActivity.class);
                startActivityForResult(intent, COD_PROGRAMACION);
            }
        });
    }
}