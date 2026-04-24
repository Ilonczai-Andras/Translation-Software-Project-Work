import evaluator.Evaluator;
import model.Cell;
import model.Spreadsheet;
import io.CsvParser;
import io.HtmlExporter;

public class Main {
    public static void main(String[] args) {
        String inputFile  = args.length > 0 ? args[0] : "src/input/input1.csv";
        String outputFile = args.length > 1 ? args[1] : "src/output/output1.html";
        String delimiter  = ";";

        System.out.println("Beolvasás: " + inputFile);
        Spreadsheet spreadsheet = CsvParser.parse(inputFile, delimiter);

        Evaluator evaluator = new Evaluator(spreadsheet);

        // Minden cellát kiértékelünk
        for (int i = 0; i < spreadsheet.getRowCount(); i++) {
            for (int j = 0; j < spreadsheet.getColumnCount(); j++) {
                String ref = (char)('A' + j) + String.valueOf(i + 1);
                Cell cell = spreadsheet.getCell(i, j);
                if (cell != null && !cell.isEvaluated()) {
                    try {
                        evaluator.evaluateCell(ref);
                    } catch (Exception e) {
                        String msg = e.getMessage() != null && e.getMessage().contains("Körkörös")
                                ? "#CIRCULAR" : "#ERROR";
                        cell.setEvaluatedValue(msg);
                        cell.setEvaluated(true);
                        System.out.println(ref + " -> " + msg + " (" + e.getMessage() + ")");
                    }
                }
                System.out.println(ref + " = " + cell.getEvaluatedValue());
            }
        }

        // HTML export
        HtmlExporter.export(spreadsheet, outputFile);
        System.out.println("HTML mentve: " + outputFile);
    }
}