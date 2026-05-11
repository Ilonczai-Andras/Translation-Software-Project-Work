package ast;

public interface Node {
    <T> T accept(Visitor<T> visitor);
}
