package evaluator;

import lexer.Lexer;
import lexer.Token;
import model.Cell;
import parser.Parser;
import parser.node.*;
import model.Spreadsheet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator {

    public Spreadsheet spreadsheet;
    private Set<String> inProgress = new HashSet<>();

    public Evaluator(Spreadsheet s){
        this.spreadsheet = s;
    }

    public Object evaluate(Node node){

        Object result = null;

        if(node instanceof NumberNode){
            result = ((NumberNode) node).getValue();
        }

        if(node instanceof StringNode){
            result = ((StringNode) node).getValue();
        }

        if(node instanceof BinaryOpNode){
            BinaryOpNode binOp = (BinaryOpNode) node;
            Object leftVal = evaluate(binOp.getLeft());
            Object rightVal = evaluate(binOp.getRight());

            if(leftVal instanceof Number && rightVal instanceof Number){
                double leftNum = ((Number) leftVal).doubleValue();
                double rightNum = ((Number) rightVal).doubleValue();

                switch (binOp.getOp()){
                    case '+':
                        result = leftNum + rightNum;
                        break;
                    case '-':
                        result = leftNum - rightNum;
                        break;
                    case '*':
                        result = leftNum * rightNum;
                        break;
                    case '/':
                        result = leftNum / rightNum;
                        break;
                    default: throw new RuntimeException("Érvénytelen operandusok: " + binOp.getOp());
                }
            }
        }

        if(node instanceof CellRefNode){
            CellRefNode cellRefNode = (CellRefNode) node;
            result = evaluateCell(cellRefNode.getCellRef());
        }

        if(node instanceof RangeNode){
            throw new RuntimeException("A tartományértékelés itt nem használható, csak függvényhívásokban");
        }

        if(node instanceof FuncCallNode){
             result = evaluateFunc((FuncCallNode) node);
        }

        return result;
    }

    public Object evaluateCell(String ref){
        Object returnValue = null;

        if( inProgress.contains(ref)) {throw new RuntimeException("Körkörös hiba");}

        inProgress.add(ref);

        Cell c = spreadsheet.getCell(ref);

        if(c.isEvaluated()) {
            inProgress.remove(ref);
            return c.getEvaluatedValue();
        }
        else if(c.isExpression()){

            String expression = c.getRawInput().substring(1);

            Lexer lexer = new Lexer(expression);
            List<Token> tokens = lexer.tokenize();
            Parser parser = new Parser(tokens);
            Node ast = parser.parse();

            returnValue = evaluate(ast);
        }
        else{
            try {
                returnValue = Double.parseDouble(c.getRawInput());
            }
            catch (NumberFormatException e){
                returnValue = c.getRawInput();
            }
        }

        c.setEvaluatedValue(returnValue);
        c.setEvaluated(true);

        inProgress.remove(ref);

        return returnValue;
    }

    public Object evaluateFunc(FuncCallNode funcCallNode) {
        List<Double> values = collectValues(funcCallNode);

        switch (funcCallNode.getName()) {
            case "SUM": {
                double sum = 0;
                for (Double v : values) sum += v;
                return sum;
            }
            case "AVG": {
                if (values.isEmpty()) throw new RuntimeException("AVG: nincs érték");
                double sum = 0;
                for (Double v : values) sum += v;
                return sum / values.size();
            }
            case "MIN": {
                if (values.isEmpty()) throw new RuntimeException("MIN: nincs érték");
                double min = values.get(0);
                for (Double v : values) if (v < min) min = v;
                return min;
            }
            case "MAX": {
                if (values.isEmpty()) throw new RuntimeException("MAX: nincs érték");
                double max = values.get(0);
                for (Double v : values) if (v > max) max = v;
                return max;
            }
            default:
                throw new RuntimeException("Ismeretlen függvény: " + funcCallNode.getName());
        }
    }

    public List<Double> collectValues(FuncCallNode node) {
        List<Double> values = new ArrayList<>();

        for (Node arg : node.arguments) {
            if (arg instanceof RangeNode) {
                RangeNode range = (RangeNode) arg;

                int fromCol = range.from.cellRef.toUpperCase().charAt(0) - 'A';
                int fromRow = Integer.parseInt(range.from.cellRef.substring(1)) - 1;
                int toCol   = range.to.cellRef.toUpperCase().charAt(0) - 'A';
                int toRow   = Integer.parseInt(range.to.cellRef.substring(1)) - 1;

                for (int row = fromRow; row <= toRow; row++) {
                    for (int col = fromCol; col <= toCol; col++) {
                        String ref = (char)('A' + col) + String.valueOf(row + 1);
                        Object val = evaluateCell(ref);
                        if (val instanceof Double) values.add((Double) val);
                    }
                }
            } else if (arg instanceof CellRefNode) {
                Object val = evaluateCell(((CellRefNode) arg).getCellRef());
                if (val instanceof Double) values.add((Double) val);
            } else if (arg instanceof NumberNode) {
                values.add(((NumberNode) arg).getValue());
            }
        }

        return values;
    }
}
