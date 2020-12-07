import model.Matrix;
import model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MatrixMultiplication extends Thread{
    Matrix a;
    Matrix b;
    Matrix result;
    List<Node> workload;
    public MatrixMultiplication(Matrix a, Matrix b, Matrix result) {
        this.a= a;
        this.b = b;
        this.result = result;

        this.workload = new ArrayList<>();
    }

    public void addPointToWorkload(int row, int col){
        this.workload.add(new Node(row, col));
    }

    @Override
    public String toString() {
        return "MatrixMultiplication{" +
                "a=" + a +
                ", b=" + b +
                ", result=" + result +
                ", workload=" + workload +
                '}';
    }

    @Override
    public void run() {
        for (Node point : this.workload){
            int mul = 0;
            for (int i = 0; i < result.rows; i++){
                Node node1 = new Node(point.line, i);
                Node node2 = new Node(i, point.column);
                mul += a.getValue1(node1) * b.getValue1(node2);

            }
            this.result.putValue(point, mul);
        }
    }
}