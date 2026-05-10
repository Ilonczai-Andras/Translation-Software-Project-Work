grammar Spreadsheet;

// $ java -jar ../antlr-4.11.1-complete.jar Ingredient.g4

options {
    language = Java;
}

@header {
package parser;
import ast.*;
import java.util.ArrayList;
import java.util.List;
}

formula returns [Node result]
    : expr EOF { $result = $expr.result; }
    ;

expr returns [Node result]
    : fstop=term { $result = $fstop.result; }
      ( op=(PLUS | MINUS) nxtop=term { $result = new BinaryOpNode($op.text.charAt(0), $result, $nxtop.result); } )*
    ;

term returns [Node result]
    : fstop=primary { $result = $fstop.result; }
      ( op=(STAR | SLASH) nxtop=primary { $result = new BinaryOpNode($op.text.charAt(0), $result, $nxtop.result); } )*
    ;

primary returns [Node result]
    : NUMBER { $result = new NumberNode(Double.parseDouble($NUMBER.text)); }
    | STRING { $result = new StringNode($STRING.text.substring(1, $STRING.text.length() - 1)); }
    | LPAREN expr RPAREN { $result = $expr.result; }
    | IDENT LPAREN arglist RPAREN { $result = new FuncCallNode($IDENT.text.toUpperCase(), $arglist.result); }
    | id1=IDENT COLON id2=IDENT { $result = new RangeNode(new CellRefNode($id1.text.toUpperCase()), new CellRefNode($id2.text.toUpperCase())); }
    | IDENT { $result = new CellRefNode($IDENT.text.toUpperCase()); }
    ;

arglist returns [List<Node> result]
    : { $result = new ArrayList<Node>(); }
      (
          arg1=expr { $result.add($arg1.result); }
          ( COMMA argx=expr { $result.add($argx.result); } )*
      )?
    ;

PLUS   : '+' ;
MINUS  : '-' ;
STAR   : '*' ;
SLASH  : '/' ;
LPAREN : '(' ;
RPAREN : ')' ;
COLON  : ':' ;
COMMA  : ',' ;

NUMBER : [0-9]+ ('.' [0-9]+)? ;
STRING : '"' (~'"')* '"' ;
IDENT  : [A-Za-z]+ [0-9]* ; // e.g. A1, SUM
WS     : [ \t\r\n]+ -> skip ;
