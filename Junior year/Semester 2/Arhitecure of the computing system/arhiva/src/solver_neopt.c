/*
 * Tema 2 ASC
 * 2024 Spring
 */
#include "utils.h"
/*
 * Add your unoptimized implementation here
 */
double *my_solver(int N, double *A, double *B)
{
	// alocare matrice rezultat
	double *C = (double *)calloc(N * N, sizeof(double));

	// alocare matrici temporare pentru calcule
	double *temp_1 = (double *)calloc(N * N, sizeof(double));
	double *temp_2 = (double *)calloc(N * N, sizeof(double));
	double *temp_3 = (double *)calloc(N * N, sizeof(double));

	// verificare alocare
	if (!C || !temp_1 || !temp_2 || !temp_3)
	{
		printf("Alocare esuata !");
		exit(-1);
	}

	// calculez A_trans * B in temp_1
	// din cauza ca A e superior triunghiulara rezulta ca transpusa va fi
	// inferior triunghilara
	// pentru a tine cont de avantajul oferit o sa
	// i-au in calcul doar partea nenula a lui A
	for (int i = 0; i < N; i++)
	{
		for (int j = 0; j < N; j++)
		{
			for (int k = 0; k <= i; k++)
			{
				temp_1[j + i * N] += A[i + k * N] * B[j + k * N];
			}
		}
	}

	// calculez B * A in temp_2
	// similar tinand cont ca a e triunghiulara calculez doar partea nenula
	// a lui A
	for (int i = 0; i < N; i++)
	{
		for (int j = 0; j < N; j++)
		{
			for (int k = 0; k <= j; k++)
			{
				temp_2[j + i * N] += B[k + i * N] * A[j + k * N];
			}
		}
	}

	// calculez A_trans * B + B * A in temp_3
	for (int i = 0; i < N; i++)
	{
		for (int j = 0; j < N; j++)
		{
			temp_3[j + i * N] = temp_1[j + i * N] + temp_2[j + i * N];
		}
	}

	// calculez ( A_trans * B + B * A ) * B_trans in C
	// tinand cont ca B este transpos
	for (int i = 0; i < N; i++)
	{
		for (int j = 0; j < N; j++)
		{
			for (int k = 0; k < N; k++)
			{
				C[j + i * N] += temp_3[k + i * N] * B[j * N + k];
			}
		}
	}

	// elibereaza memoria
	free(temp_3);
	free(temp_2);
	free(temp_1);

	return C;
}
