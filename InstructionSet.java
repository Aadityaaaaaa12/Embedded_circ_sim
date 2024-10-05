import java.lang.*;
import java.util.*;
import java.io.*;

public class InstructionSet {
    private CPU cpu;
    private Memory memory;

    public InstructionSet(CPU cpu, Memory memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

  private boolean calculateParity(int value) {
      int count = 0;
    
      for (int i = 0; i < 8; i++) {
          count += (value >> i) & 1;
      }
    
      return (count % 2 == 0);
  }

    
    public void executeInstruction(int opcode) {
        switch (opcode) {
            case 0xE4: // CLR A 
                cpu.setAccumulator((byte)0);
                break;

            case 0x74: // MOV A, #data
                int immediateValue = cpu.fetch(); 
                cpu.setAccumulator((byte)immediateValue);
                cpu.setparityFlag(calculateParity(immediateValue));
                break;

            case 0x75: // MOV direct, #data
                int address = cpu.fetch();
                int value = cpu.fetch();
                memory.writeByte(address, (byte) value);
                cpu.setparityFlag(calculateParity(value));
                break;

            
            default:
                throw new UnsupportedOperationException("Opcode not supported: " + Integer.toHexString(opcode));
        }
    }
}