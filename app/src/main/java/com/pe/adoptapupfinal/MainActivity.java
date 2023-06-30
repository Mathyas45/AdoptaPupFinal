package com.pe.adoptapupfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.pe.adoptapupfinal.API;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    LinearLayout raza, imagen, favorito, ranking;
    private ImageView imagePhoto;
    private Handler handler;
    private Runnable ratingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        raza = findViewById(R.id.Raza);
        imagen = findViewById(R.id.Imagen);
        favorito = findViewById(R.id.Favorito);
        ranking = findViewById(R.id.ChatBot);
        imagePhoto = findViewById(R.id.image_photo);
        ImageView buttonDislike = findViewById(R.id.button_dislike);
        ImageView buttonLike = findViewById(R.id.button_like);
        Button buttonFavorite = findViewById(R.id.button_favorite);

        // Carga una imagen al azar desde la API de Cat API
        loadRandomImage();

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

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la URL de la imagen actualmente mostrada
                String imageUrl = API.API_URL;

                // Crear un Intent para abrir la actividad Favoritos
                Intent intent = new Intent(MainActivity.this, Favoritos.class);

                // Pasar la URL de la imagen como dato extra al Intent
                intent.putExtra("image_url", imageUrl);

                // Iniciar la actividad Favoritos
                startActivity(intent);
            }
        });
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

    private void loadRandomImage() {
        OkHttpClient client = new OkHttpClient();

        // Construye la URL de la solicitud GET con los parámetros de consulta
        String url = API.API_URL;

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

//    private void showRatingDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View ratingView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
//        RatingBar ratingBar = ratingView.findViewById(R.id.rating_bar);
//        Button rateButton = ratingView.findViewById(R.id.rate_button);
//        Button laterButton = ratingView.findViewById(R.id.later_button);
//
//        builder.setView(ratingView);
//        AlertDialog ratingDialog = builder.create();
//        ratingDialog.setCanceledOnTouchOutside(false);
//
//        rateButton.setOnClickListener(view -> {
//            int rating = (int) ratingBar.getRating();
//            // Aquí puedes enviar la calificación a tu backend o realizar alguna acción con ella
//            // Por ahora, solo mostraremos un mensaje
//            Toast.makeText(this, "Has calificado la aplicación con " + rating + " estrellas.", Toast.LENGTH_SHORT).show();
//            ratingDialog.dismiss();
//        });
//
//        laterButton.setOnClickListener(view -> ratingDialog.dismiss());
//
//        ratingDialog.show();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(ratingRunnable);
    }

    public void ChatBot(View view) {
        Intent intent = new Intent(this, ChatBot.class);
        startActivity(intent);
    }

}
