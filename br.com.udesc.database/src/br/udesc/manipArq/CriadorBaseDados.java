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

    public void criarBaseDados(String nomeBaseDados) throws IOException {
        if (UtilArquivos.encontrarDataBase(nomeBaseDados) != null) {
            JOptionPane.showMessageDialog(null, "Base informada ja existe");
            return;
        }
        if (nomeBaseDados.length() > 20) {
            JOptionPane.showMessageDialog(null, "O nome da base pode possuir no maximo 20 caracteres");
            return;
        }
        if (!nomeBaseDados.matches("[\\w]*")) {
            JOptionPane.showMessageDialog(null, "O nome da base de dados possui caracteres invalidos");
            return;
        }
        File novaBase = new File("DADOS\\" + nomeBaseDados + "\\");
        novaBase.mkdir();
    }

    public String listarBaseDadosExistentes() {
        String nomesDb = "";
        File arqDados = new File("DADOS");
        for (String string : arqDados.list()) {
            nomesDb += string + "\n";
        }
        return nomesDb;
    }
    
    

}
