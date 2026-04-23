package parser.node;

public class NumberNode extends Node {

    public double value;

    public NumberNode(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("NumberNode(%s)", value);
    }
}
