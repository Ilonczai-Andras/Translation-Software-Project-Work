package parser.node;

public class CellRefNode extends Node {

    public String cellRef;

    public CellRefNode(String cellRef) {
        this.cellRef = cellRef;
    }

    public String getCellRef() {
        return cellRef;
    }

    public void setCellRef(String cellRef) {
        this.cellRef = cellRef;
    }

    @Override
    public String toString() {
        return String.format("CellRefNode(%s)", cellRef);
    }
}
