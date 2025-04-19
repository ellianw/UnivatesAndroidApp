package com.ellianw.univates;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginWebsocketActivity extends LoginActivity {
    @Override
    boolean validateLogin(String login, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        Map<String,String> params =new HashMap<>();
        params.put("login",login);
        params.put("password",password);

        JSONObject json = new JSONObject(params);

        final String[] outputString = {"Falha na requisição por parte do cliente"};
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlConexao = "http://10.0.2.2:8080/Websocket/api/login";   // link da API ou webpage
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                urlConexao,
                json,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            outputString[0] = ((JSONObject)response).getString("ResponseString");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        builder.setMessage(outputString[0]);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Erro na lib Volley: "+error);
                }
        });
        queue.add(req);

        return true;
    }
}
