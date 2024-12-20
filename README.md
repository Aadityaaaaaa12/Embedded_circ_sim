# Embedded Circuit Simulator

## Description
The **Embedded Circuit Simulator** is a Java-based tool for simulating simple embedded circuits with a focus on the 8051 microcontroller. This simulator allows users to execute a set of common instructions and interact with simulated components, including LEDs, seven-segment displays, resistors, and transistors (NPN and PNP types). It provides an environment for users to visualize how 8051 assembly instructions affect components and register values in real-time.

## Features
- **8051 Simulation** with support for the following instructions:
  - *ACC Operations:*
    - `CPL A`
    - `CLR A`
    - `DEC A`
    - `MOV A, immediate`
    - `MOV A, Rn`
    - `ADD A, immediate`
    - `ADD A, Rn`
    - `SUB A, immediate`
    - `SUB A, Rn`
  - **Logic Instructions:**
    - `ANL A, immediate`
    - `ANL A, Rn`
    - `ORL A, immediate`
    - `ORL A, Rn`
    - `XRL A, immediate`
    - `XRL A, Rn`
  - **register Operations:**
    - `DEC Rn`
    - `MOV Rn, A`
    - `MOV Rn, immediate`
- **Peripheral Components:**
  - LEDs
  - Seven-segment displays
  - Resistors
  - NPN and PNP transistors

## Table of Contents
1. [Installation](#installation)
2. [Code Writing](#code-writing)
3. [Usage](#usage)

## Installation

### Method 1: Clone the Repository
To download the project using Git, run:
```bash
git clone https://github.com/Aadityaaaaaa12/Embedded_circ_sim.git
```
### Method 2: Download ZIP
Alternatively, download the ZIP file from GitHub, extract it, and place it in your desired directory.

## Code Writing
The code for the processor should be written in the `instructions.txt` file located in the `program` folder. Any text editor or IDE can be used to edit this file.

- **Format**: Instructions should be written one per line, using hexadecimal opcodes.
- **Example Code**: The pre-filled example code in `instructions.txt` demonstrates moving values into the accumulator, setting all pins of ports 1 and 2 high, moving values from the accumulator to various registers, decrementing register values, and performing logical operations on them. <br/>
CURRENT sample code <br/>
MOV A, #0x56        ; Load immediate value 0x56 into A <br/>
MOV R3, #0x21       ; Load immediate value 0x21 into R3   <br/>
MOV A, #0x06        ; Load 0x06 into A for subtraction  <br/>
SUBB A, #0x05       ; Subtract 0x05 from A  <br/>
MOV R0, A           ; Move the result from A into R0  <br/>
MOV A, #0xF0        ; Load an AND mask (0xF0) into A  <br/>
ANL A, #0x0F        ; AND the mask 0x0F with A  <br/>
MOV R1, A           ; Move the result from A into R1  <br/>
MOV A, R3           ; Move the value in R3 into A  <br/>
XRL A, R0           ; XOR the value in A with R0  <br/>
SETB P1.0           ; Set all bits in P1 high  <br/>
SETB P1.1  <br/>
SETB P1.2  <br/>
SETB P1.3  <br/>
SETB P1.4  <br/>
SETB P1.5  <br/>
SETB P1.6  <br/>
SETB P1.7  <br/>
SETB P2.0           ; Set all bits in P2 high  <br/>
SETB P2.1  <br/>
SETB P2.2  <br/>
SETB P2.3  <br/>
SETB P2.4  <br/>
SETB P2.5  <br/>
SETB P2.6  <br/>
SETB P2.7  <br/>
MOV A, #0x0C        ; Load immediate value 0x0C into A  <br/>
DEC A               ; Decrement A  <br/>
MOV R2, #0x01       ; Load immediate value 0x01 into R2  <br/>
INC R2              ; Increment R2  <br/>


## Usage

### Navigate to Project Directory
Change to the project directory by running:

```bash
cd Embedded_circ_sim
```

### Compile the Project
Compile the Java files by running:
```bash
javac *.java
```

### Run the Simulator
Start the simulator by executing:

```bash
java Simulator
```
### Simulator Window
- The simulator window will appear, allowing you to initialize and interact with peripherals.
- Press **Run** to execute the instructions in `instructions.txt`.
- Upon execution, the simulation results window will open, displaying the effects of each instruction on the components.



