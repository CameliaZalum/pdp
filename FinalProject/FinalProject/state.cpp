#pragma once

#include <vector>
#include <string>
using namespace std;


class MyState {

public:
    int heapIdx;

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

    void setPrev(MyState* s) {
        prev = s;
    }

    MyState* getPrev() {
        return prev;
    }

    void addOpen() {
        inOpen = true;
    }

    void removeOpen() {
        inOpen = false;
    }

    bool checkOpen() {
        return inOpen;
    }

    size_t getHash() {
        return hashval;
    }

    bool operator<(MyState& other) {
        if (getF() < other.getF()) return true;
        if ((getF() == other.getF()) && (getG() > other.getG())) return true;
        return false;
    }

    virtual vector<MyState*> getNeighbors() = 0;

    virtual bool operator==(const MyState& other) = 0;

    virtual string toString() = 0;

    virtual ~MyState() {}

    int f;
    int g;
    int h;
    MyState* prev;
    bool inOpen;
    size_t hashval;

    void initNew(int gval) {
        g = gval;
        h = computeH();
        setF();
        prev = nullptr;
        inOpen = false;
        hashval = computeHash();
    }

    void setF() {
        f = g + h;
    }

    virtual int computeH() = 0;

    virtual size_t computeHash() = 0;

};
