package com.pe.adoptapupfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;


import com.pe.adoptapupfinal.chatmodel.Message;
import com.pe.adoptapupfinal.chatmodel.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText message_text_text;
    ImageView send_btn;
    List<Message> messageList = new ArrayList<>();
    MessageAdapter messageAdapter;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Ajusta el tiempo límite de conexión
            .readTimeout(30, TimeUnit.SECONDS) // Ajusta el tiempo límite de lectura
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot);
// Mostrar mensaje de bienvenida
//        addToChat("¡Bienvenido! Aquí podrás hacer preguntas sobre mascotas domésticas, sus cuidados, qué mascota adoptar y más.", Message.SEND_BY_BOT);

        // Inicializar vistas
        message_text_text = findViewById(R.id.message_text_text);
        send_btn = findViewById(R.id.send_btn);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar el RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Configurar el adaptador del RecyclerView
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        // Mostrar mensaje de bienvenida
        addToChat("¡Bienvenido! Aquí podrás hacer preguntas sobre mascotas domésticas, sus cuidados, qué mascota adoptar y más.", Message.SEND_BY_BOT);


        // Configurar el botón de enviar
        send_btn.setOnClickListener(view -> {
            String question = message_text_text.getText().toString().trim();
            addToChat(question, Message.SEND_BY_ME);
            message_text_text.setText("");
            callAPI(question);
        });
    }

    void addToChat(String message, String sendBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sendBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);
    }

    void callAPI(String question) {

        // Verificar si la pregunta contiene palabras clave relacionadas con medicina y pastillas
        if (!containsPetKeywords(question)) {
            addResponse("Lo siento, solo puedo responder preguntas sobre mascotas domésticas y sus cuidados.");
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(API2.API_URL)
                .header("Authorization", "Bearer " + API2.API)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("No se pudo cargar la respuesta debido a " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");

                        // Verificar si la respuesta contiene información médica relevante
                        if (containsPetInformation(result)) {
                            addResponse(result.trim());
                        } else {
                            addResponse("Lo siento, no se encontró información médica relevante.");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("No se pudo cargar la respuesta debido a " + response.body().string());
                }
            }
        });

        // Mostrar "Escribiendo..." después de realizar la llamada a la API
        messageList.add(new Message("Escribiendo...", Message.SEND_BY_BOT));
    }


    boolean containsPetKeywords(String question) {
        // Lista de palabras clave relacionadas con mascotas domésticas
        List<String> petKeywords = Arrays.asList("hola","mascota", "cuidados", "bañar","gato","gatos","mascotas","niños", "raza", "recomendar", "personalidad");

        // Verificar si la pregunta contiene alguna palabra clave relacionada con mascotas
        for (String keyword : petKeywords) {
            if (question.toLowerCase().contains(keyword)) {
                return true;
            }
        }

        return false;
    }




    boolean containsPetInformation(String response) {
        // Palabras clave relacionadas con cuidados de mascotas
        List<String> careKeywords = Arrays.asList("cuidados", "bañar", "alimentación", "entrenamiento", "juego");

        // Palabras clave relacionadas con razas de mascotas
        List<String> breedKeywords = Arrays.asList("raza", "perro", "gato", "mascota", "animal");

        // Palabras clave relacionadas con recomendaciones según la personalidad o tipo de familia
        List<String> recommendationKeywords = Arrays.asList("personalidad", "familia", "recomendación");

        // Verificar si la respuesta contiene información relevante sobre mascotas
        String lowerCaseResponse = response.toLowerCase();

        // Verificar si la respuesta contiene alguna palabra clave de cuidados de mascotas
        for (String keyword : careKeywords) {
            if (lowerCaseResponse.contains(keyword)) {
                return true;
            }
        }

        // Verificar si la respuesta contiene alguna palabra clave de razas de mascotas
        for (String keyword : breedKeywords) {
            if (lowerCaseResponse.contains(keyword)) {
                return true;
            }
        }

        // Verificar si la respuesta contiene alguna palabra clave de recomendaciones según la personalidad o tipo de familia
        for (String keyword : recommendationKeywords) {
            if (lowerCaseResponse.contains(keyword)) {
                return true;
            }
        }

        // Agrega más condiciones y verificaciones según tus necesidades

        return false;
    }


}
