'''
The representation of the state of the current problem.
'''
from __future__ import annotations
from copy import deepcopy
import time
import random

##################### MACROURI #####################
INTERVALE = 'Intervale'
ZILE = 'Zile'
MATERII = 'Materii'
PROFESORI = 'Profesori'
SALI = 'Sali'
CAPACITATE = 'Capacitate'
CONSTRANGERI = 'Constrangeri'


def change_constrains(timetable_specs : dict):
    '''
    Change the constrains from the format:
        !day
        !interval
    to the format:
        {
            'days': [day1, day2, ...],
            'intervals': [(start1, end1), (start2, end2), ...]
        }
    '''
    for teacher in timetable_specs[PROFESORI]:
        # fiter only the constrains that are starting with '!'
        constrains = [ 
                x[1:] for x in list(filter(lambda x : x[0] == '!', timetable_specs[PROFESORI][teacher][CONSTRANGERI]))
            ]
        
        intervals = []
        days =  []
        
        for constrain in constrains:
            # if it is an interval
            if '-' in constrain:
                interval = constrain.split('-')
                start, end = int(interval[0].strip()), int(interval[1].strip())
                # compute the interval in 2 hours intervals
                if start != end - 2:
                    intervals.extend((i, i + 2) for i in range(start, end, 2))
                else:
                    intervals.append((start, end))
            else:
                # it is day
                days.append(constrain)
        # modify the specs format
        timetable_specs[PROFESORI][teacher][CONSTRANGERI] = {
            'days': days,
            'intervals': intervals
        }

class State:
    def __init__(self, timetable_specs : dict, timetable : dict = None, nconflicts : tuple(int, int) = None):
        '''
        The representation of the state of the current problem.
        '''
        self.timetable_specs = timetable_specs
        
        
        # a empty timetable represents the initial state
        self.timetable = timetable if timetable is not None else \
            {day : {eval(interval) : { room : None for room in timetable_specs[SALI]} 
                for interval in timetable_specs[INTERVALE]}
                for day in timetable_specs[ZILE]}
        
        self.nconflicts = nconflicts if nconflicts is not None else self.__compute_conflicts()
    
    def clone(self) -> State:
        '''
        A deep copy of the current state.
        '''
        return State(self.timetable_specs, deepcopy(self.timetable), self.nconflicts)
    
    def __compute_conflicts(self) -> tuple(int, int):
        '''
        Computes the number of conflicts.
        '''
        
        # cacheing the timetable and his specifications
        timetable_specs = self.timetable_specs
        timetable = self.timetable
        
        broken_hard_constrains, broken_soft_constrains = (0, 0)
        
        # the number of students enroled for each subject
        the_students_number_needed = timetable_specs[MATERII]
        
        # here we store the number of students allocated to each subject
        the_students_number_allocated = {course : 0 for course in the_students_number_needed}
        # here we store the number of hours allocated to each teacher
        teachers_intervals = {teacher : 0 for teacher in timetable_specs[PROFESORI]}
        
        for day in timetable:
            for interval in timetable[day]:
                # here we store the teachers that are teaching in the current interval 
                # to check if a teacher is teaching two courses in the same interval
                teachers_in_crt_interval = set()
                
                for room in timetable[day][interval]:
                    if timetable[day][interval][room]:
                        # adding the number of students allocated to the subject to the total number of students
                        # for this course
                        teacher, course = timetable[day][interval][room]
                        the_students_number_allocated[course] += timetable_specs[SALI][room][CAPACITATE]

                        # if the teacher is already teaching in the current interval, then we have a conflict
                        if teacher in teachers_in_crt_interval:
                            broken_hard_constrains += 1
                        else:
                            teachers_in_crt_interval.add(teacher)
                        
                        # if the teacher has constraints on the day or on the interval, then we have a conflict
                        if day in timetable_specs[PROFESORI][teacher][CONSTRANGERI]['days']:
                            broken_soft_constrains += 1
                              
                        if interval in timetable_specs[PROFESORI][teacher][CONSTRANGERI]['intervals']:
                            broken_soft_constrains += 1
                            
                        # adding the number of intervals allocated to the teacher
                        # if the number of intervals allocated to a teacher is greater than 7, then we have a conflict
                        if teacher in teachers_intervals:
                            teachers_intervals[teacher] += 1
                            
                            if teachers_intervals[teacher] > 7:
                                broken_hard_constrains += 1
                                teachers_intervals.pop(teacher)
                        
        # if the number of students allocated to a course is less than the number of students needed, then we have a conflict
        for course in the_students_number_needed:
            if the_students_number_allocated[course] < the_students_number_needed[course]:
                broken_hard_constrains += 1

        # return a tuple of the constrains to be sure to not have hard constrains broken
        return broken_hard_constrains, broken_soft_constrains
        
    def conflicts(self) -> tuple(int, int):
        # returns the conficts of this state
        return self.nconflicts
    
    def is_final(self) -> bool:
        # returns true if the state is final
        return self.nconflicts == (0, 0)
    
    def get_next_states(self) -> list[State]:
        '''
        Returns a list of all the possible states that can be reached from the current state.
        '''
        # cacheing the timetable specifications
        timetable_specs = self.timetable_specs
        timetable = self.timetable
        
        new_states = []
        
        # choose a random day for the next state
        # this step increases the speed of the algorithm
        # becouse we are limiting the search space
        reduce = 2 if len(timetable_specs[ZILE]) > 3 else 3
        days = random.sample(timetable_specs[ZILE], reduce)
        
        # we iterate through all the intervals and rooms in that days
        for day in days:
            for interval in timetable[day]:
                for room in timetable_specs[SALI]:
                        # and we check if the room is empty
                        if not timetable[day][interval][room]:
                            # if it is we compute every posible combination of teacher and course
                            # that can teach in that room
                            for teacher in timetable_specs[PROFESORI]:
                                for course in timetable_specs[PROFESORI][teacher][MATERII]:
                                    if course in timetable_specs[SALI][room][MATERII]:
                                        next_state = self.clone()
                                        next_state.timetable[day][interval][room] = (teacher, course)
                                        next_state.nconflicts = next_state.__compute_conflicts()
                                        new_states.append(next_state)
                        else:
                            # if the room is not empty, we remove the teacher and the course from the room
                            # for adding more space of search at the next iteration
                            next_state = self.clone()
                            next_state.timetable[day][interval][room] = None
                            next_state.nconflicts = next_state.__compute_conflicts()
                            new_states.append(next_state)
                            
        return new_states


    def get_timetable(self) -> dict:
        # return the timetable
        return self.timetable