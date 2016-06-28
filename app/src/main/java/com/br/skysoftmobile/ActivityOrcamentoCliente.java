package com.br.skysoftmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ActivityOrcamentoCliente extends AppCompatActivity {

    private String caminho;
    private Properties prop;
    Button btnConsulta;
    Button btnVend;
    ResultSet resultado;
    EditText edtCodCli;
    EditText edtVend;
    TextView txtNm;
    TextView TxtVend;

    public ConectaBanco Banco = new ConectaBanco();
    public parametros par = new parametros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_orcamento_cliente);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setaResultado();
        setaVend();

        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");
        try {
            Banco.ConectaBanco(pegaCaminnho(),prop);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(Banco.con.isClosed()){
                Alerta();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public  Statement ExecutaSelect() throws IOException {
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");
        Banco.ConectaBanco(pegaCaminnho(),prop);
        Statement select = Banco.sts;
        return select;
    }

    public String pegaCaminnho() throws IOException {
        String caminho;
        SharedPreferences cam = getSharedPreferences("CAMINHO", MODE_PRIVATE);
        caminho = cam.getString("caminho", null);

        return caminho;
    }

    public String[] ConsultaCli(){
        edtCodCli = (EditText) findViewById(R.id.edtCodCli);
        String[] cli = new String[3];
        try {
            resultado = ExecutaSelect().executeQuery("SELECT NOME,CODIGO FROM CLIENTES WHERE CODIGO ="+edtCodCli.getText().toString());
            while (resultado.next()){
                cli[0] = resultado.getString("NOME");
                cli[1] = resultado.getString("CODIGO");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        Log.i("COD CLIENTE",cli[1]);
        return cli;
    }

    public String[] ConsultaVend(){
        String[] vendedor = new String[2];
        edtVend = (EditText) findViewById(R.id.edtVend);
        try {
            resultado = ExecutaSelect().executeQuery("SELECT CODIGO,NOME FROM FUNCIONARIOS WHERE CODIGO ="+edtVend.getText().toString());
            while (resultado.next()){
                vendedor[0] = resultado.getString("NOME");
                vendedor[1] = resultado.getString("CODIGO");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


        return vendedor;
    }

    private void setaResultado(){
        txtNm = (TextView) findViewById(R.id.txtNmCli);
        //txtCod = (TextView) findViewById(R.id.txtCod);
        btnConsulta = (Button) findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNm.setText(ConsultaCli()[0]);
                txtNm.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                passaCli();
            }
        });
    }

    private void setaVend(){
        TxtVend = (TextView) findViewById(R.id.TxtVend);
        btnVend = (Button) findViewById(R.id.btnVendOK);

        btnVend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TxtVend.setText(ConsultaVend()[0]);
                TxtVend.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                passaVend();
            }
        });

    }

    private void passaCli(){
        par.setCliente(ConsultaCli()[0]);
        par.setCodCliente(ConsultaCli()[1]);
    }

    private void passaVend() {
        par.setVendedor(ConsultaVend()[0]);
        par.setCodVendedor(ConsultaVend()[1]);

    }

    private void Alerta() throws Throwable {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("ERRO");
        alerta.setMessage("ERRO AO CONECTAR NO BANCO DE DADOS VERIFIQUE A REDE OU IP DO SERVIDOR\n" +
                          "SAIA DO APLICATIVO E ENTRE NOVAMENTE");

        alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        alerta.show();
    }

}
