
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        List<Integer> vector1 = new ArrayList<>();
        vector1.add(1);
        vector1.add(2);
        vector1.add(1);
        vector1.add(2);
        vector1.add(1);
        vector1.add(2);
        vector1.add(1);
        vector1.add(2);
        List<Integer> vector2 = new ArrayList<>();
        vector2.add(3);
        vector2.add(4);
        vector2.add(3);
        vector2.add(4);
        vector2.add(3);
        vector2.add(4);
        vector2.add(3);
        vector2.add(4);
        int buffer = vector1.size();
        Pipeline pipe = new Pipeline();

        Thread consumer = new Thread(new Consumer(buffer, pipe));
        Thread producer = new Thread(new Producer(vector1, vector2, buffer, pipe));

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
