import java.lang.*;
import java.util.*;
import java.io.*;

public class Memory {
  private byte[] dataMemory; // RAM (128)
  private byte[] codeMemory; // ROM(4096)

  public Memory(int codeMemorySize, int dataMemorySize) {
    this.dataMemory = new byte[dataMemorySize];
    this.codeMemory = new byte[codeMemorySize];
  }

  // Read from rom
 public int readByte(int address) {
    if (address >= 0 && address < codeMemory.length) {
        return codeMemory[address] & 0xFF; 
    } else {
        throw new IllegalArgumentException("Invalid memory access at address: " + address);
    }
}

  // Write to rom
  public void writeByte(int address, byte value) {
    codeMemory[address] = value;
  }

  // Write to RAM
  public void writedataByte(int address, byte value) {
    dataMemory[address] = value;
  }

  // Read from RAM
  public int readDataByte(int address) {
    return dataMemory[address] & 0xFF; // unsigned byte
  }

}

