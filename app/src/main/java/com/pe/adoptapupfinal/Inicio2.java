package com.pe.adoptapupfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Inicio2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio2);
    }

    public void login (View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void crearCuenta(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}