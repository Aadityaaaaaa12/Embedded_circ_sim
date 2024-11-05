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
            cpu.setparityFlag(calculateParity(0));
            break;

        case 0xF4: // CPL A (Complement ACC)
            byte accValue = cpu.getAccumulator();
            byte complementedValue = (byte) ~accValue;  // Bitwise NOT operation
            cpu.setAccumulator(complementedValue);
            cpu.setparityFlag(calculateParity(complementedValue));
            break;

        case 0x74: // MOV A, #data (Move immediate value to ACC)
            int immValue = cpu.fetch();
            cpu.setAccumulator((byte) immValue);
            cpu.setparityFlag(calculateParity(immValue));
            break;

        case 0xE8: // MOV A, R0
            byte r0Value = (byte) memory.readDataByte(0x00);
            cpu.setAccumulator(r0Value);
            cpu.setparityFlag(calculateParity(r0Value));
            break;

        case 0xE9: // MOV A, R1
            byte r1Value = (byte) memory.readDataByte(0x01);
            cpu.setAccumulator(r1Value);
            cpu.setparityFlag(calculateParity(r1Value));
            break;

        case 0xEA: // MOV A, R2
            byte r2Value = (byte) memory.readDataByte(0x02);
            cpu.setAccumulator(r2Value);
            cpu.setparityFlag(calculateParity(r2Value));
            break;

        case 0xEB: // MOV A, R3
            byte r3Value = (byte) memory.readDataByte(0x03);
            cpu.setAccumulator(r3Value);
            cpu.setparityFlag(calculateParity(r3Value));
            break;

        case 0xEC: // MOV A, R4
            byte r4Value = (byte) memory.readDataByte(0x04);
            cpu.setAccumulator(r4Value);
            cpu.setparityFlag(calculateParity(r4Value));
            break;

        case 0xED: // MOV A, R5
            byte r5Value = (byte) memory.readDataByte(0x05);
            cpu.setAccumulator(r5Value);
            cpu.setparityFlag(calculateParity(r5Value));
            break;

        case 0xEE: // MOV A, R6
            byte r6Value = (byte) memory.readDataByte(0x06);
            cpu.setAccumulator(r6Value);
            cpu.setparityFlag(calculateParity(r6Value));
            break;

        case 0xEF: // MOV A, R7
            byte r7Value = (byte) memory.readDataByte(0x07);
            cpu.setAccumulator(r7Value);
            cpu.setparityFlag(calculateParity(r7Value));
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
            int directValue = cpu.fetch();
            memory.writeByte(address, (byte) directValue);
            cpu.setparityFlag(calculateParity(directValue));
            break;

        case 0x78: // MOV R0, #immediate
            int immR0 = cpu.fetch();
            memory.writeDataByte(0x00, (byte) immR0);
            cpu.setparityFlag(calculateParity(immR0));
            break;

        case 0x79: // MOV R1, #immediate
            int immR1 = cpu.fetch();
            memory.writeDataByte(0x01, (byte) immR1);
            cpu.setparityFlag(calculateParity(immR1));
            break;

        case 0x7A: // MOV R2, #immediate
            int immR2 = cpu.fetch();
            memory.writeDataByte(0x02, (byte) immR2);
            cpu.setparityFlag(calculateParity(immR2));
            break;

        case 0x7B: // MOV R3, #immediate
            int immR3 = cpu.fetch();
            memory.writeDataByte(0x03, (byte) immR3);
            cpu.setparityFlag(calculateParity(immR3));
            break;

        case 0x7C: // MOV R4, #immediate
            int immR4 = cpu.fetch();
            memory.writeDataByte(0x04, (byte) immR4);
            cpu.setparityFlag(calculateParity(immR4));
            break;

        case 0x7D: // MOV R5, #immediate
            int immR5 = cpu.fetch();
            memory.writeDataByte(0x05, (byte) immR5);
            cpu.setparityFlag(calculateParity(immR5));
            break;

        case 0x7E: // MOV R6, #immediate
            int immR6 = cpu.fetch();
            memory.writeDataByte(0x06, (byte) immR6);
            cpu.setparityFlag(calculateParity(immR6));
            break;

        case 0x7F: // MOV R7, #immediate
            int immR7 = cpu.fetch();
            memory.writeDataByte(0x07, (byte) immR7);
            cpu.setparityFlag(calculateParity(immR7));
            break;
		
        case 0x24: { // ADD A, #immediate
			int immediateVal = cpu.fetch();
			int accVal = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
			int result = accVal + immediateVal;

			// Set flags with correct arguments
			cpu.setcarryFlag(calculateCarry(accVal, immediateVal, result));
			cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarry(accVal, immediateVal));
			cpu.setoverflowFlag(calculateOverflow(accVal, immediateVal, result));
		
			// Store result in accumulator
			cpu.setAccumulator((byte) result);
			cpu.setparityFlag(calculateParity(result));
			break;
		}

        // ADD A, Rn (Add register Rn to ACC)
		case 0x28: // ADD A, R0
        case 0x29: // ADD A, R1
        case 0x2A: // ADD A, R2
        case 0x2B: // ADD A, R3
        case 0x2C: // ADD A, R4
        case 0x2D: // ADD A, R5
        case 0x2E: // ADD A, R6
        case 0x2F: { // ADD A, R7
            int registerIndex = opcode - 0x28;
            int registerValue = memory.readDataByte(registerIndex) & 0xFF;
            int accumulatorValue = cpu.getAccumulator() & 0xFF;
            int result = accumulatorValue + registerValue;

            cpu.setcarryFlag(calculateCarry(accumulatorValue, registerValue, result));
            cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarry(accumulatorValue, registerValue));
            cpu.setoverflowFlag(calculateOverflow(accumulatorValue, registerValue, result));
            cpu.setAccumulator((byte) result);
            cpu.setparityFlag(calculateParity(result));
            break;
        }
		
		 case 0x94: {
            int immediateValue = cpu.fetch();
            int accumulatorValue = cpu.getAccumulator() & 0xFF;  // Mask to 8 bits
            int result = accumulatorValue - immediateValue;

            // Set flags
            cpu.setcarryFlag(calculateCarryForSubtraction(accumulatorValue, immediateValue));
            cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarryForSubtraction(accumulatorValue, immediateValue));
            cpu.setoverflowFlag(calculateOverflowForSubtraction(accumulatorValue, immediateValue, result));

            // Store result in accumulator
            cpu.setAccumulator((byte) result);
            cpu.setparityFlag(calculateParity(result));
            break;
        }

        // SUB A, Rn (Subtract register Rn from ACC)
        case 0x98: // SUB A, R0
        case 0x99: // SUB A, R1
        case 0x9A: // SUB A, R2
        case 0x9B: // SUB A, R3
        case 0x9C: // SUB A, R4
        case 0x9D: // SUB A, R5
        case 0x9E: // SUB A, R6
        case 0x9F: { // SUB A, R7
            int registerIndex = opcode - 0x98;
            int registerValue = memory.readDataByte(registerIndex) & 0xFF; // Mask to 8 bits
            int accumulatorValue = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
            int result = accumulatorValue - registerValue;

            // Set flags
            cpu.setcarryFlag(calculateCarryForSubtraction(accumulatorValue, registerValue));
            cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarryForSubtraction(accumulatorValue, registerValue));
            cpu.setoverflowFlag(calculateOverflowForSubtraction(accumulatorValue, registerValue, result));

            // Store result in accumulator
            cpu.setAccumulator((byte) result);
            cpu.setparityFlag(calculateParity(result));
            break;
        }
		
		 case 0x14: // DEC A (Decrement ACC)
            int accumulatorValue = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
            int result = accumulatorValue - 1;

            // Set flags
            cpu.setcarryFlag(calculateCarryForSubtraction(accumulatorValue, 1));
            cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarryForSubtraction(accumulatorValue, 1));
            cpu.setoverflowFlag(calculateOverflowForSubtraction(accumulatorValue, 1, result));

            // Store result in accumulator
            cpu.setAccumulator((byte) result);
            cpu.setparityFlag(calculateParity(result));
            break;

        case 0x18: // DEC R0 (Decrement Register R0)
        case 0x19: // DEC R1
        case 0x1A: // DEC R2
        case 0x1B: // DEC R3
        case 0x1C: // DEC R4
        case 0x1D: // DEC R5
        case 0x1E: // DEC R6
        case 0x1F: { // DEC R7
            int registerIndex = opcode - 0x18;
            int registerValue = memory.readDataByte(registerIndex) & 0xFF; // Mask to 8 bits
            int resultReg = registerValue - 1;

            // Set flags
            cpu.setcarryFlag(calculateCarryForSubtraction(registerValue, 1));
            cpu.setauxiliaryCarryFlag(calculateAuxiliaryCarryForSubtraction(registerValue, 1));
            cpu.setoverflowFlag(calculateOverflowForSubtraction(registerValue, 1, resultReg));

            // Store result back in register
            memory.writeDataByte(registerIndex, (byte) resultReg);
            cpu.setparityFlag(calculateParity(resultReg));
            break;
        }
		
		case 0x44: {  //ORL A, IMMEDIATE
            int immediateValue_orl = cpu.fetch();
            int accumulatorValue_orl = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
            int result_orl = accumulatorValue_orl | immediateValue_orl;

            // Set flags
            cpu.setparityFlag(calculateParity(result_orl));
            cpu.setAccumulator((byte) result_orl);
            break;
        }

        // ORL A, Rn (Bitwise OR accumulator with register Rn)
        case 0x48: // ORL A, R0
        case 0x49: // ORL A, R1
        case 0x4A: // ORL A, R2
        case 0x4B: // ORL A, R3
        case 0x4C: // ORL A, R4
        case 0x4D: // ORL A, R5
        case 0x4E: // ORL A, R6
        case 0x4F: { // ORL A, R7
            int registerIndex_orl1 = opcode - 0x48;
            int registerValue_orl = memory.readDataByte(registerIndex_orl1) & 0xFF; // Mask to 8 bits
            int accumulatorValue_orl1 = cpu.getAccumulator() & 0xFF;
            int result_orl1 = accumulatorValue_orl1 | registerValue_orl;

            // Set flags
            cpu.setparityFlag(calculateParity(result_orl1 ));
            cpu.setAccumulator((byte) result_orl1);	
            break;
        }
		
		case 0x54: {  
            int immediate_value_anl = cpu.fetch();
            int accumulator_value_anl = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
            int result_anl = accumulator_value_anl & immediate_value_anl;

            // Set flags
            cpu.setparityFlag(calculateParity(result_anl));
            cpu.setAccumulator((byte) result_anl);
            break;
        }

        // ANL A, Rn (Bitwise AND accumulator with register Rn)
        case 0x58: // ANL A, R0
        case 0x59: // ANL A, R1
        case 0x5A: // ANL A, R2
        case 0x5B: // ANL A, R3
        case 0x5C: // ANL A, R4
        case 0x5D: // ANL A, R5
        case 0x5E: // ANL A, R6
        case 0x5F: { // ANL A, R7
            int register_index_anl = opcode - 0x58;
            int register_value_anl = memory.readDataByte(register_index_anl) & 0xFF; // Mask to 8 bits
            int accumulator_value_anl_rn = cpu.getAccumulator() & 0xFF;
            int result_anl_rn = accumulator_value_anl_rn & register_value_anl;

            // Set flags
            cpu.setparityFlag(calculateParity(result_anl_rn));
            cpu.setAccumulator((byte) result_anl_rn);
            break;
        }
		
		case 0x62: {  // XRL A, immediate
			int immediateValue_xrl = cpu.fetch();
			int accumulatorValue_xrl = cpu.getAccumulator() & 0xFF; // Mask to 8 bits
			int result_xrl = accumulatorValue_xrl ^ immediateValue_xrl;
		
			// Set flags
			cpu.setparityFlag(calculateParity(result_xrl));
			cpu.setAccumulator((byte) result_xrl);
			break;
		}

		// XRL A, Rn (Bitwise XOR accumulator with register Rn)
		case 0x68: // XRL A, R0
		case 0x69: // XRL A, R1
		case 0x6A: // XRL A, R2
		case 0x6B: // XRL A, R3
		case 0x6C: // XRL A, R4
		case 0x6D: // XRL A, R5
		case 0x6E: // XRL A, R6
		case 0x6F: { // XRL A, R7
			int registerIndex_xrl = opcode - 0x68;
			int registerValue_xrl = memory.readDataByte(registerIndex_xrl) & 0xFF; // Mask to 8 bits
			int accumulatorValue_xrl = cpu.getAccumulator() & 0xFF;
			int result_xrl = accumulatorValue_xrl ^ registerValue_xrl;
		
			// Set flags
			cpu.setparityFlag(calculateParity(result_xrl));
			cpu.setAccumulator((byte) result_xrl);	
			break;
		}


        case 0xD2: { // SETB bit (Set a bit in memory, register, flag, or pin)
            int bitAddress = cpu.fetch();

            if (bitAddress < 0x80) { // RAM memory address
                int byteAddress = bitAddress >> 3;
                int bitPosition = bitAddress & 0x07;
                setMemoryBit(byteAddress, bitPosition, false);
            } else if (bitAddress >= 0x2000 && bitAddress < 0x3000) { // ROM memory address
                int byteAddress = (bitAddress - 0x2000) >> 3;
                int bitPosition = bitAddress & 0x07;
                setMemoryBit(byteAddress, bitPosition, true);
            } else if (bitAddress == 0xE0 || bitAddress == 0xF0) { // ACC or B register
                int bitPosition = bitAddress & 0x07;
                if (bitAddress == 0xE0) {
                    setRegisterBit(cpu.getAccumulator(), bitPosition, "ACC");
                } else {
                    setRegisterBit(cpu.getb_reg(), bitPosition, "B");
                }
            } else if (bitAddress == 0xD0) { // Carry flag
                setFlagBit("C");
            } else if (bitAddress >= 0xB0 && bitAddress < 0xB8) { // Other flags like OV, AC, P
                int flagPosition = bitAddress & 0x07;
                switch (flagPosition) {
                    case 2: // OV flag
                        setFlagBit("OV");
                        break;
                    case 4: // AC flag
                        setFlagBit("AC");
                        break;
                    case 5: // P flag
                        setFlagBit("P");
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown flag bit at address: " + bitAddress);
                }
            } else if (bitAddress >= 0x80 && bitAddress <= 0x87) { // Port 0
                setPinBit(bitAddress);
            } else if (bitAddress >= 0x90 && bitAddress <= 0x97) { // Port 1
                setPinBit(bitAddress);
            } else if (bitAddress >= 0xA0 && bitAddress <= 0xA7) { // Port 2
                setPinBit(bitAddress);
            } else if (bitAddress >= 0xB0 && bitAddress <= 0xB7) { // Port 3
                setPinBit(bitAddress);
            } else {
                throw new UnsupportedOperationException("Invalid bit address for SETB: " + Integer.toHexString(bitAddress));
            }
            break;
        }

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
  
  // Updated helper function for carry flag
private boolean calculateCarry(int operand1, int operand2, int result) {
    return (result & 0x100) != 0; // Check if there's a carry out of the 8th bit
}

private boolean calculateAuxiliaryCarry(int operand1, int operand2) {
    int lowerNibble1 = operand1 & 0x0F;
    int lowerNibble2 = operand2 & 0x0F;
    int lowerNibbleSum = lowerNibble1 + lowerNibble2;
    
    // Debugging outputs
    System.out.println("Operand1 Lower Nibble: " + Integer.toHexString(lowerNibble1));
    System.out.println("Operand2 Lower Nibble: " + Integer.toHexString(lowerNibble2));
    System.out.println("Lower Nibble Sum: " + Integer.toHexString(lowerNibbleSum));

    return (lowerNibbleSum >= 0x0f);
}


// Helper function to calculate overflow flag for addition
private boolean calculateOverflow(int operand1, int operand2, int result) {
    boolean operand1Sign = (operand1 & 0x80) != 0;
    boolean operand2Sign = (operand2 & 0x80) != 0;
    boolean resultSign = (result & 0x80) != 0;

    return (operand1Sign == operand2Sign) && (operand1Sign != resultSign);
}

// Helper function to calculate carry flag for subtraction
private boolean calculateCarryForSubtraction(int operand1, int operand2) {
    return operand1 < operand2; // Carry (borrow) occurs if operand1 is less than operand2
}

// Helper function to calculate auxiliary carry flag for subtraction
private boolean calculateAuxiliaryCarryForSubtraction(int operand1, int operand2) {
    return (operand1 & 0x0F) < (operand2 & 0x0F); // Borrow in lower 4 bits
}

// Helper function to calculate overflow flag for subtraction
private boolean calculateOverflowForSubtraction(int operand1, int operand2, int result) {
    boolean operand1Sign = (operand1 & 0x80) != 0; // Check sign of operand1 (7th bit)
    boolean operand2Sign = (operand2 & 0x80) != 0; // Check sign of operand2
    boolean resultSign = (result & 0x80) != 0;     // Check sign of result

    // Overflow occurs if the operands have opposite signs and result sign differs from operand1
    return (operand1Sign != operand2Sign) && (operand1Sign != resultSign);
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
