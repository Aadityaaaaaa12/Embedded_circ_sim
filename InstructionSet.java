import java.lang.*;
import java.util.*;
import java.io.*;

public class InstructionSet {
    private CPU cpu;
    private Memory memory;
	private Pins pins;

    public InstructionSet(CPU cpu, Memory memory, Pins pins) {
        this.cpu = cpu;
        this.memory = memory;
		this.pins = pins; 
    }

	  public void executeInstruction(int opcode) {
        switch (opcode) {
            case 0xE4: // CLR A (Clear ACC)
                cpu.setAccumulator((byte) 0);
                break;

            case 0x74: // MOV A, #data (Move immediate value to ACC)
                int immediateValue = cpu.fetch(); 
                cpu.setAccumulator((byte) immediateValue);
                cpu.setparityFlag(calculateParity(immediateValue));
                break;

            case 0x75: // MOV direct, #data (Move immediate value to direct address in memory)
                int address = cpu.fetch();
                int value = cpu.fetch();
                memory.writeByte(address, (byte) value);
                cpu.setparityFlag(calculateParity(value));
                break;

            case 0xD2: // SETB bit (Set a bit in memory, register, flag, or pin)
                int bitAddress = cpu.fetch(); 

                // Check if it's a memory address (RAM or ROM)
                if (bitAddress < 0x80) {  // RAM memory address
                    int byteAddress = bitAddress >> 3;
                    int bitPosition = bitAddress & 0x07; 
                    setMemoryBit(byteAddress, bitPosition, false); 
                } else if (bitAddress >= 0x2000 && bitAddress < 0x3000) { //ROM memory address
                    
                    int byteAddress = (bitAddress - 0x2000) >> 3;  
                    int bitPosition = bitAddress & 0x07;  
                    setMemoryBit(byteAddress, bitPosition, true);
                } else if (bitAddress == 0xE0 || bitAddress == 0xF0) {
                    
                    int bitPosition = bitAddress & 0x07;
                    if (bitAddress == 0xE0) {
                       
                        setRegisterBit(cpu.getAccumulator(), bitPosition, "ACC");
                    } else {
                        
                        setRegisterBit(cpu.getb_reg(), bitPosition, "B");
                    }
                } else if (bitAddress == 0xD0) {
                    // Carry flag
                    setFlagBit("C");
                } else if (bitAddress >= 0xB0 && bitAddress < 0xB8) {
                    // Other flags like OV, AC, P, etc.
                    int flagPosition = bitAddress & 0x07;
                    switch (flagPosition) {
                        case 2:  // OV flag
                            setFlagBit("OV");
                            break;
                        case 4:  // AC flag
                            setFlagBit("AC");
                            break;
                        case 5:  // P flag
                            setFlagBit("P");
                            break;
                        default:
                            throw new UnsupportedOperationException("Unknown flag bit at address: " + bitAddress);
                    }
                } else if (bitAddress >= 0x90 && bitAddress <= 0x9F) {	//port 1
                   
                    setPinBit(bitAddress);  
                } else if (bitAddress >= 0xA0 && bitAddress <= 0xAF) {	//port 2
              
                    setPinBit(bitAddress);  
                } else {
                    throw new UnsupportedOperationException("Invalid bit address for SETB: " + Integer.toHexString(bitAddress));
                }
                break;

            default:
                throw new UnsupportedOperationException("Opcode not supported: " + Integer.toHexString(opcode));
        }
    }
    

    //helper function for partity flag
    private boolean calculateParity(int value) {
        int count = 0;
    
        for (int i = 0; i < 8; i++) {
            count += (value >> i) & 1;
        }
    
        return (count % 2 == 0);
    }
  
    // Helper function to set a specific bit at a given position
    private int setBit(int value, int bitPosition) {
        return value | (1 << bitPosition);
    }

    // Helper function to set a specific bit in memory (RAM or ROM)
    private void setMemoryBit(int address, int bitPosition, boolean isROM) {
        if (isROM) {
            // For ROM memory, we assume this is read-only and raise an exception or handle it accordingly
            throw new UnsupportedOperationException("Cannot set bit in ROM memory.");
        } else {
            // For RAM memory
            int value = memory.readDataByte(address);
            value = setBit(value, bitPosition);
            memory.writeByte(address, (byte) value);
        }
    }

    // Helper function to set a specific bit in a register (ACC or B)
    private void setRegisterBit(byte regValue, int bitPosition, String registerName) {
        int updatedValue = setBit(regValue, bitPosition);
        if (registerName.equals("ACC")) {
            cpu.setAccumulator((byte) updatedValue);
        } else if (registerName.equals("B")) {
            cpu.setb_reg((byte) updatedValue);
        }
    }

    // Helper function to set a specific bit in a flag (Carry, Overflow, etc.)
    private void setFlagBit(String flagName) {
        switch (flagName) {
            case "C":
                cpu.setcarryFlag(true);
                break;
            case "OV":
                cpu.setoverflowFlag(true);
                break;
            case "P":
                cpu.setparityFlag(true);
                break;
            case "AC":
                cpu.setauxiliaryCarryFlag(true);
                break;
            default:
                throw new IllegalArgumentException("Unknown flag: " + flagName);
        }
    }

    
    private void setPinBit(int pinAddress) {
    // Access the appropriate pin based on the bit address
    switch (pinAddress) {
        case 0x90:  // P1.0
            pins.p1_0.setHigh();  // Set P1.0 high
            System.out.println("P1.0 set HIGH");
            break;

        case 0x91:  // P1.1
            pins.p1_1.setHigh();  // Set P1.1 high
            System.out.println("P1.1 set HIGH");
            break;

        default:
            throw new UnsupportedOperationException("Invalid pin address for SETB: " + Integer.toHexString(pinAddress));
		}
	}
}
