'''
This module defines every route in the webserver
'''

from flask import request, jsonify
from app import webserver

OUTPUT_FOLDER = "./results/"

@webserver.route('/api/get_results/<job_id>', methods=['GET'])
def get_response(job_id):
    '''
    Route for /api/get_results/<job_id>
    '''
    webserver.log.info("Enter /get_results with job_id = %s", job_id)
    if request.method == 'GET':
        # Check if job_id is valid
        job_id = int(job_id)
        if job_id <= 0 or job_id >= webserver.job_counter:
            # Not Acceptable
            webserver.log.error("Exit /get_results with job_id : %s not acceptable", job_id)
            return jsonify({"status": "error", "reason": "Invalid job_id"}), 406

        # Check if job_id is done and return the result
        status = webserver.tasks_runner.check_status(job_id)
        if status == 'done':
            data = res_for(job_id)
            webserver.log.info("Exit /get_results with status : %s and data : %s", status, data)
            return jsonify({'status': status, 'data': data}), 200

        # If not, return running status
        webserver.log.info("Exit /get_results with status : %s", status)
        return jsonify({'status': status}), 200

    # Method Not Allowed
    webserver.log.error("Exit /get_results with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/states_mean', methods=['POST'])
def states_mean_request():
    '''
    Route for /api/states_mean
    '''
    webserver.log.info("Enter /states_mean")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "states_mean")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /states_mean with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/state_mean', methods=['POST'])
def state_mean_request():
    '''
    Route for /api/state_mean
    '''
    webserver.log.info("Enter /state_mean")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("state") or not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "state_mean")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202
    webserver.log.error("Exit /state_mean with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/best5', methods=['POST'])
def best5_request():
    '''
    Route for /api/best5
    '''
    webserver.log.info("Enter /best5")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "best5")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info(f"Registred job with id {job_id}")

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /best5 with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/worst5', methods=['POST'])
def worst5_request():
    '''
    Route for /api/worst5
    '''
    webserver.log.info("Enter /worst5")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "worst5")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /worst5 with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/global_mean', methods=['POST'])
def global_mean_request():
    '''
    Route for /api/global_mean
    '''
    webserver.log.info("Enter /global_mean")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "global_mean")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /global_mean with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/diff_from_mean', methods=['POST'])
def diff_from_mean_request():
    '''
    Route for /api/diff_from_mean
    '''
    webserver.log.info("Enter /diff_from_mean")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "diff_from_mean")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /diff_from_mean with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/state_diff_from_mean', methods=['POST'])
def state_diff_from_mean_request():
    '''
    Route for /api/state_diff_from_mean
    '''
    webserver.log.info("Enter /state_diff_from_mean")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question") or not data.get("state"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "state_diff_from_mean")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /state_diff_from_mean with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/mean_by_category', methods=['POST'])
def mean_by_category_request():
    '''
    Route for /api/mean_by_category
    '''
    webserver.log.info("Enter /mean_by_category")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question"):
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "mean_by_category")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id %s", job_id)

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /mean_by_category with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/state_mean_by_category', methods=['POST'])
def state_mean_by_category_request():
    '''
    Route for /api/state_mean_by_category
    '''
    webserver.log.info("Enter /state_mean_by_category")
    if request.method == 'POST':
        # Get request data
        data = request.json
        webserver.log.info("Got request %s", data)

        # checking if the threadpool still accepts request
        if not webserver.tasks_runner.running():
            webserver.log.warning("The threadpool is shuting down")
            return jsonify({"status" : 'error', "reason" : "The threadpool is shuting down"}), 400

        # verify request
        if not data.get("question") or not data['state']:
            webserver.log.warning("Got request without coresponding data")
            return jsonify({"status" : 'error', "reason" : "The data isn't full"}), 400

        # Register job. Don't wait for task to finish
        job_id = webserver.job_counter
        job = (data, webserver.data_ingestor.retreave_data(), job_id, "state_mean_by_category")
        webserver.tasks_runner.add_job(job)

        # Increment job_id counter
        webserver.job_counter += 1
        webserver.log.info("Registred job with id {job_id}")

        # Return associated job_id
        return jsonify({"job_id" : job_id}), 202

    webserver.log.error("Exit /state_mean_by_category with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/graceful_shutdown', methods=['GET'])
def graceful_shutdown_request():
    '''
    Route for /api/graceful_shutdown
    '''
    webserver.log.info("Enter /graceful_shutdown")
    if request.method == 'GET':
        webserver.tasks_runner.graceful_shutdown()
        webserver.log.info("Starting the shutdown process")
        return jsonify({"status" : "done"}), 200

    webserver.log.error("Exit /graceful_shutdown with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/jobs', methods=['GET'])
def jobs_request():
    '''
    Route for /api/jobs
    '''
    webserver.log.info("Enter /jobs")
    if request.method == 'GET':
        webserver.tasks_runner.graceful_shutdown()
        data = webserver.tasks_runner.status()
        webserver.log.info("Returning the status of the submited jobs")
        return jsonify({"status" : "done", "data" : data}), 200

    webserver.log.error("Exit /jobs with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

@webserver.route('/api/num_jobs', methods=['GET'])
def num_jobs_request():
    '''
    Route /api/num_jobs
    '''
    webserver.log.info("Enter /num_jobs")
    if request.method == 'GET':
        webserver.tasks_runner.graceful_shutdown()
        data = webserver.tasks_runner.status()

        list_filtered = []
        # get the remaining jobs
        for d in data:
            for key, value in d.items():
                if value in ["running", "waiting"]:
                    list_filtered.append(key)
        webserver.log.info("Returning the remaining of the jobs before shutdown")

        num_jobs = len(list_filtered)

        return jsonify({"status" : "done", "num_jobs" : num_jobs}), 200

    webserver.log.error("Exit /num_jobs with Method not allowed")
    return jsonify({"error": "Method not allowed"}), 405

# You can check localhost in your browser to see what this displays
@webserver.route('/')
@webserver.route('/index')
def index():
    '''
    Return the available routs
    '''
    routes = get_defined_routes()
    msg = "Hello, World!\n Interact with the webserver using one of the defined routes:\n"

    # Display each route as a separate HTML <p> tag
    paragraphs = ""
    for route in routes:
        paragraphs += f"<p>{route}</p>"

    msg += paragraphs
    return msg

def get_defined_routes():
    '''
    Get the defined routes
    '''
    routes = []
    for rule in webserver.url_map.iter_rules():
        methods = ', '.join(rule.methods)
        routes.append(f"Endpoint: \"{rule}\" Methods: \"{methods}\"")
    return routes

def res_for(job_id):
    '''
    Get the response for a given id, the id must be done
    @param job_id - the id to retreave the result
    @return the result of the job
    '''
    with open(f'{OUTPUT_FOLDER}job_id_{job_id}.json', 'r', encoding='utf-8') as f:
        content = f.read()
        webserver.logger.warning(content)
    return eval(content)
