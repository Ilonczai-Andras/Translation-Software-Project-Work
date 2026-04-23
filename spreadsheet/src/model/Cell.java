package model;

public class Cell {

    private String rawInput;
    private Object evaluatedValue;
    private boolean evaluated;

    public Cell(String rawInput) {
        this.rawInput = rawInput;
        this.evaluatedValue = null;
        this.evaluated = false;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public void setEvaluatedValue(Object evaluatedValue) {
        this.evaluatedValue = evaluatedValue;
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    public String getRawInput() {
        return rawInput;
    }

    public Object getEvaluatedValue() {
        return evaluatedValue;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public boolean isExpression() {
        return rawInput.startsWith("=");
    }

    @Override
    public String toString() {
        return "Cell{" +
                "rawInput='" + rawInput + '\'' +
                ", evaluatedValue=" + evaluatedValue +
                ", evaluated=" + evaluated +
                '}';
    }
}
