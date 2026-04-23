package parser.node;

public class RangeNode extends Node {
    public CellRefNode from;
    public CellRefNode to;

    public RangeNode(CellRefNode from, CellRefNode to) {
        this.from = from;
        this.to = to;
    }

    public CellRefNode getfrom() {
        return from;
    }

    public void setfrom(CellRefNode from) {
        this.from = from;
    }

    public CellRefNode getto() {
        return to;
    }

    public void setto(CellRefNode to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("RangeNode(%s:%s)", from, to);
    }
}
