import model.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MAin {
    public static void main(String[] args) {
        int rows = 10;
        int cols = 10;
        int elements = 0;
        int count = 20;
        int task = (rows*cols + count-1) / count;
        Matrix a = new Matrix(rows, cols);
        Matrix b = new Matrix(rows, cols);
        Matrix c = new Matrix(rows, cols, 0);
        List<MatrixMultiplication> threads = new ArrayList<>();

        float start =  System.nanoTime() / 1000000;
        MatrixMultiplication mm = new MatrixMultiplication(a,b,c);

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mm.addPointToWorkload(i,j);
                elements++;
                if(elements == count)
                {
                    elements = 0;
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);

                }
            }
        }
        if(elements > 0) {
            threads.add(mm);
            elements = 0;
        }
        for (Thread t: threads) {
            t.start();

        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        float end = System.nanoTime() / 1000000;
        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);
//------------------------------------------------------------------------------
        start =  System.nanoTime() / 1000000;
        threads = new ArrayList<>();
        mm = new MatrixMultiplication(a,b,c);

        for(int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                mm.addPointToWorkload(i,j);
                elements++;
                if(elements == count)
                {
                    elements = 0;
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);

                }
            }
        }
        if(elements > 0) {
            threads.add(mm);
            elements = 0;
        }
        for (Thread t: threads) {
            t.start();

        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        end = System.nanoTime() / 1000000;

        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);

        //------------------------------------------------------------------------------
        start =  System.nanoTime() / 1000000;
        threads = new ArrayList<>();
        mm = new MatrixMultiplication(a,b,c);

        for(int k = 0; k < task; k++){
            for(int i = 0; i < rows*cols; i++) {
                int x = i / cols;
                int y = i % cols;
                mm.addPointToWorkload(x, y);
                elements++;
            }
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);


        }
        for (Thread t: threads) {
            t.start();

        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        end = System.nanoTime() / 1000000;

        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);

    //-------------'

        start =  System.nanoTime() / 1000000;
        mm = new MatrixMultiplication(a,b,c);

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mm.addPointToWorkload(i,j);
                elements++;
                if(elements == count)
                {
                    elements = 0;
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);

                }
            }
        }
        if(elements > 0) {
            threads.add(mm);
            elements = 0;
        }
        ExecutorService pool = Executors.newFixedThreadPool(task);
        for(Thread thread: threads){
            pool.execute(thread);
        }
        pool.shutdown();
        end = System.nanoTime() / 1000000;
        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);
//------------------------------------------------------------------------------
        start =  System.nanoTime() / 1000000;
        threads = new ArrayList<>();
        mm = new MatrixMultiplication(a,b,c);

        for(int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                mm.addPointToWorkload(i,j);
                elements++;
                if(elements == count)
                {
                    elements = 0;
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);

                }
            }
        }
        if(elements > 0) {
            threads.add(mm);
            elements = 0;
        }
        pool = Executors.newFixedThreadPool(task);
        for(Thread thread: threads){
            pool.execute(thread);
        }
        pool.shutdown();
        end = System.nanoTime() / 1000000;

        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);

        //------------------------------------------------------------------------------
        start =  System.nanoTime() / 1000000;
        threads = new ArrayList<>();
        mm = new MatrixMultiplication(a,b,c);

        for(int k = 0; k < task; k++){
            for(int i = 0; i < rows*cols; i++) {
                int x = i / cols;
                int y = i % cols;
                mm.addPointToWorkload(x, y);
                elements++;
            }
                    threads.add(mm);
                    mm = new MatrixMultiplication(a,b,c);


        }

        pool = Executors.newFixedThreadPool(task);
        for(Thread thread: threads){
            pool.execute(thread);
        }
        pool.shutdown();

        end = System.nanoTime() / 1000000;

        System.out.println("\n End work multiplication: " + (end - start) / 1000 + " seconds");
        System.out.println(c);

    }
//    --------------------------------------------------------------------------------------------------

}
