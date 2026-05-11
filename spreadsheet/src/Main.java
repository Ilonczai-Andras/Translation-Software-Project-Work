import utils.CSVParser;
import utils.HTMLExporter;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Használat: java Main <input.csv> <output.html>");
            return;
        }

        try {
            // 1. CSV beolvasása
            CSVParser csv = new CSVParser(args[0]);

            // 2. Kiértékelés (AST bejárás)
            Evaluator eval = new Evaluator(csv.getFlatCells());
            Map<String, Object> results = eval.evaluateAll();

            // 3. Exportálás HTML-be
            HTMLExporter.export(
                    csv.getGrid(),
                    results,
                    buildErrorMap(eval.cyclicErrors),
                    args[1]
            );

            System.out.println("Sikeres exportálás: " + args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Boolean> buildErrorMap(java.util.Set<String> errors) {
        Map<String, Boolean> map = new java.util.HashMap<>();
        for (String e : errors) map.put(e, true);
        return map;
    }
}