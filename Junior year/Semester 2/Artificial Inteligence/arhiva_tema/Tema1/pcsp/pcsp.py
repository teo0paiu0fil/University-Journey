'''
This module implements the partial constraint satisfaction problem (PCSP) algorithm.
'''
from copy import deepcopy


best_solution = {}
best_cost = 0
iterations = 0

def get_constraints(var : tuple, constraints: list) -> list:
    '''
    Get the constraints that contain the variable.
    '''
    constrains_ret = []
    for constrain in constraints:
        vars, _, _ = constrain
        if var in vars:
            constrains_ret.append(constrain)

    return constrains_ret

def fixed_constraints(solution : dict, constraints : list) -> list:
    '''
    Get the constraints that can be checked with the current solution.
    '''
    constrains_ret = []

    for constrain in constraints:
        vars, _, _ = constrain
        check = True
        
        for var in vars:
            if var not in solution.keys():
                check = False

        if check:
            constrains_ret.append(constrain)

    return constrains_ret

def check_constraint(solution : dict, constraint : tuple) -> bool:
    '''
    check if the constraint is satisfied
    '''
    vars, function, special = constraint
    args = []
    
    if special:
        for var in vars:
            day, interval, room = var
            args.append((solution[var][0], room))
    else:
        for var in vars:
            args.append(solution[var])
            
    return function(*args)

def PCSP(vars : list[tuple], domains : dict, constraints : list, acceptable_cost : int, solution : dict, cost : int) -> bool:
    '''
    The implementation of the partial constraint satisfaction problem (PCSP) algorithm.
    '''
    global best_solution
    global best_cost
    global iterations
    
    if iterations == 30000:
        if best_cost >= cost:
            best_cost = cost
            best_solution = solution
        return cost <= acceptable_cost
    
    if not vars:
        # Dacă nu mai sunt variabile, am ajuns la o soluție mai bună
        print("New best: " + str(cost) + " - " + str(solution))
        best_solution = solution
        best_cost = cost

        return cost <= acceptable_cost
    elif not domains[vars[0]]:
        # Dacă nu mai sunt valori în domeniu, am terminat căutarea
        return False
    elif cost == best_cost:
        # Dacă am ajuns deja la un cost identic cu cel al celei mai bune soluții, nu mergem mai departe
        return False
    else:
        # Luăm prima variabilă și prima valoare din domeniu
        var = vars[0]
        val = domains[var].pop(0)
        iterations += 1
        
        new_solution = {}
        new_solution.update(solution)
        new_solution[var] = val

        list_constraints = fixed_constraints(new_solution, get_constraints(var, constraints))
        new_cost = cost
        
        for constraint in list_constraints:
            if not check_constraint(new_solution, constraint):
                new_cost += 1

        # Verificăm dacă noul cost este mai mic decât cel mai bun cost
        if new_cost < best_cost and new_cost <= acceptable_cost:
            new_domains = deepcopy(domains)
            new_domains.pop(var)
            # Dacă noul cost este mai mic decât cel mai bun cunoscut, rezolvăm pentru restul variabilelor
            if PCSP(vars[1:], new_domains, constraints, acceptable_cost, new_solution, new_cost):
                return True
            # Dacă apelul recursiv întoarce True, a fost găsită o soluție suficient de bună, deci întoarcem True
        return PCSP(vars, domains, constraints, acceptable_cost, solution, cost)
