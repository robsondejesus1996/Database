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
}