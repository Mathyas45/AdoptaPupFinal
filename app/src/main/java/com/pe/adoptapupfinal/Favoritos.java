package com.pe.adoptapupfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class Favoritos extends AppCompatActivity {
    LinearLayout votar,raza,imagen,favorito,ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        votar = findViewById(R.id.Votar);
        raza = findViewById(R.id.Raza);
        imagen = findViewById(R.id.Imagen);
        ranking = findViewById(R.id.Raking);
        ImageView imagePhoto = findViewById(R.id.image_photo);

        // Obtener la URL de la imagen del Intent
        String imageUrl = getIntent().getStringExtra("image_url");

        // Cargar la imagen en imagePhoto usando Picasso
        Picasso.get().load(imageUrl).into(imagePhoto);
    }

    public void imagen(View view) {
        Intent intent = new Intent(this, Imagenes.class);
        startActivity(intent);

    }
    public void votar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void raza(View view) {
        Intent intent = new Intent(this,Raza.class);
        startActivity(intent);

    }
    public void raking(View view) {
        Intent intent = new Intent(this, ChatBot.class);
        startActivity(intent);

    }
}