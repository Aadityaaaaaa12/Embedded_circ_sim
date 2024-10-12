so far 8051 basic architecture simulation complete can only run 3 commands <br /> CLR A <br />  MOV A.#DATA <br />  MOV DIRECT,#DATA.<br /> setb, addr <br />
put asssembly code in the below format in the instructions.txt file. Current default program moves the value 56 inside the ACC and lights up the led connected to p2.1. as wells as the led at p2. and puts the number 3 on the 7 segement display<br />
0xE4<br />
0x74<br />
0x56<br />
0xd2<br />
0xA0<br />
0xd2<br />
0x90<br />
0xd2<br />
0x91<br />
0xd2<br />
0x92<br />
0xd2<br />
0x93<br />
0xd2<br />
0x96<br />
0xd2<br />
0xa1<br />
0xd2<br />
0xa3<br />
0xff<br />

 download all files in the same folder and write assembly code in the instructions.txt file: 
COMPILE and RUN using CMD cli commands:<br /> 
javac *.java <br /> 
java Simulator
