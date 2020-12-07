package model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Matrix {
    @Override
    public String toString() {
        return "Matrix{" +
                "matrix=" + matrix +
                ", rows=" + rows +
                ", cols=" + cols +
                '}';
    }

    public Map<Node, Integer > matrix = new HashMap<>();
    public int rows;
    public int cols;
    public Matrix(int rows, int cols, int nr) {
        this.rows = rows;
        this.cols = cols;
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                matrix.put(new Node(i, j ), nr);
            }
        }
    }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        int max = 100;
        int min = 0;

        Random rand = new Random();
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.cols; j++){
                matrix.put(new Node(i, j ), rand.nextInt((max - min) + 1) + min);
            }
        }
    }
    public int getValue1(Node node) {
        List<Object> result = this.matrix.keySet().stream().filter(n->(n.line == node.line && n.column == node.column)).collect(Collectors.toList());
        int value = matrix.get(result.get(0));
        return value;
    }
    public void putValue(Node node, int value) {
        for (Node n: matrix.keySet()) {
            if(n.line == node.line && n.column == node.column){
                this.matrix.put(n, value);
            }
        }
    }
}
