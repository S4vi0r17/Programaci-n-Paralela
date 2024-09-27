#include <stdio.h>

double a, b;
#define N 100
double x[N], y[N], z[N];

int main()
{
    a = 0;
    for (int i = 0; i < N; i++)
    {
        a = a + x[i];
    }

    b = 0;
    for (int i = 0; i < N; i++)
    {
        b = b + y[i];
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
