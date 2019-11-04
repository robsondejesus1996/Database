package br.udesc.manipArq;

import br.udesc.utils.UtilArquivos;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

public class CriadorBaseDados {
    
    /**
     * metodo que cria a base de dados
     * 
     * @param nomeBaseDados
     * @throws IOException 
     */
    public void criarBaseDados(String nomeBaseDados) throws IOException {
        //validacoes
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
        //criando a base de dados
        File novaBase = new File("DADOS\\" + nomeBaseDados + "\\");
        novaBase.mkdir();
    }
    
    /**
     * metodo para listar as bases de dados que ja estao disponiveis
     * 
     * @return string lista de nomes das bases 
     */
    public String listarBaseDadosExistentes() {
        String nomesDb = "";
        File arqDados = new File("DADOS");
        for (String string : arqDados.list()) {
            nomesDb += string + "\n";
        }
        return nomesDb;
    }
    
    

}
