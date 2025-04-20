package com.ellianw.univates;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CPFActivity extends AppCompatActivity {
    private WebView wvCPF = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpf);

        Log.println(Log.INFO,"Seila","Ta criando o bagulho agora paizao");

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        wvCPF = findViewById(R.id.webview);

        WebSettings webSettings = wvCPF.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvCPF.setWebViewClient(new WebViewClient());
        wvCPF.loadUrl("https://servicos.receita.fazenda.gov.br/Servicos/CPF/ConsultaSituacao/ConsultaPublica.asp");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_cpf_cnpj,menu);
        menu.findItem(R.id.CPF_menu).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.CNPJ_menu) {
            finish();
        }
        return false;
    }
}
