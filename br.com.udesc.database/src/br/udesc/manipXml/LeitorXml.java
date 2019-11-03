package br.udesc.manipXml;

import br.udesc.model.Coluna;
import br.udesc.model.ColunaFloat;
import br.udesc.model.ColunaInt;
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

public class LeitorXml {

    private List<DataBase> dataBases;

    public LeitorXml() {
        dataBases = new ArrayList<>();
    }

    public List<DataBase> ler(File ArquivoXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(ArquivoXml);

        // aki começa a putaria
        Element inserts = doc.getDocumentElement();

        NodeList datBases = doc.getElementsByTagName("db");
        criaDatabases(datBases);

        for (int indexDb = 0; indexDb < datBases.getLength(); indexDb++) {
            if (datBases.item(indexDb).getNodeType() == Node.ELEMENT_NODE) {
                Element elementDb = (Element) datBases.item(indexDb);
              //  criaTabela(elementDb, indexDb);

            }
        }
        return this.dataBases;
    }

    private void criaDatabases(NodeList dataBases) {
        for (int i = 0; i < dataBases.getLength(); i++) {
            Node db = dataBases.item(i);

            DataBase novaDataBase = new DataBase();
            novaDataBase.setNome(db.getAttributes().getNamedItem("name").getNodeValue());
            this.dataBases.add(novaDataBase);
        }
    }
private void criaTabela(Element dataBase, int index) {
        
        NodeList tabelas = dataBase.getElementsByTagName("table");
        
        for (int i = 0; i < tabelas.getLength(); i++) {
            Node tabela = tabelas.item(i);
            if (tabela.getNodeType() == Node.ELEMENT_NODE) {
                Element elementoTabela = (Element) tabela;
                
                Tabela novaTabela = new Tabela();
                novaTabela.setNome(elementoTabela.getAttributes().getNamedItem("name").getNodeValue());
                criaColunasTabela(novaTabela, elementoTabela);
                this.dataBases.get(index).addTabela(novaTabela); 
            }

        }
    }
   

    private void criaColunasTabela(Tabela tabela, Element elementoTabela) {
        NodeList colunas = elementoTabela.getElementsByTagName("column");
        Linha linha = new Linha();
        for (int i = 0; i < colunas.getLength(); i++) {
            if (colunas.item(i).getNodeType() == Node.ELEMENT_NODE) {
                
                Element elementoColuna = (Element) colunas.item(i);
                String nome = "";
                String valor = "";
                
                NodeList atributosCol = elementoColuna.getChildNodes();
                for (int j = 0; j < atributosCol.getLength(); j++) {
                    if (atributosCol.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        
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
                
                ColunaString coluna = new ColunaString();
                coluna.setNome(nome);
                coluna.setValor(valor);
                linha.addColuna(coluna);

            }
        }
        tabela.addLinha(linha); 
    }
}
