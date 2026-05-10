package ast;

public class BinaryOpNode implements Node {
    public char op;
    public Node left;
    public Node right;

    public BinaryOpNode(char op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
