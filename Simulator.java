import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private static Memory memory;
    private static CPU cpu;
    private static Pins pins;
    private static InstructionSet instructionSet;

    private static ArrayList<LED> leds = new ArrayList<>();
    private static ArrayList<Resistor> resistors = new ArrayList<>();
    private static ArrayList<npn> npnTransistors = new ArrayList<>();
    private static ArrayList<pnp> pnpTransistors = new ArrayList<>();
    private static ArrayList<seven_seg_display> sevenSegDisplays = new ArrayList<>();

    private static JPanel initPanel;
    private static DefaultListModel<String> peripheralsListModel;

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

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton ledButton = new JButton("LED");
        JButton resistorButton = new JButton("Resistor");
        JButton npnButton = new JButton("NPN");
        JButton pnpButton = new JButton("PNP");
        JButton sevenSegmentButton = new JButton("7-Segment");

        buttonPanel.add(ledButton);
        buttonPanel.add(npnButton);
        buttonPanel.add(pnpButton);
        buttonPanel.add(resistorButton);
        buttonPanel.add(sevenSegmentButton);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Initialize the initPanel for displaying peripherals list
        initPanel = new JPanel(new BorderLayout());
        JLabel initLabel = new JLabel("Initialize Peripherals", JLabel.CENTER);
        initPanel.add(initLabel, BorderLayout.NORTH);

        // List model and JList to display initialized peripherals
        peripheralsListModel = new DefaultListModel<>();
        JList<String> peripheralsList = new JList<>(peripheralsListModel);
        JScrollPane scrollPane = new JScrollPane(peripheralsList);
        initPanel.add(scrollPane, BorderLayout.CENTER);
        initPanel.setPreferredSize(new Dimension(150, frame.getHeight()));

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(initPanel, BorderLayout.EAST);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> executeProgram());
        mainPanel.add(runButton, BorderLayout.SOUTH);

        ledButton.addActionListener(e -> initializeLED());
        resistorButton.addActionListener(e -> initializeResistor());
        npnButton.addActionListener(e -> initializeNPN());
        pnpButton.addActionListener(e -> initializePNP());
        sevenSegmentButton.addActionListener(e -> initializeSevenSegmentDisplay());

        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void initializeLED() {
        JDialog dialog = new JDialog((Frame) null, "Initialize LED", true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel anodeLabel = new JLabel("Anode Pin:");
        JComboBox<String> anodeBox = new JComboBox<>(getPinNames());
        JLabel cathodeLabel = new JLabel("Cathode Pin:");
        JComboBox<String> cathodeBox = new JComboBox<>(getPinNames());
        JLabel colorLabel = new JLabel("Color:");
        JTextField colorField = new JTextField("Red");

        panel.add(anodeLabel);
        panel.add(anodeBox);
        panel.add(cathodeLabel);
        panel.add(cathodeBox);
        panel.add(colorLabel);
        panel.add(colorField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String anodePin = (String) anodeBox.getSelectedItem();
            String cathodePin = (String) cathodeBox.getSelectedItem();
            String color = colorField.getText();

            LED led = new LED(color);
            led.connect(getPinByName(anodePin), getPinByName(cathodePin));
            leds.add(led);

            peripheralsListModel.addElement(color + " LED"); // Add LED to list
            dialog.dispose();
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void initializeResistor() {
        JDialog dialog = new JDialog((Frame) null, "Initialize Resistor", true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel pin1Label = new JLabel("Pin 1:");
        JComboBox<String> pin1Box = new JComboBox<>(getPinNames());
        JLabel pin2Label = new JLabel("Pin 2:");
        JComboBox<String> pin2Box = new JComboBox<>(getPinNames());
        JLabel resistanceLabel = new JLabel("Resistance (Ohms):");
        JTextField resistanceField = new JTextField("10000");

        panel.add(pin1Label);
        panel.add(pin1Box);
        panel.add(pin2Label);
        panel.add(pin2Box);
        panel.add(resistanceLabel);
        panel.add(resistanceField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String pin1 = (String) pin1Box.getSelectedItem();
            String pin2 = (String) pin2Box.getSelectedItem();
            double resistance = Double.parseDouble(resistanceField.getText());

            Resistor resistor = new Resistor(resistance);
            resistor.connect(getPinByName(pin1), getPinByName(pin2));
            resistors.add(resistor);

            peripheralsListModel.addElement("Resistor (" + resistance + "Ω)"); // Add Resistor to list
            dialog.dispose();
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void initializeSevenSegmentDisplay() {
        JDialog dialog = new JDialog((Frame) null, "Initialize 7-Segment Display", true);
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        JLabel enLabel = new JLabel("Enable Pin:");
        JComboBox<String> enBox = new JComboBox<>(getPinNames());
        JLabel aLabel = new JLabel("A Pin:");
        JComboBox<String> aBox = new JComboBox<>(getPinNames());
        JLabel bLabel = new JLabel("B Pin:");
        JComboBox<String> bBox = new JComboBox<>(getPinNames());
        JLabel cLabel = new JLabel("C Pin:");
        JComboBox<String> cBox = new JComboBox<>(getPinNames());
        JLabel dLabel = new JLabel("D Pin:");
        JComboBox<String> dBox = new JComboBox<>(getPinNames());
        JLabel eLabel = new JLabel("E Pin:");
        JComboBox<String> eBox = new JComboBox<>(getPinNames());
        JLabel fLabel = new JLabel("F Pin:");
        JComboBox<String> fBox = new JComboBox<>(getPinNames());
        JLabel gLabel = new JLabel("G Pin:");
        JComboBox<String> gBox = new JComboBox<>(getPinNames());

        panel.add(enLabel);
        panel.add(enBox);
        panel.add(aLabel);
        panel.add(aBox);
        panel.add(bLabel);
        panel.add(bBox);
        panel.add(cLabel);
        panel.add(cBox);
        panel.add(dLabel);
        panel.add(dBox);
        panel.add(eLabel);
        panel.add(eBox);
        panel.add(fLabel);
        panel.add(fBox);
        panel.add(gLabel);
        panel.add(gBox);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            seven_seg_display ssd = new seven_seg_display();
            ssd.connect(
                getPinByName((String) enBox.getSelectedItem()),
                getPinByName((String) aBox.getSelectedItem()),
                getPinByName((String) bBox.getSelectedItem()),
                getPinByName((String) cBox.getSelectedItem()),
                getPinByName((String) dBox.getSelectedItem()),
                getPinByName((String) eBox.getSelectedItem()),
                getPinByName((String) fBox.getSelectedItem()),
                getPinByName((String) gBox.getSelectedItem())
            );
            sevenSegDisplays.add(ssd);

            peripheralsListModel.addElement("7-Segment Display " + ssd.id); // Add SSD to list
            dialog.dispose();
        });

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void initializeNPN() {
        JDialog dialog = new JDialog((Frame) null, "Initialize NPN Transistor", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel baseLabel = new JLabel("Base Pin:");
        JComboBox<String> baseBox = new JComboBox<>(getPinNames());
        JLabel collectorLabel = new JLabel("Collector Pin:");
        JComboBox<String> collectorBox = new JComboBox<>(getPinNames());
        JLabel emitterLabel = new JLabel("Emitter Pin:");
        JComboBox<String> emitterBox = new JComboBox<>(getPinNames());

        dialog.add(baseLabel);
        dialog.add(baseBox);
        dialog.add(collectorLabel);
        dialog.add(collectorBox);
        dialog.add(emitterLabel);
        dialog.add(emitterBox);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String basePin = (String) baseBox.getSelectedItem();
            String collectorPin = (String) collectorBox.getSelectedItem();
            String emitterPin = (String) emitterBox.getSelectedItem();

            npn npnTransistor = new npn();
            npnTransistor.connect(getPinByName(basePin), getPinByName(collectorPin), getPinByName(emitterPin));
            npnTransistors.add(npnTransistor);

            peripheralsListModel.addElement("NPN Transistor " + npnTransistor.id); // Add NPN to list
            dialog.dispose();
        });
        dialog.add(okButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void initializePNP() {
        JDialog dialog = new JDialog((Frame) null, "Initialize PNP Transistor", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel baseLabel = new JLabel("Base Pin:");
        JComboBox<String> baseBox = new JComboBox<>(getPinNames());
        JLabel collectorLabel = new JLabel("Collector Pin:");
        JComboBox<String> collectorBox = new JComboBox<>(getPinNames());
        JLabel emitterLabel = new JLabel("Emitter Pin:");
        JComboBox<String> emitterBox = new JComboBox<>(getPinNames());

        dialog.add(baseLabel);
        dialog.add(baseBox);
        dialog.add(collectorLabel);
        dialog.add(collectorBox);
        dialog.add(emitterLabel);
        dialog.add(emitterBox);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String basePin = (String) baseBox.getSelectedItem();
            String collectorPin = (String) collectorBox.getSelectedItem();
            String emitterPin = (String) emitterBox.getSelectedItem();

            pnp pnpTransistor = new pnp();
            pnpTransistor.connect(getPinByName(basePin), getPinByName(collectorPin), getPinByName(emitterPin));
            pnpTransistors.add(pnpTransistor);

            peripheralsListModel.addElement("PNP Transistor " + pnpTransistor.id); // Add PNP to list
            dialog.dispose();
        });
        dialog.add(okButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

 private static void executeProgram() {
        loadOpcodesFromFile(memory, "instructions.txt");
        executeInstructions();
        displayResults();
    }

    private static void executeInstructions() {
        while (true) {
			
            int opcode = memory.readByte(cpu.getProgramCounter());
            if (opcode == 0xFF) {
                break;
            }
            cpu.cycle();
        }
    }

    private static void displayResults() {
        JFrame resultsFrame = new JFrame("Simulation Results");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultsFrame.setLayout(new BorderLayout());

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.add(new JLabel("Accumulator: 0x" + String.format("%02X", cpu.getAccumulator())));
        registerPanel.add(new JLabel("DPTR: 0x" + String.format("%04X", cpu.getdptr())));
        registerPanel.add(new JLabel("B Register: 0x" + String.format("%02X", cpu.getb_reg())));
        registerPanel.add(new JLabel("Program Counter: 0x" + String.format("%04X", cpu.getProgramCounter())));

        JPanel flagPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        flagPanel.add(new JLabel("Carry Flag (CY): " + cpu.getcarryFlag()));
        flagPanel.add(new JLabel("Auxiliary Carry Flag (AC): " + cpu.getauxiliaryCarryFlag()));
        flagPanel.add(new JLabel("FO: " + cpu.getfo()));
        flagPanel.add(new JLabel("RS1: " + cpu.getregset1()));
        flagPanel.add(new JLabel("RS0: " + cpu.getregset0()));
        flagPanel.add(new JLabel("Overflow Flag (OV): " + cpu.getoverflowFlag()));
        flagPanel.add(new JLabel("Parity Flag (P): " + cpu.getparityFlag()));

        JPanel mainResultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainResultPanel.add(registerPanel);
        mainResultPanel.add(flagPanel);

        JPanel peripheralPanel = new JPanel();
        peripheralPanel.setLayout(new BoxLayout(peripheralPanel, BoxLayout.Y_AXIS));
        peripheralPanel.setBorder(BorderFactory.createTitledBorder("Peripheral States"));

        for (LED led : leds) {
            peripheralPanel.add(new JLabel(led.checkState()));
        }
        for (Resistor resistor : resistors) {
            peripheralPanel.add(new JLabel(resistor.checkVoltageDrop()));
        }
        for (npn npnTransistor : npnTransistors) {
            peripheralPanel.add(new JLabel(npnTransistor.checkVoltage()));
        }
        for (pnp pnpTransistor : pnpTransistors) {
            peripheralPanel.add(new JLabel(pnpTransistor.checkVoltage()));
        }

        JPanel sevenSegPanel = new JPanel();
        sevenSegPanel.setLayout(new BoxLayout(sevenSegPanel, BoxLayout.Y_AXIS));
        sevenSegPanel.setBorder(BorderFactory.createTitledBorder("7-Segment Display States"));

        for (seven_seg_display ssd : sevenSegDisplays) {
            String[] displayLines = ssd.run_ssd();
            sevenSegPanel.add(new JLabel(displayLines[0]));
            sevenSegPanel.add(new JLabel(displayLines[1]));
            sevenSegPanel.add(new JLabel(displayLines[2]));
        }

        JPanel peripheralAndDisplayPanel = new JPanel(new GridLayout(1, 2));
        peripheralAndDisplayPanel.add(peripheralPanel);
        peripheralAndDisplayPanel.add(sevenSegPanel);

        resultsFrame.add(mainResultPanel, BorderLayout.NORTH);
        resultsFrame.add(peripheralAndDisplayPanel, BorderLayout.CENTER);

        JPanel memoryPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel ramLabel = new JLabel("RAM:");
        JComboBox<String> ramComboBox = new JComboBox<>();
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
        for (int i = 0; i < 4096; i++) {
            romComboBox.addItem("0x" + String.format("%04X", i));
        }
        romComboBox.addActionListener(e -> {
            int address = romComboBox.getSelectedIndex();
            int value = memory.readByte(address);
            JOptionPane.showMessageDialog(resultsFrame, "ROM[" + address + "] = " + value);
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            cpu.reset();
            leds.clear();
            resistors.clear();
            npnTransistors.clear();
            pnpTransistors.clear();
            sevenSegDisplays.clear();
            npn.resetInstanceCount();
            pnp.resetInstanceCount();
			peripheralsListModel.clear(); 
            resultsFrame.dispose();
        });

        memoryPanel.add(ramLabel);
        memoryPanel.add(ramComboBox);
        memoryPanel.add(romLabel);
        memoryPanel.add(romComboBox);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(memoryPanel, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.SOUTH);

        resultsFrame.add(bottomPanel, BorderLayout.SOUTH);

        resultsFrame.setSize(700, 600);
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setVisible(true);
    }

    private static void loadOpcodesFromFile(Memory memory, String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    int opcode = Integer.parseInt(line.substring(2), 16);
                    memory.writeByte(i, (byte) opcode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading opcodes from file: " + e.getMessage());
        }
    }



    
    private static String[] getPinNames() {
        return new String[]{"p0_0", "p0_1", "p0_2", "p0_3", "p0_4", "p0_5", "p0_6", "p0_7",
                            "p1_0", "p1_1", "p1_2", "p1_3", "p1_4", "p1_5", "p1_6", "p1_7",
                            "p2_0", "p2_1", "p2_2", "p2_3", "p2_4", "p2_5", "p2_6", "p2_7"};
    }

    private static Pin getPinByName(String pinName) {
        try {
            return (Pin) Pins.class.getField(pinName).get(pins);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
