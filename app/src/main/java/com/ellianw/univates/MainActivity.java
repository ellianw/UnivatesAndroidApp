package com.ellianw.univates;


import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendButton = findViewById(R.id.send);
        DatePicker bornDatePicker = findViewById(R.id.born);
        bornDatePicker.setMaxDate(c.getTimeInMillis());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (missingField(findViewById(R.id.root))) {
                    Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                String cpf = ((EditText) findViewById(R.id.cpf)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String address = ((EditText) findViewById(R.id.address)).getText().toString();
                RadioButton gender = findViewById(((RadioGroup)findViewById(R.id.gender)).getCheckedRadioButtonId());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(Html.fromHtml("Confirme seus dados: <br>"+"<b>"+"Nome: "+"</b>"+name+
                        "<br><b>"+"CPF: "+"</b>"+cpf+
                        "<br><b>"+"Telefone: "+"</b>"+phone+
                        "<br><b>"+"E-mail: "+"</b>"+email+
                        "<br><b>"+"Endereço: "+"</b>"+address+
                        "<br><b>"+"Sexo: "+"</b>"+gender.getText().toString()+
                        "<br><b>"+"Nascimento: "+"</b>"+bornDatePicker.getDayOfMonth()+
                        "/"+bornDatePicker.getMonth()+"/"+bornDatePicker.getYear(),Html.FROM_HTML_MODE_LEGACY)).setNegativeButton("Não",null).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Obrigado por informar seus dados!", Toast.LENGTH_SHORT).show();
                        clearForm(findViewById(R.id.root));
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    // Código base vindo de: https://stackoverflow.com/a/5816949
    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) { // Loop pelo ViewGroup
            View view = group.getChildAt(i);
            if (view instanceof EditText) { //Se a view for um EditText
                ((EditText)view).setText("");
                continue;
            }

            if (view instanceof DatePicker) { //Se a view for DatePicker
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                ((DatePicker) view).updateDate(mYear,mMonth,mDay);
                continue;
            }

            if (view instanceof RadioGroup) { //Se a view for RadioGroup
                ((RadioGroup) view).clearCheck();
                continue;
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) //Se for outro ViewGroup
                clearForm((ViewGroup)view); //Faz uma chamada recursiva
        }
    }

    //Baseado no metodo MainActivity.clearForm()
    private boolean missingField(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) { // Loop pelo ViewGroup
            View view = group.getChildAt(i);
            if (view instanceof EditText) { //Se a view for um EditText
                if (((EditText) view).getText().toString().isBlank()) return true;
                continue;
            }

            if (view instanceof RadioGroup) { //Se a view for RadioButton
                int checkedId = ((RadioGroup) view).getCheckedRadioButtonId();
                if (checkedId <0) return true;
                continue;
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) //Se for outro ViewGroup(RadioGroup)
                missingField((ViewGroup)view); //Faz uma chamada recursiva
        }
        return false;
    }
}