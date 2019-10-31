/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.model;

import br.udesc.utils.UtilArquivos;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robson de Jesus e Thiago Correira
 */
public class ComandoSql {
    
    private static ComandoSql instance;
    
    private String       comando;
    private String       baseDados;
    private String       nomeTabela;
    private List<String> nomeColunas;
    private List<String> tipoColunas;
    private List<String> literais;

    private ComandoSql() {
        nomeColunas = new ArrayList<String>();
        tipoColunas = new ArrayList<String>();
        literais    = new ArrayList<String>();
    }
    
    public static ComandoSql getInstance() {
        if (instance == null) {
            instance = new ComandoSql();
        }
        return instance;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public void setBaseDados(String baseDados) {
        this.baseDados = baseDados;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela+".dat";
    }

    public void addColuna(String nomeColuna) {
        nomeColunas.add(UtilArquivos.trataTamanhoString(nomeColuna, 20));
    }

    public void addTipoColuna(String tipoColuna) {
        tipoColunas.add(tipoColuna);
    }

    public void addLiteral(String literal) {
        literais.add(literal);
    }

    public String getComando() {
        return comando;
    }

    public String getBaseDados() {
        return baseDados;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public List<String> getNomeColunas() {
        return nomeColunas;
    }

    public List<String> getTipoColunas() {
        return tipoColunas;
    }

    public List<String> getLiterais() {
        return literais;
    }
    
    public void limparDados() {
        limparColunas();
        limparLiterais();
        comando = "";
        baseDados = "";
        nomeTabela = "";
    }
    
    public void limparColunas() {
        nomeColunas.clear();
        tipoColunas.clear();
    }
    public void limparLiterais() {
        literais.clear();
    }
    
}