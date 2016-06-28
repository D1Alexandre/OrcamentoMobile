package com.br.skysoftmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.br.skysoftmobile.util.datagrid.DataGrid;
import com.br.skysoftmobile.util.datatable.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ActivityOrcamentoItens extends AppCompatActivity {

    private DataTable dtS;
    public  DataGrid  dg;

    private Integer Itpos = null;
    private Properties prop;
    private prod produto = new prod();

    private Button btnPesqProd;
    private EditText edtQtd;
    private EditText edtParam;
    private Button btnAddItem;
    private RadioGroup rgOpcao;
    private TextView TxtVal;

    public parametros par = new parametros();

    private static final String COL_CODIGO = "codigo";
    private static final String COL_BARRA = "codigo de barras";
    private static final String DESC = "descrição";
    private static final String PRECO = "preco";
    private static final String QTD = "qtd";
    private static final String TOT = "total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_orcamento_itens);

        this.iniciar();
        //ADD COLUNAS
        dg.addColumnStyles(new DataGrid.ColumnStyle[] {
                new DataGrid.ColumnStyle("COD",COL_CODIGO, 100),
                new DataGrid.ColumnStyle("COD. BARRAS",COL_BARRA,250),
                new DataGrid.ColumnStyle("DESCRIÇÃO",DESC, 400),
                new DataGrid.ColumnStyle("PREÇO",PRECO,150),
                new DataGrid.ColumnStyle("QTD",QTD,100),
                new DataGrid.ColumnStyle("TOTAL",TOT,100)
        });
        dg.setDataSource(this.dtS);
        dg.refresh();
        chamaActivity();

        AddItemNoGrid();

        dg.setLvOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View v, int i, long l) {
                Itpos = i;
                ViewGroup row = (ViewGroup) v.getParent();
                for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                    View view = row.getChildAt(itemPos);
                    view.setBackgroundColor(Color.WHITE);
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityOrcamentoItens.this);
                alertDialog.setTitle("ATENÇÃO");
                alertDialog.setMessage("DESEJA EXLCUIR ITEM DO ORCAMENTO ?");

                alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dtS.remove(Itpos);
                        dg.refresh();
                        CalcTot();
                        Itpos = null;

                    }
                });

                alertDialog.setNegativeButton("NAO",null);
                alertDialog.show();
                return false;
            }
        });

    }

    private void iniciar(){
        if (this.dtS == null ){
            this.dtS = new DataTable();
            this.dtS.addAllColumns(new String[] {COL_CODIGO,COL_BARRA,DESC,PRECO,QTD,TOT});
        }

        this.dg = (DataGrid) findViewById(R.id.gridView);
    }

    private void chamaActivity(){
        btnPesqProd = (Button) findViewById(R.id.btnPesqProd);
        btnPesqProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(ActivityOrcamentoItens.this,ActivityPesqProd.class);
                startActivity(act);
            }
        });

    }

    private void AddItem(prod produto){

        DataTable.DataRow drRow;
        drRow = dtS.newRow();

        if (edtQtd.getText().toString().length() == 0){
            Toast.makeText(getApplicationContext(),"QUANTIDADE OBRIGATORIA",Toast.LENGTH_LONG).show();
        }

        else {
            Double total = (Double.valueOf(produto.getPreco())) * (Double.valueOf(edtQtd.getText().toString()));
            BigDecimal prTot = new BigDecimal(total);
            prTot.setScale(2, BigDecimal.ROUND_HALF_EVEN);

            drRow.set(COL_CODIGO, String.valueOf(produto.getCod()));
            drRow.set(COL_BARRA, produto.getCodBarra());
            drRow.set(DESC, produto.getNome());
            drRow.set(PRECO, String.valueOf(produto.getPreco()));
            drRow.set(QTD, String.valueOf(produto.getQtd()));
            drRow.set(TOT, String.valueOf(prTot.doubleValue()));

            this.dtS.add(drRow);
            this.dg.refresh();
        }
    }

    public String pegaCaminnho() throws IOException {
        SharedPreferences cam = getSharedPreferences("CAMINHO", MODE_PRIVATE);
        String caminho = cam.getString("caminho", null);

        return caminho;
    }

    private PreparedStatement ExecutaSelect(String sel) throws IOException, SQLException {
        ConectaBanco Banco = new ConectaBanco();
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");

        Banco.ConectaBanco(pegaCaminnho(), prop);
        PreparedStatement sts = Banco.con.prepareStatement(sel);

        return sts;
    }

    private prod pegaProd(PreparedStatement sts) throws SQLException, IOException {
        edtQtd = (EditText) findViewById(R.id.editText);
        boolean temRes = sts.execute();
        while (temRes) {
            ResultSet select = sts.getResultSet();
            while(select.next()) {
                produto.setCod(select.getInt("CODIGO"));
                produto.setCodBarra(select.getString("CODBARRA"));
                produto.setNome(select.getString("LEFT"));
                produto.setPreco(select.getDouble("PRECO_VENDA"));
                if(edtQtd.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"QUANTIDADE OBRIGATORIA",Toast.LENGTH_LONG).show();
                }
                else{
                    produto.setQtd(Integer.parseInt(edtQtd.getText().toString()));
                }

            }
            select.close();
            temRes = sts.getMoreResults();
        }

        return produto;
    }

    private void AddItemNoGrid(){
        btnAddItem = (Button) findViewById(R.id.BtnAddIt);
        rgOpcao = (RadioGroup) findViewById(R.id.rgp);
        edtQtd = (EditText) findViewById(R.id.editText);
        if (rgOpcao != null) {
            rgOpcao.check(R.id.rbCodigo);
        }
        edtParam = (EditText) findViewById(R.id.edtItOrcamento);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtParam.getText().toString())){
                    Toast.makeText(getApplicationContext(),"PRODUTO OBRIGATORIO",Toast.LENGTH_LONG).show();
                }

                switch (rgOpcao.getCheckedRadioButtonId()) {
                    case R.id.rbCodigo:
                    try {

                        if (edtQtd.getText().toString().length() == 0){
                            Toast.makeText(getApplicationContext(),"QUANTIDADE OBRIGATORIA",Toast.LENGTH_LONG).show();
                        }

                        else{
                            AddItem(pegaProd(ExecutaSelect("SELECT CODIGO,CODBARRA,LEFT(NOME,18),PRECO_VENDA FROM PRODUTOS WHERE CODIGO =" + "" + edtParam.getText().toString() + "")));
                        }


                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                switch (rgOpcao.getCheckedRadioButtonId()){
                    case R.id.rbCod:
                        try {

                            if (edtQtd.getText().toString().length() == 0){
                                Toast.makeText(getApplicationContext(),"QUANTIDADE OBRIGATORIA",Toast.LENGTH_LONG).show();
                            }

                            else{
                                AddItem(pegaProd(ExecutaSelect("SELECT CODIGO,CODBARRA,LEFT(NOME,18),PRECO_VENDA FROM PRODUTOS WHERE CODBARRA ="+"\'"+edtParam.getText().toString()+"\'")));
                            }


                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    break;
                    case R.id.rbDesc:
                        try {

                            if (edtQtd.getText().toString().length() == 0){
                                Toast.makeText(getApplicationContext(),"QUANTIDADE OBRIGATORIA",Toast.LENGTH_LONG).show();
                            }

                            else{
                                AddItem(pegaProd(ExecutaSelect("SELECT CODIGO,CODBARRA,LEFT(NOME,18),PRECO_VENDA FROM PRODUTOS WHERE NOME LIKE "+"\'"+"%"+edtParam.getText().toString()+"\'")));
                            }


                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                       }
                    break;
                }

                CalcTot();
                envProd();
                edtQtd.setText("");
                edtParam.setText("");
                edtParam.requestFocus();

            }

        });

    }

    private String CalcTot(){

        List<DataTable.DataRow> rows = this.dtS.getAllRows();
        double total_linha, total_itens;
        total_itens = 0;
        for (DataTable.DataRow r : rows) {
            total_linha = Double.parseDouble(r.get("total"));
            total_itens = total_itens + total_linha;
        }
        BigDecimal total_pedido = new BigDecimal(total_itens);
        TxtVal = (TextView) findViewById(R.id.txtVal);
        TxtVal.setText(String.valueOf(total_pedido.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()));

        par.setTotal(String.valueOf(total_pedido.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()));


        return String.valueOf(total_pedido.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());

    }

    private void envProd() {
        List<DataTable.DataRow> rows = this.dtS.getAllRows();
        ArrayList<String> Codprod = new ArrayList<String>();
        ArrayList<String> NmProd = new ArrayList<String>();
        ArrayList<String> PrecoProd = new ArrayList<String>();
        ArrayList<String> QtdProd = new ArrayList<String>();
        ArrayList<String> TotProd = new ArrayList<String>();

        for (DataTable.DataRow r : rows) {

                Codprod.add(String.valueOf(r.get(COL_CODIGO)));
                NmProd.add(String.valueOf(r.get(DESC)));
                QtdProd.add(String.valueOf(r.get(QTD)));
                PrecoProd.add(String.valueOf(r.get(PRECO)));
                TotProd.add(String.valueOf(r.get(TOT)));


            parametros.setProdutos(Codprod);
            parametros.setTotProd(TotProd);
            parametros.setNmProd(NmProd);
            parametros.setPrecProd(PrecoProd);
            parametros.setQtdProd(QtdProd);

        }

    }


}
