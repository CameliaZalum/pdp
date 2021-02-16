import java.util.Arrays;
import java.util.Random;

public class Game {
    public int size;
    // Number of tiles
    private int nbTiles;
    // Grid UI Dimension
    private int dimension;
    // Random object to shuffle tiles
    public static final Random RANDOM = new Random();
    // Storing the tiles in a 1D Array of integers
    public int[] tiles;
    // Size of tile on UI
    private int tileSize;
    // Position of the blank tile
    public int blankPos;
    // Margin for the grid on the frame
    private int margin;
    // Grid UI Size
    private int gridSize;
    public boolean gameOver;

    public Game(int size, int dim, int mar) {
        this.size = size;
        tiles = new int[size * size];
        nbTiles = size * size - 1;
        this.newGame();

        gameOver = true;
    }

    private void newGame() {
        do {
            reset(); // reset in intial state
            shuffle(); // shuffle
        } while(!isSolvable()); // make it until grid be solvable

        gameOver = false;
    }

    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }

        // we set blank cell at the last
        blankPos = tiles.length - 1;
    }

    private void shuffle() {
        // don't include the blank tile in the shuffle, leave in the solved position
        int n = nbTiles;

        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }

    // Only half permutations o the puzzle are solvable
    // Whenever a tile is preceded by a tile with higher value it counts
    // as an inversion. In our case, with the blank tile in the solved position,
    // the number of inversions must be even for the puzzle to be solvable
    public boolean isSolvable() {
        int countInversions = 0;

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }
        return countInversions % 2 == 0;
    }

    public boolean isSolved() {

        if (tiles[tiles.length - 1] != 0) { // if blank tile is not in the solved position ==> not solved
            return false;
        }
        for (int i = nbTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Game{" +
                "size=" + size +
                ", tiles=" + Arrays.toString(tiles) +
                '}';
    }

    public Game copy() {
        return new Game(size, 0, 0);
    }
}
