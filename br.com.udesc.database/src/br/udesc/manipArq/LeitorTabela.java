package br.udesc.manipArq;

import br.udesc.model.Coluna;
import br.udesc.model.ColunaFloat;
import br.udesc.model.ColunaInt;
import br.udesc.model.ColunaString;
import br.udesc.model.ComandoSql;
import br.udesc.model.Linha;
import br.udesc.model.Tabela;
import br.udesc.utils.UtilArquivos;
import java.io.File;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;

public class LeitorTabela {

    private File diretorio;
    private ComandoSql comando;

    public LeitorTabela() {
        this.comando = ComandoSql.getInstance();
    }
    
    public Tabela ler() throws Exception {
        atulizarDiretorio();
        if (diretorio == null) {
            return null;
        }
        Tabela tabelaRetorno = new Tabela();
        tabelaRetorno.setColunasCabecario(UtilArquivos.montarColunasCabecalho(diretorio));
        RandomAccessFile ran = new RandomAccessFile(diretorio, "rw");
        
        int BytesDeCabecario = ran.readInt(); 
        ran.seek(BytesDeCabecario + 4); //somente para pular os bytes do cabecalho
        
        long bytesParaLer = diretorio.length() - (BytesDeCabecario + 4);
        while (bytesParaLer > 0) {            
            Linha linhaAux = new Linha();
           
            //para cada coluna do cabecalho
            for (Coluna coluna : tabelaRetorno.getColunasCabecario()) {
                
                if (coluna.getTipo() == 'i') {
                    ColunaInt colAux = new ColunaInt();
                    colAux.setNome(coluna.getNome());
                    colAux.setTipo(coluna.getTipo());
                    colAux.setTamanhoBytes(coluna.getTamanhoBytes());
                    colAux.setValor(ran.readInt());
                    
                    bytesParaLer-= coluna.getTamanhoBytes();
                    linhaAux.addColuna(colAux);
                    
                } else if (coluna.getTipo() == 'f') {
                    ColunaFloat colAux = new ColunaFloat();
                    colAux.setNome(coluna.getNome());
                    colAux.setTipo(coluna.getTipo());
                    colAux.setTamanhoBytes(coluna.getTamanhoBytes());
                    colAux.setValor(ran.readFloat());
                    
                    bytesParaLer-= coluna.getTamanhoBytes();
                    linhaAux.addColuna(colAux);
                    
                } else if (coluna.getTipo() == 'c') {
                    ColunaString colAux = new ColunaString();
                    colAux.setNome(coluna.getNome());
                    colAux.setTipo(coluna.getTipo());
                    colAux.setTamanhoBytes(coluna.getTamanhoBytes());
                    
                    byte[] by = new byte[coluna.getTamanhoBytes()];
                    ran.read(by);
                    String valor = "";
                    
                    for (byte b : by) {
                        valor += (char) b;
                    }
                    
                    colAux.setValor(valor);
                    
                    bytesParaLer-= coluna.getTamanhoBytes();
                    linhaAux.addColuna(colAux);
                }
            }
            tabelaRetorno.addLinha(linhaAux);
            linhaAux = new Linha();
            
        }
        return tabelaRetorno;
    }
    
    private void atulizarDiretorio() {
       this.diretorio = UtilArquivos.encontrarDataBase(this.comando.getBaseDados());
        if (diretorio == null) {
            JOptionPane.showMessageDialog(null, "base de dados nao encontrada");
        } else {
            this.diretorio = UtilArquivos.encontrarTabela(diretorio, comando.getNomeTabela());
            if (diretorio == null) {
                JOptionPane.showMessageDialog(null, "tabela nao encontrada");
            }
        }
    }
}
