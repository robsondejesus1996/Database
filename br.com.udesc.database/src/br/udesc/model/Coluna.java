/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.model;

/**
 *
 * @author Robson de Jesus e Thiago Correira
 */
public class Coluna {
    private String  nome;  //20 bytes
    private char    tipo;  //2 bytes
    private int     tamanhoBytes; //4 bytes -- numero de bytes utilizado no momento do insert (varia quando for uma string)
    //atributos assima utilizados no cabecalho da tabela
    //total - 26 bytes
    
    private boolean desconsiderar;

    public Coluna() { }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getTamanhoBytes() {
        return tamanhoBytes;
    }

    public void setTamanhoBytes(int tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }

    public boolean isDesconsiderar() {
        return desconsiderar;
    }

    public void setDesconsiderar(boolean desconsiderar) {
        this.desconsiderar = desconsiderar;
    }
    
}
