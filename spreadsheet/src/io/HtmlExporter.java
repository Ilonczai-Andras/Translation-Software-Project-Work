package io;

import model.Cell;
import model.Spreadsheet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlExporter {

    public static void export(Spreadsheet spreadsheet, String outputPath) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"hu\">\n");
        sb.append("<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <title>Spreadsheet</title>\n");
        sb.append("  <style>\n");
        sb.append("    table { border-collapse: collapse; font-family: Arial, sans-serif; }\n");
        sb.append("    th, td { border: 1px solid #aaa; padding: 8px 14px; text-align: center; }\n");
        sb.append("    th { background-color: #f0f0f0; font-weight: bold; }\n");
        sb.append("    .error { background-color: #ffcccc; color: #cc0000; font-weight: bold; }\n");
        sb.append("  </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<table>\n");

        // Fejléc sor: A, B, C...
        sb.append("  <tr><th></th>");
        for (int col = 0; col < spreadsheet.getColumnCount(); col++) {
            sb.append("<th>").append((char)('A' + col)).append("</th>");
        }
        sb.append("</tr>\n");

        // Adatsorok
        for (int row = 0; row < spreadsheet.getRowCount(); row++) {
            sb.append("  <tr>");
            sb.append("<th>").append(row + 1).append("</th>");

            for (int col = 0; col < spreadsheet.getColumnCount(); col++) {
                Cell cell = spreadsheet.getCell(row, col);
                if (cell == null) {
                    sb.append("<td></td>");
                } else {
                    Object val = cell.getEvaluatedValue();
                    String display = val == null ? "" : val.toString();
                    boolean isError = display.startsWith("#");

                    if (isError) {
                        sb.append("<td class=\"error\">").append(escapeHtml(display)).append("</td>");
                    } else {
                        // Ha egész szám kinézetű double, pl. 12.0 → 12
                        if (val instanceof Double) {
                            double d = (Double) val;
                            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                                display = String.valueOf((long) d);
                            }
                        }
                        sb.append("<td>").append(escapeHtml(display)).append("</td>");
                    }
                }
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("HTML export hiba: " + e.getMessage(), e);
        }
    }

    private static String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}

