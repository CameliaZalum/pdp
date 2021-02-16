package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Board  extends State {
    int[][] board;
    int size;
    int emptyRow;
    int emptyColumn;

    public Board(int n){
        super();
        size = n;

    }
    public Board( int n , int moves){
        super();
        size = n;
        board = new int[n][];
        for (int i = 0; i < n; i ++){
            board[i] = new int[n];
            int row = 1 + i * size;
            for (int j = 0; j < size; j++) {
                board[i][j] = row + j;
            }
        }
        board[size - 1][size - 1] = 0;
        emptyRow = size - 1;
        emptyColumn = size - 1;

        while(moves - 1 >= 0) {
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};
            int[] valid = {0, 0, 0, 0};

            int numvalid = 0;
            if (emptyRow > 0) { valid[0] = 1; numvalid++;}
            if (emptyRow < size-1) { valid[1] = 1; numvalid++;}
            if (emptyColumn > 0) { valid[2] = 1; numvalid++;}
            if (emptyColumn < size-1) { valid[3] = 1; numvalid++;}
            if (numvalid == 0) { initNewState(0); return; }

            int idx = 0;
            int swapidx = (new Random()).nextInt(numvalid);
            for (int i = 0; i <= swapidx; i++) {
                if (i > 0) idx++;
                while (valid[idx] == 0) idx++;
            }

            int newEmptyRow = emptyRow + dx[idx];
            int newEmptyCol = emptyColumn + dy[idx];
            board[emptyRow][emptyColumn] = board[newEmptyRow][newEmptyCol];
            board[newEmptyRow][newEmptyCol] = 0;
            emptyRow = newEmptyRow;
            emptyColumn = newEmptyCol;
        }

        initNewState(0);

    }

    public Board(String s) throws FileNotFoundException {
        super();
        File fp = new File(s);
        Scanner myReader = new Scanner(fp);
        if (myReader.hasNextInt()){
            size = myReader.nextInt();
        }


        board = new int[size][];
        for (int i = 0; i < size; i++) {
            board[i] = new int[size];
            int row = i*size;
            for (int j = 0; j < size; j++) {
                if (myReader.hasNextInt()) {
                    board[i][j] = myReader.nextInt();
                    if (board[i][j] == 0) {
                        emptyRow = i;
                        emptyColumn = j;
                    }
                }

            }
        }
        myReader.close();

        initNewState(0);
    }
    int computeH(){
        int total = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int element = board[i][j];
                if (i == emptyRow && j == emptyColumn) continue;
                int row = (element-1)/size;
                int col = (element-1)%size;
                int distance = Math.abs(row-i) + Math.abs(col-j);
                total += distance;
            }
        }
        return total;
    }
    int computeHash(){
            int h = 0;
            int size2 = size * size;
            double mod = (1L<<56)-5;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    h = (int) ((h * size + board[i][j]) % mod);
                }
            }

            int v = h;
            v = v ^ (v >> 21);
            v = v ^ (v << 37);
            v = v ^ (v >> 4);
            v = v * 47632717;
            v = v ^ (v << 20);
            v = v ^ (v >> 41);
            v = v ^ (v <<  5);
            return v;
    }
}
