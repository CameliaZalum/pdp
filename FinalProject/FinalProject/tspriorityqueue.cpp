#pragma once
#include "state.cpp"
#include "priorityqueue.cpp"
#include <unordered_set>
#include <pthread.h>
using namespace std;

struct MyStateHash {
public:
    size_t operator()(MyState* s) const {
        return s->getHash();
    }
};

struct MyStateEqual {
public:
    bool operator()(MyState* s1, MyState* s2) const {
        return *s1 == *s2;
    }
};


template <class T, class MyStateHash, class MyStateEqual>
class TSPriorityQueue {

public:

    typedef typename unordered_set<T, MyStateHash, MyStateEqual>::iterator iterator;

    TSPriorityQueue() {}

    void init(int n) {
        numBuckets = n;
        locks = (pthread_mutex_t*)malloc(n * sizeof(pthread_mutex_t));
        for (int k = 0; k < n; k++) {
            pthread_mutex_init(&locks[k], NULL);
            pqs.push_back(MyPriorityQueue<T>());
            hashes.push_back(unordered_set<T, MyStateHash, MyStateEqual>());
        }
    }

    bool empty(int k) {
        pthread_mutex_lock(&locks[k]);
        bool b = pqs[k].empty();
        pthread_mutex_unlock(&locks[k]);
        return b;
    }

    T find(T element) {
        int k = bucketNum(element);
        T e = NULL;
        pthread_mutex_lock(&locks[k]);
        iterator it = hashes[k].find(element);
        if (it != hashes[k].end()) {
            e = *it;
        }
        pthread_mutex_unlock(&locks[k]);
        return e;
    }

    int size(int k) {
        pthread_mutex_lock(&locks[k]);
        int size = pqs[k].size();
        pthread_mutex_unlock(&locks[k]);
        return size;
    }

    int hashSize(int k) {
        pthread_mutex_lock(&locks[k]);
        int size = hashes[k].size();
        pthread_mutex_unlock(&locks[k]);
        return size;
    }

    void push(T element, MyState* cur) {
        int k = bucketNum(element);
        pthread_mutex_lock(&locks[k]);
        int altG;
        if (cur == NULL) {
            altG = 0;
        }
        else {
            altG = cur->getG() + 1;
        }

        iterator it = hashes[k].find(element);
        if (it != hashes[k].end()) {
            // found existing MyState
            delete element;
            element = *it;

            if (altG < element->getG()) {
                element->setPrev(cur);
                element->setG(altG);
                if (element->checkOpen()) {
                    pqs[k].update(element);
                }
                else {
                    pqs[k].push(element);
                }
            }
        }
        else {
            // new MyState
            hashes[k].insert(element);
            element->addOpen();

            element->setPrev(cur);
            element->setG(altG);
            pqs[k].push(element);
        }
        pthread_mutex_unlock(&locks[k]);
    }

    T pop(int k) {
        pthread_mutex_lock(&locks[k]);
        T element = pqs[k].pop();
        pthread_mutex_unlock(&locks[k]);
        return element;
    }

    void remove(T element) {
        int k = bucketNum(element);
        pthread_mutex_lock(&locks[k]);
        pqs[k].remove(element);
        pthread_mutex_unlock(&locks[k]);
    }

    void update(T element) {
        int k = bucketNum(element);
        pthread_mutex_lock(&locks[k]);
        pqs[k].update(element);
        pthread_mutex_unlock(&locks[k]);
    }

    int getMinKey(int k) {
        pthread_mutex_lock(&locks[k]);
        int key = pqs[k].getMinKey();
        pthread_mutex_unlock(&locks[k]);
        return key;
    }

private:

    pthread_mutex_t* locks;
    vector< MyPriorityQueue<T> > pqs;
    vector< unordered_set<T, MyStateHash, MyStateEqual> > hashes;
    int numBuckets;

    int bucketNum(T element) {
        size_t hashval = MyStateHash()(element);
        return hashval % numBuckets;
    }

};
