/*
 * Loader Implementation
 *
 * 2022, Operating Systems
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <signal.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <unistd.h>

#define page_size getpagesize()
#include "exec_parser.h"

static so_exec_t *exec;
static int file;
static struct sigaction handler;

static void segv_handler(int signum, siginfo_t *info, void *context)
{
	void *segv_addr = (void*)info->si_addr;
	so_seg_t *segment = NULL;

	for (int i = 0; i < exec->segments_no; i++) {
		void *start_seg = (void *)exec->segments[i].vaddr;
		void *last_seg = (void *)(exec->segments[i].vaddr + exec->segments[i].mem_size);

		if (start_seg <= segv_addr && segv_addr <= last_seg) {
			segment = (exec->segments) + i;
			break;
		}
	}

	if (!segment)
		handler.sa_sigaction(signum, info, context);

	int pageNumber = (int)((uintptr_t)segv_addr - segment->vaddr) / page_size;

	if (!segment->data)
		segment->data = calloc((segment->mem_size / page_size) + 1, sizeof(int8_t));

	if (((int8_t *)(segment->data))[pageNumber] == 1)
		handler.sa_sigaction(signum, info, context);

	((int8_t *)(segment->data))[pageNumber] = 1;

	void *map_addr = mmap((void *)((uintptr_t)segment->vaddr + pageNumber * page_size),
		page_size, PROT_WRITE, MAP_FIXED | MAP_ANONYMOUS | MAP_SHARED, -1, 0);

	if (map_addr == MAP_FAILED)
		exit(-10);

	if (segment->file_size > page_size*pageNumber) {
		lseek(file, segment->offset + pageNumber * page_size, SEEK_SET);
		read(file, map_addr, (page_size < segment->file_size - page_size * pageNumber) ? page_size : (segment->file_size - page_size * pageNumber));
	}
	mprotect(map_addr, page_size, segment->perm);
}

void free_segment_data(void)
{
	for (int i = 0; i < exec->segments_no; i++) {
		so_seg_t *segment = exec->segments + i;

		if (segment->data)
			free(segment->data);
	}
}

int so_init_loader(void)
{
	int rc;
	struct sigaction sa;

	memset(&sa, 0, sizeof(sa));
	sa.sa_sigaction = segv_handler;
	sa.sa_flags = SA_SIGINFO;
	rc = sigaction(SIGSEGV, &sa, NULL);
	if (rc < 0) {
		perror("sigaction");
		return -1;
	}
	return 0;
}

int so_execute(char *path, char *argv[])
{
	file = open(path, O_RDONLY);

	exec = so_parse_exec(path);
	if (!exec)
		return -1;

	so_start_exec(exec, argv);

	free_segment_data();

	close(file);

	return -1;
}
