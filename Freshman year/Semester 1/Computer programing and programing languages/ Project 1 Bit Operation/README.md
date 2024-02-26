# ACS_PCLP_First_Homework

The topic of the homework was working with bit operations and for this a bit interpreter similar to a processor had to be implemented. It will have the ability to decode and execute simple addition, subtraction, multiplication and division instructions.

## First Task - Decoding instruction

Given an instruction in binary format, decode the instruction.

![image](https://github.com/teo0paiu0fil/ACS_PCLP_First_Homework/blob/main/format.png)

Where:

>N represents the number of instructions to be executed; it is represented on 3 bits and is obtained by converting the value of the most significant 3 bits from binary to decimal and adding 1. Thus, for 000 we will have to execute one instruction, for 010 we will have to execute 3 instructions. The maximum number of instructions to execute is 8.

> Op represents the code of an instruction and is represented by 2 bits. Op can be +, -, * or / according to the table below:
```sh
Operation Code
00 +
01 -
10 *
11 /
```
>In the input string, the 3 bits designating the number of operations are followed by the number of N*2 bits designating the operations to be executed.

> Dim represents the size of an operand and is represented by 4 bits. Dim is calculated similarly to N by converting the least significant 4 bits to the decimal value and adding by 1. Thus, the size of the operands can take values ​​in the range [1, 16].

>In this task, you will read from the standard input a number of type unsigned int that contains the instruction and you will decode it. Thus, on the standard output you will display N, the operators and the size of the operands, all separated by a space.



## Secound Task - Instruction execution

In this exercise we will continue the previous task by:

1. Reading the operands. Starting from the previous program, add a section of code that interprets (N+1) operands from standard input. For this task, the size of the operands (Dim) is a power of 2 number in the range [1, 16]. That is, the possible values are: 1, 2, 4, 8, 16. The operands will be read in the form of unsigned short numbers (size 16) from the standard input. The number of operands read from the keyboard will be broken down into several unsigned short numbers, using the formula: ((N+1)*Dim)/16, to which we add +1 if the result has any remainder. Thus, ((N+1)*Dim)/16 numbers will be read from the keyboard and will be decomposed into (N+1) operands.

2. Execution of the instruction: since we have both the operations and the operands, all that remains is to calculate the result. The calculation of the result will be done in the order of receipt of the operations and not according to the priority of the operators (that is, * has no precedent over +).

