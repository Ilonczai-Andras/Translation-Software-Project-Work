package spreadsheet;

import java.io.*;
import java.util.Map;

/**
 * HTMLExporter - Az értékelt cellákat HTML táblázatba exportálja.
 */
public class HTMLExporter {
    
    public static void export(String[][] grid, Map<String, Object> evaluatedCells, 
                             Map<String, ?> cyclicErrors, String outputPath) throws IOException {
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<title>Táblázat</title>\n");
        html.append("<style>\n");
        html.append("table { border-collapse: collapse; font-family: Arial, sans-serif; }\n");
        html.append("th, td { border: 1px solid #000; padding: 10px; text-align: left; }\n");
        html.append("th { background-color: #f0f0f0; font-weight: bold; }\n");
        html.append(".error { background-color: #ffcccc; color: red; }\n");
        html.append("</style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("<table>\n");
        
        // Fejléc sor (oszlopok)
        html.append("<tr>\n");
        html.append("<th></th>"); // Sarkos cella
        for (int col = 0; col < grid[0].length; col++) {
            html.append("<th>").append(colToRef(col)).append("</th>\n");
        }
        html.append("</tr>\n");
        
        // Sorok
        for (int row = 0; row < grid.length; row++) {
            html.append("<tr>\n");
            html.append("<th>").append(row + 1).append("</th>\n"); // Sor fejléc
            
            for (int col = 0; col < grid[row].length; col++) {
                String cellRef = colToRef(col) + (row + 1);
                Object value = evaluatedCells.get(cellRef);
                boolean isError = cyclicErrors.containsKey(cellRef);
                
                if (isError) {
                    html.append("<td class=\"error\">KÖRKÖRÖS HIVATKOZÁS</td>\n");
                } else if (value != null) {
                    html.append("<td>").append(escapeHtml(String.valueOf(value))).append("</td>\n");
                } else {
                    html.append("<td></td>\n");
                }
            }
            html.append("</tr>\n");
        }
        
        html.append("</table>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(html.toString());
        }
    }
    
    private static String colToRef(int col) {
        StringBuilder sb = new StringBuilder();
        while (col >= 0) {
            sb.insert(0, (char) ('A' + col % 26));
            col = col / 26 - 1;
        }
        return sb.toString();
    }
    
    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}

