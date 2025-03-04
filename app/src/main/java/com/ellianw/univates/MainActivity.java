package com.ellianw.univates;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText loginEdit = null;
    private EditText passwordEdit = null;
    private Map<String, String> users;
    private Map<String, String> hints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        users = new HashMap<>();
        users.put("admin","admin");
        users.put("user","user");

        hints = new HashMap<>();
        hints.put("admin","Senha padrão de administrador");
        hints.put("user","'Usuário' em inglês");

        loginEdit = findViewById(R.id.it_login);
        passwordEdit = findViewById(R.id.it_password);
        Button btnLogin = findViewById(R.id.bt_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                if(login.isBlank())
                {
                    Toast.makeText(MainActivity.this, "Por favor, insira seu nome de usuário!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isBlank()){
                    Toast.makeText(MainActivity.this, "Por favor, insira sua senha!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!users.containsKey(login)){
                    Toast.makeText(MainActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                    return;
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
                    return;
                }
                builder.setMessage("Usuário Logado!");
                AlertDialog alert = builder.create();
                alert.show();
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
}