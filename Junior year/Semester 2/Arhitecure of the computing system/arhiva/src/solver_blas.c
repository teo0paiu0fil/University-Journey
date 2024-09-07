/*
 * Tema 2 ASC
 * 2024 Spring
 */
#include "utils.h"
#include "cblas.h"
#include "string.h"
/*
 * Add your BLAS implementation here
 */
double *my_solver(int N, double *A, double *B)
{
	// alocare matrice rezultat
	double *C = (double *)malloc(N * N * sizeof(double));
	// alocare matrici temporare pentru calcule
	double *B_temp_1 = (double *)malloc(N * N * sizeof(double));
	double *B_temp_2 = (double *)malloc(N * N * sizeof(double));

	// verificare alocare
	if (!C || !B_temp_1 || !B_temp_2)
	{
		printf("Alocare esuata !");
		exit(-1);
	}

	// copiaza valoarea matricilor A si B in cele alocate temporar
	memcpy(B_temp_1, B, N * N * sizeof(double));
	memcpy(B_temp_2, B, N * N * sizeof(double));

	// calculeaza A_trans * B in B_temp_1
	cblas_dtrmm(CblasRowMajor, CblasLeft, CblasUpper, CblasTrans,
				CblasNonUnit, N, N, 1, A, N, B_temp_1, N);

	// calculeaza B * A in B_temp_2
	cblas_dtrmm(CblasRowMajor, CblasRight, CblasUpper, CblasNoTrans,
				CblasNonUnit, N, N, 1, A, N, B_temp_2, N);

	// calculeaza A_trans * B + B * A in B_temp_2
	cblas_daxpy(N * N, 1, B_temp_1, 1, B_temp_2, 1);

	// calculeaza (A_trans * B + B * A) x B_trans in C
	cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasTrans,
				N, N, N, 1, B_temp_2, N, B, N, 0, C, N);

	// dezaloca memoria
	free(B_temp_2);
	free(B_temp_1);

	return C;
}
