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



/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe responsavel por inserri dados em uma tabela
 */
public class IntrodutorDadosTabela {

    private File diretorio;
    private ComandoSql comando;

    public IntrodutorDadosTabela() {
        this.comando = ComandoSql.getInstance();
    }

    /**
     * metodo que insere dados em uma tabela se basenado pelo comandoSql
     *
     * @return true se inserio corretamente <br> false caso contrario
     * @throws Exception
     */
    public boolean inserir() throws Exception {
        atulizarDiretorio();
        //validacoes
        if (this.diretorio == null) {
            return false;
        }
        List<Coluna> colunasParaInserir = obterColunasUtilizadas();
        if (colunasParaInserir == null) {
            return false;
        }
        //inserindo
        RandomAccessFile ran = new RandomAccessFile(diretorio, "rw");
        ran.seek(diretorio.length());//escrevendo no final do arquivo
        //iterando as colunas que serao inseridas
        for (Coluna coluna : colunasParaInserir) {
            if (coluna.isDesconsiderar()) {
                byte[] byDesc = new byte[coluna.getTamanhoBytes()];

                for (byte b : byDesc) {
                    ran.writeByte(0); //uma coluna desconsiderada tem todos os seus bytes vazios
                }
                ran.writeBoolean(true);
            } else {
                //verificando o tipo da coluna
                if (coluna instanceof ColunaInt) {
                    ColunaInt colunaAux = (ColunaInt) coluna; //fazendo cast para poder obter o valor
                    ran.writeInt(colunaAux.getValor());
                    ran.writeBoolean(false);

                } else if (coluna instanceof ColunaFloat) {
                    ColunaFloat colunaAux = (ColunaFloat) coluna; //fazendo cast para poder obter o valor
                    ran.writeFloat(colunaAux.getValor());
                    ran.writeBoolean(false);

                } else if (coluna instanceof ColunaString) {
                    ColunaString colunaAux = (ColunaString) coluna; //fazendo cast para poder obter o valor

                    for (int i = 0; i < colunaAux.getValor().length(); i++) {
                        ran.write(colunaAux.getValor().charAt(i));
                    }
                    ran.writeBoolean(false);
                }
            }
        }
        return true;
    }

    /**
     * metodo que ajusta o comando sql de acordo com os dados da database
     * informada e insere esses dados
     *
     * @param db
     * @throws Exception
     */
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
            inserir(); //insere por meio do outro metodo inserir
            comando.limparColunas();
            comando.limparLiterais();

        }
    }

    /**
     * meotodo responvavel por atualizar o diretorio e fazer as devidas
     * validacoes
     */
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

    /**
     * metodo que retorna uma lista de colunas com o seu valor, e tambem com as
     * colunas ordenadas igual as colunas do cabecalho da tabela
     *
     * @return
     * @throws Exception
     */
    private List<Coluna> obterColunasUtilizadas() throws Exception {
        List<Coluna> colCab = UtilArquivos.montarColunasCabecalho(diretorio);
        //setando todas para desconsiderar
        if (comando.getNomeColunas().size() == 0) {
            JOptionPane.showMessageDialog(null, "Informe pelomenos uma coluna para inserir");
            return null;
        }
        if (comando.getNomeColunas().size() != comando.getLiterais().size()) {
            JOptionPane.showMessageDialog(null, "Quantidade de colunas e de valores para inserir sao diferentes");
            return null;
        }
        for (Coluna coluna : colCab) {
            coluna.setDesconsiderar(true);
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
                Coluna colAux = obterColunaComValor(colCab.get(i)); //obtendo a coluna com o valor
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

    /**
     * metodo que transforma uma coluna em uma coluna com valor se baseando no
     * comando sql
     *
     * @param colCabecario
     * @return generalizacao de uma coluna com valor
     */
    private Coluna obterColunaComValor(Coluna colCabecario) {
        Coluna retorno = null;
        //obtendo literal para esta coluna
        for (int i = 0; i < comando.getNomeColunas().size(); i++) {
            //obtendo somente a coluna desejada
            if (comando.getNomeColunas().get(i).equals(colCabecario.getNome())) {
                //verificando por tipo
                if (colCabecario.getTipo() == 'i') {
                    //criando coluna int
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
                    //criando coluna float
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
                    //criando coluna char
                    ColunaString novaColuna = new ColunaString();
                    novaColuna.setNome(colCabecario.getNome());
                    novaColuna.setTamanhoBytes(colCabecario.getTamanhoBytes());
                    novaColuna.setTipo('c');
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
