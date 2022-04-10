package com.languainsta.lox;

// Visitor<String> because want to return string
public class AstPrinter implements Expr.Visitor<String>{

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme(),
                expr.left,expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group",expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme(),expr.right);
    }

    private String parenthesize(String operator, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(operator);

        for (Expr expr : exprs ) {
            builder.append(" ");
            // recursively call all the other
            // accept methods
            // for example ->
            // expr = Binary(Unary(-25),"*",Literal(45))
            // Binary.accept(AstPrinter) -> visitBinaryExpr()
            // -> parenthesize("*",Unary(-25),Literal(45))
            // -> Unary.accept(astPrinter) && -> Literal.accept(AstPrinter))
            // -> visitUnaryExpr("-",25) && -> visitLiteralExpr(45) -> return 45
            // -> parenthesize(25)
            // -> visitLiteralExpr -> 25
            // Binary -> (* (- 25) 45)
            builder.append(expr.accept(this));
        }

        builder.append(")");

        return builder.toString();
    }
}
