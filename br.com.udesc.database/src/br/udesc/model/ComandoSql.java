package br.udesc.model;

import br.udesc.utils.UtilArquivos;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe que representa um comando sql
 * esta classe e um SingleTom
 * 
 */
public class ComandoSql {
    
    //atributo da unica instancia deste objeto no projeto inteiro
    private static ComandoSql instance;
    
    private String       comando;
    private String       baseDados;
    private String       nomeTabela;
    private List<String> nomeColunas;
    private List<String> tipoColunas;
    private List<String> literais;
    
    //construtor privado
    private ComandoSql() {
        nomeColunas = new ArrayList<String>();
        tipoColunas = new ArrayList<String>();
        literais    = new ArrayList<String>();
    }
    
    //metodo que retorna a instancia deste objeto
    public static ComandoSql getInstance() {
        if (instance == null) {
            instance = new ComandoSql();
        }
        return instance;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public void setBaseDados(String baseDados) {
        this.baseDados = baseDados;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public void addColuna(String nomeColuna) {
        nomeColunas.add(UtilArquivos.trataTamanhoString(nomeColuna, 20));
    }

    public void addTipoColuna(String tipoColuna) {
        tipoColunas.add(tipoColuna);
    }

    public void addLiteral(String literal) {
        literal = literal.replaceAll("'", "");
        literais.add(literal);
    }

    public String getComando() {
        return comando;
    }

    public String getBaseDados() {
        return baseDados;
    }

    public String getNomeTabela() {
        return nomeTabela + ".dat";
    }
    public String getNomeTabela2() {
        return nomeTabela;
    }

    public List<String> getNomeColunas() {
        return nomeColunas;
    }

    public List<String> getTipoColunas() {
        return tipoColunas;
    }

    public List<String> getLiterais() {
        return literais;
    }
    
    public void limparDados() {
        limparColunas();
        limparLiterais();
        comando = "";
        baseDados = "";
        nomeTabela = "";
    }
    
    public void limparColunas() {
        nomeColunas.clear();
        tipoColunas.clear();
    }
    public void limparLiterais() {
        literais.clear();
    }

}
