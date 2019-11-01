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
import org.antlr.v4.runtime.misc.Utils;


public class CriadorTabela {
        
    private File       diretorio;
    private ComandoSql comando;
    

    public CriadorTabela() throws FileNotFoundException {
        this.comando = ComandoSql.getInstance();
        
    }
    
    public void criarTabela() {
        atulizarDiretorio();
        if (UtilArquivos.encontrarTabela(diretorio, comando.getNomeTabela()) != null) {
            JOptionPane.showMessageDialog(null, "Tabela informada ja existe nesta base de dados");
            return;
        }
        File novaTabela = new File(diretorio.getPath()+"\\"+comando.getNomeTabela()); 
        try {
            novaTabela.createNewFile();
            criarCabecalhoTabela(novaTabela);
        } catch (Exception ex) {
            System.out.println("Houve um problema...");
        }
    }
    
    private void atulizarDiretorio() {
        this.diretorio = UtilArquivos.encontrarDataBase(this.comando.getBaseDados());
    }
    
    private void criarCabecalhoTabela(File tabela) throws Exception{
        RandomAccessFile ran = new RandomAccessFile(tabela, "rw");
        List<Coluna> colunasCabecario = converteParaColunaCabecario();
        
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

    }
    
    private List<Coluna> converteParaColunaCabecario() {
        List<Coluna> colunas = new ArrayList<Coluna>();
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
