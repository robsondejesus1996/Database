package br.udesc.manipXml;

import br.udesc.model.ColunaString;
import br.udesc.model.DataBase;
import br.udesc.model.Linha;
import br.udesc.model.Tabela;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe responsavel por fazer a leitura de um XML
 *
 */
public class LeitorXml {

    private List<DataBase> dataBases;

    public LeitorXml() {
        dataBases = new ArrayList<>();
    }

    /**
     * metodo que faz a leitura de um xml e retorna uma lista de objetos dataBase
     *
     * @param ArquivoXml
     * @return uma lista de objtos do tipo DataBase com todas as informaçoes que
     * esta no xml
     * @throws Exception
     */
    public List<DataBase> ler(File ArquivoXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(ArquivoXml);

        // aki começa a putaria
        Element inserts = doc.getDocumentElement();
        NodeList datBases = doc.getElementsByTagName("db"); //obtendo elementos db
        criaDatabases(datBases); //criando databases
        
        //iterando todas as databases do xml
        for (int indexDb = 0; indexDb < datBases.getLength(); indexDb++) {
            //se for um elemento
            if (datBases.item(indexDb).getNodeType() == Node.ELEMENT_NODE) {
                Element elementDb = (Element) datBases.item(indexDb);// cast do node atual para elemento
                criaTabela(elementDb, indexDb);//criando tabela e adicionando na database da posicao = indexDb

            }
        }
        return this.dataBases;
    }
    /**
     * cria objetos do tipo DataBase e adiciona no atributo dessa classe dataBases
     * 
     * @param dataBases 
     */
    private void criaDatabases(NodeList dataBases) {
        for (int i = 0; i < dataBases.getLength(); i++) {
            Node db = dataBases.item(i);

            DataBase novaDataBase = new DataBase();
            novaDataBase.setNome(db.getAttributes().getNamedItem("name").getNodeValue());
            this.dataBases.add(novaDataBase);
        }
    }
    
    /**
     * metodo que cria objetos do tipo Tabela e adiciona em uma DataBase da lista de databases
     * 
     * @param dataBase Elemento do xml db
     * @param index  interio da posiçao da dataBase na lista de databases desta Classe
     */
    private void criaTabela(Element dataBase, int index) {
        //obtendo as tabelas destea base de dados
        NodeList tabelas = dataBase.getElementsByTagName("table");
        
        for (int i = 0; i < tabelas.getLength(); i++) {
            Node tabela = tabelas.item(i);
            if (tabela.getNodeType() == Node.ELEMENT_NODE) {
                Element elementoTabela = (Element) tabela;
                //criando tabela
                Tabela novaTabela = new Tabela();
                novaTabela.setNome(elementoTabela.getAttributes().getNamedItem("name").getNodeValue());
                criaColunasTabela(novaTabela, elementoTabela);//adicionando as colunas nessa tabela
                this.dataBases.get(index).addTabela(novaTabela); //adiconando a tabela na databese
            }

        }
    }
    /**
     * metodo que faz a leitura do xml e adiciona as colunas no objeto Tabela 
     * 
     * @param tabela objeto do tipo tabela que sera adicionada as colunas
     * @param elementoTabela 
     */
    private void criaColunasTabela(Tabela tabela, Element elementoTabela) {
        NodeList colunas = elementoTabela.getElementsByTagName("column");
        Linha linha = new Linha();
        for (int i = 0; i < colunas.getLength(); i++) {
            if (colunas.item(i).getNodeType() == Node.ELEMENT_NODE) {
                //obtendo a coluna
                Element elementoColuna = (Element) colunas.item(i);
                String nome = "";
                String valor = "";
                //obtendo atributos
                NodeList atributosCol = elementoColuna.getChildNodes();
                for (int j = 0; j < atributosCol.getLength(); j++) {
                    if (atributosCol.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        //obtendo os valores daquela coluna
                        Element atributo = (Element) atributosCol.item(j);
                        switch (atributo.getTagName()) {
                            case "name":
                                nome = (atributo.getTextContent() == null) ? " " : atributo.getTextContent();
                                break;
                            case "value":
                                valor = (atributo.getTextContent() == null) ? " " : atributo.getTextContent();
                                break;
                        }
                    }
                }
                //estao abertas as porteiras da gambiarra
                //neste caso podem ser consideradas todas ColunasString porque esse tratamento so é necessario no momento da insercao
                ColunaString coluna = new ColunaString();
                coluna.setNome(nome);
                coluna.setValor(valor);
                linha.addColuna(coluna);//adicionando a coluna

            }
        }
        tabela.addLinha(linha); //adicionando a linha
    }
}
