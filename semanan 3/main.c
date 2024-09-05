#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define N 100000000
double x[N];

// void init()
// {
//     for (int i = 0; i < N; i++)
//     {
//         x[i] = (double)rand() / RAND_MAX;
//     }
// }

void init(double v[], int n)
{
    for (int i = 0; i < n; i++)
    {
        v[i] = (double)rand() / RAND_MAX;
    }
}

double sum(double v[], int start, int end)
{
    double s = 0;
    for (int i = start; i < end; i++)
    {
        s += v[i];
    }
    return s;
}

void sum_serial(double v[], int n)
{
    double s = 0;
    s = sum(v, 0, n);
    printf("Serial sum = %f\n", s);
}

// void sum_parallel(double v[], int n)
// {
//     double s = 0;
//     int num_threads = 4;
//     omp_set_num_threads(num_threads);
//     #pragma omp parallel
//     {
//         int id = omp_get_thread_num();
//         int start = id * n / num_threads;
//         int end = (id + 1) * n / num_threads;
//         s += sum(v, start, end);
//     }
//     printf("Parallel sum = %f\n", s);
// }

// void sum_parallel(double v[], int n)
// {
//     double s1 = 0, s2 = 0;
//     #pragma omp parallel sections
//     {
//         #pragma omp section
//         {
//             s1 = sum(v, 0, n / 2);
//         }
//         #pragma omp section
//         {
//             s2 = sum(v, n / 2, n);
//         }
//     }
//     printf("Parallel sum = %f\n", s1 + s2);
// }

void sum_parallel(double v[], int n)
{
    double s1 = 0, s2 = 0, s3 = 0, s4 = 0;
    #pragma omp parallel sections
    {
        #pragma omp section
        {
            s1 = sum(v, 0, n / 4);
        }
        #pragma omp section
        {
            s2 = sum(v, n / 4, n / 2);
        }
        #pragma omp section
        {
            s3 = sum(v, n / 2, 3 * n / 4);
        }
        #pragma omp section
        {
            s4 = sum(v, 3 * n / 4, n);
        }
    }
    printf("Parallel sum = %f\n", s1 + s2 + s3 + s4);
}

// int main()
// {
//     double timeStart, work, cpl;
//     init(x, N);
//     timeStart = omp_get_wtime();
//     sum_serial(x, N);
//     work = omp_get_wtime() - timeStart;
//     printf("Time serial = %f\n", work);

//     timeStart = omp_get_wtime();
//     sum_parallel(x, N);
//     cpl = omp_get_wtime() - timeStart;
//     printf("Time parallel = %f\n", cpl);

//     printf("Aceleration = %f\n", work / cpl);
//     return 0;
// }

int main() {
    double timeStart, work = 0, cpl = 0;

    for (int i = 0; i < 10; i++) {
        init(x, N);
        timeStart = omp_get_wtime();
        sum_serial(x, N);
        work += omp_get_wtime() - timeStart;

        timeStart = omp_get_wtime();
        sum_parallel(x, N);
        cpl += omp_get_wtime() - timeStart;
    }

    printf("Time serial = %f\n", work / 10);
    printf("Time parallel = %f\n", cpl / 10);
    printf("Aceleration = %f\n", work / cpl);
}
