package parser.node;

import java.util.List;

public class FuncCallNode extends Node {

    public String name;
    public List<Node> arguments;

    public FuncCallNode(String name, List<Node> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("FuncCallNode(%s, %s)", name, arguments);
    }
}
