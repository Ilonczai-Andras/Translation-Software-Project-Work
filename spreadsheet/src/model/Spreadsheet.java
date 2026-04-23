package model;

import java.util.List;

public class Spreadsheet {

    List<List<Cell>> grid = new java.util.ArrayList<>();
    int rowCount;
    int columnCount;

    public void addRow(List<Cell> row){
        grid.add(row);
        rowCount++;

        columnCount = Math.max(columnCount, row.size());
    }

    public Cell getCell(int row, int column){
        if(row < rowCount && column < columnCount){
            return grid.get(row).get(column);
        } else {
            return null;
        }
    }

    //A1 B2...
    public Cell getCell(String ref){
        char columnLetter = ref.toUpperCase().charAt(0);
        int column = columnLetter - 'A';
        int row = Integer.parseInt(ref.substring(1)) - 1;

        return getCell(row, column);
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
}
