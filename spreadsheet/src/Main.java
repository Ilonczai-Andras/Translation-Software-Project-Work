import org.antlr.v4.runtime.CharStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import ast.Node;
import parser.SpreadsheetLexer;
import parser.SpreadsheetParser;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String testFormula = "A1 + 5 * SUM(B1:C3)";
        System.out.println("Testing parsing of formula: " + testFormula);
        
        CharStream charStream = CharStreams.fromString(testFormula);
        SpreadsheetLexer lexer = new SpreadsheetLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SpreadsheetParser parser = new SpreadsheetParser(tokens);
        
        Node astRoot = parser.formula().result;
        System.out.println("Parsing successful! Root AST Node is of type: " + astRoot.getClass().getSimpleName());
        
        // Check for input1.csv
        File csvFile = new File("input1.csv");
        if (csvFile.exists()) {
            System.out.println("input1.csv was found successfully.");
        } else {
            System.out.println("input1.csv was not found.");
        }
    }
}
