package com.pe.adoptapupfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    LinearLayout raza, imagen, favorito, ranking;
    private ImageView imagePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        raza = findViewById(R.id.Raza);
        imagen = findViewById(R.id.Imagen);
        favorito = findViewById(R.id.Favorito);
        ranking = findViewById(R.id.Raking);
        imagePhoto = findViewById(R.id.image_photo);
        ImageView buttonDislike = findViewById(R.id.button_dislike);
        ImageView buttonLike = findViewById(R.id.button_like);

        // Carga una imagen al azar desde la API de Cat API
        loadRandomImage();

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí el código para mostrar una actividad o pantalla de imágenes
            }
        });

        raza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí el código para mostrar una actividad o pantalla de razas
            }
        });

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí el código para mostrar una actividad o pantalla de favoritos
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí el código para mostrar una actividad o pantalla de popularidad
            }
        });

        buttonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carga la siguiente imagen al azar
                loadRandomImage();
            }
        });

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carga la siguiente imagen al azar
                loadRandomImage();
            }
        });
    }

    private void loadRandomImage() {
        OkHttpClient client = new OkHttpClient();

        // Construye la URL de la solicitud GET con los parámetros de consulta
        String url = "https://api.thecatapi.com/v1/images/search?breed_ids=beng&include_breeds=true&limit=10";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        if (jsonArray.length() > 0) {
                            int randomIndex = new Random().nextInt(jsonArray.length());
                            JSONObject imageObj = jsonArray.getJSONObject(randomIndex);
                            String imageUrl = imageObj.getString("url");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.get().load(imageUrl).into(imagePhoto);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Error en la solicitud: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
