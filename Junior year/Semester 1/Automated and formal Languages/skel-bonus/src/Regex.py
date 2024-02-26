from .NFA import NFA
from dataclasses import dataclass

EPSILON = ''  # this is how epsilon is represented by the checker in the transition function of NFAs

class Regex:
    def thompson(self) -> NFA[int]:
        raise NotImplementedError('the thompson method of the Regex class should never be called')

@dataclass
class RegexConcat(Regex):
    """
    This class is the reprezentation and of the concatenation
    of two regexs
    """
    left : Regex
    right : Regex

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that is reprezented by the thomson
        algorithm
        """
        nfa_left = self.left.thompson()
        nfa_right = self.right.thompson()

        final_state_left = nfa_left.F.pop()

        nfa_right = nfa_right.remap_states(lambda x: x + final_state_left + 1)
        
        nfa_left.S.update(nfa_right.S)
        nfa_left.K.update(nfa_right.K)

        nfa_left.d.update(nfa_right.d)
        nfa_left.d[(final_state_left, EPSILON)] = set([nfa_right.q0])

        nfa_left.F = nfa_right.F
        
        return nfa_left

@dataclass
class RegexUnion(Regex):
    """
    This class is the reprezentation and of the union
    of two regexs
    """
    left : Regex
    right : Regex

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that is reprezented by the thomson
        algorithm
        """
        nfa_left = self.left.thompson()
        nfa_right = self.right.thompson()

        nfa_left = nfa_left.remap_states(lambda x: x + 1)

        final_state_left = nfa_left.F.pop()
        final_state_right = nfa_right.F.pop()

        nfa_right = nfa_right.remap_states(lambda x: x + final_state_left + 1)

        nfa_left.S.update(nfa_right.S)
        nfa_left.K.update(nfa_right.K)
        nfa_left.d.update(nfa_right.d)

        nfa_left.d[(0, EPSILON)] = set([nfa_right.q0, nfa_left.q0])

        final_state = final_state_left + final_state_right + 2
        
        nfa_left.K.add(0)
        nfa_left.K.add(final_state)
        nfa_left.F.add(final_state)
        nfa_left.q0 = 0

        nfa_left.d[(final_state_left, EPSILON)] = set([ final_state])
        nfa_left.d[(final_state - 1, EPSILON)] = set([ final_state])

        return nfa_left
        
@dataclass
class RegexStar(Regex):
    """
    This class is the reprezentation and of the star operator
    on one regex
    """
    center : Regex

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that is reprezented by the thomson
        algorithm
        """
        nfa_center = self.center.thompson()
        nfa_center = nfa_center.remap_states(lambda x : x + 1)
        
        nfa_final = nfa_center.F.pop()

        nfa_center.d[(0, EPSILON)] = set([nfa_center.q0, nfa_final + 1])
        nfa_center.d[(nfa_final, EPSILON)] = set([nfa_final + 1, nfa_center.q0])

        nfa_center.K.add(0)
        nfa_center.K.add(nfa_final + 1)

        nfa_center.F.add(nfa_final + 1)

        nfa_center.q0 = 0

        return nfa_center

@dataclass
class RegexCharacter(Regex):
    """
    This class is the reprezentation and of single characther on
    on a regex basis
    """
    characters : str

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that is reprezented by a single tranzition 
        on the provided characters
        """
        S : set[str] = set([c for c in self.characters])
        K : set[int] = set()
        q0 : int = 0
        D : dict[tuple[int, str], set(int)] = {}
        F : set[int] = set()

        K.add(q0)

        for c in self.characters:
            D[(q0, c)] = set([q0 + 1])

        K.add(q0 + 1)
        F.add(q0 + 1) 

        return NFA(S, K, q0, D, F)

@dataclass
class RegexPlus(Regex):
    """
    This class is the reprezentation and of the concatenation
    of two regexs
    """
    center: Regex

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that is reprezented by the thomson
        algorithm for the star but the initial epsilon tranzition from initial state and final
        state is gone forcing the aparition of the regex at least one time
        """
        nfa_center = self.center.thompson()
        nfa_center = nfa_center.remap_states(lambda x : x + 1)

        
        nfa_final = nfa_center.F.pop()

        nfa_center.d[(0, EPSILON)] = set([nfa_center.q0])
        nfa_center.d[(nfa_final, EPSILON)] = set([nfa_final + 1, nfa_center.q0])

        nfa_center.K.add(0)
        nfa_center.K.add(nfa_final + 1)

        nfa_center.F.add(nfa_final + 1)

        nfa_center.q0 = 0

        return nfa_center

@dataclass
class RegexQuestion(Regex):
    """
    This class is the reprezentation and of the question 
    operator on one regex
    """
    center : Regex

    def thompson(self) -> NFA[int]:
        """
        This function returns a nfa that with an additionaly tranzition from initial
        state to final state makeing the aparition of the regex optionaly 
        """
        nfa_center = self.center.thompson()

        if nfa_center.d.get((nfa_center.q0, EPSILON)):
            group : set(int) = nfa_center.d[(nfa_center.q0, EPSILON)]
            group.update(nfa_center.F)
            nfa_center.d[(nfa_center.q0, EPSILON)] = group
        else:
            nfa_center.d[(nfa_center.q0, EPSILON)] = nfa_center.F

        return nfa_center
    
def parse_regex(regex: str) -> Regex:
    """
        This method parses a string to a regex object
        Args:
            regex (str): the string reprezenting the regex
        Returns:
            Regex (obj) - the object interpretetion of a regex
    """
    priority = {"+" : 6, "*" : 6, "?" : 6, "|" : 2, "(" : 8, ")" : 0, "concat" : 3}
    out_queue, stack_op = [], []

    i = 0

    while i < len(regex):
        curr_char = regex[i]

        if not curr_char in priority.keys() and curr_char != " ":
            if curr_char == "[":
                reg = parse_syntactic_sugars(regex[i:i+5])
                if reg:
                    out_queue.append(reg)
                    i += 4
            elif curr_char == "\\":
                i += 1
                out_queue.append(RegexCharacter(regex[i]))
            else:
                out_queue.append(RegexCharacter(curr_char))
            if i + 1 < len(regex):
                if (not regex[i + 1] in priority.keys() or regex[i + 1] == "(") and regex[i + 1] != " ":
                    stack_op, out_queue = check_and_insert(stack_op, out_queue, priority, "concat")
                    
        elif curr_char in priority.keys():
            stack_op, out_queue = check_and_insert(stack_op, out_queue, priority, curr_char)
            if curr_char in ["*", "?", "+", ")"] and i + 1 < len(regex):
                next_char = regex[i + 1]
                j = i + 1
                
                while next_char == " ":
                    next_char = regex[j]
                    j += 1
                
                if (not next_char in priority.keys() or next_char == "(") and next_char != " ":
                    stack_op, out_queue = check_and_insert(stack_op, out_queue, priority, "concat")
        i += 1

    while stack_op:
        pop_regex = stack_op.pop()
        out_queue.append(pop_regex)

    return postfix_solver(out_queue)

def check_and_insert(stack : list, queue : list, priority : dict[str, int], curr_char : str) -> tuple[list]:
    """
    """
    while stack:
        stack_top = stack[-1]
        if priority[curr_char] < priority[stack_top]:
            if curr_char != ')':
                if stack_top != '(':
                    stack.pop()
                    queue.append(stack_top)
                else:
                    break
            else:
                if stack_top != '(':
                    stack.pop()
                    queue.append(stack_top)
                else:
                    stack.pop()
                    break
        else:   
            break

    if curr_char != ')':
        stack.append(curr_char)

    return (stack, queue)

def postfix_solver(queue : list) -> Regex:
    """
    """
    stack = []

    while queue:
        element = queue[0]
        queue = queue[1:]
        
        if isinstance(element, RegexCharacter):
            stack.append(element)
        else:
            operator1 = stack.pop()

            match element:
                case "+":
                    stack.append(RegexPlus(operator1))
                case "?":
                    stack.append(RegexQuestion(operator1))
                case "*":
                    stack.append(RegexStar(operator1))
                case "|":
                    operator2 = stack.pop()
                    stack.append(RegexUnion(operator2, operator1))
                case "concat":
                    operator2 = stack.pop()
                    stack.append(RegexConcat(operator2, operator1))
                case _:
                    raise ValueError("Operation unknown")

    return stack.pop()

def parse_syntactic_sugars(regex : str) -> RegexCharacter:
    """
    """
    start = regex[1]
    assert regex[2] == "-"
    end = regex[3]
    assert regex[4] == "]"

    if start == "a" and end == "z":
        return RegexCharacter("abcdefghijklmnopqrstuvwxyz")
    elif start == "A" and end == "Z":
        return RegexCharacter("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    elif start == "0" and end == "9":
        return RegexCharacter("0123456789")
    return None
