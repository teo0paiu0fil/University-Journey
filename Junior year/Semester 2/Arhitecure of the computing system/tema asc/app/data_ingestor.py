'''
The data module is responsible for opening and retreaving the data
'''
import pandas as pd

class DataIngestor:
    '''
    an capsulation of "the database file"
    '''
    def __init__(self, csv_path: str):
        '''
        @param csv_path - the path to the csv file
        '''
        self.data = pd.read_csv(csv_path)

    def retreave_data(self):
        '''
        getter for the data atributte
        '''
        return self.help_getter()

    def help_getter(self):
        '''
        helper getter for the data atributte
        '''
        return self.data
