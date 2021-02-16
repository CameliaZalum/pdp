#pragma once
#include <vector>
#include "state.cpp"
#include <unordered_set>
#include "tspriorityqueue.cpp"
#include "priorityqueue.cpp"
#include "Source.cpp"
using namespace std;
void sequential() {
	vector<MyState*> tempPath;

	unordered_set<MyState*, MyStateHash, MyStateEqual> hash;

	MyPriorityQueue<MyState*> open;

	hash.insert(start);
	start->addOpen();
	open.push(start);

	int expanded = 0;

	while (!open.empty()) {
		if (expanded % 100000 == 0) {
			printf("Finding optimal solution...\n");
		}

		expanded++;
		MyState* cur = open.pop();

		if (*cur == *goal) {
			while (cur != start) {
				tempPath.push_back(cur);
				cur = cur->getPrev();
			}
			tempPath.push_back(start);

			int pathLength = tempPath.size();
			for (int i = 0; i < pathLength; i++) {
				path.push_back(tempPath[pathLength - 1 - i]);
			}

			return;
		}

		cur->removeOpen();
		vector<MyState*> neighbors = cur->getNeighbors();

		int altG = cur->getG() + 1;
		for (int i = 0; i < neighbors.size(); i++) {
			MyState* neighbor = neighbors[i];
			unordered_set<MyState*, MyStateHash, MyStateEqual>::iterator it = hash.find(neighbor);
			if (it != hash.end()) {
				// found existing MyState
				delete neighbor;
				neighbor = *it;

				if (altG < neighbor->getG()) {
					neighbor->setPrev(cur);
					neighbor->setG(altG);
					if (neighbor->checkOpen()) {
						open.update(neighbor);
					}
					else {
						open.push(neighbor);
					}
				}
			}
			else {
				// new MyState
				hash.insert(neighbor);
				neighbor->addOpen();

				neighbor->setPrev(cur);
				neighbor->setG(altG);
				open.push(neighbor);
			}
		}
	}

	return;
}