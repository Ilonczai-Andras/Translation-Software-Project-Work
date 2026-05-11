import ast.*;
import org.antlr.v4.runtime.*;
import utils.CellUtils;

import java.util.*;

public class Evaluator implements ast.Visitor<Object> {
    private final Map<String, String> rawCells;
    private final Map<String, Object> cache = new HashMap<>();
    private final Set<String> visiting = new HashSet<>();
    public final Set<String> cyclicErrors = new HashSet<>();

    public Evaluator(Map<String, Object> flatCells) {
        this.rawCells = new HashMap<>();
        flatCells.forEach((k, v) -> rawCells.put(k, String.valueOf(v)));
    }

    public Map<String, Object> evaluateAll() {
        for (String ref : rawCells.keySet()) {
            evaluateCell(ref);
        }
        return cache;
    }

    private Object evaluateCell(String ref) {
        if (cache.containsKey(ref)) return cache.get(ref);
        if (visiting.contains(ref)) {
            cyclicErrors.add(ref);
            return "#KÖR";
        }

        visiting.add(ref);
        String content = rawCells.getOrDefault(ref, "");
        Object result;

        if (content.startsWith("=")) {
            try {
                SpreadsheetLexer lexer = new SpreadsheetLexer(CharStreams.fromString(content.substring(1)));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SpreadsheetParser parser = new SpreadsheetParser(tokens);
                Node ast = parser.formula().result;
                result = ast.accept(this);
            } catch (Exception e) {
                result = "#HIBA";
            }
        } else {
            result = parseLiteral(content);
        }

        visiting.remove(ref);
        cache.put(ref, result);
        return result;
    }

    private Object parseLiteral(String s) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return s; }
    }

    @Override
    public Object visit(BinaryOpNode node) {
        Object left = node.left.accept(this);
        Object right = node.right.accept(this);

        if (node.op == '+') {
            if (left instanceof String || right instanceof String) {
                return String.valueOf(left) + String.valueOf(right);
            }
            return (Double) left + (Double) right;
        }

        double l = (Double) left;
        double r = (Double) right;
        return switch (node.op) {
            case '-' -> l - r;
            case '*' -> l * r;
            case '/' -> l / r;
            default -> 0.0;
        };
    }

    @Override
    public Object visit(NumberNode node) { return node.value; }

    @Override
    public Object visit(StringNode node) { return node.value; }

    @Override
    public Object visit(CellRefNode node) {
        return evaluateCell(node.ref);
    }

    @Override
    public Object visit(FuncCallNode node) {
        if (node.funcName.equals("SUM")) {
            double sum = 0;
            for (Node arg : node.args) {
                Object val = arg.accept(this);
                if (val instanceof List) { // RangeNode eredménye
                    for (Object v : (List<?>) val) {
                        if (v instanceof Double) sum += (Double) v;
                    }
                } else if (val instanceof Double) sum += (Double) val;
            }
            return sum;
        } else if (node.funcName.equals("IND")) {
            Object target = node.args.get(0).accept(this);
            return evaluateCell(String.valueOf(target).toUpperCase());
        }
        return 0.0;
    }

    @Override
    public Object visit(RangeNode node) {
        List<Object> values = new ArrayList<>();
        int[] start = CellUtils.parseRef(node.start.ref);
        int[] end = CellUtils.parseRef(node.end.ref);

        for (int r = Math.min(start[1], end[1]); r <= Math.max(start[1], end[1]); r++) {
            for (int c = Math.min(start[0], end[0]); c <= Math.max(start[0], end[0]); c++) {
                values.add(evaluateCell(CellUtils.toRef(c, r)));
            }
        }
        return values;
    }
}