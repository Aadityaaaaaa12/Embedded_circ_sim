import java.lang.*;
import java.util.*;
import java.io.*;

public class Simulator {
  public static void main(String[] args) {
    Memory memory = new Memory(4096, 128); // 4KB ROM, 128B RAM
    CPU cpu = new CPU(memory);

    byte[] testProgram = {
      (byte) 0xE4,         // CLR A (Clear ACC)
      (byte) 0x74, 0x56,   // MOV A, #0x56 (Load 0x56 into ACC)
      (byte) 0xFF          // HALT (Custom HALT to stop execution)
    };

    // Load the opcodes into memory starting from address 0
    for (int i = 0; i < testProgram.length; i++) {
      memory.writeByte(i, testProgram[i]);
    }


    while (true) {
        int opcode = memory.readByte(cpu.getProgramCounter());

       //custom END FUNCTION. like keil.
        if (opcode == 0xFF) {
            System.out.println("Program halted.");
            break;
        }

        //fetch-decode-execute
        cpu.cycle();
    }

    cpu.displayRegisters();
    cpu.setAndDisplayPSW();

    
  }

}