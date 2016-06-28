package com.br.skysoftmobile;

/**
 * Classe para a Consulta de Orcamento
 */
public class Orcamento {

    String CodOrcamento;
    String Data;
    String CodClinte;
    String NmCliente;
    String CodFuncionario;
    String Valor;

    public String getCodOrcamento() {
        return CodOrcamento;
    }

    public void setCodOrcamento(String codOrcamento) {
        CodOrcamento = codOrcamento;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCodClinte() {
        return CodClinte;
    }

    public void setCodClinte(String codClinte) {
        CodClinte = codClinte;
    }

    public String getNmCliente() {
        return NmCliente;
    }

    public void setNmCliente(String nmCliente) {
        NmCliente = nmCliente;
    }

    public String getCodFuncionario() {
        return CodFuncionario;
    }

    public void setCodFuncionario(String codFuncionario) {
        CodFuncionario = codFuncionario;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }
}
