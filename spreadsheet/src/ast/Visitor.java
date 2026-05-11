package ast;

public interface Visitor<T> {
    T visit(BinaryOpNode node);
    T visit(CellRefNode node);
    T visit(FuncCallNode node);
    T visit(NumberNode node);
    T visit(RangeNode node);
    T visit(StringNode node);
}
