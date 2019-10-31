/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.manipArq;


import br.udesc.model.ComandoSql;
import br.udesc.utils.UtilArquivos;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Robson de Jesus e Thiago Correira
 */
public class CriadorBaseDados {
    
    public void criarBaseDados(String nomeBaseDados) throws IOException{
        if (UtilArquivos.encontrarDataBase(nomeBaseDados) != null) {
            JOptionPane.showMessageDialog(null, "Base informada ja existe");
            return;
        }
        File novaBase = new File("DADOS\\"+nomeBaseDados+"\\");
        novaBase.mkdir();
    }
}
