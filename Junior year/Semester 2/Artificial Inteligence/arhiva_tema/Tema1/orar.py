from sys import argv
import time
from copy import deepcopy
import matplotlib.pyplot as plt

# skeleton and checker
import check_constraints

import utils
from utils import read_yaml_file
from utils import pretty_print_timetable

# pcsp
from pcsp import constrains_variables_domains as cvd
from pcsp.pcsp import PCSP as pcsp_function
import pcsp.pcsp as pcsp

# hill_climbing
from hill_climing import stochastic_hill_climbing as hc
from hill_climing import state as st
from hill_climing.state import change_constrains as cc

def run_test_hill_climbing(timetable_specs, nr_tests, debug_flag = False):
    # create a state
    cc(timetable_specs)
    state = st.State(timetable_specs) 
    
    total_iters = 0
    total_states = 0
    total_period = 0
    wins = 0
    constrangeri_total = (0, 0)
    
    all_states = []
    all_iters = []
    all_states_nr = []
    all_constraints = []
    
    # run the number of tests
    for _ in range(nr_tests):
        # start benchmark
        start = time.time()
        is_final, iters, states, final_state = hc.stochastic_hill_climbing(state)
        # get the runtime
        end = time.time()

        if debug_flag:
            constrangeri_incalcate = check_constraints.check_mandatory_constraints(final_state.get_timetable(), read_yaml_file(filename))
            constrangeri_optionale = check_constraints.check_optional_constraints(final_state.get_timetable(), read_yaml_file(filename))
            constrangeri_total = (constrangeri_total[0] + constrangeri_incalcate ,constrangeri_total[1] + constrangeri_optionale)
            all_constraints.append((constrangeri_incalcate, constrangeri_optionale))
        
        if (is_final and not debug_flag) or (debug_flag and constrangeri_incalcate == 0 and constrangeri_optionale == 0):
            wins += 1
            all_states.append(True)
        else:
            all_states.append(False)
            
        total_iters += iters
        all_iters.append(iters)
        total_states += states
        all_states_nr.append(states)
        total_period += end - start
    
    print()
    if wins > 0:
        score = (wins / nr_tests) * 100.
        print(f'Wins: {wins} out of {nr_tests} -> {score:.2f}%')
        print(f'Average iterations for win: {total_iters / wins:.2f}')
        print(f'Average states for win: {float(total_states) / float(wins):.2f}')
        print(f'Average period for win: {total_period / wins:.2f} secunde')
        if wins < nr_tests:
            print(f'Average constrangeri incalcate per failed: {constrangeri_total[0] / (nr_tests - wins):.2f} obligatorii, {constrangeri_total[1] / (nr_tests - wins):.2f} optionale')
    else:
        print(f'Wins: {wins} out of {nr_tests} -> 0%')
        print(f'Average iterations: {total_iters / nr_tests:.2f}')
        print(f'Average states: {total_states / nr_tests:.2f}')
        
    if total_period > 59:
        m, s = divmod(total_period, 60)
        print(f'Run for period: {int(m)}:{int(s):0>2} minute')
    else:
        print(f'Run for period: {total_period:.2f} secunde')
        
    return all_states, all_iters, all_states_nr, all_constraints


def run_test_pcsp(timetable_specs, acceptable_cost, debug_flag = False):
    
    # get the constrains, variables and domains

    constrains, variables, domains = cvd.get_all(timetable_specs)
    
    pcsp.best_solution = {}
    pcsp.best_cost = len(constrains)
    constrangeri_total = (0, 0)
    pcsp.iterations = 0
    
    start = time.time()
    print()
    if pcsp_function(variables, domains, constrains, acceptable_cost, {}, 0):
        print(f"Best found in {pcsp.iterations} iterations: {str(pcsp.best_cost)}")
    else:
        print(f"Acceptable solution not found in {pcsp.iterations}; Best found: {str(pcsp.best_cost)}")
    period = time.time() - start
    
    timetable = transform_pcsp_timetable(pcsp.best_solution)
        
    if debug_flag:
        constrangeri_incalcate = check_constraints.check_mandatory_constraints(timetable, read_yaml_file(filename))
        constrangeri_optionale = check_constraints.check_optional_constraints(timetable, read_yaml_file(filename))
        constrangeri_total = (constrangeri_total[0] + constrangeri_incalcate ,constrangeri_total[1] + constrangeri_optionale)
        
    return constrangeri_total == (0, 0), pcsp.iterations, period, constrangeri_total

def analise_diff(results_hc = None, results_pcsp = None, nr_tests = None):
    if results_hc is not None and nr_tests is not None:
        all_states, all_iters, all_states_nr, all_constraints = results_hc
    
        print("Analize the results hill climbing")
    
        avg_state = sum(all_states_nr) / nr_tests
        plt.plot(range(1, nr_tests + 1), all_states_nr, 'r')
        plt.plot(range(1, nr_tests + 1), [avg_state for x in range(1, nr_tests + 1)], 'g')
        plt.xlabel('Test number')
        plt.ylabel('Number of states')
        plt.title('Number of states for each test')
        plt.show()
        
        avg_iter = sum(all_iters) / nr_tests
        plt.plot(range(1, nr_tests + 1), all_iters, 'r')
        plt.plot(range(1, nr_tests + 1), [avg_iter for x in range(1, nr_tests + 1)], 'g')
        plt.xlabel('Test number')
        plt.ylabel('Number of iterations')
        plt.title('Number of iterations for each test')
        plt.show()
        
        avg_win = sum([1 for x in all_states if x == True]) / nr_tests
        plt.plot(range(1, nr_tests + 1), all_states, 'r')
        plt.plot(range(1, nr_tests + 1), [avg_win for x in range(1, nr_tests + 1)], 'g')
        plt.xlabel('Test number')
        plt.ylabel('Win')
        plt.title('Win for each test')
        plt.show()
      
        plt.plot(range(1, nr_tests + 1), [x[0] for x in all_constraints], 'r')
        plt.plot(range(1, nr_tests + 1), [x[1] for x in all_constraints], 'b')
        plt.xlabel('Test number')
        plt.ylabel('Number of constraints')
        plt.title('Number of constraints for each test')
        plt.show()

    if results_pcsp is not None:
        had_win, iterations, period, constrangeri_total = results_pcsp
        print("TODO: Analize the results pcsp if managed to find a solution") 
        print("Analize the results pcsp")

        

def transform_pcsp_timetable(best_solution):
    timetable = {}
    for var in best_solution.keys():
        day, interval, room = var
        if day not in timetable.keys():
            timetable[day] = {}
        if interval not in timetable[day].keys():
            timetable[day][interval] = {}
        timetable[day][interval][room] = best_solution[var] if best_solution[var] != (None, None) else None
        
    return timetable

if __name__ == '__main__':
    # get the input file
    algo = argv[1].strip() if len(argv) > 1 else None
    filename =  f'inputs/{argv[2]}' if len(argv) > 2 else None
    
    # check if the input file was provided
    if not filename:
        print('No input file provided')
        exit(1)
    
    # check if the number of tests was provided 
    nr_tests = int(argv[3]) if len(argv) > 2 else 1
    
    if nr_tests < 1:
        print('Number of tests must be greater than 0')
        exit(1)
        
    # read the file
    if algo == 'pcsp':
        # test the pcsp algorithm
        results_pcsp = run_test_pcsp(timetable_specs, 0, debug_flag = True)
    
    if algo == 'hc':
        timetable_specs = read_yaml_file(filename)
        # test the hill climbing algorithm
        results_hc = run_test_hill_climbing(deepcopy(timetable_specs), nr_tests, debug_flag = True)
        
        # analize the results
        analise_diff(results_hc, nr_tests = nr_tests)
    
    
   
        

    