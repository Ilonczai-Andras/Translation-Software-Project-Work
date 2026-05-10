package ast;

public class RangeNode implements Node {
    public CellRefNode start;
    public CellRefNode end;

    public RangeNode(CellRefNode start, CellRefNode end) {
        this.start = start;
        this.end = end;
    }
}
