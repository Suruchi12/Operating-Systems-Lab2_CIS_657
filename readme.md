# Creation of Peroxide in JNachos

#### Operating Systems
#### CIS 657
#### Lab 2

Implemented Peroxide.java
IDE -IntelliJ
Sample output :-output.txt
To run water uncomment water() in main.java

Similar to water class   we have created a new class Peroxide.java
In this we have implemented synchronization program for the generation of Peroxide

The first H atom waits for the second H atom to be combined together.Likewise using the same concept we
 have made sure that the first O atom created will wait until the second O atom is created.This is done by P() and V() methods
Along with this we also have to make sure that there is equal number of H and O atoms required to create Hydrgoen Peroxide

P() waits for the value of semaphore to be greater than zero and then decrements it. If the value is lesser than 0, 
it adds the last process in the queue and puts that process to sleep.

V() wakes up a thread which is waiting in the queue and makes it ready to run if the queue is not empty. It also increments the value of the semaphore.

