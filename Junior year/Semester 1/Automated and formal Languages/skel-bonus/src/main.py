from sys import argv
from .Lexer import Lexer
from dataclasses import dataclass

class ASTNode:
    pass

class NumberNode(ASTNode):
    def __init__(self, value):
        self.value = value

    def eval(self):
        return self.value
    
    def __str__(self):
        return f'{self.value}'
    
    def __repr__(self):
        return f'{self.value}'

class IdNode(ASTNode):
    def __init__(self, name):
        self.name = name
        
    def eval(self):
        return self.name
    
    def __str__(self):
        return f'{self.name}'
    
    def __repr__(self):
        return f'{self.name}'

class LambdaNode(ASTNode):
    def __init__(self, parameter, body):
        self.parameter = parameter
        self.body = body

    def eval(self, args):
        aux = self.eval_helper(args, self.parameter)
        return aux
    
    def eval_helper(self, args, parameter, still_in_lambda_check = False, level = 0):
        def replace(args, nodes, parameter):
            aux = []
            for node in nodes:
                if isinstance(node, IdNode):
                    if node.eval() == parameter.eval():
                        aux.append(args)
                    else:
                        aux.append(node)
                elif isinstance(node, ListNode):
                    aux.append(replace(args, node.nodes, parameter))
                elif isinstance(node, list):
                    aux.append(replace(args, node, parameter))
                else:
                    aux.append(node)
            return aux
        
        if isinstance(self.body, LambdaNode):
            if parameter.eval() == self.body.parameter.eval():
                if level == 0:
                    return self.body
                return self
            else:
                self.body = self.body.eval_helper(args, parameter, True, level + 1)
                if level == 0: 
                    return self.body
                return self
        
        if isinstance(self.body, IdNode):
            if still_in_lambda_check:
                if parameter.eval() == self.body.eval():
                    self.body = args
                return self
            return args
        
        if isinstance(self.body, ListNode):
            aux = []

            for i, node in enumerate(self.body.nodes):
                if isinstance(node, FunctionNode):
                    for elem in self.body.nodes[1].eval():
                        if not isinstance(elem, IdNode):
                            aux.append(elem)
                        else:
                            aux.append(args)
                            
                    self.body.nodes[1].nodes = aux

                    if level == 0:
                        return self.body.nodes[0].eval(self.body.nodes[1].eval())
                    else:
                        return self.body
                    
            self.body.nodes = replace(args, self.body.nodes, parameter)

            if still_in_lambda_check:
                return self
            
            return self.body
        
        if level == 0:  
            return self.body
        else: 
            return self
    
    def __str__(self):
        return f'lambda {self.parameter} : {self.body} '
    
    def __repr__(self):
        return f'lambda {self.parameter} : {self.body} '

class FunctionNode(ASTNode):
    def __init__(self, func):
        self.func = func
        
    def sum(self, elements):
        if isinstance(elements, list):
            aux = 0
            for element in elements:
                if isinstance(element, ASTNode):
                    to_sum = element.eval()
                else:
                    to_sum = element

                if isinstance(to_sum, list):
                    aux += self.sum(to_sum)
                else:
                    aux += to_sum
            return aux
        else:
            return elements.eval()
    
    def concat(self, elements):
        toBeReturned = []
        for element in elements:
            if isinstance(element, ListNode):
                for e in element.nodes:
                    toBeReturned.append(e.eval())
            elif isinstance(element, list):
                toBeReturned.extend(element)
            else:
                toBeReturned.append(element)
        return toBeReturned
    
    
    def eval(self, args):
        if self.func == "+":
            return NumberNode(self.sum(args))
        elif self.func == "++":
            return ListNode(self.concat(args))

class ListNode(ASTNode):
    def __init__(self, nodes):
        self.nodes = nodes

    def eval(self):
        if self.nodes:
            aux = []
            for node in self.nodes:
                aux.append(node)
            return aux
        else:
            return []
        
    def __str__(self):
        return f'{self.nodes}'
    def __repr__(self):
        return f'{self.nodes}'

def main():
    if len(argv) != 2:
        return

    filename = argv[1]
    # i open and read the program
    with open(filename) as file:
        program = file.read().strip()

    # the specification for every TOKEN in the language
    spec = [
        ("EMPTY_LIST", "\\(\\)"),
        ("WS", "(\\ |\t|\n)+"),
        ("NUMBER", "[0-9]+"),
        ("ID", "([A-Z] | [a-z])+"),
        ("OP", "\\+\\+ | \\+"),
        ("LAMBDA", "lambda\\ "),
        ("LAMBDA_EXP", ":"),
        ("OPEN_PAR", "\\("),
        ("CLOSE_PAR", "\\)"),
    ]

    lexer = Lexer(spec)
    rez = [(token, val) for (token, val) in lexer.lex(program) if token != "WS"]
    ast = parse_tokens(rez)
    rez = evaluate(ast)
    print(rez_to_string(rez))

def evaluate(ast_tree):
    aux = parse_ast(ast_tree)
    return parse_ast(aux)

def parse_ast(ast):
    if isinstance(ast, ListNode):
        if not ast.nodes:
            return ast
        if isinstance(ast.nodes[0], FunctionNode):
            aux = parse_ast(ast.nodes[1])
            if isinstance(aux, ListNode):
                aux = aux.eval()
            return ast.nodes[0].eval(aux)
        elif isinstance(ast.nodes[0], LambdaNode):
            if isinstance(ast.nodes[1], LambdaNode):
                return ast.nodes[0].eval(ast.nodes[1])
            return ast.nodes[0].eval(ast.nodes[1].eval())
        
        aux = []
        for i, node in enumerate(ast.nodes):
            new_node = parse_ast(node)
            
            if isinstance(new_node, ListNode):
                new_node = new_node.eval()
                
            aux.append(new_node)
            if isinstance(aux[0], LambdaNode):
                new_node = aux[0].eval(ast.nodes[i + 1])
                return new_node
            
        return ListNode(aux)
    elif isinstance(ast, NumberNode):
        return ast.eval()
    elif isinstance(ast, LambdaNode):
        return ast
    elif isinstance(ast, int):
        return ast
    elif isinstance(ast, list):
        if not ast:
            return ast
        if isinstance(ast[0], FunctionNode):
            aux = parse_ast(ast[1])
            if isinstance(aux, ListNode):
                aux = aux.eval()
            return ast[0].eval(aux[1])
        elif isinstance(ast[0], LambdaNode):
            if isinstance(ast[1], LambdaNode):
                return ast[0].eval(ast[1])
            if isinstance(ast[1], ASTNode):
                return ast[0].eval(ast[1].eval())
            else:
                return ast[0].eval(ast[1])

        aux = []
        for i, node in enumerate(ast):
            new_node = parse_ast(node)
            
            if isinstance(new_node, ListNode):
                new_node = new_node.eval()
                
            aux.append(new_node) 
            if isinstance(aux[0], LambdaNode) and i == 1:
                new_node = aux[0].eval(new_node)
                return new_node
            
        return aux

def parse_tokens(tokens):
    if not tokens:
        return None

    token = tokens.pop(0)

    if token[0] == 'LAMBDA':
        parameter = IdNode(tokens.pop(0)[1])
        tokens.pop(0)  # LAMBDA_EXP ":"
        body = parse_tokens(tokens)
        return LambdaNode(parameter, body)
    elif token[0] == 'ID':
        return IdNode(token[1])
    elif token[0] == 'OPEN_PAR':
        elements = []
        while tokens[0][0] != 'CLOSE_PAR':
            element = parse_tokens(tokens)
            if element != None:
                elements.append(element)
        tokens.pop(0)  # Consume the ')'
        return ListNode(elements)
    elif token[0] == 'NUMBER':
        return NumberNode(int(token[1]))
    elif token[0] == 'OP':
        return FunctionNode(token[1])
    elif token[0] == 'EMPTY_LIST':
        return ListNode([])

def rez_to_string(lst):
    if isinstance(lst, ASTNode):
        return rez_to_string(lst.eval())
    
    if not lst and isinstance(lst, list):
        return "()"
    elif isinstance(lst, list):
        result = "( "
        for item in lst:
            if isinstance(item, list):
                result += rez_to_string(item)
            else:
                result += str(item)
            result += " "
        result = result + ")"
        return result
    else:
        return str(lst)

def print_ast(ast, i):
    if ast:
        if isinstance(ast, list):
            for a in ast:
                print_ast(a, i)
        elif isinstance(ast, ListNode):
            print(i * "\t", "Nod Lista:")
            print(i * "\t", "  Membri:")
            print_ast(ast.nodes, i + 1)
        elif isinstance(ast, NumberNode):
            print(i * "\t", "Nod Numeric:")
            print(i * "\t", "  Valuare: ", ast)
        elif isinstance(ast, FunctionNode):
            print(i * "\t", "Nod Functie:")
            print(i * "\t", "  Functie: ", ast.func)
        elif isinstance(ast, LambdaNode):
            print(i * "\t", "Nod Lambda:")
            print(i * "\t", "  Parametru: ", ast.parameter)
            print(i * "\t", "  Corp: ")
            print_ast(ast.body, i + 1)
        else:
            print(i * "\t", "  ", ast)

if __name__ == '__main__':
    main()
