# SecoundHomework


## Task 1 - Magic Words

  As you enter the magic temple you find yourself in a huge room in the form of a matrix of size N * M. A voice tells you that you are in his magic temple and that to get to the next room you need to move through the room (through the matrix ) following a well-established route. To make things more interesting, the voice tells you that the required route must be decoded by you and then tells you a magic code.

On the walls of the room are the instructions to understand the mystical code:

    1.A code describes the route you must follow in the room.
    2.The code can consist of any number of space-separated magic words (each word describing a single step in the route).

The words are of 3 types:

1. Words starting with the letter 'a' are of the form "ax1x2x3x4" where xi are digits (1⇐ i ⇐ 4). Each figure corresponds to a movement direction (x1 – right, x2 – up, x3 – left, x4 - down in the matrix). The maximum figure dictates the corresponding path movement.

Ex: The code "a1235" has the maximum digit (5) in the position of x4, so the next movement in the path is down.

2. Words starting with the letter 'b' are of the form "bx1x2...xn" where xi are digits (1⇐ i ⇐ n). We consider the number formed by the digits "x1x2...xn" as a number K. Let X be the number formed by the last 2 digits of K (xn-1xn). Depending on the palindrome and prime properties of K and X, respectively, we have the following 4 situations:

    1.K palindrome, X prime: left
    2.K palindrome, X is NOT prime: right
    3.K is NOT a palindrome, X prime: up
    4.K is NOT palindrome, X is NOT prime: down

Ex: The code "b121" has K = 121 and X = 21. K is a palindrome and X is not prime ⇒ the next movement in the path is to the right.

3. Words starting with the letter 'c' are of the form "cnkx1x2...xn", where n, k, xi are numbers. Let S be the sum of the first k digits taken from k to k circularly. S = x0 + xk + x2k + … (if the index goes over n then it is reset to 0). Depending on the remainder of the division of S by 4 we have the following situations:

    S % 4 = 0 ⇒ left
    S % 4 = 1 ⇒ up
    S % 4 = 2 ⇒ right
    S % 4 = 3 ⇒ down

Ex: The code "c64123456" has n = 6 (6 digits), k = 4 and the digits 123456. The desired sum is: S = x0 + x4 + x8 % 6 + x12 % 6 = 1 + 5 + 3 + 1 = 10 The rest of division is: S % 4 = 10 % 4 = 2 ⇒ the next move in the route is to the right.

## Task 2 - The strange voice

  As you exit the maze, you hear the same strange voice again, but this time you can't understand the words it says. Suddenly, you realize that the messages the voice is saying are encrypted. Being a good programmer, you decided to try to decipher them.
  
  ### Task 2.1

   The first variant you think of, the Caesar cipher, obtains an encrypted text based on a message (called plaintext) and a key (an integer) in the following way: each letter in the message is "rotated" by the value specified by the key. For example, the string "abCZ" rotated with key 3 becomes "deFC". In addition to this classic functionality, we will consider that the numbers can also "rotate". That is, the string "1239" rotated with key 3 becomes "4562". Decryption is exactly the opposite operation: starting from an encrypted text, for which the key with which it was encrypted is known, the original message can be found.
    
  ### Task 2.2 
  
  The second cipher, Vigenère, is also based on a message and a key, which this time is a string of uppercase letters. To obtain the encrypted text, the key is repeated throughout the message, and each letter in the message is "rotated" based on the alphabetical position of the corresponding letter in the key. Similar to the previous requirement, the numbers will also "rotate" according to the corresponding letter in the key.
  
  ### Task 2.3

  You realized that the voice uses the Caesar cipher to encrypt messages, so when it tells you two large numbers, you think about calculating their sum after decrypting them. So the requirement is the sum of two large numbers, which must be stored as a string. The numbers are initially encrypted using the Caesar cipher described in requirement 2.1 and must be decrypted before computing their sum.
  
## Task 3 - The Help (50p)

Since you've successfully deciphered and solved the previous challenges, the voice decides to ask for your help in completing a word prediction algorithm. The voice tells you that it already knows the frequencies of words in a text, but it needs you to calculate the number of occurrences of 2-grams, in the order of their occurrence. An n-gram represents a group of n consecutive elements (words, syllables, letters, etc.) in a text. For this task we will treat n-grams as groups of n consecutive words. A word is separated by spaces or newlines, and punctuation such as ',', '.', '!', ';' is ignored. Also, the input is case sensitive and there is no need to convert uppercase to lowercase.
