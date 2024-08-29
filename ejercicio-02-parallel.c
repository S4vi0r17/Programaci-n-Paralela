#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

double a, b;
#define N 10000
double x[N], y[N], z[N];

int main()
{
    // omp_set_num_threads(2);

    int nThreads = omp_get_max_threads();
    printf("Number of threads: %d\n", nThreads); fflush(stdout);

    #pragma omp parallel sections
    {
        #pragma omp section
        {
            printf("Task 1 started - Thread %d\n", omp_get_thread_num()); fflush(stdout);
            a = 0;
            for (int i = 0; i < N; i++)
            {
                a = a + x[i];
            }
            printf("Task 1 finished - Thread %d\n", omp_get_thread_num()); fflush(stdout);
        }

        #pragma omp section
        {
            printf("Task 2 started - Thread %d\n", omp_get_thread_num()); fflush(stdout);
            b = 0;
            for (int i = 0; i < N; i++)
            {
                b = b + y[i];
            }
            printf("Task 2 finished - Thread %d\n", omp_get_thread_num()); fflush(stdout);
        }
    }

    for (int i = 0; i < N; i++)
    {
        z[i] = x[i] / b + y[i] / a;
    }

    for (int i = 0; i < N; i++)
    {
        y[i] = (a + b) * y[i];
    }
    
    return 0;
}
