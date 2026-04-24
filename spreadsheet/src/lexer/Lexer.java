package lexer;

import java.util.List;

public class Lexer {

    public String input;
    public int pos = 0;

    public Lexer(String input){
        this.input = input;
    }

    public List<Token> tokenize(){
        List<Token> result = new java.util.ArrayList<>();

        while (!isAtEnd()) {
            char c = peek();

            // Whitespace-ok kihagyása
            if (c == ' ' || c == '\t') {
                advance();
                continue;
            }

            //Számok
            if(Character.isDigit(c)){
                result.add(readNumber());
                continue;
            }

            // Stringek
            if(c == '"'){
                result.add(readString());
                continue;
            }

            // Azonosító / cellahivatkozás / függvénynév
            if(Character.isLetter(c)){
                result.add(readIdent());
                continue;
            }

            // Operátorok és egyéb jelek
            switch (c) {
                case '+' -> { result.add(new Token(TokenType.PLUS,   "+")); advance(); }
                case '-' -> { result.add(new Token(TokenType.MINUS,  "-")); advance(); }
                case '*' -> { result.add(new Token(TokenType.STAR,   "*")); advance(); }
                case '/' -> { result.add(new Token(TokenType.SLASH,  "/")); advance(); }
                case '(' -> { result.add(new Token(TokenType.LPAREN, "(")); advance(); }
                case ')' -> { result.add(new Token(TokenType.RPAREN, ")")); advance(); }
                case ':' -> { result.add(new Token(TokenType.COLON,  ":")); advance(); }
                case ',' -> { result.add(new Token(TokenType.COMMA,  ",")); advance(); }
                default  -> throw new RuntimeException("Unexpected character: '" + c + "' at position " + pos);
            }
        }

        result.add(new Token(TokenType.EOF, ""));
        return result;
    }

    char peek(){
        return input.charAt(pos);
    }

    char advance(){
        return input.charAt(pos++);
    }

    boolean isAtEnd(){
        return pos >= input.length();
    }

    Token readNumber(){
        int start = pos;
        boolean hasDot = false;

        while (!isAtEnd())
        {
            char c = peek();
            if (Character.isDigit(c)) {
                advance();
                continue;

            }

            if (c == '.' && !hasDot) {
                hasDot = true;
                advance();
                continue;
            }

            break;
        }

        String numberText = input.substring(start, pos);

        if (numberText.equals(".")) {
            throw new RuntimeException("Invalid number format at position " + start);
        }

        return new Token(TokenType.NUMBER, numberText);

    }

    Token readString(){
        advance(); // első " átugrása
        int start = pos;

        while (!isAtEnd() && peek() != '"') {
            advance();
        }

        if (isAtEnd()) {
            throw new RuntimeException("Unterminated string literal at position " + start);
        }

        advance(); // utolsó " átugrása
        return new Token(TokenType.STRING, input.substring(start + 1, pos - 1));
    }

    Token readIdent(){
        int start = pos;

        while (!isAtEnd() && (Character.isLetterOrDigit(peek()))) {
            advance();
        }

        return new Token(TokenType.IDENT, input.substring(start, pos));
    }
}
