
#include <iostream>
#include <mpi.h>
#include "Polynomial.h"
#include "Polynomial.cpp"
#include <string>
#include <thread>
#include <cmath>

#define POLYNOMIAL_DEGREE 10
#define POLYNOMIAL1_DESTIMATION 12
#define POLYNOMIAL2_DESTIMATION 13
#define POLYNOMIAL_RESULT_DESTIMATION 100
#define INTERVAL1_DESTINATION 10
#define INTERVAL2_DESTINATION 11
#define POLYNOMIAL1_LENGHT_DESTINATION 9
#define POLYNOMIAL_RESULT_LENGHT_DESTINATION 101
#define POLYNOMIAL2_LENGHT_DESTINATION 8

static void printFinalResult(chrono::steady_clock::time_point start) {
    
    int size;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Status status;
    Polynomial* finalOut = new Polynomial(POLYNOMIAL_DEGREE * 2);
    for (int i = 1; i < size; ++i) {
        Polynomial* result = new Polynomial(POLYNOMIAL_DEGREE * 2);
        int length = 0;
        MPI_Recv(&length, 1, MPI_INT, MPI_ANY_SOURCE, POLYNOMIAL_RESULT_LENGHT_DESTINATION, MPI_COMM_WORLD, &status);
        char* rec_buf1;
        string out1 = "";
        rec_buf1 = (char*)calloc(length + 1, sizeof(char));
        MPI_Recv(rec_buf1, length + 1, MPI_CHAR, MPI_ANY_SOURCE, POLYNOMIAL_RESULT_DESTIMATION, MPI_COMM_WORLD, &status);
        out1 += rec_buf1;
        free(rec_buf1);
        int index1 = 0;

        string word = "";
        for (auto it : out1) {

            if (it == ' ')
            {
                result->setCoefficient(index1, stoi(word));
                index1++;
                word = "";
            }
            else {
                word = word + it;
            }
        }
        finalOut->add(*result);
    }
    auto stop = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(stop - start);

    cout << duration.count() << "ms" << "\n";
    cout.flush();
    //MPI_Finalized(MPI_SUCCESS);
}

static void MPIMultiplicationMaster(Polynomial* p1, Polynomial* p2)
{
    auto start = chrono::high_resolution_clock::now();
    int n;
    MPI_Status status;
    int begin = 0;
    int end = 0;
    string coeeficients1 = "";
    string coeeficients2 = "";
    for (auto it : p1->getCoefficients()) {
        coeeficients1 += to_string(it) + " ";
    }

    for (auto it : p2->getCoefficients()) {
        coeeficients2 += to_string(it) + " ";
    }
    int size;
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    for (int i = 1; i < size; i++)
    {
        begin = end;
        end = end + 2;

        if (i == size - 1)
            end = p1->degree;

        int length1 = coeeficients1.length();
        int length2 = coeeficients2.length();
        char* temp1 = (char*)calloc(length1 + 1, sizeof(char));
        strcpy(temp1, coeeficients1.c_str());
        char* temp2 = (char*)calloc(length2 + 1, sizeof(char));
        strcpy(temp2, coeeficients2.c_str());

        MPI_Send(&length1, 1, MPI_INT, i, POLYNOMIAL1_LENGHT_DESTINATION, MPI_COMM_WORLD);
        MPI_Send(&length2, 1, MPI_INT, i, POLYNOMIAL2_LENGHT_DESTINATION, MPI_COMM_WORLD);
        MPI_Send(temp1, coeeficients1.length(), MPI_CHAR, i, POLYNOMIAL1_DESTIMATION, MPI_COMM_WORLD);
        MPI_Send(temp2, coeeficients2.length(), MPI_CHAR, i, POLYNOMIAL2_DESTIMATION, MPI_COMM_WORLD);
        MPI_Send(&begin, 1, MPI_INT, i, INTERVAL1_DESTINATION, MPI_COMM_WORLD);
        MPI_Send(&end, 1, MPI_INT, i, INTERVAL2_DESTINATION, MPI_COMM_WORLD);
        
       
    }
    printFinalResult(start);
    
}
static Polynomial MPIMultiply(Polynomial polynomial1, Polynomial polynomial2, int begin, int end)
{
    int maxDegree = max(polynomial1.getDegree(), polynomial2.getDegree());
    
    Polynomial *result = new Polynomial(maxDegree * 2 );
    for (int i = begin; i < end; i++)
        for (int j = 0; j < polynomial2.getDegree(); j++) {
            
            result->setCoefficient(i + j, polynomial1.getCoefficients()[i] * polynomial2.getCoefficients()[j]);
        }

    return *result;
}

static Polynomial multiplicationSequentialForm(Polynomial p1, Polynomial p2) {
    Polynomial* coefficients = new Polynomial(p1.getCoefficients().size() + p2.getCoefficients().size() - 1);
    
    for (int i = 0; i < p1.getCoefficients().size(); i++) {
        for (int j = 0; j < p2.getCoefficients().size(); j++) {
            int index = i + j;
            int value = p1.getCoefficients()[i] * p2.getCoefficients()[j];
            coefficients->setCoefficient(index, coefficients->coefficients[index] + value);
        }
    }
    return *coefficients;
}
static void MPIMultiplicationWorker()
{
    
    MPI_Status status;
    int length1;
    int length2;
    int begin;
    int end;


    MPI_Recv(&begin, 1, MPI_INT, MPI_ANY_SOURCE, INTERVAL1_DESTINATION, MPI_COMM_WORLD, &status);
    MPI_Recv(&end, 1, MPI_INT, MPI_ANY_SOURCE, INTERVAL2_DESTINATION, MPI_COMM_WORLD, &status);

    MPI_Recv(&length1, 1, MPI_INT, 0, POLYNOMIAL1_LENGHT_DESTINATION, MPI_COMM_WORLD, &status);
    MPI_Recv(&length2, 1, MPI_INT, 0, POLYNOMIAL2_LENGHT_DESTINATION, MPI_COMM_WORLD, &status);

    Polynomial* polynomial1 = new Polynomial(POLYNOMIAL_DEGREE);
    Polynomial* polynomial2 = new Polynomial(POLYNOMIAL_DEGREE);

    char* rec_buf1;
    string out1 = "";
    rec_buf1 = (char*)calloc(length1 + 1, sizeof(char));
    MPI_Recv(rec_buf1, length1 + 1, MPI_CHAR, 0, POLYNOMIAL1_DESTIMATION, MPI_COMM_WORLD, &status);
    out1 += rec_buf1;
    free(rec_buf1);

    char* rec_buf2;
    string out2 = "";
    rec_buf2 = (char*)calloc(length2 + 1, sizeof(char));
    MPI_Recv(rec_buf2, length2 + 1, MPI_CHAR, MPI_ANY_SOURCE, POLYNOMIAL2_DESTIMATION, MPI_COMM_WORLD, &status);
    out2 += rec_buf2;
    free(rec_buf2);
    int index1 = 0;
    
    string word = "";
    for (auto it: out1) {
       
            if (it == ' ')
            {
                polynomial1->setCoefficient(index1, stoi(word));
                index1++;
                word = "";
            }
            else {
                word = word + it;
            }
            
        
    }
   
    int index2 = 0;
    word = "";
    for (auto it : out2) {
        if (it == ' ')
        {
            polynomial2->setCoefficient(index2, stoi(word));
            index2++;
            word = "";
        }
        else {
            word = word + it;
        }
    }
    
    
    Polynomial result = MPIMultiply(*polynomial1, *polynomial2, begin, end);

    string coeeficients = "";
    for (auto it : result.getCoefficients()) {
        coeeficients += to_string(it) + " ";
    }
    int len = coeeficients.length();
    char* temp = (char*)calloc(length1 + 1, sizeof(char));
    strcpy(temp, coeeficients.c_str());
    int size;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Send(&len, 1, MPI_INT, 0 , POLYNOMIAL_RESULT_LENGHT_DESTINATION, MPI_COMM_WORLD);
    MPI_Send(temp, coeeficients.length(), MPI_CHAR, 0, POLYNOMIAL_RESULT_DESTIMATION, MPI_COMM_WORLD);
    
}

int main()
{
    MPI_Init(0, 0);
    int me;
    int size;
    int length;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &me);
    string out = "";
    string message;
    Polynomial* p1 = new Polynomial(POLYNOMIAL_DEGREE);
    Polynomial* p2 = new Polynomial(POLYNOMIAL_DEGREE);

    if (me == 0) {
        for (int i = 0; i < POLYNOMIAL_DEGREE; i++) {
            p1->setCoefficient(i, rand() % 10 + 1);
        }

        for (int i = 0; i < POLYNOMIAL_DEGREE; i++) {
            p2->setCoefficient(i, rand() % 10 + 1);
        }
        MPIMultiplicationMaster(p1, p2);
    }
    else {
        MPIMultiplicationWorker();
    }
    
    MPI_Finalize();
    
}