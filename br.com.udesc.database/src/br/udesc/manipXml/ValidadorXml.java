package br.udesc.manipXml;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;



/**
 *
 * @author Robson de Jesus e Thiago Moraes Correia
 */

/**
 * classe responsavel por fazer a validacao do XML
 */
public class ValidadorXml {
    
    /**
     * metodo estatico para fazer a validacao de um XMl
     * 
     * @param arqXml xml que sera verificado
     * @return true se o xml for valido <br> false caso o contrario
     */
    public static boolean validaXML(File arqXml) {

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaLocation = new File("udescdb.xsd");
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(arqXml);
            validator.validate(source);
            return true;
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
    }
}
