/*
 * Tema 2 ASC
 * 2024 Spring
 */
#include "utils.h"
#include <time.h>
/*
 * Add your optimized implementation here
 */
double *my_solver(int N, double *A, double *B)
{
	// alocare matrice rezultat
	double *C = (double *)calloc(N * N, sizeof(double));
	double *temp_1 = (double *)calloc(N * N, sizeof(double));

	// verificare alocare
	if (!C || !temp_1)
	{
		printf("Alocare esuata !");
		exit(-1);
	}

	// A_trans * B
	register double *temp_1_p = temp_1;
	for (register int i = 0; i < N; i++)
	{
		for (register int k = 0; k <= i; k++)
		{
			register double *temp_1_p_aux = temp_1_p;
			register double *b_p = B + k * N;
			register double val = A[i + k * N];

			for (int j = 0; j < N; j++)
			{
				*temp_1_p_aux += val * *b_p;
				temp_1_p_aux++;
				b_p++;
			}
		}
		temp_1_p += N;
	}

	// A_trans * B + B * A
	for (register int i = 0; i < N; i++)
	{
		register double *temp_1_p = temp_1 + i * N;
		register double *p_b = B + i * N;

		for (register int k = 0; k < N; k++)
		{
			register double *temp_1_p_aux = temp_1_p + k;
			register double *p_a = A + k * N + k;
			register double val = *p_b;

			for (register int j = k; j < N; j++)
			{
				*temp_1_p_aux += val * *p_a;
				temp_1_p_aux++;
				p_a++;
			}

			p_b++;
		}
	}

	// (A_trans * B + B * A) * B_trans
	register double *c_aux = C;
	for (register int i = 0; i < N; i++)
	{
		register double *c_aux_p = c_aux;
		register double *temp_1_p = temp_1 + i * N;

		for (register int j = 0; j < N; j++)
		{
			register double *temp_1_p_aux = temp_1_p;
			register double *b_p = B + j * N;
			register double sum = 0;

			for (register int k = 0; k < N; k++)
			{
				sum += *temp_1_p_aux * *b_p;
				temp_1_p_aux++;
				b_p++;
			}

			*c_aux_p = sum;
			c_aux_p++;
		}
		c_aux += N;
	}

	// dezalocare memorie
	free(temp_1);

	return C;
}
