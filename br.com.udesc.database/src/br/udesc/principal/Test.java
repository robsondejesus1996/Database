package br.udesc.principal;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import br.udesc.udescdb.SQLiteBaseListener;
import br.udesc.udescdb.SQLiteLexer;
import br.udesc.udescdb.SQLiteParser;
/**
 * 
 * @author Robson de Jesus
 */
public class Test {

	public static void main(String[] args) {
		String create = "create table xpto (col1 int, col2 char(20), col3 float)";
		String insert = "insert into xpto (col1, col2) values (1, 'abc')";
		String select = "select * from xpto";
		
		CodePointCharStream inputStream = CharStreams.fromString(create);
		SQLiteLexer lexer = new SQLiteLexer(inputStream);
		CommonTokenStream cts = new CommonTokenStream(lexer);
		SQLiteParser parser = new SQLiteParser(cts);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.parse();
				
		ParseTreeWalker p = new ParseTreeWalker();
		p.walk(new SQLiteBaseListener(), tree);

		// agora vamos pegar as informacoes que o listener capturou e processar o comando 
	}

}
