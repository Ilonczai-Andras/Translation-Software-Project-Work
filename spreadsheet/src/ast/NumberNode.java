package ast;

public class NumberNode implements Node {
    public double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
