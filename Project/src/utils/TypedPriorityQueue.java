package utils;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class TypedPriorityQueue<T, stateHash , stateEqual> {
    ReentrantLock locks; // as mutex
    List<PriorityQueue<T>> pqs;
    List<Set<T>> hashes;
    int numBuckets;
    public TypedPriorityQueue() {}

    void init(int n) {
        numBuckets = n;
        locks = (pthread_mutex_t*)malloc(n * sizeof(pthread_mutex_t));
        for (int k = 0; k < n; k++) {
            pthread_mutex_init(&locks[k], NULL);
            pqs.push_back(PriorityQueue<T>());
            hashes.push_back(std::unordered_set<T, stateHash, stateEqual>());
        }
    }
}
