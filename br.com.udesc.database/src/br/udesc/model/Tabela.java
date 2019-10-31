/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.model;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Robson de Jesus e Thiago Correira
 */
public class Tabela {
    
    
    private String       nome;
    private List<Coluna> colunasCabecario;
    private List<Linha>  linhas;

    public Tabela() {
        linhas = new ArrayList<Linha>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<Coluna> getColunasCabecario() {
        return colunasCabecario;
    }

    public void setColunasCabecario(List<Coluna> colunasCabecario) {
        this.colunasCabecario = colunasCabecario;
    }

    public List<Linha> getLinhas() {
        return linhas;
    }
    
    public void addLinha(Linha linha) {
        this.linhas.add(linha);
    }
    
}
