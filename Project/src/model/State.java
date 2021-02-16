package model;

public class State{
    int f;
    int g;
    int h;
    State previous;
    boolean inOpen;
    int hashValue;


    void setF(){
        f = (int) (g + h);
    }
    void initNewState(int gValue){
        g = gValue;
        h = computeH();
        setF();
        previous = null;
        inOpen = false;
        hashValue = computeHash();
    }

    private int computeHash() {
        return 0;
    }

    private int computeH() {
        return 0;
    }

    int getF() {
        return f;
    }

    void setG(int gValue) {
        g = gValue;
        setF();
    }

    int getG() {
        return g;
    }

    int getH() {
        return h;
    }

    void setPrev(State s) {
        previous = s;
    }

    State getPrev() {
        return previous;
    }

    void addOpen() {
        inOpen = true;
    }

    void removeOpen() {
        inOpen = false;
    }

    boolean checkOpen() {
        return inOpen;
    }

     int getHash() {
        return hashValue;
    }

    boolean compareTo(State other) {
        if (getF() < other.getF()) return true;
        if ((getF() == other.getF()) && (getG() > other.getG())) return true;
        return false;
    }


}
