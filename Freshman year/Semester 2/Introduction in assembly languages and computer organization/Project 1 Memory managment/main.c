// Paiu_Teofil_313CB
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "structs.h"
#include <inttypes.h>

int add_last(void **arr, int *len, data_structure *data) {
	if(*arr == NULL)
		*arr = calloc(1, data->header->len + sizeof(head));
	else
		*arr = realloc(*arr, *len + data->header->len + sizeof(head));
	if(!arr)
		return 1;
	memcpy(*arr + *len, data->header, sizeof(head));
	memcpy(*arr + *len + sizeof(head), data->data , data->header->len);
	*len +=  data->header->len + sizeof(head);
	return 0;
}

int add_at(void **arr, int *len, data_structure *data, int index) {
	
	if(index < 0) return 1;

	*arr = realloc(*arr, *len + data->header->len + sizeof(head));

	int i = 0;
	int poznext = 0;

	while (index != i && poznext < *len) {
		int tip = ((head*)(*arr + poznext))->type ;
		int pozSir1 = sizeof(head) + poznext;
		int pozFisrtInt = pozSir1 + strlen((char*)(*arr + pozSir1)) + 1;
		int pozSir2 = 0;

		if ( tip == 1)
			pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
		else if ( tip == 2)
			pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
		else if ( tip == 3)
			pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);

		poznext = pozSir2 + strlen((char*)(*arr + pozSir2)) + 1;
		i++;
	}
	
	if ( poznext > *len) {
		add_last(arr, len, data);
		return 0;
	}
	
	memcpy(*arr + poznext + sizeof(head) + data->header->len, *arr + poznext, *len - poznext);
	memcpy(*arr + poznext, data->header, sizeof(head));
	memcpy(*arr + poznext + sizeof(head), data->data , data->header->len);
	
	*len += data->header->len + sizeof(head);

	return 0;
}

void find(void *data_block, int len, int index) {
	
	int i = 0;
	int poznext = 0;
	int pozSir2 = 0;
	int tip, pozSir1, pozFisrtInt ;

	while (index != i) {
			
		tip = ((head*)(data_block + poznext))->type ;
		
		pozSir1 = sizeof(head) + poznext;
		pozFisrtInt = pozSir1 + strlen((char*)(data_block + pozSir1)) + 1;

		if ( tip == 1)
			pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
		else if ( tip == 2)
			pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
		else if ( tip == 3)
			pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);
		
		poznext = pozSir2 + strlen((char*)(data_block + pozSir2)) + 1;
		i++;
	}

	tip = ((head*)(data_block + poznext))->type ;
	
	pozSir1 = sizeof(head) + poznext;
	pozFisrtInt = pozSir1 + strlen((char*)(data_block + pozSir1)) + 1;

	if ( tip == 1)
		pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
	else if ( tip == 2)
		pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
	else if ( tip == 3)
		pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);
	
	if (poznext > len) return;
	if( index < 0) return;

	printf("Tipul %d\n" , tip);

	printf("%s pentru %s\n", (char*)(data_block + pozSir1) , (char*)(data_block + pozSir2));
				
	if( tip == 1) {
		printf("%"PRId8"\n", *(int8_t*)(data_block + pozFisrtInt));
		printf("%"PRId8"\n\n", *(int8_t*)(data_block + pozFisrtInt + sizeof(int8_t)));
	} else if( tip == 2) {
		printf("%"PRId16"\n", *(int16_t*)(data_block + pozFisrtInt));
		printf("%"PRId32"\n\n", *(int32_t*)(data_block + pozFisrtInt + sizeof(int16_t)));
	} else if (tip == 3) {
		printf("%"PRId32"\n", *(int32_t*)(data_block + pozFisrtInt));
		printf("%"PRId32"\n\n", *(int32_t*)(data_block + pozFisrtInt + sizeof(int32_t)));
	}

}

int delete_at(void **arr, int *len, int index) {
	
	if( index < 0) return 1;
	
	int i = 0;
	int poznext = 0;
	
	int tip = ((head*)(*arr + poznext))->type ;

	int pozSir1 = sizeof(head) + poznext;
	int pozFisrtInt = pozSir1 + strlen((char*)(*arr + pozSir1)) + 1;
	int pozSir2 = 0;

	if ( tip == 1)
		pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
	else if ( tip == 2)
		pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
	else if ( tip == 3)
		pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);

	while (index != i) {
			
		int tip = ((head*)(*arr + poznext))->type ;
		
		int pozSir1 = sizeof(head) + poznext;
		int pozFisrtInt = pozSir1 + strlen((char*)(*arr + pozSir1)) + 1;
		int pozSir2 = 0;

		if ( tip == 1)
			pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
		else if ( tip == 2)
			pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
		else if ( tip == 3)
			pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);
		
		poznext = pozSir2 + strlen((char*)(*arr + pozSir2)) + 1;
		i++;
	}
	
	if (poznext > *len) return 1;

	tip = ((head*)(*arr + poznext))->type ;
	pozSir1 = sizeof(head) + poznext;
	pozFisrtInt = pozSir1 + strlen((char*)(*arr + pozSir1)) + 1;
	if ( tip == 1)
		pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
	else if ( tip == 2)
		pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
	else if ( tip == 3)
		pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);

	int poznext2 = pozSir2 + strlen((char*)(*arr+ pozSir2)) + 1;

	memcpy(*arr + poznext , *arr + poznext2, *len - poznext2);
	*arr = realloc(*arr, *len - (poznext2 - poznext));
	*len -= (poznext2 - poznext);
	return 0;
}

data_structure *tip1(void *nume1, void* sum1, void* sum2, void* nume2) {
	data_structure *data = calloc(1,sizeof(data_structure));
	
	data->header = calloc(1, sizeof(head));
	
	data->header->type = 1;
	
	data->data = calloc(1, strlen(nume1) + strlen(nume2)+ 2 * sizeof(int8_t) + 2);
	
	memcpy(data->data , nume1, strlen(nume1)+1);
	memcpy(data->data + strlen(nume1) + 1, (int8_t*)sum1, sizeof(int8_t));
	memcpy(data->data + strlen(nume1) + 2, (int8_t*)sum2, sizeof(int8_t));
	memcpy(data->data + strlen(nume1) + 3, nume2, strlen(nume2) + 1);
	
	data->header->len = (strlen(nume1) + strlen(nume2) + 4);
	
	return data;
}

data_structure *tip2(void *nume1, void* sum1, void* sum2, void* nume2) {
	data_structure *data = calloc(1, sizeof(data_structure));

	data->header = calloc(1, sizeof(head));

	data->header->type = 2;

	data->data = calloc(1, strlen(nume1) + strlen(nume2) + sizeof(int16_t) + sizeof(int32_t) + 2);

	memcpy(data->data , nume1, strlen(nume1)+1);
	memcpy(data->data + strlen(nume1) + 1, (int16_t*)sum1, sizeof(int16_t));
	memcpy(data->data + strlen(nume1) + 3, (int32_t*)sum2, sizeof(int32_t));
	memcpy(data->data + strlen(nume1) + 7, nume2, strlen(nume2) + 1);

	data->header->len = (strlen(nume1) + strlen(nume2) + 8);
	
	return data;
}

data_structure *tip3(void *nume1, void* sum1, void* sum2, void* nume2) {
	data_structure *data = calloc(1, sizeof(data_structure));
	
	data->header = calloc(1, sizeof(head));
	
	data->header->type = 3;
	
	data->data = calloc(1, strlen(nume1) + strlen(nume2)+ 2 * sizeof(int32_t) + 2);
	
	memcpy(data->data , nume1, strlen(nume1)+1);
	memcpy(data->data + strlen(nume1) + 1, (int32_t*)sum1, sizeof(int32_t));
	memcpy(data->data + strlen(nume1) + 1 + sizeof(int32_t), (int32_t*)sum2, sizeof(int32_t));
	memcpy(data->data + strlen(nume1) + 1 + 2 * sizeof(int32_t), nume2, strlen(nume2) + 1);
	
	data->header->len = (strlen(nume1) + strlen(nume2) + 2 + 2 * sizeof(int32_t));

	return data;
}

int main() {
	
	void *arr = NULL;
	int len = 0;
	size_t l;
	
	char *line = NULL;

	data_structure * data = NULL;

	while(getline(&line, &l, stdin)) {
		char *comanda = strtok(line, " ");

		if(comanda[strlen(comanda) - 1] == '\n')
			comanda[strlen(comanda) - 1] = '\0';

		if(strcmp(comanda, "insert") == 0) {

			char *tip = strtok(NULL, " ");
			
			if(tip[strlen(tip) - 1] == '\n')
				tip[strlen(tip) - 1] = '\0';

			char *dedicator = strtok(NULL, " ");
			char *sum1 = strtok(NULL, " ");
			char *sum2 = strtok(NULL, " ");
			char *dedicatului = strtok(NULL, " ");

			if(dedicatului[strlen(dedicatului) - 1] == '\n')
				dedicatului[strlen(dedicatului) - 1] = '\0';

			int su1 = atoi(sum1);
			int su2 = atoi(sum2);

			if(strcmp(tip,"1") == 0) {
		
				data = tip1(dedicator, &su1, &su2, dedicatului);
				add_last(&arr, &len, data);
		
			} else if(strcmp(tip,"2") == 0) {
				
				data = tip2(dedicator, &su1, &su2, dedicatului);
				add_last(&arr, &len, data);

			} else if(strcmp(tip,"3") == 0) {
				
				data = tip3(dedicator, &su1, &su2, dedicatului);
				add_last(&arr, &len, data);

			}
			free( data->data);
			free(data->header);
			free(data);

		} else if(strcmp(comanda, "insert_at") == 0) {

			char *index = strtok(NULL, " ");
			int ind = atoi(index);

			char *tip = strtok(NULL, " ");
			
			if(tip[strlen(tip) - 1] == '\n')
				tip[strlen(tip) - 1] = '\0';

			char *dedicator = strtok(NULL, " ");
			char *sum1 = strtok(NULL, " ");
			char *sum2 = strtok(NULL, " ");
			char *dedicatului = strtok(NULL, " ");

			if(dedicatului[strlen(dedicatului) - 1] == '\n')
				dedicatului[strlen(dedicatului) - 1] = '\0';

			int su1 = atoi(sum1);
			int su2 = atoi(sum2);
			if(tip[strlen(tip) - 1] == '\n')
				tip[strlen(tip) - 1] = '\0';

			if(strcmp(tip,"1") == 0) {
		
				data = tip1(dedicator, &su1, &su2, dedicatului);
				add_at(&arr, &len, data, ind);
		
			} else if(strcmp(tip,"2") == 0) {
				
				data = tip2(dedicator, &su1, &su2, dedicatului);
				add_at(&arr, &len, data, ind);

			} else if(strcmp(tip,"3") == 0) {
				
				data = tip3(dedicator, &su1, &su2, dedicatului);
				add_at(&arr, &len, data, ind);

			}
			free( data->data);
			free(data->header);
			free(data);

		} else if(strcmp(comanda, "print") == 0) {
		
			int poznext = 0;

			while ( poznext < len) {
			
				int tip = ((head*)(arr + poznext))->type ;
				
				printf("Tipul %d\n" , tip);
				
				int pozSir1 = sizeof(head) + poznext;
				int pozFisrtInt = pozSir1 + strlen((char*)(arr + pozSir1)) + 1;
				int pozSir2 = 0;

				if ( tip == 1)
					pozSir2 = pozFisrtInt + 2 * sizeof(int8_t);
				else if ( tip == 2)
					pozSir2 = pozFisrtInt + sizeof(int16_t) + sizeof(int32_t);
				else if ( tip == 3)
					pozSir2 = pozFisrtInt + 2 * sizeof(int32_t);
				
				printf("%s pentru %s\n", (char*)(arr + pozSir1) , (char*)(arr + pozSir2));
				
				if( tip == 1) {
					printf("%"PRId8"\n", *(int8_t*)(arr + pozFisrtInt));
					printf("%"PRId8"\n\n", *(int8_t*)(arr + pozFisrtInt + sizeof(int8_t)));
				} else if( tip == 2) {
					printf("%"PRId16"\n", *(int16_t*)(arr + pozFisrtInt));
					printf("%"PRId32"\n\n", *(int32_t*)(arr + pozFisrtInt + sizeof(int16_t)));
				} else if (tip == 3) {
					printf("%"PRId32"\n", *(int32_t*)(arr + pozFisrtInt));
					printf("%"PRId32"\n\n", *(int32_t*)(arr + pozFisrtInt + sizeof(int32_t)));
				}
				poznext = pozSir2 + strlen((char*)(arr + pozSir2)) + 1;
			}

		} else if(strcmp(comanda, "find") == 0) {

			char *index = strtok(NULL, " ");
			int ind = atoi(index);

			find(arr, len , ind);
				
		} else if(strcmp(comanda, "delete_at") == 0) {
			
			char *index = strtok(NULL, " ");
			int ind = atoi(index);

			delete_at(&arr, &len , ind);

		} else if(strcmp(comanda, "exit") == 0) {
			
			break;
	
		}
	}
	
	free(line);
	free(arr);

	return 0;
}
