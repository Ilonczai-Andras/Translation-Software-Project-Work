package parser.node;

public class StringNode extends Node {

    public String value;

    public StringNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("StringNode(%s)", value);
    }
}
