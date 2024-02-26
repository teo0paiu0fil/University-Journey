# First Phase - PROJECT LFA 
## Paiu Teofil 333CB
----------

### Introduction
    For this assigment I completed the necesary methods of the NFA and DFA classes.
    To respect the assigned skelet i used the type notation for every variable.

### DFA
    For the DFA class I implemented the method accept that for every character in the
    input
    

    ```
    def accept(self, word: str) -> bool:
        # simulate the dfa on the given word. return true if the dfa accepts the word, false otherwise
        curr_state = self.q0

        for c in word:
            curr_state = self.d[(curr_state, c)]

        return curr_state in self.F
    ```