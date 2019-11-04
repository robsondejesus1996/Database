package br.udesc.model;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

public class Tabela {
    
    private String       nome;
    private List<Coluna> colunasCabecalho;
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
    
    public List<Coluna> getColunasCabecalho() {
        return colunasCabecalho;
    }

    public void setColunasCabecalho(List<Coluna> colunasCabecario) {
        this.colunasCabecalho = colunasCabecario;
    }

    public List<Linha> getLinhas() {
        return linhas;
    }
    
    public void addLinha(Linha linha) {
        this.linhas.add(linha);
    }
    

}
