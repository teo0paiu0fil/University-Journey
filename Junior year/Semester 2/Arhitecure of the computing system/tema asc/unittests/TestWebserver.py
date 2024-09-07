'''
Test case 
'''
import unittest
import json
import os
from datetime import datetime
from time import sleep
import requests

from deepdiff import DeepDiff



class TestWebserve(unittest.TestCase):
    '''
    TestClass
    '''
    def setUp(self):
        '''
        clean
        '''
        os.system("rm -rf results/*")

    def check_res_timeout(self, res_callable, ref_result, timeout_sec, poll_interval = 0.2):
        '''
        Helper test
        '''
        initial_timestamp = datetime.now()
        while True:
            response = res_callable()
            # print(response)
            # Asserting that the response status code is 200 (OK)
            self.assertEqual(response.status_code, 200)

            # Asserting the response data
            response_data = response.json()
            # print(f"Response_data\n{response_data}")
            if response_data['status'] == 'done':
                # print(f"Ref data {ref_result} and type {type(ref_result)}")
                d = DeepDiff(response_data['data'], ref_result, math_epsilon=0.01)
                self.assertTrue(not d, str(d))
                break
            if response_data['status'] == 'running':
                current_timestamp = datetime.now()
                time_delta = current_timestamp - initial_timestamp
                if time_delta.seconds > timeout_sec:
                    self.fail("Operation timedout")
                else:
                    sleep(poll_interval)

    def test_num_jobs(self):
        '''
        Test
        '''
        output_dir = "unittests/tests/num_jobs/output/"
        input_dir = "unittests/tests/num_jobs/input/"
        input_files = os.listdir(input_dir)

        for input_file in input_files:
            # Get the index from in-idx.json
            # The idx is between a dash (-) and a dot (.)
            idx = input_file.split('-')[1]
            idx = int(idx.split('.')[0])

            with open(f"{input_dir}/{input_file}", "r", encoding='utf-8') as fin:
                # Data to be sent in the POST request
                req_data = json.load(fin)

            with open(f"{output_dir}/ref-{idx}.json", "r", encoding='utf-8') as fout:
                ref_result = json.load(fout)

            with self.subTest():
                # Sending a POST request to the Flask endpoint
                res = requests.post("http://127.0.0.1:5000/api/num_jobs",
                                    json=req_data, timeout=1000)

                job_id = res.json()
                job_id = job_id["job_id"]

                self.check_res_timeout(
                    res_callable = lambda: requests.get(
                        f"http://127.0.0.1:5000/api/get_results/{job_id}",
                        timeout=1000),
                    ref_result = ref_result,
                    timeout_sec = 1)

    def test_jobs(self):
        '''
        Test
        '''
        output_dir = "unittests/tests/jobs/output/"
        input_dir = "unittests/tests/jobs/input/"
        input_files = os.listdir(input_dir)

        for input_file in input_files:
            # Get the index from in-idx.json
            # The idx is between a dash (-) and a dot (.)
            idx = input_file.split('-')[1]
            idx = int(idx.split('.')[0])

            with open(f"{input_dir}/{input_file}", "r", encoding='utf-8') as fin:
                # Data to be sent in the POST request
                req_data = json.load(fin)

            with open(f"{output_dir}/ref-{idx}.json", "r", encoding='utf-8') as fout:
                ref_result = json.load(fout)

            with self.subTest():
                # Sending a POST request to the Flask endpoint
                res = requests.post("http://127.0.0.1:5000/api/jobs",
                                    json=req_data, timeout=1000)

                job_id = res.json()
                job_id = job_id["job_id"]

                self.check_res_timeout(
                    res_callable = lambda: requests.get(
                        f"http://127.0.0.1:5000/api/get_results/{job_id}",
                        timeout=1000),
                    ref_result = ref_result,
                    timeout_sec = 1)

    def test_gracefull_shutdown(self):
        '''
        test
        '''
        output_dir = "unittests/tests/graceful_shutdown/output/"
        input_dir = "unittests/tests/graceful_shutdown/input/"
        input_files = os.listdir(input_dir)

        for input_file in input_files:
            # Get the index from in-idx.json
            # The idx is between a dash (-) and a dot (.)
            idx = input_file.split('-')[1]
            idx = int(idx.split('.')[0])

            with open(f"{input_dir}/{input_file}", "r", encoding='utf-8') as fin:
                # Data to be sent in the POST request
                req_data = json.load(fin)

            with open(f"{output_dir}/ref-{idx}.json", "r", encoding='utf-8') as fout:
                ref_result = json.load(fout)

            with self.subTest():

                res = requests.get(
                    "http://127.0.0.1:5000/api/graceful_shutdown",
                    timeout=1000)

                # Sending a POST request to the Flask endpoint
                res = requests.post("http://127.0.0.1:5000/api/states_mean"
                                    , json=req_data, timeout=1000)

                d = DeepDiff(res.json(), ref_result)

                self.assertTrue(not d)

if __name__ == '__main__':
    try:
        unittest.main()
    finally:
        print("Finished!")
