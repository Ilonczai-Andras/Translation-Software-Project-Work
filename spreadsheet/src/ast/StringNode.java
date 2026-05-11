package ast;

public class StringNode implements Node {
    public String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
