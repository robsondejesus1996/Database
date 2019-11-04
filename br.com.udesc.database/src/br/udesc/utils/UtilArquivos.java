package br.udesc.utils;

import br.udesc.model.Coluna;
import br.udesc.model.ComandoSql;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe com metodos de uso geral no projeto
 *
 */
public class UtilArquivos {

    private static final String ARQ_DADOS = "DADOS";

    /**
     * metodo que faz a busca de uma base de dados e retorna o seu File
     *
     * @param nomeDatabase o nome da base de dados
     * @return File arquivo da base de dados <br> Null caso nao encontre
     */
    public static File encontrarDataBase(String nomeDatabase) {
        for (File arquivo : new File(ARQ_DADOS).listFiles()) {
            if (arquivo.isDirectory() && arquivo.getName().equals(nomeDatabase)) {
                return arquivo;
            }
        }
        return null;
    }

    /**
     * metodo que faz a busca de uma tabela e retorna o seu File
     *
     * @param database database
     * @param nomeTabela nome da tabela
     * @return File arquivo .dat da tabela <br> null caso nao encontre
     */
    public static File encontrarTabela(File database, String nomeTabela) {
        for (File tabela : database.listFiles()) {
            if (!tabela.isDirectory() && tabela.getName().equals(nomeTabela)) {
                return tabela;
            }
        }
        return null;
    }

    /**
     * metodo para ajustar o tamanho de uma string
     *
     * @param strTratar string que sera modificada
     * @param tamanho tamanho que ela deve ficar
     * @return string com o tamanho informado
     */
    public static String trataTamanhoString(String strTratar, int tamanho) {
        char[] caracteres = Arrays.copyOf(strTratar.toCharArray(), tamanho);
        String stringValida = "";
        for (char carac : caracteres) {
            stringValida += carac;
        }
        return stringValida;
    }

    /**
     * metodo para ajustar o tamanho de uma string adicionando espaços no final
     *
     * @param strTratar string que sera modificada
     * @param tamanho tamanho que ela deve ficar
     * @return string com o tamanho informado
     */
    public static String trataTamanhoStringEspacos(String strTratar, int tamanho) {
        strTratar = strTratar.trim();
        if (strTratar.length() < tamanho) {
            while (strTratar.length() < tamanho) {
                strTratar += " ";
            }
        }
        return strTratar;
    }

    /**
     * metodo que faz a leitura do cabecalho de uma tabela e retorna as colunas
     * que essa tabela possui
     *
     * @param diretorioTabela arquivo da tabela
     * @return lista de colunas
     * @throws Exception
     */
    public static List<Coluna> montarColunasCabecalho(File diretorioTabela) throws Exception {
        List<Coluna>     colunasCabecario = new ArrayList<Coluna>();
        RandomAccessFile ran              = new RandomAccessFile(diretorioTabela, "rw");

        String nomeColuna       = "";
        char   tipo             = ' ';
        int    tamanho          = 0;
        int    tamanhoCabecario = ran.readInt();
        
        while (tamanhoCabecario > 0) {
            //fazendo a leitura dos dados
            byte[] bi = new byte[20];
            ran.read(bi);
            for (byte b : bi) {
                nomeColuna += (char) b;
            }

            tipo = ran.readChar();

            tamanho = ran.readInt();
            
            //criando o objeto coluna e adiconando na lista de retorno do metodo
            Coluna colAux = new Coluna();
            colAux.setNome(nomeColuna);
            colAux.setTipo(tipo);
            colAux.setTamanhoBytes(tamanho);
            colunasCabecario.add(colAux);

            //limpandovalores e descontandoos bytes ja lidos
            tamanhoCabecario -= 26;
            nomeColuna = "";
            tipo = ' ';
            tamanho = 0;
        }
        return colunasCabecario;
    }
    
    /**
     * metodo que verifica se o comando sql e valido
     * 
     * @return True se o comando sql for valido false caso o contrario
     */
    public static boolean comandoValido() {
        ComandoSql comando = ComandoSql.getInstance();
        if (comando.getNomeTabela2() == null) {
            JOptionPane.showMessageDialog(null, "Sql invalido");
            return false;
        }
        if (!comando.getNomeTabela2().matches("[\\w]*")) {
            JOptionPane.showMessageDialog(null, "o nome da tabela possui caracteres invalidos");
            return false;
        }
        if (comando.getNomeTabela2().length() > 20) {
            JOptionPane.showMessageDialog(null, "o nome da tabela pode possuir no maximo 20 caracteres");
            return false;
        }
        for (String nomeColuna : comando.getNomeColunas()) {
            if (nomeColuna.equals("")) {
                JOptionPane.showMessageDialog(null, "colunas sem nome nao são validas");
                return false;
            }
            if (nomeColuna.length() > 20) {
                JOptionPane.showMessageDialog(null, "o nome de uma coluna pode possuir no maximo 20 caracteres \n erro na coluna: " + nomeColuna);
                return false;
            }
            if (!nomeColuna.trim().matches("[\\w]*")) {
                JOptionPane.showMessageDialog(null, "a coluna : " + nomeColuna + "  possui caracteres invalidos");
                return false;
            }

        }
        return true;
    }
}
