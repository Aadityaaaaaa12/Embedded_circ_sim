import java.lang.*;
import java.util.*;
import java.io.*;

public class Memory {
  private byte[] dataMemory; // default RAM (128)
  private byte[] codeMemory; // default ROM(4096)

  public Memory(byte codeMemorySize){ //RAM defualts to 128. ROM user defined  
	this.dataMemory = new byte[128];
    this.codeMemory = new byte[codeMemorySize];  
  }	
 
  
  public Memory(int dataMemorySize){ //ROM defaults to 4096. RAM user defined
	this.dataMemory = new byte[dataMemorySize];
    this.codeMemory = new byte[4096];  
  }
	
  	
  public Memory(int codeMemorySize, int dataMemorySize) { //RAM & ROM user defined
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
    if(address >= 0 && address < codeMemory.length){
		codeMemory[address] = value;
	}else{
	  throw new IllegalArgumentException("Invalid memory access at address: " + address);
		}
  }

  // Write to RAM
  public int readDataByte(int address) {
    if (address >= 0 && address < dataMemory.length) {
        return dataMemory[address] & 0xFF;
    } else {
        throw new IllegalArgumentException("Invalid memory access at address: " + address);
    }
}

public void writedataByte(int address, byte value) {
    if (address >= 0 && address < dataMemory.length) {
        dataMemory[address] = value;
    } else {
        throw new IllegalArgumentException("Invalid memory access at address: " + address);
    }
}
}

