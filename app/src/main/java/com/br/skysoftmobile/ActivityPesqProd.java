package com.br.skysoftmobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.br.skysoftmobile.util.datagrid.DataGrid;
import com.br.skysoftmobile.util.datatable.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class ActivityPesqProd extends AppCompatActivity {

    private static final String COL_CODIGO = "codigo";
    private static final String COL_BARRA = "codigo de barras";
    private static final String DESC = "descrição";
    private static final String PRECO = "preco";

    private DataTable ds;
    public  DataGrid  dg;
    private Properties prop;

    public prod produto = new prod();
    public ActivityConfiguraBanco BD = new ActivityConfiguraBanco();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesq_prod);

        this.criaDataGrid();

        dg.addColumnStyles(new DataGrid.ColumnStyle[] {
                new DataGrid.ColumnStyle("COD",COL_CODIGO, 100),
                new DataGrid.ColumnStyle("COD. BARRAS",COL_BARRA,250),
                new DataGrid.ColumnStyle("DESCRIÇÃO", DESC, 400),
                new DataGrid.ColumnStyle("PREÇO",PRECO , 150)

        });
        dg.setDataSource(this.ds);
        dg.refresh();

        try {

            if (ExecutaSelect() != null) {
                AddProd(pegaProd(ExecutaSelect()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void criaDataGrid(){
        if (this.ds == null ){
            this.ds = new DataTable();
            this.ds.addAllColumns(new String[] {COL_CODIGO,"codigo de barras","descrição","preco"});
        }
        this.dg = (DataGrid) findViewById(R.id.dtGridPesqProd);
    }

    private PreparedStatement ExecutaSelect() throws IOException, SQLException {
        ConectaBanco Banco = new ConectaBanco();
        prop = new Properties();
        prop.put("user","SYSDBA");
        prop.put("password","masterkey");

        Banco.ConectaBanco(pegaCaminnho(), prop);


        PreparedStatement sts = Banco.con.prepareStatement("SELECT CODIGO,CODBARRA,LEFT(NOME,18),PRECO_VENDA FROM PRODUTOS ORDER BY CODIGO");

        return sts;
    }

    private prod pegaProd(PreparedStatement sts) throws SQLException, IOException {

        boolean temRes = sts.execute();
        while (temRes) {
            ResultSet select = sts.getResultSet();
            while(select.next()) {
                produto.setCod(select.getInt("CODIGO"));
                produto.setCodBarra(select.getString("CODBARRA"));
                produto.setNome(select.getString("LEFT"));
                produto.setPreco(select.getDouble("PRECO_VENDA"));
                AddProd(produto);
            }
            select.close();
            temRes = sts.getMoreResults();
        }

        return produto;
    }

    private void AddProd(prod produto){
        DataTable.DataRow drRow;
        drRow = ds.newRow();

        drRow.set(COL_CODIGO, String.valueOf(produto.getCod()));
        drRow.set(COL_BARRA, produto.getCodBarra());
        drRow.set(DESC, produto.getNome());
        drRow.set(PRECO, String.valueOf(produto.getPreco()));

        this.ds.add(drRow);
        this.dg.refresh();

    }

    public String pegaCaminnho() throws IOException {
        SharedPreferences cam = getSharedPreferences("CAMINHO", MODE_PRIVATE);
        String caminho = cam.getString("caminho", null);

        return caminho;
    }

}
