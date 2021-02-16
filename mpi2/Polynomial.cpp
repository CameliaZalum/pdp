#include "Polynomial.h"
#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
using namespace std;
class Polynomial {
public :

	vector<int> coefficients;
	int degree;

	Polynomial(int degree) {
		this->coefficients = vector<int>(degree, 0);
		this->degree = degree;
	}
	int getDegree() {
		return this->degree;
	}

	vector<int> getCoefficients() {
		return this->coefficients;
	}
	void setCoefficient(int index, int value) {
		this->coefficients[index] = value;
	}

	string toString() {
		string toOutput = "Polynomial with degree : ";
		toOutput +=  to_string(this->degree);
		toOutput += " and coefficients: ";
		for (auto it : this->coefficients)
		{
			toOutput += to_string(it);
			toOutput += ", ";
		}

		return toOutput;
	}
	void add(Polynomial other) {
		for (int i = 0; i < this->getCoefficients().size() - 1; i++) {
			this->coefficients[i] += other.getCoefficients()[i];
		}
	}
	Polynomial(vector<int> co) {
		this->coefficients = co;
		this->degree = co.size() - 1;
	}
};
/*
ostream& operator<<(ostream& Str, const Polynomial& v) {
	string toOutput = "Polynomial with degree : ";
	toOutput += v.degree;
	toOutput += " and coefficients: ";
	for (auto it : v.coefficients)
	{
		toOutput += it;
		toOutput += ", ";
	}

	return Str << toOutput;
}*/