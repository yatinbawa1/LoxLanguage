package com.languainsta.lox;

//import java.util.List;

abstract class Expr {

//    Interface that every
//    function to be performed
//    should implement
//    and have a method
//    for every type of expression

    interface Visitor<R> {
        // <R> Represent return type
        // because there is no fixed return
        // type, <R> is a generic one, kind of placeholder
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
    }
//  A method every class of Expr should
//  have so that a function could be passed
    abstract <R> R accept(Visitor<R> visitor);

//    A Class to handle Binary Expr
    static class Binary extends Expr {

        Binary(Expr left,Token operator,Expr right){
            this.left=left;
            this.operator=operator;
            this.right=right;
        }

        // accept method to route
        // to correct function
        // inside the function class
        // a functional class is passed
        // to our accept function
        // it then calls the visitBinaryExpr
        // function.
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Grouping extends Expr{
        Grouping(Expr expression){
            this.expression=expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expr expression;
    }

    static class Literal extends Expr{
        final Object value;
        Literal(Object value){
            this.value=value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

    }

    static class Unary extends Expr{
        final Token operator;
        final Expr right;

        Unary(Token operator,Expr right){
            this.operator=operator;
            this.right=right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

}
