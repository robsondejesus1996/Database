package br.udesc.manipArq;

import br.udesc.model.Coluna;
import br.udesc.model.ColunaFloat;
import br.udesc.model.ColunaInt;
import br.udesc.model.ColunaString;
import br.udesc.model.ComandoSql;
import br.udesc.model.DataBase;
import br.udesc.model.Linha;
import br.udesc.model.Tabela;
import br.udesc.utils.UtilArquivos;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import javax.swing.JOptionPane;

public class IntrodutorDadosTabela {

    private File diretorio;
    private ComandoSql comando;

    public IntrodutorDadosTabela() {
        this.comando = ComandoSql.getInstance();
    }

    public boolean inserir() throws Exception {
        atulizarDiretorio();
        if (this.diretorio == null) {
            return false;
        }
        List<Coluna> colunasParaInserir = obterColunasUtilizadas();
        if (colunasParaInserir == null) {
            return false;
        }
        RandomAccessFile ran = new RandomAccessFile(diretorio, "rw");
        ran.seek(diretorio.length());//escrevendo no final do arquivo
        for (Coluna coluna : colunasParaInserir) {
            if (coluna.isDesconsiderar()) {
                byte[] byDesc = new byte[coluna.getTamanhoBytes()];

                for (byte b : byDesc) {
                    ran.writeByte(0); //uma coluna desconsiderada tem todos os seus bytes vazios
                }
            } else {
                //verificando o tipo da coluna
                if (coluna instanceof ColunaInt) {
                    ColunaInt colunaAux = (ColunaInt) coluna; //fazendo cast para poder obter o valor
                    ran.writeInt(colunaAux.getValor());

                } else if (coluna instanceof ColunaFloat) {
                    ColunaFloat colunaAux = (ColunaFloat) coluna; //fazendo cast para poder obter o valor
                    ran.writeFloat(colunaAux.getValor());

                } else if (coluna instanceof ColunaString) {
                    ColunaString colunaAux = (ColunaString) coluna; //fazendo cast para poder obter o valor

                    for (int i = 0; i < colunaAux.getValor().length(); i++) {
                        ran.write(colunaAux.getValor().charAt(i));
                    }
                }
            }
        }
        return true;
    }

    public void inserir(DataBase db) throws Exception {
        comando.limparDados();
        comando.setBaseDados(db.getNome());
        for (Tabela tabela : db.getTabelas()) {
            comando.setNomeTabela(tabela.getNome());
            for (Linha linha : tabela.getLinhas()) { //tecnicamente so pode vir uma linha
                for (Coluna coluna : linha.getColunas()) {
                    ColunaString colunaAux = (ColunaString) coluna;
                    comando.addColuna(colunaAux.getNome());
                    comando.addLiteral(colunaAux.getValor());
                }
            }
            inserir();
            comando.limparColunas();
            comando.limparLiterais();

        }
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

    private List<Coluna> obterColunasUtilizadas() throws Exception {
        List<Coluna> colCab = UtilArquivos.montarColunasCabecalho(diretorio);
        //setando todas para desconsiderar
        for (Coluna coluna : colCab) {
            coluna.setDesconsiderar(true);
        }
        if ( comando.getNomeColunas().size() == 0) {
            JOptionPane.showMessageDialog(null, "Informe pelomenos uma coluna para inserir");
            return null;
        }
        
        
        //descobrindo quais as colunas da tabela sao utilizadas no comando
        for (String nomeColuna : comando.getNomeColunas()) {
            boolean flagFoiUtilizado = true; //flag para garantir que todas as colunas do comando sao utilizadas...
            for (Coluna coluna : colCab) {
                //as colunas que forem utilizadas nao sao desonsideradas
                if (coluna.getNome().equals(nomeColuna)) {
                    coluna.setDesconsiderar(false);
                    flagFoiUtilizado = false;
                }
            }
            //se esta variavel esta vazia e porque nenhuma coluna possui o mesmo nome
            if (flagFoiUtilizado) {
                JOptionPane.showMessageDialog(null, "Coluna " + nomeColuna + " invalida");
                return null;
            }
        }

        //os elementos que sao considerados devem ser atualizados
        for (int i = 0; i < colCab.size(); i++) {
            if (!colCab.get(i).isDesconsiderar()) {
                Coluna colAux = obterColunaComValor(colCab.get(i));
                if (colAux != null) {
                    colCab.set(i, colAux);
                } else {
                    //somente entra aki porque passou em alguma das condicoes do metodo obterColunaComValor()
                    return null;
                }
            }
        }
        return colCab;
    }

    private Coluna obterColunaComValor(Coluna colCabecario) {
        Coluna retorno = null;
        //obtendo literal para esta coluna
        for (int i = 0; i < comando.getNomeColunas().size(); i++) {
            //obtendo somente a coluna desejada
            if (comando.getNomeColunas().get(i).equals(colCabecario.getNome())) {
                //verificando por tipo
                if (colCabecario.getTipo() == 'i') {
                    ColunaInt novaColuna = new ColunaInt();
                    novaColuna.setNome(colCabecario.getNome());
                    novaColuna.setTamanhoBytes(colCabecario.getTamanhoBytes());
                    novaColuna.setTipo('i');
                    if (comando.getLiterais().get(i).equals("")) {
                       novaColuna.setValor(0);
                    } else {
                        novaColuna.setValor(Integer.parseInt(comando.getLiterais().get(i)));
                    }

                    retorno = novaColuna;

                } else if (colCabecario.getTipo() == 'f') {
                    ColunaFloat novaColuna = new ColunaFloat();
                    novaColuna.setNome(colCabecario.getNome());
                    novaColuna.setTamanhoBytes(colCabecario.getTamanhoBytes());
                    novaColuna.setTipo('f');
                    
                    if (comando.getLiterais().get(i).equals("")) {
                       novaColuna.setValor(0f);
                    } else {
                        novaColuna.setValor(Float.parseFloat(comando.getLiterais().get(i)));
                    }
                    
                    retorno = novaColuna;

                } else if (colCabecario.getTipo() == 'c') {
                    ColunaString novaColuna = new ColunaString();
                    novaColuna.setNome(colCabecario.getNome());
                    novaColuna.setTamanhoBytes(colCabecario.getTamanhoBytes());
                    novaColuna.setTipo('c');
                    //comando.getLiterais().set(i, comando.getLiterais().get(i).replaceAll("'", "")); // tirando aspas para inserir
                    if (comando.getLiterais().get(i).length() > novaColuna.getTamanhoBytes()) {
                        JOptionPane.showMessageDialog(null, "String maior que o suportado pela tabela");
                        return null;
                    } else {
                        novaColuna.setValor(UtilArquivos.trataTamanhoString(comando.getLiterais().get(i), novaColuna.getTamanhoBytes()));
                    }

                    retorno = novaColuna;

                }
            }
        }
        return retorno;
    }

}
