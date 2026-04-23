package io;

import model.Cell;
import model.Spreadsheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static Spreadsheet parse(String filePath, String delimiter){
        String line;

        Spreadsheet result = new Spreadsheet();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while((line = br.readLine()) != null) {

                String[] values = line.split(delimiter);
                List<Cell> cellList = new ArrayList<>();

                for (String value : values) {

                    Cell c = new Cell(value.trim().isEmpty() ? "" : value.trim());
                    cellList.add(c);
                }
                result.addRow(cellList);
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
