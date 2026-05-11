package ast;

public class CellRefNode implements Node {
    public String ref;

    public CellRefNode(String ref) {
        this.ref = ref;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
