package com.br.skysoftmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.tonicsystems.jarjar.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    ImageButton imgCli;
    ImageButton imgOrcamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ChamaOrcamento();
        ChamaConsultaOrcamentos();

    }

    private void ChamaOrcamento(){
        imgOrcamento = (ImageButton) findViewById(R.id.imgOrca);
        imgOrcamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act = new Intent(MainActivity.this,ActivityOrcamento.class);
                startActivity(act);
            }
        });
    }

    private void ChamaConsultaOrcamentos(){
        imgCli = (ImageButton) findViewById(R.id.imgCli);
        imgCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityConsultaOrcamento.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent act = new Intent(this,ActivityConfiguraBanco.class);
            startActivity(act);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
