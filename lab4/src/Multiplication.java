import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return multiplicationSequentialForm(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.coefficients.size()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.coefficients.size()));

        Polynomial z1 = multiplicationKaratsubaSequential(lowP1, lowP2);
        Polynomial z2 = multiplicationKaratsubaSequential(plus(lowP1, highP1), plus(lowP2, highP2));
        Polynomial z3 = multiplicationKaratsubaSequential(highP1, highP2);

        //calculate the final result
        Polynomial r1 = addZeros(z3, 2 * len);
        Polynomial r2 = addZeros(minus(minus(z2, z3), z1), len);
        Polynomial result = plus(plus(r1, r2), z1);
        return result;
    }
    public static Polynomial minus(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) - p2.getCoefficients().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        //remove coefficients starting from biggest power if coefficient is 0

        int i = coefficients.size() - 1;
        while (coefficients.get(i) == 0 && i > 0) {
            coefficients.remove(i);
            i--;
        }

        return new Polynomial(coefficients);
    }
    public static Polynomial addZeros(Polynomial a, int n){
        List<Integer> newCoefficients = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            newCoefficients.add(0);
        }
        for (int i = 0; i < a.getDegree() + 1; i++) {
            newCoefficients.add(a.coefficients.get(i));
        }
        return new Polynomial(newCoefficients);
    }
    public static Polynomial plus(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);

        for (int i = 0; i <= minDegree; i++) {
            coefficients.add(p1.getCoefficients().get(i) + p2.getCoefficients().get(i));
        }

        addRemainingCoefficients(p1, p2, minDegree, maxDegree, coefficients);

        return new Polynomial(coefficients);
    }
    private static void addRemainingCoefficients(Polynomial p1, Polynomial p2, int minDegree, int maxDegree,
                                                 List<Integer> coefficients) {
        if (minDegree != maxDegree) {
            if (maxDegree == p1.getDegree()) {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p1.getCoefficients().get(i));
                }
            } else {
                for (int i = minDegree + 1; i <= maxDegree; i++) {
                    coefficients.add(p2.getCoefficients().get(i));
                }
            }
        }
    }

    public static Polynomial multiplicationKaratsubaParallelized(Polynomial p1, Polynomial p2, int depth) throws ExecutionException, InterruptedException {
        if (depth > 4) {
            return multiplicationKaratsubaSequential(p1, p2);
        }

        if (p1.getDegree() < 2 || p2.getDegree() < 2) {
            return multiplicationKaratsubaSequential(p1, p2);
        }

        int len = Math.max(p1.getDegree(), p2.getDegree()) / 2;
        Polynomial lowP1 = new Polynomial(p1.getCoefficients().subList(0, len));
        Polynomial highP1 = new Polynomial(p1.getCoefficients().subList(len, p1.coefficients.size()));
        Polynomial lowP2 = new Polynomial(p2.getCoefficients().subList(0, len));
        Polynomial highP2 = new Polynomial(p2.getCoefficients().subList(len, p2.coefficients.size()));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polynomial> task1 = () -> multiplicationKaratsubaParallelized(lowP1, lowP2, depth + 1);
        Callable<Polynomial> task2 = () -> multiplicationKaratsubaParallelized(plus(lowP1, highP1),
                plus(lowP2, highP2), depth + 1);
        Callable<Polynomial> task3 = () -> multiplicationKaratsubaParallelized(highP1, highP2, depth);

        Future<Polynomial> f1 = executor.submit(task1);
        Future<Polynomial> f2 = executor.submit(task2);
        Future<Polynomial> f3 = executor.submit(task3);

        executor.shutdown();

        Polynomial z1 = f1.get();
        Polynomial z2 = f2.get();
        Polynomial z3 = f3.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        //calculate the final result
        Polynomial r1 = addZeros(z3, 2 * len);
        Polynomial r2 = addZeros(minus(minus(z2, z3), z1), len);
        Polynomial result = plus(plus(r1, r2), z1);
        return result;
    }
}
