

public class Consumer extends MyRunnable{
    int buf;
    Pipeline pipe;
    double sum;
    public Consumer(int buffer, Pipeline pipe) {
        buf = buffer;
        this.pipe = pipe;
    }

    @Override
    public void run() {
        for ( int i = 0; i < buf; i++) {
            sum += pipe.outFromPipe();

        }
        System.out.println(" final result " + sum);
    }
}
