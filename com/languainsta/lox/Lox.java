package com.languainsta.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    static boolean hadError = false; // check if error has taken place
    public static void main(String[] args) throws IOException{
        if (args.length > 1) {
            System.out.println("Usage: lox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]); // run whole file
        } else {
            runPrompt(); // run prompt
        }
    }

    // Function to run file
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // read file
        run(new String(bytes,Charset.defaultCharset())); // run the file
        // Indicate an error in exit code.
        if (hadError) System.exit(65);
    }

    //  Function to run prompt
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">  ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        // Scanner Functionality will be build
        // Not in built scanner
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token: tokens) {
            System.out.println("{" + token + "}");
        }
    }

    // Error function
    static void error(int line,String Where,String message) {
        report(line,Where,message);
    }

    // Report Function for errors and such
    private static void report(int line, String where,
                               String message) {
        System.err.println(
                "[line " + line + "] Error " + where + ": " + message);
        hadError = true;
    }
}
