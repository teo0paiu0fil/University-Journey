#!/usr/bin/env python3

import base64
import json
import gmpy2


def str_to_number(text):
    """ Encodes a text to a long number representation (big endian). """
    return int.from_bytes(text, 'big')

def number_to_str(num):
    """ Decodes a text to a long number representation (big endian). """
    num = int(num)
    return num.to_bytes((num.bit_length() + 7) // 8, byteorder='big').decode('ASCII')

def encrypt(pub_k, msg_num):
    """ We only managed to steal this function... """
    cipher_num = gmpy2.powmod(msg_num, pub_k["e"], pub_k["n"])
    # note: gmpy's to_binary uses a custom format (little endian + special GMPY header)!
    cipher_b64 = base64.b64encode(gmpy2.to_binary(cipher_num))
    return cipher_b64


if __name__ == "__main__":
    pub_k = {"e": 144523, "n": 15522773959497980926131828149468887193705366917735991299493821669385064353981487542276489079534511337384280201183669227764748197000996995844884760685832584011163237225754430765304456118266900534532470389290526899183946536505146075606220626064611447668509476460784856519589899653404222882615030176635865327975585382135441390433638570804919535084502159863695405685381530702498582192784219341385105988825424759558787152091884980266317688033223516498445853542182160796748683793502111086607822247852525282391251865401630415332804900783369253325172505355286036943473128659224280687271540866492373808119945331509890909349773}  

    cipher_b64 = "AQG5WV6dHTNgONLTrgke3Ar/U4Y3OJBDr6sIm9QAinGOCC/g4JC5oOY64aFcKxkmEWwhsRtgNgB+nLvxV2/u4/ax+EnMdL4fERtqRjPRZdyqZxj+rHG9uaQR924qy6WiB1vVTRlFI0783HkgT02pJQC3ED/dILuAISLEPnAkXteyXquDgesT8f+L+kM4FP+oeNEKtOp1vIkbotnsA9aAwBh9WITjEiqcheWGogeqQWk4qokAV23/1NWDgfPqNLlgPMwNnulVCxEo1o/LKJWp1e2tQerIj9kuCVujy0biOmica2aSgzqauqeKyMwU+B1Ini9RI7ezoDd0K0mTGKCHMQAQ"
    cipher_num = gmpy2.from_binary(base64.b64decode(cipher_b64))
    # rsa choosen cipher attack
    r = 2  
    cipher_num2 = cipher_num * gmpy2.powmod(2, pub_k['e'], pub_k['n'])
    
    pvar = { "flag" : base64.b64encode(gmpy2.to_binary(cipher_num2)).decode('utf-8')}

    message = base64.b64encode(json.dumps(pvar).encode('utf-8'))

    print(message.decode('utf-8'))

    # with that message server responded with
    response = b'\xa6\xe0\xca\xd2\xe6\xd0\x8c\xd8\xc2\xce\xf6l\x8a\xc2\xe4\x8e\xc8j\xe6\x9cp\xa8\xdcb\x84\x96\xe8\xb0\xe4\xca\x8e\xa4\xc6\xa2\xc6\x84\x92\x90b\xa8\x9c\xa2\x90\xfa' 

    response_n = str_to_number(response)
    # response_n = gmpy2.div(response_n) / 2    #  pentru un oarecare motiv nu reuseam sa impart numarul fara a intra in notatia matematica -15e care crea un padding de 0-uri  
    response_n = 2990114684129732183930581563852873755388703681198411272422590761462460352206262469083357838606279382222973
    response = number_to_str(response_n).encode('utf-8')
    print(response)


