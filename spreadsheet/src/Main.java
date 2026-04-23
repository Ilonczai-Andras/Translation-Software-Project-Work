import model.Cell;
import model.Spreadsheet;
import io.CsvParser;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import parser.node.Node;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/input/input1.csv";
        String delimiter = ";";
        Spreadsheet spreadsheet = CsvParser.parse(filePath, delimiter);

        for (int i = 0; i < spreadsheet.getRowCount(); i++) {
            for (int j = 0; j < spreadsheet.getColumnCount(); j++) {
                Cell cell = spreadsheet.getCell(i, j);
                if (cell != null) {
                    String raw = cell.getRawInput();

                    if (cell.isExpression()) {
                        String expr = raw.substring(1);
                        Lexer lexer = new Lexer(expr);
                        List<Token> tokens = lexer.tokenize();
                        Parser parser = new Parser(tokens);
                        Node ast = parser.parse();
                        System.out.println(raw + " -> AST: " + ast);
                    } else {
                        System.out.println(raw);
                    }
                }
            }
            System.out.println();
        }
    }
}