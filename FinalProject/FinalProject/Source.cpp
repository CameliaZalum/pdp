#pragma once
#include <iostream>
#include <vector>
#include <unordered_set>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <limits.h>

#include "state.cpp"
#include "board.cpp"
using namespace std;

MyState* start = NULL;
MyState* goal = NULL;

vector<MyState*> path;
int numThreads = 0;
int bucketMultiplier = -1;

#include "priorityqueue.cpp"
#include "tspriorityqueue.cpp"
#include "sequential.cpp"
#include "parallel.cpp"

int main(int argc, char* argv[]) {
    int size = 4;
    int moves = -1;
    string inputFile = "";
    int opt;
    

    if (inputFile.empty()) {
        if (moves >= 0) {
            start = (MyState*)(new Board(size, moves));
        }
        else {
            start = (MyState*)(new Board(size));
        }
    }
    else {
        start = (MyState*)(new Board(inputFile));
    }
    cout << "Start board:" << endl;
    cout << start->toString() << endl;

    if (numThreads == 0) {
        cout << "Running sequential baseline..." << endl;
    }
    else {
        cout << "Running parallel version with " << numThreads << " threads..." << endl;
    }
    if (bucketMultiplier != -1) {
        int numBuckets = bucketMultiplier * numThreads;
        cout << "Using " << numBuckets << " buckets..." << endl;
    }

    goal = (MyState*)(new Board(size, 0));

    time_t start_t = time(0);

    if (numThreads == 0) {
        sequential();
    }
    else {
        parallel(numThreads);
    }

    time_t end_t = time(0);
    double time = difftime(end_t, start_t);


    cout << "Optimal solution found!" << endl << endl;
    int length = path.size();
    for (int i = 0; i < length; i++) {
        cout << "Step " << i << ":" << endl;
        cout << path[i]->toString() << endl;
    }
    cout << "Length of path: " << length - 1 << endl;
    cout << "Total time: " << time << "s" << endl;

    return 0;
}
