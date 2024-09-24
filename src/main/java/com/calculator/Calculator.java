package com.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;
import java.lang.Math;

public class Calculator {
    private static final Logger logger = LogManager.getLogger(Calculator.class);
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        logger.info("Initializing Calculator App");
        System.out.println("============= Calculator App =============");
        int num = 0;
    
        while (num != -1) {
            printOps(); // No need for "this" since it's static now
            num = sc.nextInt();
            logger.info("User input: " + num);
            if (num == -1) {
                logger.info("Terminate Calculator App");
                System.out.println("\nExiting...");
                break;
            }
            double ans = parse(num, sc); 
            logger.info("Answer: " + ans);
            System.out.println("Answer: " + ans + "\n");
        }
    
    }
    
    public static void printOps() {
        System.out.println("Choose your operation:");
        System.out.println("1. Square Root");
        System.out.println("2. Factorial");
        System.out.println("3. Natural Log");
        System.out.println("4. Power Function");
        System.out.println("Enter -1 to exit");
        logger.info("Printing operations");
    }
    
    public static double parse(int choice, Scanner sc) {
        switch (choice) {
            case 1:
                System.out.println("Enter a number: ");
                int num = sc.nextInt();
                logger.info("Calculating square root of " + num);
                return sqRoot(num);
            case 2:
                System.out.println("Enter a number: ");
                num = sc.nextInt();
                logger.info("Calculating factorial of " + num);
                return factorial(num);
            case 3:
                System.out.println("Enter a number: ");
                num = sc.nextInt();
                logger.info("Calculating natural log of " + num);
                return natLog(num);
            case 4:
                System.out.println("Enter the base: ");
                int base = sc.nextInt();
                System.out.println("Enter the exponent: ");
                int exp = sc.nextInt();
                logger.info("Calculating " + base + " raised to the power of " + exp);
                return power(base, exp);
            default:
                return 0;
        }
    }
    
    public static double sqRoot(int num) {
        double ans = Math.sqrt(num);
        return ans;
    }
    
    public static double factorial(int num) {
        int ans = 1;
        for (int i = 1; i <= num; i++) {
            ans *= i;
        }
        return ans;
    }
    
    public static double natLog(int num) {
        double ans = Math.log(num);
        return ans;
    }
    
    public static double power(int base, int exp) {
        double ans = Math.pow(base, exp);
        return ans;
    }    
}
