import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static java.lang.Thread.sleep;

public class Producer extends MyRunnable {
    List<Integer> vector1;
    List<Integer> vector2;
    int buffer;
    Pipeline pipe;
    public Producer(List<Integer> vector1, List<Integer> vector2, int buffer, Pipeline pipe) {
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.buffer = buffer;
        this.pipe = pipe;
    }

    @Override
    public void run() {
        for(int i =0 ; i < buffer; i++){
            pipe.putInPipe(vector1.get(i) * vector2.get(i));
        }
    }
}
