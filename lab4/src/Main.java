import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class Main {
    static int  POLYNOMIAL1_DEGREE = 1000;
    static int  POLYNOMIAL2_DEGREE = 1000;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> p1Coefficients = new ArrayList<>();
        List<Integer> p2Coefficients = new ArrayList<>();
        for( int i =0 ; i< POLYNOMIAL1_DEGREE; i++){
            p1Coefficients.add(new Random().nextInt());
        }
        for( int i =0 ; i< POLYNOMIAL2_DEGREE; i++){
            p2Coefficients.add(new Random().nextInt());
        }

        Polynomial p = new Polynomial(p1Coefficients);
        Polynomial q = new Polynomial(p2Coefficients);
        System.out.println("pol p: " + p);
        System.out.println("pol q: " + q);
        System.out.println("\n");

        System.out.println(multiplicationSimpleSequential(p, q).toString() + "\n");
        sleep(1000);
        System.out.println(multiplicationSimpleParallelized(p, q).toString() + "\n");
        sleep(1000);
        System.out.println(multiplicationKaratsubaSequential(p, q).toString() + "\n");
        System.out.println(multiplicationKaratsubaParallelized(p, q).toString() + "\n");
    }
    private static Polynomial multiplicationSimpleSequential(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result1 = Multiplication.multiplicationSequentialForm(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result1;
    }
    private static Polynomial multiplicationSimpleParallelized(Polynomial p, Polynomial q) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Polynomial result2 = Multiplication.multiplicationParallelizedForm(p, q, 5);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result2;
    }
    private static Polynomial multiplicationKaratsubaSequential(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result3 = Multiplication.multiplicationKaratsubaSequential(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Karatsuba sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result3;
    }
    private static Polynomial multiplicationKaratsubaParallelized(Polynomial p, Polynomial q) throws ExecutionException,
            InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Polynomial result4 = Multiplication.multiplicationKaratsubaParallelized(p, q, 4);
        long endTime = System.currentTimeMillis();
        System.out.println("Karatsuba parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        return result4;
    }
}
