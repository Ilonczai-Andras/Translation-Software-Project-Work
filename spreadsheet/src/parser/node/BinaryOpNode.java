package parser.node;

public class BinaryOpNode extends Node {

    public char op;
    public Node left;
    public Node right;

    public BinaryOpNode(char op, Node left, Node right) {
        this.op = op;
        this.right = right;
        this.left = left;
    }

    public char getOp() {
        return op;
    }

    public void setOp(char op) {
        this.op = op;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return String.format("BinaryOpNode(%s, %s, %s)", op, left, right);
    }
}
