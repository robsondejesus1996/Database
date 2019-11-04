package br.udesc.model;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

public class Linha {
    
    private List<Coluna> colunas;

    public Linha() {
        colunas = new  ArrayList<>();
    }

    public List<Coluna> getColunas() {
        return colunas;
    }
    
    public void addColuna(Coluna col) {
        colunas.add(col); 
    }
    
}
