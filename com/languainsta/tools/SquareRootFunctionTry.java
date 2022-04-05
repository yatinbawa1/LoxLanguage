package com.languainsta.tools;

import java.util.Scanner;
import java.lang.Math;

public class SquareRootFunctionTry {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double x= scanner.nextDouble();
        double guess = (double) (Math.random() * (x+1)  + 1);
        double previousGuess = 0;
        int i =0;

        while (true) {
            i++;
            guess = (guess + (x/guess)) / 2;
            if (previousGuess == guess) {
                break;
            }
            previousGuess = guess;
            System.out.println("Try: "+i+" Resultant: "+guess);
        }

    }
}
