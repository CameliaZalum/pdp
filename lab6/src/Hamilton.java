import java.util.ArrayList;
import java.util.List;

public class Hamilton implements Runnable {

    private Graph graph;
    private int startingNode;
    private List<Integer> path;
    private List<Integer> result;

    Hamilton(Graph graph, int node, List<Integer> result) {
        this.graph = graph;
        this.startingNode = node;
        path = new ArrayList<>();
        this.result = result;
    }

    @Override
    public void run() {
        visit(startingNode);
    }

    private void setResult() {
        this.result.clear();
        this.result.addAll(this.path);
    }

    private void visit(int node) {
        path.add(node);

        if (path.size() == graph.size()) {
            if (graph.getNeighbours(node).contains(startingNode)) {
                setResult();
            }
            return;
        }

        for (int neighbour : graph.getNeighbours(node)) {
            if (!this.path.contains(neighbour)) {
                visit(neighbour);
            }
        }
    }
}