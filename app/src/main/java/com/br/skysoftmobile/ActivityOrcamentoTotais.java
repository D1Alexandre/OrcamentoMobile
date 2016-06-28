package com.br.skysoftmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.text.*;
import android.widget.Button;
import android.widget.TextView;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class ActivityOrcamentoTotais extends AppCompatActivity {

    TextView txtNomeCli;
    TextView TxtValTotal;
    TextView TxtCodOrc;
    Button BtnSalvar;
    ConectaBanco Banco;
    Properties prop;

    public String CodVen;
    public String vend;
    public String cliente;
    public String CodCli;
    public String total;
    public String[] prod;
    public int ult;

    public parametros par = new parametros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_totais);
        //PEGANDO VENDEDOR
        vend = par.getVendedor();
        CodVen = par.getCodVendedor();

        //PEGANDO O CLIENTE
        CodCli = par.getCodCliente();
        cliente = par.getCliente();

        Log.i("CDOIGO", String.valueOf(CodCli));

        //PEGANDO TOTAL
        total = par.getTotal();

        if (CodCli == null || CodVen == null || total == null){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("ATENÇÃO");
            alerta.setMessage(" PREENCHA OS DADOS PARA PROCESSEGUIR COM O ORÇAMENTO  ");


            alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ActivityOrcamentoTotais.this,ActivityOrcamento.class);
                    startActivity(intent);
                }
            });

            alerta.show();

        }

        txtNomeCli = (TextView) findViewById(R.id.txtNomeCli);
        TxtValTotal = (TextView) findViewById(R.id.TxtValTotal);

        txtNomeCli.setText(cliente);
        txtNomeCli.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        TxtValTotal.setText(total);
        TxtValTotal.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        try {
            GravaDados();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String pegaCaminnho() throws IOException {
        SharedPreferences cam = getSharedPreferences("CAMINHO", MODE_PRIVATE);
        String caminho = cam.getString("caminho", null);

        return caminho;
    }

    private Integer InsertOrcamento(String CodC,String nmCli, String tot, String vende,String CodVend) throws IOException, SQLException {
        int UltCod = 0;
        ResultSet set;
        Banco = new ConectaBanco();
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");
        Banco.ConectaBanco(pegaCaminnho(),prop);

        Statement Insert = Banco.con.createStatement();

        try {
            Insert.execute("INSERT INTO ORCAMENTO (DATA, CLIENTE_ID,nome_cliente,FUNCIONARIO_ID,nome_funcionario,VALOR_TOTAL,VALOR_VENDA) VALUES (current_timestamp,\'"+CodC+"\',"+"\'"+nmCli+"\'"+",\'"+CodVend+"\',"+"\'"+vende+"\'"+","+tot+","+tot+")");
            Banco.con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        set = Insert.executeQuery("SELECT MAX(ORCAMENTO_ID) FROM ORCAMENTO");
        while(set.next()){
            UltCod = set.getInt("MAX");
        }

        return UltCod;
    }

    private void InsereItens(int CodOrcamento) throws SQLException, IOException {
        Banco = new ConectaBanco();
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");
        Banco.ConectaBanco(pegaCaminnho(),prop);
        int n;

        ArrayList<String> CodProd = parametros.getProdutos();
        ArrayList<String> NmProd = parametros.getNmProd();
        ArrayList<String> PrecProd = parametros.getPrecProd();
        ArrayList<String> QtdProd = parametros.getQtdProd();
        ArrayList<String> TotProd = parametros.getTotProd();


        Log.i("Codprodutos", String.valueOf(CodProd));
        Statement Insert = Banco.con.createStatement();
        for(n = 0;  n < CodProd.size();n++){
            Log.i("produtos", String.valueOf(CodProd));
            Insert.execute("INSERT INTO ORCAMENTO_ITEM (NUMERO, ORCAMENTO_ID, QUANTIDADE, PRECO, PRODUTO_ID, DESCRICAO_PRODUTO, DESCONTO, DATA, PRECO_TOTAL, CMV)"+
                    "VALUES ("+n+","+CodOrcamento+","+QtdProd.get(n)+","+PrecProd.get(n)+","+"\'"+ConvertToChar(CodProd.get(n))+"\'"+","+"\'"+NmProd.get(n)+"\'"   +", 0, current_timestamp,"+TotProd.get(n)+", 0)");
            Banco.con.commit();
        }


    }

    private void GravaDados() throws IOException, SQLException {
        BtnSalvar = (Button) findViewById(R.id.BtnSalvar);
        TxtCodOrc = (TextView) findViewById(R.id.txtCodOrc);
        BtnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ult = InsertOrcamento(CodCli,cliente,total,vend,CodVen);
                    InsereItens(ult);
                    AlertaOrcamentoGravado(ult);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                TxtCodOrc.setText(Integer.toString(ult));
                TxtCodOrc.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });

    }

    private String ConvertToChar(String para){

        if(para.length() == 1){
            para = "000000000"+para;
        }

        if(para.length() == 2){
            para = "00000000"+para;
        }

        if(para.length() == 3){
            para = "0000000"+para;
        }

        if(para.length() == 4){
            para = "000000"+para;
        }

        if(para.length() == 5){
            para = "00000"+para;
        }

        if(para.length() == 6){
            para = "0000"+para;
        }

        if(para.length() == 7){
            para = "000"+para;
        }

        if(para.length() == 8){
            para = "00"+para;
        }

        if(para.length() == 9){
            para = "0"+para;
        }

        return para;
    }

    private void AlertaOrcamentoGravado(int i){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("ATENÇÃO");
        alerta.setMessage("REGISTRO GRAVADO COM SUCESSO\n"+
                                    "ORÇAMENTO "+i);


        alerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ActivityOrcamentoTotais.this,ActivityOrcamentoCliente.class);
                finish();
                startActivity(intent);
            }
        });
        alerta.show();
    }

}
