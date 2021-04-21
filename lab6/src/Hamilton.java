import java.util.ArrayList;
import java.util.List;
/**
 * Creates an Runnable entity that finds a Hamilton Cycle in a graph
 */
public class Hamilton implements Runnable {

    private Graph graph;
    private int startingNode;
    private List<Integer> path;
    private List<Integer> result;

    /**
     * Constructor that initiates the parameters for the graph
     * @param graph
     * @param node the start node for seraching for a cycle
     * @param result the final path of the cycle
     */
    Hamilton(Graph graph, int node, List<Integer> result) {
        this.graph = graph;
        this.startingNode = node;
        path = new ArrayList<>();
        this.result = result;
    }

    /**
     * Starts the search
     */
    @Override
    public void run() {
        visit(startingNode);
    }

    /**
     * Sets the path for the resulting cycle
     */
    private void setResult() {
        this.result.clear();
        this.result.addAll(this.path);
    }

    /**
     * Tahes as parameter a node and checkes if there exist a 
     * cycle that contains that node in its path as starting node
     */
    private void visit(int node) {
        path.add(node); // adds the node to the path

        if (path.size() == graph.size()) { // checks if the obtained path is a cycle
            if (graph.getNeighbours(node).contains(startingNode)) { // checks if there exists an edge between the last node and the starting node to mark the existance of a cycle
                setResult(); // marks a found result
            }
            return;
        }

        for (int neighbour : graph.getNeighbours(node)) { // takes the next neighbours og the current node
            if (!this.path.contains(neighbour)) { // checks if the node is not already contained in the path
                visit(neighbour); // parforms the next step for the current node
            }
        }
    }
}