import com.google.gson.Gson;
import mpi.MPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MPIProgram {
    public Gson gson = new Gson();
    public static int THREADS_COUNT = 50 ;
    public static int OBJECT_TAG = 1 ;
    public static int LENGTH_TAG = 2 ;
    public static int ACTIVE_TAG = 0;

    public MPIProgram(){}

    public void StartMaster(Game g) throws InterruptedException {
        long startTime = System.nanoTime();
            String a = gson.toJson(g, Game.class);
            for (int i = 1; i < MPI.COMM_WORLD.Size(); i ++) {
                int[] leng = new int[1];
                leng[0] = a.length();
                MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, i, ACTIVE_TAG);
                MPI.COMM_WORLD.Send(leng, 0, 1, MPI.INT, i , LENGTH_TAG);
                MPI.COMM_WORLD.Send(a.toCharArray(), 0, leng[0], MPI.CHAR, i , OBJECT_TAG);
            }


        printFinal(startTime);
    }

    private void printFinal(long startTime) {
        Game gameObject = null;
        for ( int i =1; i < MPI.COMM_WORLD.Size(); i ++ ) {
            int[] active = new int[1];
            MPI.COMM_WORLD.Recv(active, 0, 1, MPI.INT, MPI.ANY_SOURCE, ACTIVE_TAG);
            if (active[0] != 0) {
                int[] length = new int[1];
                MPI.COMM_WORLD.Recv(length, 0, 1, MPI.INT, MPI.ANY_SOURCE, LENGTH_TAG);
                char[] game = new char[length[0]];
                MPI.COMM_WORLD.Recv(game, 0, length[0], MPI.CHAR, MPI.ANY_SOURCE, OBJECT_TAG);
                gameObject = gson.fromJson(String.valueOf(game), Game.class);
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        System.out.println("duration : " + duration + " ms");
        System.out.println(gameObject);
    }


    public void StartWorker() {

        int[] active = new int[1];
        MPI.COMM_WORLD.Recv(active, 0, 1, MPI.INT, MPI.ANY_SOURCE, ACTIVE_TAG);
        if (active[0] != 0) {
            int[] length = new int[1];
            MPI.COMM_WORLD.Recv(length, 0, 1, MPI.INT, MPI.ANY_SOURCE, LENGTH_TAG);
            char[] game = new char[length[0]];
            MPI.COMM_WORLD.Recv(game, 0, length[0], MPI.CHAR, MPI.ANY_SOURCE, OBJECT_TAG);

            Game gameObject = gson.fromJson(String.valueOf(game), Game.class);
            gameObject = this.nextMove(gameObject);

            String a = gson.toJson(gameObject, Game.class);
            int[] leng = new int[1];
            leng[0] = a.length();
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, 0, ACTIVE_TAG);
            MPI.COMM_WORLD.Send(leng, 0, 1, MPI.INT, 0 , LENGTH_TAG);
            MPI.COMM_WORLD.Send(a.toCharArray(), 0, leng[0], MPI.CHAR, 0 , OBJECT_TAG);
        }
    }
    public Game nextMove(Game game){
        return null;
    }
}
