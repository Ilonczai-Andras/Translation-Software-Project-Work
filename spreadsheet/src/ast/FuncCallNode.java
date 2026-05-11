package ast;

import java.util.List;

public class FuncCallNode implements Node {
    public String funcName;
    public List<Node> args;

    public FuncCallNode(String funcName, List<Node> args) {
        this.funcName = funcName;
        this.args = args;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
