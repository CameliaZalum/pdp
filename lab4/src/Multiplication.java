import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Multiplication {

    public static Polynomial multiplicationSequentialForm(Polynomial p1, Polynomial p2) {
        Polynomial coefficients = new Polynomial(p1.getDegree() + p2.getDegree() + 1);
        for (int i = 0; i < p1.getCoefficients().size(); i++) {
            for (int j = 0; j < p2.getCoefficients().size(); j++) {
                int index = i + j;
                int value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                coefficients.setCoefficient(index, coefficients.coefficients.get(index) + value);
            }
        }
        return coefficients;
    }

    public static Polynomial multiplicationParallelizedForm(Polynomial p1, Polynomial p2, int nrOfThreads) throws
            InterruptedException {
        Polynomial result = new Polynomial(p1.getDegree() + p2.getDegree() + 1);
        //calculate the coefficients
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nrOfThreads);
        int step = (result.getDegree() + 1) / nrOfThreads;
        if (step == 0) {
            step = 1;
        }
        System.out.println("STEP: " + step);
        int end;
        for (int i = 0; i < (result.getDegree() + 1); i += step) {
            end = i + step;
            Task task = new Task(i, end, p1, p2, result);
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(50, TimeUnit.SECONDS);

        return result;
    }

    public static Polynomial multiplicationKaratsubaSequential(Polynomial p1, Polynomial p2){
        int n  = Math.max(p1.getDegree(), p2.getDegree());
        if (n == 1){
            return multiplicationSequentialForm(p1, p2);
        } else {
            Polynomial upperPolynomial1 = new Polynomial(p1.getCoefficients().subList(n / 2, p1.getCoefficients().size()));
            Polynomial lowerPolynomial1 = new Polynomial(p1.getCoefficients().subList(0, n / 2));
            Polynomial upperPolynomial2 = new Polynomial(p2.getCoefficients().subList(n / 2, p2.getCoefficients().size()));
            Polynomial lowerPolynomial2 = new Polynomial(p2.getCoefficients().subList(0, n / 2));

            Polynomial firstMultiplication = multiplicationKaratsubaSequential(lowerPolynomial1, lowerPolynomial2);
            Polynomial secondMultiplication = multiplicationKaratsubaSequential(upperPolynomial1, upperPolynomial2);
            Polynomial finalMultiplication = multiplicationKaratsubaSequential(lowerPolynomial1.plus(upperPolynomial1), lowerPolynomial2.plus(upperPolynomial2));
            secondMultiplication.addZeros(n * 2);
            finalMultiplication.minus(secondMultiplication).minus(finalMultiplication).addZeros(n);
            return secondMultiplication.plus(finalMultiplication).plus(finalMultiplication);
        }
    }

}
