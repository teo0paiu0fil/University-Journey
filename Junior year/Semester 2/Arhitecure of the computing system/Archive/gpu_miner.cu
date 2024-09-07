
#include <stdio.h>
#include <stdint.h>
#include "../include/utils.cuh"
#include <string.h>
#include <stdlib.h>
#include <inttypes.h>

// TODO: Implement function to search for all nonces from 1 through MAX_NONCE (inclusive) using CUDA Threads
__global__ void findNonce(uint64_t *nonce, size_t size, BYTE *block_content, BYTE *block_hash, BYTE *diff) {
	if (*nonce) return;
	
	uint64_t nonce_try = threadIdx.x + blockDim.x * blockIdx.x;
	
	// make the nonce a string 
	char nonce_string[NONCE_SIZE];
	intToString(nonce_try, nonce_string);

	// concat the nonce to the block content
	char block_content_aux[BLOCK_SIZE];
	char block_hash_aux[SHA256_HASH_SIZE];
	
	d_strcpy(block_content_aux, (const char *)block_content);
	d_strcpy(block_content_aux + size, nonce_string);
	
	// apply the hash algo
	apply_sha256((BYTE *)block_content_aux, d_strlen(block_content_aux), (BYTE *)block_hash_aux, 1);

	// check results
	if (compare_hashes((BYTE *)block_hash_aux, diff) <= 0) {
	    	// if it is already found 
		if (*nonce) return;
		
		// else copy result
    		atomicExch((unsigned long long *)nonce, (unsigned long long)nonce_try);
		d_strcpy((char *)block_hash, block_hash_aux);
	   	
        }

}

int main(int argc, char **argv) {
	BYTE hashed_tx1[SHA256_HASH_SIZE], hashed_tx2[SHA256_HASH_SIZE], hashed_tx3[SHA256_HASH_SIZE], hashed_tx4[SHA256_HASH_SIZE],
			tx12[SHA256_HASH_SIZE * 2], tx34[SHA256_HASH_SIZE * 2], hashed_tx12[SHA256_HASH_SIZE], hashed_tx34[SHA256_HASH_SIZE],
			tx1234[SHA256_HASH_SIZE * 2], top_hash[SHA256_HASH_SIZE], block_content[BLOCK_SIZE];
	BYTE block_hash[SHA256_HASH_SIZE] = "0000000000000000000000000000000000000000000000000000000000000000"; // TODO: Update
	uint64_t nonce = 0; // TODO: Update
	size_t current_length;

	// Top hash
	apply_sha256(tx1, strlen((const char*)tx1), hashed_tx1, 1);
	apply_sha256(tx2, strlen((const char*)tx2), hashed_tx2, 1);
	apply_sha256(tx3, strlen((const char*)tx3), hashed_tx3, 1);
	apply_sha256(tx4, strlen((const char*)tx4), hashed_tx4, 1);
	strcpy((char *)tx12, (const char *)hashed_tx1);
	strcat((char *)tx12, (const char *)hashed_tx2);
	apply_sha256(tx12, strlen((const char*)tx12), hashed_tx12, 1);
	strcpy((char *)tx34, (const char *)hashed_tx3);
	strcat((char *)tx34, (const char *)hashed_tx4);
	apply_sha256(tx34, strlen((const char*)tx34), hashed_tx34, 1);
	strcpy((char *)tx1234, (const char *)hashed_tx12);
	strcat((char *)tx1234, (const char *)hashed_tx34);
	apply_sha256(tx1234, strlen((const char*)tx34), top_hash, 1);

	// prev_block_hash + top_hash
	strcpy((char*)block_content, (const char*)prev_block_hash);
	strcat((char*)block_content, (const char*)top_hash);
	current_length = strlen((char*) block_content);

	uint64_t *nonce_gpu;
	cudaMalloc((void **) &nonce_gpu, sizeof(uint64_t));

	BYTE *block_content_gpu;
	cudaMalloc((void **) &block_content_gpu, sizeof(BYTE) * BLOCK_SIZE);

	BYTE *block_hash_gpu;
	cudaMalloc((void **) &block_hash_gpu, sizeof(BYTE) * SHA256_HASH_SIZE);

	BYTE *diff_gpu;	
	cudaMalloc((void **) &diff_gpu, sizeof(BYTE) * SHA256_HASH_SIZE);

	if (block_content_gpu == 0 || diff_gpu == 0 ||  nonce_gpu == 0 || block_hash_gpu == 0) {
		printf("Eroare de alocare!\n");
		exit(1);
	}

	cudaMemcpy(block_content_gpu, block_content, sizeof(BYTE) * BLOCK_SIZE, cudaMemcpyHostToDevice);
	cudaMemcpy(diff_gpu, DIFFICULTY, sizeof(BYTE) * SHA256_HASH_SIZE, cudaMemcpyHostToDevice);
	cudaMemcpy(nonce_gpu, &nonce, sizeof(uint64_t), cudaMemcpyHostToDevice);

	uint64_t block_size = 128; 
	uint64_t blocks_no = MAX_NONCE / block_size;

	if (100000000 % block_size) {
		++blocks_no;
	}

	cudaEvent_t start, stop;
    startTiming(&start, &stop);

	findNonce<<<blocks_no, block_size>>>(nonce_gpu, current_length, block_content_gpu, block_hash_gpu, diff_gpu);
	cudaDeviceSynchronize();

	float seconds = stopTiming(&start, &stop);

	cudaMemcpy(&nonce, nonce_gpu, sizeof(uint64_t), cudaMemcpyDeviceToHost);
        cudaMemcpy(block_hash, block_hash_gpu, sizeof(BYTE) * SHA256_HASH_SIZE, cudaMemcpyDeviceToHost);
	
	printResult(block_hash, nonce, seconds);

	return 0;
}
