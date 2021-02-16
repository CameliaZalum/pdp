import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static int VERTICE_COUNT = 10;
    public static int EDGE_COUNT = VERTICE_COUNT * 10; // a graph with 10 times more vertices contains a Hamiltonian Graph for sure
    public static int THREADS_COUNT = 10;
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        Graph graph = new Graph(VERTICE_COUNT);

        for (int i = 0; i < EDGE_COUNT; i++) {
            int v = random.nextInt(VERTICE_COUNT);
            int w = random.nextInt(VERTICE_COUNT);

            if (!graph.getNeighbours(v).contains(w)) {
                graph.addEdge(v, w);
            } else {
                i--;
            }
        }
        System.out.println(graph);
        long startTime = System.nanoTime();
        ExecutorService pool = Executors.newFixedThreadPool(THREADS_COUNT);
//        Lock lock = new ReentrantLock();
        List<Integer> result = new ArrayList<>(graph.size());

        for (int i = 0; i < graph.size(); i++){
            pool.execute(new Hamilton(graph, i, result));
        }

        pool.shutdown();

        pool.awaitTermination(10, TimeUnit.SECONDS);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        System.out.println("duration : " + duration + " ms");
        System.out.println(result);
    }
}
