package br.udesc.model;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */


public class DataBase {
    
    private String       nome;
    private List<Tabela> tabelas;

    public DataBase() {
        tabelas = new ArrayList<Tabela>();
    }

    public List<Tabela> getTabelas() {
        return tabelas;
    }

    public void setTabelas(List<Tabela> tabelas) {
        this.tabelas = tabelas;
    }
    
    public void addTabela(Tabela t) {
        this.tabelas.add(t);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
     
}
