import java.util.ArrayList;
import java.util.List;

class Graph {
    private List<List<Integer>> container;
    int n;
    private List<Integer> nodes;

    Graph(int n) {
        this.n = n;

        this.container = new ArrayList<>();
        this.nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            this.container.add(new ArrayList<>());
            this.nodes.add(i);
        }
    }

    void addEdge(int from, int to) {
        this.container.get(from).add(to);
    }

    List<Integer> getNeighbours(int node) {
        return this.container.get(node);
    }

    @Override
    public String toString() {
        String toPrint = "";
        for (int i = 0; i < container.size(); i ++ ){
            toPrint += "node " + i + "  to " + container.get(i) + " \n";
        }
        return toPrint;
    }

    int size() {
        return n;
    }

}