from .DFA import DFA

from dataclasses import dataclass
from collections.abc import Callable

EPSILON = ''  # this is how epsilon is represented by the checker in the transition function of NFAs

@dataclass
class NFA[STATE]:
    S: set[str]
    K: set[STATE]
    q0: STATE
    d: dict[tuple[STATE, str], set[STATE]]
    F: set[STATE]

    def epsilon_closure(self, state: STATE) -> set[STATE]:
        """
        This method perform a depth first search on the epsilon symbol
        where the state is the starting node, and the group the current visited nodes
        Args:
            state (STATE): the root node of the dfs
        Returns:
            set[STATE] - representing the epsilon closure of state
        """
        group : set[STATE] = set([state])

        # if there is not a epsilon tranzition on this state return the state
        if not self.d.get((state, EPSILON)):
            return group
        
        # nodes to be visited
        stack : list[STATE] = list(self.d.get((state, EPSILON)))

        while stack:
            curr_state : STATE = stack.pop()
            group.add(curr_state)
            if self.d.get((curr_state, EPSILON)):
                stack.extend(self.d.get((curr_state, EPSILON)) - group)

        # return the visited nodes
        return group

    def subset_construction(self) -> DFA[frozenset[STATE]]:
        """
        This method returns a DFA base on the subset construction algorithm of the curent NFA.
        Returns:
            DFA[frozenset[STATE]]
        """
        ## Initialization
        # the alfabet is the same as the NFA except the epsilon character
        dfa_S : set[str] = self.S - set(EPSILON) 
        dfa_K : set[frozenset(STATE)] = set()
        # the initial state is the epsilon transition from the initial NFA state
        dfa_q0 : frozenset[STATE] = frozenset(self.epsilon_closure(self.q0))
        dfa_d : dict[tuple[frozenset[STATE], str], frozenset[STATE]] = {}
        dfa_F : set[frozenset(STATE)] = set()

        ## Computing the algorithm
        stack : set[frozenset[STATE]] = [frozenset(self.epsilon_closure(self.q0))]

        while stack:
            curr_state : frozenset[STATE] = stack.pop()
            
            # for every character in the alfabet
            for character in dfa_S: 
                new_group_of_states = set()
                # for every state in the state group 
                for state in curr_state:
                    # if there is a tranzistion on c add all the epsilon_closure of the resulting states
                    if self.d.get((state, character)):
                        for dest in self.d.get((state, character)):
                            new_group_of_states.update(self.epsilon_closure(dest))

                    # if the state in the group is final add the group in the final dfa states
                    if state in self.F:
                        dfa_F.add(curr_state)

                # if the new_group_of_states is still empty i will consider that as sink state
                # and will be process the same as the rest of the states
                
                # complete the function on that character
                dfa_d[(curr_state, character)] = frozenset(new_group_of_states)

                # if the new group_state is not in the stack or in the dfa state set add the new group_group in 
                # the stack so it can be processed
                if not frozenset(new_group_of_states) in dfa_K and not frozenset(new_group_of_states) in stack:
                    stack.append(frozenset(new_group_of_states))
        
            # add the curr_state to the dfa states
            dfa_K.add(curr_state)

        # Returning the constructed DFA
        return DFA(dfa_S, dfa_K, dfa_q0, dfa_d, dfa_F)

    def remap_states[OTHER_STATE](self, f: 'Callable[[STATE], OTHER_STATE]') -> 'NFA[OTHER_STATE]':
        """ 
        This method remaps the states of this current object to another reprezentation without modifing the 
        internal structure of the nfa
        Args:
            f (Callable[[STATE], OTHER_STATE]): a function that converts the STATE to OTHER_STATE
        Returns:
            NFA[OTHER_STATE]
        """
        q0 = f(self.q0)

        K = { f(x) for x in self.K}

        d = { (f(x), c) : {f(z) for z in self.d[(x,c)]} for (x, c), y in self.d.items()}

        F = { f(x) for x in self.F}

        return NFA(self.S, K, q0, d, F)
    

