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


public class CriadorTabela {
        
    private File       diretorio;
    private ComandoSql comando;
    

    public CriadorTabela() throws FileNotFoundException {
        this.comando = ComandoSql.getInstance();
        
    }
    
    public boolean criarTabela() {
        atulizarDiretorio();
        if (diretorio == null) {
            JOptionPane.showMessageDialog(null, "Base de dados nao encontrada");
            return false;
        }
        if (UtilArquivos.encontrarTabela(diretorio, comando.getNomeTabela()) != null) {
            JOptionPane.showMessageDialog(null, "Tabela informada ja existe nesta base de dados");
            return false;
        }
        
        try {
            if (!criarCabecalhoTabela(diretorio.getPath()+"\\"+comando.getNomeTabela())) {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Houve um problema...");
            return false;
        }
        return true;
    }
    
    private void atulizarDiretorio() {
        this.diretorio = UtilArquivos.encontrarDataBase(this.comando.getBaseDados());
    }
    
    private boolean criarCabecalhoTabela(String tabela) throws Exception{
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
    
    private List<Coluna> converteParaColunaCabecario() {
        List<Coluna> colunas = new ArrayList<Coluna>();
        if (comando.getNomeColunas().size() == 0) {
            JOptionPane.showMessageDialog(null, "Erro: a tabela precisa ter pelomenos uma coluna");
            return null;
        }
        for (int i = 0; i < comando.getNomeColunas().size(); i++) {
            Coluna colunaAux = new Coluna();
            colunaAux.setNome(comando.getNomeColunas().get(i));
            colunaAux.setTipo(comando.getTipoColunas().get(i).charAt(0));
            colunaAux.setTamanhoBytes(obterTamanhoBytesColuna(comando.getTipoColunas().get(i)));
            colunas.add(colunaAux);
        }

        return colunas;
    }
    
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
