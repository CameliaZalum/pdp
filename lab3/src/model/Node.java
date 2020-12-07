package model;

public class Node {
    public int line;
    public int column;

    @Override
    public String toString() {
        return "Node{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }

    public Node(int line, int column) {
        this.line = line;
        this.column = column;
    }
}
