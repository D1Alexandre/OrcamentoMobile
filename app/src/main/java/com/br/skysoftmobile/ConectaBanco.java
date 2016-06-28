package com.br.skysoftmobile;

/**
 * Classe de Conexao do BD;
 */
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class ConectaBanco {
    Connection con;
    Statement sts;

    public Connection ConectaBanco(String url, Properties prop){

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
            con = DriverManager.getConnection(url,prop);
            sts = con.createStatement();
            con.setAutoCommit(false);
            Log.i("CONEXAO", "CONECTOU");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;

    }

}

