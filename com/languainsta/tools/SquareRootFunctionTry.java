package com.languainsta.tools;
import java.lang.Math;

public class SquareRootFunctionTry {
    public static void main(String[] args) {
        int l = sqrt(256);
        System.out.println(l);
    }

    public static int sqrt(int x) {
        int guess = (int) (Math.random() * (x+1)  + 1);
        System.out.println("Starting Guess: "+guess);
        int previousGuess = 0;
        int i =0;

        while (true) {
            i++;
            // Method for finding a fixed point of
            // a function F ie F(y) = y

            // start with
            // a random guess y
            // keep applying f over and over
            // until result doesnt change much

            // example-----
            // to compute sqrt(x), find a fixed
            // we do not know function that will
            // compute us sqrt
            // so we start with an asumption
            // let y = sqrt(x);
            // 2y/2 = sqrt(x);
            // (y+y)/2 = sqrt(x);
            // (y+sqrt(x))/2 = sqrt(x)
            // (y+(sqrt(x).sqrt(x))/sqrt(x)) / 2 = sqrt(x)
            // (y+x/sqrt(x)) / 2 == sqrt(x)
            // ----------------------
            // (y+x/y)/2 = sqrt(x)
            // ----------------------
            // we have got a function sqrt(x)
            // now we loop the function until
            // the value doesnt change, we get our answer
            // we are finding fixed point to lower our error

            System.out.println(guess + " + " + "("+x+"/"+guess+")" + "/2");
            guess = (guess + (x/guess)) / 2;
            System.out.println("answer: "+guess);
            if (previousGuess == guess) {
                break;
            }
            previousGuess = guess;
        }

        return guess;
    }
}
