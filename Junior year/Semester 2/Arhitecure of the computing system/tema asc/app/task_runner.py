'''
The threadpool module is responsible for making the computations that is passed in to it
'''
from queue import Queue
from threading import Thread
import os
import os.path
import pandas as pd

OUTPUT_FOLDER = "./results/"

QUESTIONS_BEST_IS_MIN = [
    'Percent of adults aged 18 years and older who have an overweight classification',
    'Percent of adults aged 18 years and older who have obesity',
    'Percent of adults who engage in no leisure-time physical activity',
    'Percent of adults who report consuming fruit less than one time daily',
    'Percent of adults who report consuming vegetables less than one time daily'
]

QUESTIONS_BEST_IS_MAX = [
    'Percent of adults who achieve at least 150 minutes a week of moderate-intensity aerobic' + 
    ' physical activity or 75 minutes a week of vigorous-intensity aerobic activity' +
    ' (or an equivalent combination)',
    'Percent of adults who achieve at least 150 minutes a week of moderate-intensity aerobic' +
    ' physical activity or 75 minutes a week of vigorous-intensity aerobic physical activity' +
    ' and engage in muscle-strengthening activities on 2 or more days a week',
    'Percent of adults who achieve at least 300 minutes a week of moderate-intensity aerobic' +
    ' physical activity or 150 minutes a week of vigorous-intensity aerobic activity (or an' +
    ' equivalent combination)',
    'Percent of adults who engage in muscle-strengthening activities on 2 or more days a week',
]

class ThreadPool:
    '''
    A thread pool executioner that gets work and passed to workers
    '''
    def __init__(self):
        '''
        The constructor of the class
        '''
        # the task will be placed in this queue to be later process by the workers
        self.job_queue = Queue()

        # a task running with be found in job_running and finished in job_done
        self.job_done = []
        self.job_running = []

        # create the directory
        if not os.path.exists(OUTPUT_FOLDER):
            os.mkdir(OUTPUT_FOLDER)

        # get the number of threads and initialize them with an reference to the job queue
        # and acces to the data ingestor
        number_of_threads = os.environ.get("TP_NUM_OF_THREADS", os.cpu_count())

        self.threads = [
            TaskRunner(self.job_queue, self.job_done, self.job_running)
            for i in range(number_of_threads)
            ]

        # accept input until false
        self.running_var = True

        # open threads
        for thread in self.threads:
            thread.start()

    def add_job(self, job):
        '''
        Submit a job to the ThreadPool to be solved
        @param job -  a tuple with the request, data, id, and action
        '''
        self.job_queue.put(job)

    def graceful_shutdown(self):
        '''
        This stops the threadpool from reciveing jobs and waits to
        finish the waiting and running tasks
        '''
        # tell the threads that form now on they will exit if the queue is empty
        for thread in self.threads:
            thread.go_shutdown()

        self.running_var = False

    def running(self):
        '''
        Checking if the threadpool still accepts requests
        '''
        return self.running_var

    def status(self):
        '''
        Retreving the status of every job_id
        @return a list with dictionary job_id : status
        '''
        res = []

        for job in self.job_done:
            res.append({f'job_id_{job}' : "done"})

        for job in self.job_running:
            res.append({f'job_id_{job}' : "running"})

        for job in list(self.job_queue):
            res.append({f'job_id_{job[2]}' : "waiting"})

        return []

    def check_status(self, job_id):
        '''
        Retreving the status of every job_id
        @param job_id the job to be verified
        @return str - reprezentig tha state of the job
        '''
        # return the status of job id
        if job_id in self.job_done:
            return "done"
        if job_id in self.job_running:
            return "running"
        return "waiting"

class TaskRunner(Thread):
    '''
    A worker that resolve task from the queue job and writes the result to a file
    @param queue - a reference to the master job queue
    '''
    def __init__(self, queue, done, running):
        '''
        @param queue - the jobs queue that will run
        @param done - done is the list of threads that haved finished
        @param running - the list of threads that are currently running
        '''
        super().__init__()
        self.queue_of_task = queue
        self.done = done
        self.running = running
        self.shutdown = False

    def go_shutdown(self):
        '''
        A function that signals the end of execution
        '''
        self.shutdown = True

    def solve_mean(self, data, sort_order = False):
        '''
        Helper function to calculate the mean of every element in the dataframe
        '''
        res = {}

        # for every row add the data_value for the states in one variable
        # and track the number of element
        for _, value in data.iterrows():
            curr_val = float(value["Data_Value"])
            if res.get(value["LocationDesc"]):
                partial_sum, nr = res[value["LocationDesc"]]
                res[value["LocationDesc"]] = (curr_val + partial_sum, nr + 1)
            else:
                res[value["LocationDesc"]] = (curr_val, 1)
        # sort them in the ask order and compute the mean
        res = { key : partial_sum / nr for key, (partial_sum, nr) in res.items()}
        sorted_res = sorted(res.items(), key=lambda x: x[1], reverse=sort_order)

        return dict(sorted_res)

    def solve_global_mean(self, data):
        '''
        Helper function to calculate the global mean of the dataframe
        '''
        res = {"global_mean" : (0, 0)}

        # for every row add the data_value for the states in one variable
        # and track the number of element
        for _, value in data.iterrows():
            curr_val = float(value["Data_Value"])
            partial_sum, nr = res["global_mean"]
            res["global_mean"] = (curr_val + partial_sum, nr + 1)
        # compute the mean
        return { key : partial_sum / nr for key, (partial_sum, nr) in res.items()}

    def solve_diff(self, data, req_state = None):
        '''
        Helper function to calculate the diff of every mean and the global mean
        '''
        # get the global mean
        global_mean = self.solve_global_mean(data)

        if req_state:
            data = data[data["LocationDesc"] == req_state]

        # get the states mean
        states_mean = self.solve_mean(data)
        # return the diff between the values
        return {key : global_mean["global_mean"] - value for key, value in states_mean.items()}

    def solved_category(self, data):
        '''
        Helper function to calculate the mean of every category
        '''
        res = {}

        # for every row add the data_value for the states in one variable
        # and track the number of element
        for _, value in data.iterrows():
            # if one value is missing, skip it
            if pd.isna(value["StratificationCategory1"]) or pd.isna(value["Stratification1"]):
                continue

            curr_val = float(value["Data_Value"])
            key = (value["LocationDesc"],
                   value["StratificationCategory1"],
                   value["Stratification1"])

            if res.get(key):
                partial_sum, nr = res[key]
                res[key] = (curr_val + partial_sum, nr + 1)
            else:
                res[key] = (curr_val, 1)

        # compute the mean
        return { str(key) : partial_sum / nr for key, (partial_sum, nr) in res.items()}

    def solved_category_by_state(self, data, req_state):
        '''
        Helper function to calculate the mean of every category of a given state
        '''
        res = {}

        # for every row add the data_value for the states in one variable
        # and track the number of element
        for _, value in data.iterrows():
             # compute the mean
            if pd.isna(value["StratificationCategory1"]) or pd.isna(value["Stratification1"]):
                continue

            curr_val = float(value["Data_Value"])
            key = (value["StratificationCategory1"], value["Stratification1"])

            if res.get(key):
                partial_sum, nr = res[key]
                res[key] = (curr_val + partial_sum, nr + 1)
            else:
                res[key] = (curr_val, 1)

        # compute the mean
        return { req_state :
            { str(key) : partial_sum / nr for key, (partial_sum, nr) in res.items()}}

    def solve_job(self, job):
        '''
        A helper function for solving diffrent types of question
        @param job - a tuple with the request, data, id, and action
        @return a value of the computation
        '''
        req, data, _, action = job
        #filter the date for easy computation
        data_filtred = data[data["Question"] == req["question"]]
        sort_order = req["question"] in QUESTIONS_BEST_IS_MAX

        if not req.get("state"):
            return self.solve_action(data_filtred, action, sort_order)
        return self.solve_action(data_filtred, action, sort_order, req["state"])

    def solve_action(self, data, action, sort_order, req_state = None):
        '''
        A helper function for solving diffrent actions for the dataset
        @param data - the data for the computation
        @param action - an action coresponding the api route
        @param sort_order - the order to be sorted
        @return a value of the computation
        '''
        res = None

        if action in ("states_mean", "state_mean"):
            if req_state:
                data = data[data["LocationDesc"] == req_state]
            return self.solve_mean(data)
        if action == "best5":
            res = self.solve_mean(data, sort_order)
            res = {key : res[key]  for key in list(res)[:5]}
        if action == "worst5":
            res = self.solve_mean(data, sort_order)
            res = {key : res[key]  for key in list(res)[-5:]}
        if action == "global_mean":
            res = self.solve_global_mean(data)
        if action == "diff_from_mean":
            res = self.solve_diff(data)
        if action == "mean_by_category":
            res = self.solved_category(data)
        if action == "state_diff_from_mean":
            res = self.solve_diff(data, req_state)
        if action == "state_mean_by_category":
            if req_state:
                data = data[data["LocationDesc"] == req_state]
            res = self.solved_category_by_state(data, req_state)

        return res

    def run(self):
        while True:
            # Get pending job
            if self.shutdown and self.queue_of_task.empty:
                break

            job = self.queue_of_task.get()
            self.running.append(job[2])

            # Execute the job and save the result to disk
            data = self.solve_job(job)
            with open(f'{OUTPUT_FOLDER}job_id_{job[2]}.json', 'w', encoding='utf-8') as f:
                f.write(str(data))
                self.running.remove(job[2])
                self.done.append(job[2])
