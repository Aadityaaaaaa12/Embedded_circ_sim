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
			case 0xF4: // CPL A
				byte accumulatorValue = cpu.getAccumulator();
				byte complementedValue = (byte) ~accumulatorValue;  // Bitwise NOT operation
				cpu.setAccumulator(complementedValue);
				break;

            case 0x74: // MOV A, #data (Move immediate value to ACC)
                int immediateValue = cpu.fetch(); 
                cpu.setAccumulator((byte) immediateValue);
                cpu.setparityFlag(calculateParity(immediateValue));
                break;

			case 0xE8: // MOV A, R0
				cpu.setAccumulator((byte) memory.readDataByte(0x00));
				break;

			case 0xE9: // MOV A, R1
				cpu.setAccumulator((byte) memory.readDataByte(0x01));
				break;

			case 0xEA: // MOV A, R2
				cpu.setAccumulator((byte) memory.readDataByte(0x02));
				break;

			case 0xEB: // MOV A, R3
				cpu.setAccumulator((byte) memory.readDataByte(0x03));
				break;

			case 0xEC: // MOV A, R4
				cpu.setAccumulator((byte) memory.readDataByte(0x04));
				break;

			case 0xED: // MOV A, R5
				cpu.setAccumulator((byte) memory.readDataByte(0x05));
			break;

			case 0xEE: // MOV A, R6
				cpu.setAccumulator((byte) memory.readDataByte(0x06));
				break;

			case 0xEF: // MOV A, R7
			cpu.setAccumulator((byte) memory.readDataByte(0x07));
				break;
			case 0xF8: // MOV R0, A
				memory.writeDataByte(0x00, cpu.getAccumulator());
				break;

			case 0xF9: // MOV R1, A
				memory.writeDataByte(0x01, cpu.getAccumulator());
				break;

			case 0xFA: // MOV R2, A
				memory.writeDataByte(0x02, cpu.getAccumulator());
				break;

			case 0xFB: // MOV R3, A
				memory.writeDataByte(0x03, cpu.getAccumulator());
				break;

			case 0xFC: // MOV R4, A
				memory.writeDataByte(0x04, cpu.getAccumulator());
				break;

			case 0xFD: // MOV R5, A
				memory.writeDataByte(0x05, cpu.getAccumulator());
				break;

			case 0xFE: // MOV R6, A
				memory.writeDataByte(0x06, cpu.getAccumulator());
				break;

			case 0xFF: // MOV R7, A
				memory.writeDataByte(0x07, cpu.getAccumulator());
				break;
			
            case 0x75: // MOV direct, #immediate (Move immediate value to direct address in memory)
                int address = cpu.fetch();
                int value = cpu.fetch();
                memory.writeByte(address, (byte) value);
                cpu.setparityFlag(calculateParity(value));
                break;
				
			case 0x78: // MOV R0, #immediate
				memory.writeDataByte(0x00, (byte) memory.readByte(cpu.fetch()));
				break;

			case 0x79: // MOV R1, #immediate
				 int value_r = cpu.fetch();
				memory.writeDataByte(0x01, (byte) value_r);
				break;

			case 0x7A: // MOV R2, #immediate
				memory.writeDataByte(0x02, (byte) memory.readByte(cpu.fetch()));
				break;

			case 0x7B: // MOV R3, #immediate
				memory.writeDataByte(0x03, (byte) memory.readByte(cpu.fetch()));
				break;

			case 0x7C: // MOV R4, #immediate
				memory.writeDataByte(0x04, (byte) memory.readByte(cpu.fetch()));
				break;

			case 0x7D: // MOV R5, #immediate
				memory.writeDataByte(0x05, (byte) memory.readByte(cpu.fetch()));
				break;
			
			case 0x7E: // MOV R6, #immediate
				memory.writeDataByte(0x06, (byte) memory.readByte(cpu.fetch()));
				break;

			case 0x7F: // MOV R7, #immediate
				memory.writeDataByte(0x07, (byte) memory.readByte(cpu.fetch()));
				break;


            case 0xD2: // SETB bit (Set a bit in memory, register, flag, or pin)
                int bitAddress = cpu.fetch(); 

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
                } else if (bitAddress >= 0x80 && bitAddress <= 0x87) {	//port 0
  
                    setPinBit(bitAddress);  
					
                } else if (bitAddress >= 0x90 && bitAddress <= 0x97) {	//port 1
              
                    setPinBit(bitAddress);  
					
                }else if(bitAddress >= 0xA0 && bitAddress <= 0xA7){ //port  2
				
					setPinBit(bitAddress);  
					
				}else if(bitAddress >= 0xB0 && bitAddress <= 0xB7){ //PORT 3
					
					 setPinBit(bitAddress);  
					
				}else {
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
            // For ROM memory,this is read-only 
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


   public void pinReset() {
    // Port 0 (P0.0 - P0.7)
    pins.p0_0.setLow();
    pins.p0_1.setLow();
    pins.p0_2.setLow();
    pins.p0_3.setLow();
    pins.p0_4.setLow();
    pins.p0_5.setLow();
    pins.p0_6.setLow();
    pins.p0_7.setLow();

    // Port 1 (P1.0 - P1.7)
    pins.p1_0.setLow();
    pins.p1_1.setLow();
    pins.p1_2.setLow();
    pins.p1_3.setLow();
    pins.p1_4.setLow();
    pins.p1_5.setLow();
    pins.p1_6.setLow();
    pins.p1_7.setLow();

    // Port 2 (P2.0 - P2.7)
    pins.p2_0.setLow();
    pins.p2_1.setLow();
    pins.p2_2.setLow();
    pins.p2_3.setLow();
    pins.p2_4.setLow();
    pins.p2_5.setLow();
    pins.p2_6.setLow();
    pins.p2_7.setLow();

    // Port 3 (P3.0 - P3.7)
    pins.p3_0.setLow();
    pins.p3_1.setLow();
    pins.p3_2.setLow();
    pins.p3_3.setLow();
    pins.p3_4.setLow();
    pins.p3_5.setLow();
    pins.p3_6.setLow();
    pins.p3_7.setLow();

    System.out.println("All pins have been reset to LOW.");
}

   
   //helper function to set pins in a port 
   private void setPinBit(int pinAddress) { 
    switch (pinAddress) {
        // Port 0 (P0.0 - P0.7)
        case 0x80:  // P0.0
            pins.p0_0.setHigh();
            System.out.println("P0.0 set HIGH");
            break;
        case 0x81:  // P0.1
            pins.p0_1.setHigh();
            System.out.println("P0.1 set HIGH");
            break;
        case 0x82:  // P0.2
            pins.p0_2.setHigh();
            System.out.println("P0.2 set HIGH");
            break;
        case 0x83:  // P0.3
            pins.p0_3.setHigh();
            System.out.println("P0.3 set HIGH");
            break;
        case 0x84:  // P0.4
            pins.p0_4.setHigh();
            System.out.println("P0.4 set HIGH");
            break;
        case 0x85:  // P0.5
            pins.p0_5.setHigh();
            System.out.println("P0.5 set HIGH");
            break;
        case 0x86:  // P0.6
            pins.p0_6.setHigh();
            System.out.println("P0.6 set HIGH");
            break;
        case 0x87:  // P0.7
            pins.p0_7.setHigh();
            System.out.println("P0.7 set HIGH");
            break;

        // Port 1 (P1.0 - P1.7)
        case 0x90:  // P1.0
            pins.p1_0.setHigh();
            System.out.println("P1.0 set HIGH");
            break;
        case 0x91:  // P1.1
            pins.p1_1.setHigh();
            System.out.println("P1.1 set HIGH");
            break;
        case 0x92:  // P1.2
            pins.p1_2.setHigh();
            System.out.println("P1.2 set HIGH");
            break;
        case 0x93:  // P1.3
            pins.p1_3.setHigh();
            System.out.println("P1.3 set HIGH");
            break;
        case 0x94:  // P1.4
            pins.p1_4.setHigh();
            System.out.println("P1.4 set HIGH");
            break;
        case 0x95:  // P1.5
            pins.p1_5.setHigh();
            System.out.println("P1.5 set HIGH");
            break;
        case 0x96:  // P1.6
            pins.p1_6.setHigh();
            System.out.println("P1.6 set HIGH");
            break;
        case 0x97:  // P1.7
            pins.p1_7.setHigh();
            System.out.println("P1.7 set HIGH");
            break;

        // Port 2 (P2.0 - P2.7)
        case 0xA0:  // P2.0
            pins.p2_0.setHigh();
            System.out.println("P2.0 set HIGH");
            break;
        case 0xA1:  // P2.1
            pins.p2_1.setHigh();
            System.out.println("P2.1 set HIGH");
            break;
        case 0xA2:  // P2.2
            pins.p2_2.setHigh();
            System.out.println("P2.2 set HIGH");
            break;
        case 0xA3:  // P2.3
            pins.p2_3.setHigh();
            System.out.println("P2.3 set HIGH");
            break;
        case 0xA4:  // P2.4
            pins.p2_4.setHigh();
            System.out.println("P2.4 set HIGH");
            break;
        case 0xA5:  // P2.5
            pins.p2_5.setHigh();
            System.out.println("P2.5 set HIGH");
            break;
        case 0xA6:  // P2.6
            pins.p2_6.setHigh();
            System.out.println("P2.6 set HIGH");
            break;
        case 0xA7:  // P2.7
            pins.p2_7.setHigh();
            System.out.println("P2.7 set HIGH");
            break;

        // Port 3 (P3.0 - P3.7)
        case 0xB0:  // P3.0
            pins.p3_0.setHigh();
            System.out.println("P3.0 set HIGH");
            break;
        case 0xB1:  // P3.1
            pins.p3_1.setHigh();
            System.out.println("P3.1 set HIGH");
            break;
        case 0xB2:  // P3.2
            pins.p3_2.setHigh();
            System.out.println("P3.2 set HIGH");
            break;
        case 0xB3:  // P3.3
            pins.p3_3.setHigh();
            System.out.println("P3.3 set HIGH");
            break;
        case 0xB4:  // P3.4
            pins.p3_4.setHigh();
            System.out.println("P3.4 set HIGH");
            break;
        case 0xB5:  // P3.5
            pins.p3_5.setHigh();
            System.out.println("P3.5 set HIGH");
            break;
        case 0xB6:  // P3.6
            pins.p3_6.setHigh();
            System.out.println("P3.6 set HIGH");
            break;
        case 0xB7:  // P3.7
            pins.p3_7.setHigh();
            System.out.println("P3.7 set HIGH");
            break;

        default:
            throw new UnsupportedOperationException("Invalid pin address for SETB: " + Integer.toHexString(pinAddress));
		}
	}

}
