import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Simulator {
    public static void main(String[] args) {
        Memory memory = new Memory(4096, 128); // 4KB ROM, 128B RAM
		Pins pins = new Pins();
		CPU cpu = new CPU(memory,pins);
		InstructionSet instructionSet = new InstructionSet(cpu, memory, pins);
		Ground ground = new Ground();
        LED led = new LED();
        Resistor resistor = new Resistor();
		
		resistor.connect(pins.p1_1, ground);  
        led.connect(pins.p1_0, pins.p1_1); 
		
        // Load instructions from a file
        loadOpcodesFromFile(memory, "instructions.txt");

        while (true) {
            int opcode = memory.readByte(cpu.getProgramCounter());

            // Custom END FUNCTION
            if (opcode == 0xFF) {
                System.out.println("Program halted.");
                break;
            }

            // Fetch-decode-execute
            cpu.cycle();
        }

        cpu.displayRegisters();
        cpu.setAndDisplayPSW();
		led.checkState();   
        resistor.checkVoltageDrop();
    }
	//loader function
    private static void loadOpcodesFromFile(Memory memory, String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    // Parse the hex string to an integer
                    int opcode = Integer.parseInt(line.substring(2), 16); // Remove "0x" and parse
                    memory.writeByte(i, (byte) opcode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading opcodes from file: " + e.getMessage());
        }
    }
}
