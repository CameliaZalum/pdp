#include <thread>
#include <iostream>
#include <queue>
#include <mutex>
#include <condition_variable>
using namespace std;
mutex mx;
condition_variable cv;
queue<int> q;

bool finished = false;

void producer(int n) {
	for(int i=0; i<n; ++i) {
		{
			lock_guard<std::mutex> lk(mx);
			q.push(i);
			cout << "pushing " << i << std::endl;
		}
		cv.notify_all();
	}
	{
		lock_guard<mutex> lk(mx);
		finished = true;
	}
	cv.notify_all();
}

void consumer() {
	while (true) {
		unique_lock<mutex> lk(mx);
		cv.wait(lk, []{ return finished || !q.empty(); });
		while (!q.empty()) {
			cout << "consuming " << q.front() << endl;
			q.pop();
		}
		if (finished) break;
	}
}

int main() {
	thread t1(producer, 10);
	thread t2(consumer);
	t1.join();
	t2.join();
	cout << "finished!" << endl;
}
