package com.br.skysoftmobile;

import java.util.ArrayList;

/**
 * Classe para passar os valores;
 */
public class parametros {

    public static String CodCliente;
    public static String CodVendedor;
    public static String Cliente;
    public static String Vendedor;
    public static String Total;
    public static ArrayList<String> produtos;
    public static ArrayList<String> PrecProd;
    public static ArrayList<String> NmProd;
    public static ArrayList<String> QtdProd;

    public static ArrayList<String> getTotProd() {
        return TotProd;
    }

    public static void setTotProd(ArrayList<String> totProd) {
        TotProd = totProd;
    }

    public static ArrayList<String> TotProd;

    public static ArrayList<String> getPrecProd() {
        return PrecProd;
    }

    public static void setPrecProd(ArrayList<String> precProd) {
        PrecProd = precProd;
    }

    public static ArrayList<String> getNmProd() {
        return NmProd;
    }

    public static void setNmProd(ArrayList<String> nmProd) {
        NmProd = nmProd;
    }

    public static ArrayList<String> getQtdProd() {
        return QtdProd;
    }

    public static void setQtdProd(ArrayList<String> qtdProd) {
        QtdProd = qtdProd;
    }

    public static String getCodVendedor() {
        return CodVendedor;
    }

    public static void setCodVendedor(String codVendedor) {
        CodVendedor = codVendedor;
    }


    public static String getCodCliente() {
        return CodCliente;
    }

    public static void setCodCliente(String codCliente) {
        CodCliente = codCliente;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public static ArrayList<String> getProdutos() {
        return produtos;
    }

    public static void setProdutos(ArrayList<String> produtos) {
        parametros.produtos = produtos;
    }


}
