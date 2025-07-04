package com.ellianw.univates;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.StrictMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText loginEdit = null;
    EditText passwordEdit = null;
    private Map<String, String> users = new HashMap<>();
    private Map<String, String> hints = new HashMap<>();

    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        builder = new AlertDialog.Builder(LoginActivity.this);

        users.put("admin","admin");
        users.put("user","user");

        hints.put("admin","Senha padrão de administrador");
        hints.put("user","'Usuário' em inglês");

        loginEdit = findViewById(R.id.it_login);
        passwordEdit = findViewById(R.id.it_password);
        Button btnLogin = findViewById(R.id.bt_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProcedure();
            }
        });

        loginEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextInputLayout loginLayout = findViewById(R.id.nameInputLayout);
                if (!hasFocus) {
                    loginLayout.setHint("Insira seu nome de usuário!");
                } else {
                    loginLayout.setHint("Login:");
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextInputLayout passwordLayout = findViewById(R.id.passowordInputLayout);
                if (hasFocus) {
                    passwordLayout.setHint("Senha:");
                } else {
                    passwordLayout.setHint("Insira sua senha!");
                }
            }
        });
    }

    protected void loginProcedure(){
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if(login.isBlank())
        {
            Toast.makeText(LoginActivity.this, "Por favor, insira seu nome de usuário!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isBlank()){
            Toast.makeText(LoginActivity.this, "Por favor, insira sua senha!", Toast.LENGTH_SHORT).show();
            return;
        }
        validateLogin(login,password);
    }

    boolean validateLogin(String login, String password){
        if(!users.containsKey(login)){
            Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!users.get(login).equals(password)){
            builder.setMessage("Senha incorreta! \nGostaria de ver a dica de senha?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(),hints.get(login), Toast.LENGTH_LONG).show();
                }
            }).setNegativeButton("Não",null);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        builder.setMessage("Usuário Logado!");
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
}