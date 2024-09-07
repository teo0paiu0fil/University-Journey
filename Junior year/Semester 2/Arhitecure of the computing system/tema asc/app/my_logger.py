'''
this module handles the capsulation of the method of configurating of the logger
'''
import logging
from logging.handlers import RotatingFileHandler

def logger_init():
    '''
    A function that configure the logger and return it
    '''
    # initializare logger
    logger = logging.getLogger('webserver_logger')
    logger.setLevel(logging.INFO)

    # Configurare RotatingFileHandler pentru a scrie în fișierul "webserver.log"
    # 2 MB
    handler = RotatingFileHandler('webserver.log', maxBytes=1024*2048, backupCount=5)
    logger.addHandler(handler)

    # Configurare formatter pentru a formata timestamp-urile în format UTC/GMT
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                                  datefmt='%Y-%m-%d %H:%M:%S', style='%')

    handler.setFormatter(formatter)

    return logger
    