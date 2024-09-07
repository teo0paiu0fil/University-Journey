'''
This module implements the the stochastic hill climbing algorithm.
''' 

import random
from hill_climing.state import State

Result = tuple[bool, int, int, State]

def stochastic_hill_climbing(initial, max_iters: int = 2000) -> Result:
	'''
	Stoachastic hill climbing algorithm. Choose a random state from the neighbours
	states that are better then the current state.
	:param initial: the initial state
	:param max_iters: the maximum number of iterations
	:return: a tuple with the following elements:
		- a boolean value that is True if a final state was reached
		- the number of iterations
		- the number of states generated
		- the final state
	'''
	iters, states = 0, 0
	state = initial.clone()

	# run the iterations
	while iters < max_iters and not state.is_final():
		iters += 1

		# get the successors
		hard, soft = state.conflicts()
		succesors = state.get_next_states()
  
		# fiter them
		next_state = [new_state for new_state in succesors if new_state.conflicts()[0] < hard or (
					new_state.conflicts()[0] == hard and new_state.conflicts()[1] <= soft)]
		# if there is a posibility to exit the local maximum then take it
  
		next_best_state = list(filter(lambda x: x.conflicts()[0] < hard 
                                or (x.conflicts()[0] == hard and x.conflicts()[1] < soft), next_state))
  
		states += len(succesors)
  
		# if there are no better states, then we have reached a local maximum
		if not next_state:
		  	break
 
		# select a random state from the better states
		if next_best_state:
			state = random.choice(next_best_state)
		else:
			state = random.choice(next_state)

	return state.is_final(), iters, states, state
