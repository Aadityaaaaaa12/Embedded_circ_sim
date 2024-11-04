import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Simulator {
    private static Memory memory;
    private static CPU cpu;
    private static Pins pins;
    private static InstructionSet instructionSet;

    public static void main(String[] args) {
        memory = new Memory(4096, 128); // 4KB ROM, 128B RAM
        pins = new Pins();
        cpu = new CPU(memory, pins);
        instructionSet = new InstructionSet(cpu, memory, pins);

        SwingUtilities.invokeLater(Simulator::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Embedded Circuit Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Peripheral Button Panel in Grid Layout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.add(new JButton("LED"));
        buttonPanel.add(new JButton("NPN"));
        buttonPanel.add(new JButton("PNP"));
        buttonPanel.add(new JButton("Resistor"));
        buttonPanel.add(new JButton("7-Segment"));

        // Align the button panel in the center
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Right side Initialize Peripherals Panel with Separator
        JPanel initPanel = new JPanel(new BorderLayout());
        JLabel initLabel = new JLabel("Initialize Peripherals", JLabel.CENTER);
        initPanel.add(initLabel, BorderLayout.NORTH);
        initPanel.setPreferredSize(new Dimension(150, frame.getHeight()));
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(initPanel, BorderLayout.EAST);  // Add it to the right side

        // Run Button at the Bottom
        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> executeProgram());
        mainPanel.add(runButton, BorderLayout.SOUTH);

        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void executeProgram() {
        // Load instructions from file and execute
        loadOpcodesFromFile(memory, "instructions.txt");
        executeInstructions();

        // Show results after execution
        displayResults();
    }

    // Execution function to simulate instruction cycle
    private static void executeInstructions() {
        while (true) {
            int opcode = memory.readByte(cpu.getProgramCounter());
            if (opcode == 0xFF) {
                System.out.println("Program halted.");
                break;
            }
            cpu.cycle();
        }
    }

    // Display results in a new frame
    private static void displayResults() {
        JFrame resultsFrame = new JFrame("Simulation Results");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setLayout(new BorderLayout());

        // Left Column - Register Values from CPU
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.add(new JLabel("Accumulator: 0x" + String.format("%02X", cpu.getAccumulator())));
        registerPanel.add(new JLabel("DPTR: 0x" + String.format("%04X", cpu.getdptr())));
        registerPanel.add(new JLabel("B Register: 0x" + String.format("%02X", cpu.getb_reg())));
        registerPanel.add(new JLabel("Program Counter: 0x" + String.format("%04X", cpu.getProgramCounter())));

        // Right Column - Flag Values from CPU
        JPanel flagPanel = new JPanel();
        flagPanel.setLayout(new BoxLayout(flagPanel, BoxLayout.Y_AXIS));
        flagPanel.add(new JLabel("Carry Flag (CY): " + cpu.getcarryFlag()));
        flagPanel.add(new JLabel("Auxiliary Carry Flag (AC): " + cpu.getauxiliaryCarryFlag()));
        flagPanel.add(new JLabel("FO: " + cpu.getfo()));
        flagPanel.add(new JLabel("RS1: " + cpu.getregset1()));
        flagPanel.add(new JLabel("RS0: " + cpu.getregset0()));
        flagPanel.add(new JLabel("Overflow Flag (OV): " + cpu.getoverflowFlag()));
        flagPanel.add(new JLabel("Parity Flag (P): " + cpu.getparityFlag()));

        // Main Panel to hold register and flag columns
        JPanel mainResultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainResultPanel.add(registerPanel);
        mainResultPanel.add(flagPanel);

        // RAM and ROM ComboBoxes at the bottom
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel ramLabel = new JLabel("RAM:");
        JComboBox<String> ramComboBox = new JComboBox<>();
        
        // Use a fixed size for RAM (128)
        for (int i = 0; i < 128; i++) {
            ramComboBox.addItem("0x" + String.format("%02X", i));
        }
        ramComboBox.addActionListener(e -> {
            int address = ramComboBox.getSelectedIndex();
            int value = memory.readDataByte(address);
            JOptionPane.showMessageDialog(resultsFrame, "RAM[" + address + "] = " + value);
        });

        JLabel romLabel = new JLabel("ROM:");
        JComboBox<String> romComboBox = new JComboBox<>();
        
        // Use a fixed size for ROM (4096)
        for (int i = 0; i < 4096; i++) {
            romComboBox.addItem("0x" + String.format("%04X", i));
        }
        romComboBox.addActionListener(e -> {
            int address = romComboBox.getSelectedIndex();
            int value = memory.readByte(address);
            JOptionPane.showMessageDialog(resultsFrame, "ROM[" + address + "] = " + value);
        });

        // Add components to the bottom panel
        bottomPanel.add(ramLabel);
        bottomPanel.add(ramComboBox);
        bottomPanel.add(romLabel);
        bottomPanel.add(romComboBox);

        // Add all panels to results frame
        resultsFrame.add(mainResultPanel, BorderLayout.CENTER);
        resultsFrame.add(bottomPanel, BorderLayout.SOUTH);

        resultsFrame.setSize(500, 300);
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setVisible(true);
    }

    private static void loadOpcodesFromFile(Memory memory, String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    int opcode = Integer.parseInt(line.substring(2), 16); // Remove "0x" and parse
                    memory.writeByte(i, (byte) opcode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading opcodes from file: " + e.getMessage());
        }
    }
}
