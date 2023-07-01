package com.pe.adoptapupfinal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Favoritos extends AppCompatActivity {
    LinearLayout votar, raza, imagen, favorito, ranking;
    ImageView imageViewFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        votar = findViewById(R.id.Votar);
        raza = findViewById(R.id.Raza);
        imagen = findViewById(R.id.Imagen);
        ranking = findViewById(R.id.Raking);
        imageViewFavorito = findViewById(R.id.image_photo);
        cargarImagenFavorita();
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
        Intent intent = new Intent(this, Raza.class);
        startActivity(intent);
    }

    public void raking(View view) {
        Intent intent = new Intent(this, ChatBot.class);
        startActivity(intent);
    }

    private void cargarImagenFavorita() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(API.API_URLS[1] + "?sub_id=" + "YOUR_SUB_ID") // Reemplaza YOUR_SUB_ID con el identificador único del usuario
                .method("GET", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "YOUR_API_KEY")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    try {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            final String imageUrl = jsonObject.getString("image_url");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Cargar la imagen favorita en el ImageView usando Glide u otra biblioteca similar
                                    Glide.with(Favoritos.this)
                                            .load(imageUrl)
                                            .into(imageViewFavorito);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Favoritos.this, "Error al obtener la imagen favorita", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Favoritos.this, "Error al obtener la imagen favorita", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
