package br.udesc.manipArq;

import br.udesc.model.Coluna;
import br.udesc.model.ComandoSql;
import br.udesc.utils.UtilArquivos;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe responsavel por criar as tabelas
 */
public class CriadorTabela {

    private File diretorio;
    private ComandoSql comando;

    public CriadorTabela() throws FileNotFoundException {
        this.comando = ComandoSql.getInstance();

    }

    /**
     * metodo que cria atabela se baseando no comando sql
     *
     * @return
     */
    public boolean criarTabela() {
        atulizarDiretorio();
        //validacoes
        if (diretorio == null) {
            JOptionPane.showMessageDialog(null, "Base de dados nao encontrada");
            return false;
        }
        if (UtilArquivos.encontrarTabela(diretorio, comando.getNomeTabela()) != null) {
            JOptionPane.showMessageDialog(null, "Tabela informada ja existe nesta base de dados");
            return false;
        }

        try {
            if (!criarCabecalhoTabela(diretorio.getPath() + "\\" + comando.getNomeTabela()) /*criando aki*/) {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Houve um problema...");
            return false;
        }
        return true;
    }

    /**
     * metodo que seta o diretorio onde sera criado a tabela
     */
    private void atulizarDiretorio() {
        this.diretorio = UtilArquivos.encontrarDataBase(this.comando.getBaseDados());
    }

    /**
     * metodo que cria o arquivo da tabela e escreve o cabecalho com as
     * informacoes sobre as colunas que a tabela possui
     *
     * @param tabela nome da tabela
     * @return
     * @throws Exception
     */
    private boolean criarCabecalhoTabela(String tabela) throws Exception {
        List<Coluna> colunasCabecario = converteParaColunaCabecario();
        if (colunasCabecario == null) {
            return false;
        }
        File arquiVoTabela = new File(tabela); // criando a tabela
        RandomAccessFile ran = new RandomAccessFile(arquiVoTabela, "rw");

        //gravando numero de bytes das informacoes
        ran.writeInt(colunasCabecario.size() * 26);
        //gravando info das colunas
        for (Coluna coluna : colunasCabecario) {
            //salvando nome da coluna no cabecario
            String nomeColuna = UtilArquivos.trataTamanhoString(coluna.getNome(), 20);
            for (int i = 0; i < nomeColuna.length(); i++) {
                ran.write(nomeColuna.charAt(i));
            }
            //gravando tipo da coluna
            ran.writeChar(coluna.getTipo());
            //gravando tamanho da coluna
            ran.writeInt(coluna.getTamanhoBytes());
        }
        return true;
    }

    /**
     * metodo que cria uma lista de colunas se baseando nas informaçoes contidas
     * no comando sql
     *
     * @return lista de colunas do comando sql
     */
    private List<Coluna> converteParaColunaCabecario() {
        List<Coluna> colunas = new ArrayList<Coluna>();
        //validacao
        if (comando.getNomeColunas().size() == 0) {
            JOptionPane.showMessageDialog(null, "Erro: a tabela precisa ter pelomenos uma coluna");
            return null;
        }
        //criando as colunas
        for (int i = 0; i < comando.getNomeColunas().size(); i++) {
            Coluna colunaAux = new Coluna();
            colunaAux.setNome(comando.getNomeColunas().get(i));
            colunaAux.setTipo(comando.getTipoColunas().get(i).charAt(0));
            colunaAux.setTamanhoBytes(obterTamanhoBytesColuna(comando.getTipoColunas().get(i)));
            colunas.add(colunaAux); //adicionando na lista
        }

        return colunas;
    }

    /**
     * metodo para obter o tamanho que um tipo de coluna ira ocupar no arquivo
     * da tabela
     *
     * @param tipo
     * @return numero de bytes
     */
    private int obterTamanhoBytesColuna(String tipo) {
        switch (tipo.charAt(0)) {
            case 'i':
                return 4;
            case 'f':
                return 4;
            case 'c':
                return obterNumeroTipoChar(tipo);
        }
        return -1;
    }
    /**
     * metodo que recebe exclusivamente o tipo char(numero)
     * e obtem o numero de bytes que ele ira ocupar no arquivo da tabela
     * 
     * @param tipo
     * @return numro de bytes
     */
    private int obterNumeroTipoChar(String tipo) {
        String numero = "";
        for (int i = 5; i < tipo.length(); i++) {
            if (tipo.charAt(i) != ')') {
                numero += tipo.charAt(i);
            }
        }
        return Integer.parseInt(numero);
    }

}
