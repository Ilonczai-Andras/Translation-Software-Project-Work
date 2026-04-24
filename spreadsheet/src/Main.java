import evaluator.Evaluator;
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

        Evaluator evaluator = new Evaluator(spreadsheet);

        for (int i = 0; i < spreadsheet.getRowCount(); i++) {
            for (int j = 0; j < spreadsheet.getColumnCount(); j++) {
                // ref felépítése: A1, B1, C1, A2...
                String ref = (char)('A' + j) + String.valueOf(i + 1);
                Cell cell = spreadsheet.getCell(i, j);
                if (cell != null) {
                    Object result = evaluator.evaluateCell(ref);
                    System.out.println(ref + " = " + result);
                }
            }
        }
    }
}