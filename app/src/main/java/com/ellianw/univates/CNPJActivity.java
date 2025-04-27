package com.ellianw.univates;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CNPJActivity extends AppCompatActivity {
    private EditText etCNPJ = null;
    private Button btSend = null;
    private TextView tvInfo = null;
    private String actualView = "CNPJ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnpj);

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        etCNPJ = findViewById(R.id.cnpj);
        btSend = findViewById(R.id.bt_send);
        tvInfo = findViewById(R.id.cnpj_info);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCNPJ.getText().toString().isEmpty()) {
                    Toast.makeText(CNPJActivity.this, "Campo CNPJ vazio", Toast.LENGTH_SHORT).show();
                    return;
                }
                validaCep();
            }
        });
    }

    public void validaCep() throws RuntimeException {
        String siteUrl = "https://www.receitaws.com.br/v1/cnpj/" + etCNPJ.getText();
        String contudoJSON = "";

        BufferedReader bufferReader = null;
        try {
            try {
                URL url = new URL(siteUrl);
                BufferedInputStream inputStream = new BufferedInputStream(url.openConnection().getInputStream());
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            } catch (Exception e) {
                e.printStackTrace();
            }

            contudoJSON = BufferParaString(bufferReader);

        } catch (Exception e) {
            Log.e("Log error", "Não foi possível conectar: " + e.getMessage());

            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            JSONObject jObj = new JSONObject(contudoJSON);
            setCNPJInfo(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCNPJInfo(JSONObject infos) {
        String strInfo = "Falha na busca do CNPJ";
        try {
            if (infos.getString("status").equals("ERROR")) {
                strInfo = "Erro: "+infos.getString("message");
            } else {
                strInfo = "Situação do CNPJ: " + infos.getString("situacao")
                        + "\nRazão Social: " + infos.getString("nome")
                        + "\nNome Fantasia: " + (infos.has("fantasia") ? infos.getString("fantasia") : "Não há")
                        + "\nEndereço: " + infos.getString("logradouro") + ", " + infos.getString("numero") + ", " + infos.getString("bairro")
                        + ", " + infos.getString("municipio") + "-" + infos.getString("uf");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvInfo.setText(strInfo);
    }

    private void changeActivity(){
        Intent intencao_de_navegacao = new Intent(CNPJActivity.this, CPFActivity.class);
        startActivity(intencao_de_navegacao);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_cpf_cnpj,menu);
        menu.findItem(R.id.CNPJ_menu).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.CPF_menu) {
            changeActivity();
        }
        return false;
    }

    private String BufferParaString(BufferedReader reader) {
        String linha;
        StringBuffer buffer = new StringBuffer();
        try {
            while ((linha = reader.readLine()) != null) {
                buffer.append(linha);
                buffer.append("\n");
            }

            return buffer.toString();
        } catch (Exception e) {
            Log.e("Erro:", "Erro durante a conversão do buffer para string:" + e.getMessage());
            return "";
        }
    }
}
