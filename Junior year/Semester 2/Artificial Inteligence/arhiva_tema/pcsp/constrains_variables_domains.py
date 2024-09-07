'''
Module for constraining the problem parameters.
'''
import utils
from functools import reduce
import random

##################### MACROURI #####################
INTERVALE = 'Intervale'
ZILE = 'Zile'
MATERII = 'Materii'
PROFESORI = 'Profesori'
SALI = 'Sali'
CONSTRANGERI = 'Constrangeri'
CAPACITATE = 'Capacitate'

def get_constrains(timetable_specs : dict, variables: list[tuple]) -> list:
    '''
    Extracting and creating the constrains from the specifications.
    :param timetable_specs: the specifications of the timetable
    :return: a dictionary with the constrains
    '''
    constrains = []
    
    # add hard constrains:
    # a teacher can't be in two rooms at the same time
    for var1 in variables:
        day1, interval1, room1 = var1
        for var2 in variables[:variables.index(var1)]:
            day2, interval2, room2 = var2
            
            if day1 == day2 and interval1 == interval2 and room1 != room2:
                constrains.append(([var1, var2], lambda x, y: x[0] != y[0] if x[0] is not None or y[0] is not None else True, False))
                

    # add hard constrains:
    # all students of that course need to take the course
    def check_students_course(values, course):
        every_room_for_course = [timetable_specs[SALI][value[1]][CAPACITATE] for value in values if value[0] == course]
        sum = 0
        for capacity in every_room_for_course:
            sum += capacity
        
        return sum >= timetable_specs[MATERII][course]
    
    for course in timetable_specs[MATERII]:
        constrains.append((variables, lambda *x, course=course: check_students_course(x, course), True))
    
    # add hard constrains:
    # a teacher can't work more then 7 intervals
    for teacher in timetable_specs[PROFESORI]:
        constrains.append((variables, lambda *x, teacher=teacher: len([var for var in x if var[0] == teacher]) <= 7, False))
    
    # add soft constrains:
    # a teacher dosen't want to work in a specific day or interval
    for teacher in timetable_specs[PROFESORI]:
        teacher_constraints = timetable_specs[PROFESORI][teacher][CONSTRANGERI]
        
        for constraint in teacher_constraints:
            if constraint[0] == '!':
                constraint = constraint[1:]

                if constraint in timetable_specs[ZILE]:
                    vars = [variable for variable in variables if variable[0] == constraint]
                    for var in vars:
                        constrains.append(([var], lambda x, teacher=teacher: x[0] != teacher, False))
                
                else:
                    intervals = constraint.split('-')
                    start, end = int(intervals[0].strip()), int(intervals[1].strip())

                    if start != end - 2:
                        intervals = [(i, i + 2) for i in range(start, end, 2)]
                    else:
                        intervals = [(start, end)]
                        
                    vars = [variable for variable in variables if variable[1] in intervals]
                    for var in vars:
                        constrains.append(([var], lambda x, teacher=teacher: x[0] != teacher, False))
            else:
                continue
    
        
    return constrains
    
def get_variables(timetable_specs : dict) -> list[tuple]:
    '''
    Extracting and creating the variables from the specifications.
    :param timetable_specs: the specifications of the timetable
    :return: a list with the variables represents as tuples as (day, interval, room)
    '''
    
    # the vars are the combinations of the days, intervals and rooms
    vars = [(day, eval(interval), room)
            for day in timetable_specs[ZILE]
            for room in timetable_specs[SALI]
            for interval in timetable_specs[INTERVALE]
            ]
    
    return vars

def get_domaines(timetable_specs : dict, variables: list[tuple]) -> dict:
    '''
    Extracting and creating the domains from the specifications.
    :param timetable_specs: the specifications of the timetable
    :param variables: the variables
    :return: a dictionary with the domains of every variable
    '''
    domains = {var : [] for var in variables}
    
    # the domains are the combinations of the courses and teachers that can be teached in that room
    # and is a teacher for that course
    for var in domains:
        day, interval, room = var
        domains[var] = [(teacher, course)
            for teacher in timetable_specs[PROFESORI]
            for course in timetable_specs[PROFESORI][teacher][MATERII]
            if course in timetable_specs[SALI][room][MATERII]
        ]
        
        domains[var].append((None, None))
        domains[var] = list(set(domains[var]))
   
    return domains

def get_all(timetable_specs : dict) -> tuple:
    '''
    Extracting and creating the constrains, variables and domains from the specifications.
    :param timetable_specs: the specifications of the timetable
    :return: a tuple with the constrains, variables and domains
    '''
    variables = get_variables(timetable_specs)
    constrains = get_constrains(timetable_specs, variables)
    domains = get_domaines(timetable_specs, variables)
    
    return constrains, variables, domains


