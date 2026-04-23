package parser;

import lexer.Token;
import lexer.TokenType;
import parser.node.*;

import java.util.ArrayList;

import java.util.List;

public class Parser {

    public List<Token> tokens;
    public int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node parse(){
        return parseExpression();
    }

    private Node parseExpression() {
        Node left = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            Token operator = advance();
            Node right = parseTerm();
            left = new BinaryOpNode(operator.getValue().charAt(0), left, right);
        }

        return left;
    }

    private Node parseTerm() {
        Node left = parsePrimary();
        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            Token operator = advance();
            Node right = parsePrimary();
            left = new BinaryOpNode(operator.getValue().charAt(0), left, right);
        }

        return left;
    }

    public Node parsePrimary(){
        if (check(TokenType.NUMBER)) {
            Token t = advance();
            return new NumberNode(Double.parseDouble(t.getValue()));
        }

        if (check(TokenType.STRING)) {
            Token t = advance();
            return new StringNode(t.getValue());
        }

        if (check(TokenType.LPAREN)) {
            expect(TokenType.LPAREN);
            Node expr = parseExpression();
            expect(TokenType.RPAREN);
            return expr;
        }

        if (check(TokenType.IDENT)) {
            Token name = advance(); // pl. "A1", "SUM", "IND"

            // függvényhívás: SUM(...), IND(...)
            if (check(TokenType.LPAREN)) {
                expect(TokenType.LPAREN);
                List<Node> args = new ArrayList<>();
                while (!check(TokenType.RPAREN)) {
                    args.add(parseExpression());
                    if (check(TokenType.COMMA)) {
                        advance();
                    }
                }
                expect(TokenType.RPAREN);
                return new FuncCallNode(name.getValue().toUpperCase(), args);
            }

            // tartomány: A1:B2
            if (check(TokenType.COLON)) {
                advance(); // : átlépése
                Token to = advance(); // jobb oldali cella pl. "B2"
                return new RangeNode(new CellRefNode(name.getValue()), new CellRefNode(to.getValue()));
            }

            // egyszerű cellahivatkozás: A1, B2
            return new CellRefNode(name.getValue());
        }

        throw new RuntimeException("Unexpected token: " + peek().getType() + " (" + peek().getValue() + ")");
    }

    //segéd metódousok
    public Token peek() {
        return tokens.get(pos);
    }

    public Token advance(){
        return tokens.get(pos++);
    }

    public boolean check(TokenType type){
        return peek().getType() == type;
    }

    public Token expect(TokenType type){
        if (check(type)) {
            return advance();
        }
        throw new RuntimeException("Expected token of type " + type + " but got " + peek().getType());
    }
}
