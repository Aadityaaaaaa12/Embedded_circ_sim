import java.lang.*;
import java.util.*;
import java.io.*;

class CPU {
  // cpu main component def
  private int pc;
  private int dptr;
  private byte acc;
  private byte b;
  private byte psw;

  // defining flags of the psw
  private boolean carryFlag;
  private boolean auxiliaryCarryFlag;
  private boolean fo; // user def bit
  private boolean regset1;
  private boolean regset0;
  private boolean overflowFlag;
  private boolean parityFlag;

  private Memory memory;
  private InstructionSet instructionSet;
  private Pins pins;	
  public CPU(Memory memory, Pins pins) {
      this.memory = memory;
      this.instructionSet = new InstructionSet(this, memory,pins);
      reset();
  }
 // cpu main component def end
 
   // imp functions
  public void reset() {
    this.pc = 0x0000;
    this.dptr = 0x0000;
    this.acc = 0x00;
    this.b = 0x00;
    this.psw = 0x00;
    this.carryFlag = false;
    this.auxiliaryCarryFlag = false;
    this.fo = false;
    this.regset1 = false;
    this.regset0 = false;
    this.overflowFlag = false;
    this.parityFlag = false;
  }


  public int fetch() {
    int opcode = memory.readByte(pc);
    pc++;
    return opcode;
  }

  public void execute(int opcode) {
    instructionSet.executeInstruction(opcode);
  }

  public void cycle() {
    int opcode = fetch();
    execute(opcode);
  }
  // imp functions end

  // getters setterssss for various thingys
  public byte getAccumulator() {
    return acc;
  }

  public void setAccumulator(byte acc) {
    this.acc = acc;
  }

  public int getProgramCounter() {
    return pc;
  }

  public void setProgramCounter(int pc) {
    this.pc = pc;
  }

  public int getdptr() {
    return dptr;
  }

  public void setdptr(int dptr) {
    this.dptr = dptr;
  }

  public byte getb_reg() {
    return b;
  }

  public void setb_reg(byte b) {
    this.b = b;
  }

  public int getPsw() {
    return psw;
  }

  public void setpsw(byte psw) {
    this.psw = psw;
  }

  public boolean getcarryFlag() {
    return carryFlag;
  }

  public void setcarryFlag(Boolean carryFlag) {
    this.carryFlag = carryFlag;
  }

  public boolean getauxiliaryCarryFlag() {
    return auxiliaryCarryFlag;
  }

  public void setauxiliaryCarryFlag(Boolean auxiliaryCarryFlag) {
    this.auxiliaryCarryFlag = auxiliaryCarryFlag;
  }

  public boolean getfo() {
    return fo;
  }

  public void setfo(Boolean fo) {
    this.fo = fo;
  }

  public boolean getregset1() {
    return regset1;
  }

  public void setregset1(Boolean regset1) {
    this.regset1 = regset1;
  }

  public boolean getregset0() {
    return regset0;
  }

  public void setregset0(Boolean regset0) {
    this.regset0 = regset0;
  }

  public boolean getoverflowFlag() {
    return overflowFlag;
  }

  public void setoverflowFlag(Boolean overflowFlag) {
    this.overflowFlag = overflowFlag;
  }

  public boolean getparityFlag() {
    return parityFlag;
  }

  public void setparityFlag(Boolean parityFlag) {
    this.parityFlag = parityFlag;
  }
  // getters setters for various thingys end


  // display
  public void displayRegisters() {
    System.out.println("Accumulator: 0x" + String.format("%02X", acc));
    System.out.println("DPTR: 0x" + String.format("%04X", dptr));
    System.out.println("B Register: 0x" + String.format("%02X", b));
    System.out.println("Program Counter: 0x" + String.format("%04X", pc));
  }

  
  public void setAndDisplayPSW() {
   
    psw = 0;
    if (carryFlag) psw |= (1 << 7);
    if (auxiliaryCarryFlag) psw |= (1 << 6);
    if (fo) psw |= (1 << 5);
    if (regset1) psw |= (1 << 4);
    if (regset0) psw |= (1 << 3);
    if (overflowFlag) psw |= (1 << 2);
    if (parityFlag) psw |= (1 << 0);

    System.out.println("PSW: 0x" + String.format("%02X", psw));
    System.out.println("Carry Flag (CY): " + carryFlag);
    System.out.println("Auxiliary Carry Flag (AC): " + auxiliaryCarryFlag);
    System.out.println("FO: " + fo);
    System.out.println("Register Bank Select (RS1): " + regset1);
    System.out.println("Register Bank Select (RS0): " + regset0);
    System.out.println("Overflow Flag (OV): " + overflowFlag);
    System.out.println("Parity Flag (P): " + parityFlag);
  }
  //display end

}
