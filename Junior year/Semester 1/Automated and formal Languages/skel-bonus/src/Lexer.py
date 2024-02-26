from .Regex import parse_regex
from .NFA import NFA
from .DFA import DFA
from dataclasses import dataclass

EPSILON = ''

@dataclass
class Lexer:
    dfa : DFA[frozenset[str]]
    final : list[str]
    """
        Initialization of a Lexer DFA from a set of specifications
    """
    def __init__(self, spec: list[tuple[str, str]]) -> None:
        S : set[str] = set()
        q0 : str = "S"
        K : set[str] = set(q0)
        D : dict[tuple[str, str], set(str)] = {}
        F : set[str] = set()

        self.final = []

        for token, regex in spec:
            nfa_aux = parse_regex(regex).thompson().remap_states(lambda x: f'{token} {x}')
            S.update(nfa_aux.S)
            K.update(nfa_aux.K)
            if D.get((q0, EPSILON)):
                D.get((q0, EPSILON)).add(nfa_aux.q0)
            else:
                D[(q0, EPSILON)] = {nfa_aux.q0}
            D.update(nfa_aux.d)

            final_state = nfa_aux.F.pop()
            F.add(final_state)
            self.final.append(final_state)

        self.dfa = NFA(S, K, q0, D, F).subset_construction()

    """
        This function tokenizes a input text base of the specification of the object
    """
    def lex(self, word: str) -> list[tuple[str, str]] | None:
        tokens : list[tuple[str, str]] = []
        curr : int = 0
        line : int  = 0
        character : int = 0

        while curr < len(word):
            state = self.dfa.q0
            pos = curr

            possible_match = None

            while pos < len(word):
                if (state, word[pos]) not in self.dfa.d:
                    return [("", f'No viable alternative at character {pos - character}, line {line}')]
                
                state = self.dfa.d[(state, word[pos])]

                if state in self.dfa.F:
                    minim = 10000
                    for states in state:
                        if states in self.final and minim > self.final.index(states):
                            token = states.split()[0]
                            minim = self.final.index(states)

                    possible_match = (token, word[curr : pos + 1])
                    last_matched_position = pos + 1

                if not state:
                    if not possible_match:
                        return [("", f'No viable alternative at character {pos - character}, line {line}')]
                    curr = last_matched_position
                    break
                
                if word[pos] == "\n":
                    line += 1
                    character = pos + 1
            
                pos += 1

            if not possible_match:
                return [("", f'No viable alternative at character EOF, line {line}')]

            tokens.append(possible_match)
            
            if pos == len(word):
                break
        
        return tokens
