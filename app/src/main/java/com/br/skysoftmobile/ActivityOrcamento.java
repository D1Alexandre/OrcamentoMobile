package com.br.skysoftmobile;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;



public class ActivityOrcamento extends ActivityGroup {

    TabHost abas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamneto);

        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        abas = (TabHost) findViewById(R.id.tabHost);
        abas.setup(mLocalActivityManager);


        TabHost.TabSpec descAba1 = abas.newTabSpec("CLIENTES");
        Intent TabCli = new Intent().setClass(this,ActivityOrcamentoCliente.class);
        descAba1.setIndicator("CLIENTE").setContent(TabCli);
        abas.addTab(descAba1);

        TabHost.TabSpec descAba2 = abas.newTabSpec("ITENS");
        Intent TabIt = new Intent().setClass(this,ActivityOrcamentoItens.class);
        descAba2.setIndicator("ITENS").setContent(TabIt);
        abas.addTab(descAba2);

        TabHost.TabSpec descAba3 = abas.newTabSpec("TOTAIS");
        Intent TabTot = new Intent().setClass(this,ActivityOrcamentoTotais.class);
        descAba3.setIndicator("TOTAIS").setContent(TabTot);
        abas.addTab(descAba3);

    }



}
