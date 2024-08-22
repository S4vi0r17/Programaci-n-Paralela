#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main()
{
    pid_t id = getpid();
    printf("Mi programa tiene este id: %i\n", id);

    pid_t clon = fork(); // Crear un clon del proceso actual

    if (clon != 0)
    {
        printf("Soy el padre y mi id es: %i\n", getpid());
        for (int i = 0; i < 1000; i++)
        {
            if(i % 100 == 0)
            {
                printf("%i ", i);
            }

            fflush(stdout);
        }
    }
    else
    {
        printf("Soy el hijo y mi id es: %i\n", getpid());
        for (int i = 0; i < 1000; i++)
        {
            if(i % 10 == 0)
            {
                printf("%i ", i);
            }

            fflush(stdout);
        }
    }

    return 0;
}
