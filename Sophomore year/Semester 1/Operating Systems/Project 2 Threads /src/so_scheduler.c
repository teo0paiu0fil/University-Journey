#include <stdlib.h>
#include <stdio.h>
#include "semaphore.h"
#include "../util/so_scheduler.h"

#define SO_MAX_UNITS	32
#define SO_MAX_THREADS 200

typedef struct thread {
    tid_t id;
    unsigned int time_remaining;
    unsigned int priority;
    so_handler *func;
    sem_t *sem;
} *pThread_t, Thread_t;

typedef struct scheduler {
    unsigned int time_q;
    unsigned int io_number;
    pThread_t running;
    pThread_t *waiting;
    pThread_t *ready;
    int nr_ready;
    int nr_wait;
} so_scheduler;

static so_scheduler *sched = NULL;

int so_init(unsigned int time_quantum, unsigned int io)
{
    if (io > SO_MAX_NUM_EVENTS || time_quantum > SO_MAX_UNITS || time_quantum <= 0)
        return -1;

    if (sched == NULL) {
        sched = calloc(1 , sizeof(so_scheduler));
        if (sched == NULL)
            return -1;

        sched->time_q = time_quantum;
        sched->io_number = io;
        sched->ready = calloc(SO_MAX_THREADS, sizeof(pThread_t));
        if (!sched->ready) {
            free(sched);
            return -1;
        }
        
        sched->waiting = calloc(SO_MAX_THREADS, sizeof(pThread_t));
        if (!sched->waiting) {
            free(sched->ready);
            free(sched);
            return -1;
        }
    } else 
        return -1;

    return 0;
}

void insert_sort(pThread_t thread)
{
    int i = 0, j = 0;
    sched->ready[sched->nr_ready] = thread;
    sched->nr_ready++;

    for (i = 0; i < sched->nr_ready; i++)
        if (sched->ready[i]->priority > thread->priority)
            break;


    for (j = sched->nr_ready - 1; j >= i; j--)
        sched->ready[j] = sched->ready[j - 1];

    sched->ready[j] = thread;
}

pThread_t get_thread()
{
    pThread_t t = sched->ready[0];

    for (int i = 0; i < sched->nr_ready - 1; i++)
        sched->ready[i] = sched->ready[i + 1];

    sched->nr_ready--;
    sched->ready[sched->nr_ready] = NULL;
    return t;
}

void run_all() {
    if (sched->nr_ready == 0 && sched->running == NULL) return;

    if (sched->running) {
        pthread_join(sched->running->id, NULL);
        free(sched->running->sem);
        free(sched->running);
        sched->running = NULL;
    } else {
        pThread_t t = get_thread();
        sem_post(t->sem);
        pthread_join(t->id, NULL);
        free(t->sem);
        free(t);
    }

    run_all();
}

void so_end(void)
{
    if (sched != NULL) {
        run_all();
       
        if(sched->ready)
            free(sched->ready);
        sched->ready = NULL;

        if(sched->waiting)
            free(sched->waiting);
        sched->waiting = NULL;
        
        free(sched);
    }
    sched = NULL;
}

void so_exec(void)
{
    if(sched->running) {
        sched->running->time_remaining--;
        if (sched->running->time_remaining == 0) {
            pThread_t thread = sched->running;
            pThread_t t;
            if (sched->nr_ready != 0) {
                t = get_thread();
                sem_post(t->sem);
                sched->running = t;
                thread->time_remaining = sched->time_q;
                insert_sort(thread);
                sem_wait(thread->sem);
            } else {
                thread->time_remaining = sched->time_q;
                t = thread;
            }
        }
    }
}

int checkIO(unsigned int io) {
    if(io < sched->io_number)
        return 1;
    return 0;
}

void insert_sort_wait(pThread_t thread){
    int i = 0, j = 0;
    sched->waiting[sched->nr_wait] = thread;
    sched->nr_wait++;

    for (i = 0; i < sched->nr_wait; i++)
        if (sched->waiting[i]->priority > thread->priority)
            break;

    for (j = sched->nr_wait - 1; j >= i; j--)
        sched->waiting[j] = sched->waiting[j - 1];

    sched->waiting[j] = thread;
}

int so_wait(unsigned int io)
{
    if (checkIO(io) == 0)
        return -1;

    return 0;   
}

int so_signal(unsigned int io)
{
    if (checkIO(io) == 1)
        return io;

    return -1;
}

void *start_thread(void *args)
{
    pThread_t th = (pThread_t) args;
    if(sem_wait(th->sem) == -1)
        return NULL;

    th->func(th->priority);

    return NULL;
}

tid_t so_fork(so_handler *func, unsigned int priority)
{
    if (priority > SO_MAX_PRIO || !func || !sched)
        return INVALID_TID; 

    pThread_t thread = calloc(1, sizeof(Thread_t));
    if (!thread)
        return INVALID_TID;

    thread->sem = calloc(1, sizeof(sem_t));
    if (!thread->sem) {
        free(thread);
        return INVALID_TID;
    }

    if (sem_init(thread->sem, 0, 0) == -1) {
        return INVALID_TID;
        free(thread->sem);
        free(thread);
    }

    thread->func = func;
    thread->priority = priority;
    thread->time_remaining = sched->time_q;

    if(pthread_create(&thread->id, NULL, &start_thread, thread) != 0) {
        free(thread->sem);
        free(thread);
        return INVALID_TID;
    }

    if (!sched->running) {
        sched->running = thread;
        sem_post(thread->sem);
    } else if (sched->running->priority < thread->priority) {
        // TODO
    } else {
        insert_sort(thread);
        so_exec();
    }

    return thread->id;
}
