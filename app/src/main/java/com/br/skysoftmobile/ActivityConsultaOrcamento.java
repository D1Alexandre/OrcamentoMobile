package com.br.skysoftmobile;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.br.skysoftmobile.util.datagrid.DataGrid;
import com.br.skysoftmobile.util.datatable.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class ActivityConsultaOrcamento extends AppCompatActivity {

    private DataTable dtS;
    public DataGrid  dg;
    public Properties prop;

    public ResultSet select;
    public ConectaBanco Banco = new ConectaBanco();
    public Orcamento orcamento = new Orcamento();

    Button BtnOK;
    EditText edtCod;

    private static final String COL_CODIGO = "codigo";
    private static final String DATA = "data";
    private static final String COD_CLI = "cliente_id";
    private static final String NOME_CLIENTE = "nome_cliente";
    private static final String COD_FUNCIONARIO = "funcionario_id";
    private static final String NOME_FUNCIONARIO = "nome_funcionario";
    private static final String VALOR = "valor_total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_orcamento);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");
        try {
            Banco.ConectaBanco(pegaCaminnho(),prop);
        } catch (IOException e) {
            e.printStackTrace();
        }

        iniciar();
        try {
            Consulta();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dg.addColumnStyles(new DataGrid.ColumnStyle[] {
                new DataGrid.ColumnStyle("COD",COL_CODIGO, 100),
                new DataGrid.ColumnStyle("DATA",DATA,200),
                new DataGrid.ColumnStyle("COD. CLIENTE",COD_CLI,150),
                new DataGrid.ColumnStyle("NOME DO CLIENTE",NOME_CLIENTE,350),
                new DataGrid.ColumnStyle("COD. VENDEDOR",COD_FUNCIONARIO,150),
                new DataGrid.ColumnStyle("TOTAL",VALOR,150)
        });
        this.dg.setDataSource(this.dtS);
        this.dg.refresh();

    }

    private void iniciar(){
        if (this.dtS == null ){
            this.dtS = new DataTable();
            this.dtS.addAllColumns(new String[] {COL_CODIGO,DATA,COD_CLI,NOME_CLIENTE,COD_FUNCIONARIO,NOME_FUNCIONARIO,VALOR});
        }

        this.dg = (DataGrid) findViewById(R.id.gridOrcamento);
    }

    private void Consulta() throws SQLException, IOException {
        BtnOK = (Button) findViewById(R.id.BtnBuscar);
        BtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddToGrid(pegaOrcamento(ExecutaSelect()));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });


    }

    private void AddToGrid(Orcamento orc){
        DataTable.DataRow drRow = dtS.newRow();

        drRow.set(COL_CODIGO, orcamento.getCodOrcamento());
        drRow.set(DATA,orcamento.getData());
        drRow.set(COD_CLI,orcamento.getCodClinte());
        drRow.set(NOME_CLIENTE,orcamento.getNmCliente());
        drRow.set(COD_FUNCIONARIO,orcamento.getCodFuncionario());
        drRow.set(VALOR,orcamento.getValor());


        this.dtS.add(drRow);
        this.dg.refresh();


    }

    public String pegaCaminnho() throws IOException {
        SharedPreferences cam = getSharedPreferences("CAMINHO", MODE_PRIVATE);
        String caminho = cam.getString("caminho", null);

        return caminho;
    }

    private PreparedStatement ExecutaSelect() throws IOException, SQLException {
        edtCod = (EditText) findViewById(R.id.edtBuscaOrcamento);
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");

        Banco.ConectaBanco(pegaCaminnho(), prop);


        PreparedStatement sts = Banco.con.prepareStatement("select orcamento_id,cast(data as DATE) ,cliente_id,funcionario_id,LEFT(nome_cliente,17),valor_total from orcamento where CAST(data as date) = current_date and funcionario_id ="+"\'"+ConvertToChar(edtCod.getText().toString())+"\'");

        return sts;
    }

    private Orcamento pegaOrcamento(PreparedStatement sts) throws SQLException, IOException {

        boolean temRes = sts.execute();
        while (temRes) {
            ResultSet select = sts.getResultSet();
            while(select.next()) {
                orcamento.setCodOrcamento(select.getString("ORCAMENTO_ID"));
                orcamento.setData(select.getString("CAST"));
                orcamento.setCodClinte(select.getString("CLIENTE_ID"));
                orcamento.setNmCliente(select.getString("LEFT"));
                orcamento.setCodFuncionario(select.getString("FUNCIONARIO_ID"));
                orcamento.setValor(select.getString("VALOR_TOTAL"));
                AddToGrid(orcamento);
            }
            select.close();
            temRes = sts.getMoreResults();
        }

        return orcamento;
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



}
