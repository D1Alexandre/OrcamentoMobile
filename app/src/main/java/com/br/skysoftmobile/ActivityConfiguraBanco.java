package com.br.skysoftmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Properties;

public class ActivityConfiguraBanco extends AppCompatActivity {

    String arq;
    EditText edtCaminho;
    Button btnCon;
    public String cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configura_banco);
        edtCaminho = (EditText) findViewById(R.id.edtCaminho);

        try {
            Conecta();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            cam = pegaCaminnho();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {

            if (cam != null){
                Conecta();
                edtCaminho.setText(pegaCaminnho());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String pegaCaminnho() throws IOException {
        String caminho = null;
        try {
                InputStream in = openFileInput("banco.txt");
                if (in != null) {
                    InputStreamReader tmp = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuffer buf = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str);
                    }
                    in.close();
                    caminho = buf.toString();

                }
            } catch (java.io.FileNotFoundException e) {
            } catch (Throwable t) {

            }
        return caminho;
    }



    private void CriaCamin(String txt) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("banco.txt", 0));
            out.write(txt);
            out.close();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void Conecta() throws IOException {

        edtCaminho = (EditText) findViewById(R.id.edtCaminho);
        btnCon = (Button) findViewById(R.id.btnCon);
        arq = ("banco.txt");

        btnCon.setOnClickListener(new View.OnClickListener() {
            private Properties prop;
            @Override
            public void onClick(View view) {
                prop = new Properties();
                prop.put("user","SYSDBA");
                prop.put("password","masterkey");
                ConectaBanco conectaBanco = new ConectaBanco();
                try {
                    conectaBanco.ConectaBanco("jdbc:firebirdsql:"+edtCaminho.getText().toString(),prop);
                    OutputStreamWriter out = new OutputStreamWriter(openFileOutput("banco.txt", 0));
                    out.write("jdbc:firebirdsql:"+edtCaminho.getText().toString());
                    out.close();

                    Connection cone = conectaBanco.con;

                    if (cone.isClosed()){
                        Toast.makeText(getApplicationContext(),"VERIFIQUE OS DADOS INFORMADOS",Toast.LENGTH_LONG).show();
                    }

                    else{
                         Toast.makeText(getApplicationContext(),"CONFIGURAÇÕES SALVAS COM SUCESSO",Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"VERIFIQUE OS DADOS INFORMADOS",Toast.LENGTH_LONG).show();
                }

            }



        });

        SharedPreferences.Editor cam = getSharedPreferences("CAMINHO", MODE_PRIVATE).edit();
        cam.putString("caminho",pegaCaminnho());
        cam.apply();


    }
}
