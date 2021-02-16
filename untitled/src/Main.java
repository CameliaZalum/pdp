import mpi.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
            Game g;
            MPIProgram mp = new MPIProgram();
            if(MPI.COMM_WORLD.Rank() == 0){
                int[] a = {1,2,3,4,5};
                int [] b = {6,7,8,9,10};
            } else {
                worker(MPI.COMM_WORLD.Rank(), MPI.COMM_WORLD.Size());
            }
        MPI.Finalize();
    }

static int[] primes(int[] a, int[] b, int nrProcs){
        int begin = 0, end;
    for (int i = 1; i < nrProcs; i ++) {
        end =  (i *a.length)/ nrProcs;
            int[] lengA = new int[1];
            lengA[0] = a.length;
            int[] lengB = new int[1];
            lengB[0] = b.length;
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, i, 0);

            MPI.COMM_WORLD.Send(lengA, 0, 1, MPI.INT, i, 1);
            MPI.COMM_WORLD.Send(String.valueOf(a).toCharArray(), 0, lengA[0], MPI.CHAR, i, 2);

            MPI.COMM_WORLD.Send(lengB, 0, 1, MPI.INT, i, 3);
            MPI.COMM_WORLD.Send(String.valueOf(b).toCharArray(), 0, lengB[0], MPI.CHAR, i, 4);

            MPI.COMM_WORLD.Send(begin, 0, 1, MPI.INT, i, 19);
            MPI.COMM_WORLD.Send(end, 0, 1, MPI.INT, i, 20);
        begin = end;

    }
    int [] r= new int[a.length];
    for( int i =0; i <nrProcs; i++){
        int[] active = new int[1];
        MPI.COMM_WORLD.Recv(active, 0, 1, MPI.INT, MPI.ANY_SOURCE, 0);
        if (active[0] != 0) {
            int[] leng = new int[1];
            MPI.COMM_WORLD.Recv(leng, 0, 1, MPI.INT, MPI.ANY_SOURCE, 21);
            char[] rr = new char[leng[0]];
            MPI.COMM_WORLD.Recv(rr, 0, leng[0], MPI.CHAR, MPI.ANY_SOURCE, 22);

            for (int j = 0; j < leng[0]; j++) {

                r[i] += (int)rr[i] - 48;
            }

        }
    }

    return r;
}
static void worker(int myId, int nrProcs){
    int[] active = new int[1];
    MPI.COMM_WORLD.Recv(active, 0, 1, MPI.INT, myId, 0);
    if (active[0] != 0) {
        int[] lengthA = new int[1];
        MPI.COMM_WORLD.Recv(lengthA, 0, 1, MPI.INT, myId, 1);
        char[] aa = new char[lengthA[0]];
        MPI.COMM_WORLD.Recv(aa, 0, lengthA[0], MPI.CHAR, myId, 2);
        int[] lengthB = new int[1];
        MPI.COMM_WORLD.Recv(lengthB, 0, 1, MPI.INT, myId, 3);
        char[] bb = new char[lengthB[0]];
        MPI.COMM_WORLD.Recv(bb, 0, lengthB[0], MPI.CHAR, myId, 4);


        int[] beginn = new int[1];
        int[] endd = new int[1];
        MPI.COMM_WORLD.Recv(beginn, 0, 1, MPI.INT, myId, 19);
        MPI.COMM_WORLD.Recv(endd, 0, 1, MPI.INT, myId, 20);
        int begin = beginn[0];
        int end = endd[0];

        int []a = new int[lengthA[0]];
        int []b = new int[lengthB[0]];
        int[]r = new int[lengthA[0]];
        for (int i = 0; i < lengthA[0]; i++) {

            a[i] = (int)aa[i] - 48;
        }
        for (int i = 0; i < lengthA[0]; i++) {

            b[i] = (int)bb[i] - 48;
        }
        for( int i =begin; i < end; i ++)
        {
            for ( int j = 0; j < a.length; j ++){
                r[i] += a[j] * b[i-j];
            }
        }
        System.out.println(r);
        int[]lengR = new int[]{r.length};
        MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Send(lengR, 0, 1, MPI.INT, 0, 21);
        MPI.COMM_WORLD.Send(String.valueOf(r).toCharArray(), 0, lengR[0], MPI.CHAR, 0, 22);
    }
}
}
