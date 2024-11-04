interface Peripheral {
    void connect(Pin... pins);
}

class Pin {
    private boolean state; 
    private String pinName; // For identification (e.g., P1_0 for pin 0 of Port 1)

    public Pin(String pinName) {
        this.pinName = pinName;
        this.state = false; // Default to LOW
    }

    public void setHigh() {
        this.state = true;
    }

    public void setLow() {
        this.state = false;
    }

    public boolean isHigh() {
        return state;
    }

    public String getPinName() {
        return pinName;
    }

    @Override
    public String toString() {
        return pinName + ": " + (state ? "HIGH" : "LOW");
    }
}

class VoltageChecker implements Peripheral {
    private Pin monitoredPin;

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 1) {
            this.monitoredPin = pins[0];
        } else {
            throw new IllegalArgumentException("Voltage Checker requires 1 pin.");
        }
    }

    public void checkVoltage() {
        System.out.println(monitoredPin.getPinName() + " is " + (monitoredPin.isHigh() ? "HIGH" : "LOW"));
    }
}

class LED implements Peripheral {
    private Pin anode;
    private Pin cathode;
    private String color;

    public LED() { 
        this.color = "red";
    }

    public LED(String color) {
        this.color = color;    
    }

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 2) {
            this.anode = pins[0];
            this.cathode = pins[1];
        } else {
            throw new IllegalArgumentException("LED requires 2 pins (anode and cathode).");
        }
    }

    public String checkState() {
        if (anode.isHigh() && !cathode.isHigh()) {
            return color + " LED is ON";
        } else {
            return color + " LED is OFF";
        }
    }

    public Pin getCathode() {
        return cathode;
    }
}

class seven_seg_display implements Peripheral {
    private static int instanceCount = 0;
    private final int id;
    private Pin en;
    private Pin A;
    private Pin B;
    private Pin C;
    private Pin D;
    private Pin E;
    private Pin F;
    private Pin G;

    public seven_seg_display() {
        this.id = ++instanceCount;
    }

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 8) {
            this.en = pins[0];
            this.A  = pins[1];
            this.B  = pins[2];
            this.C  = pins[3];
            this.D  = pins[4];
            this.E  = pins[5];
            this.F  = pins[6];
            this.G  = pins[7];
        } else {
            throw new IllegalArgumentException("7 SEGMENT DISPLAY requires 8 pins (EN and 7 data pins).");
        }
    }

    public String[] run_ssd() {
        if (!en.isHigh()) {
            return new String[]{"Display disabled!", "", ""};
        }

        if (A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && !G.isHigh()) {
            return new String[]{" __ ", "|  |", "|__|"}; // code for 0
        } else if (!A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && !F.isHigh() && !G.isHigh()) {
            return new String[]{"   ", "  |", "  |"}; // code for 1
        } else if (A.isHigh() && B.isHigh() && !C.isHigh() && D.isHigh() && E.isHigh() && !F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", " __|", "|__ "}; // code for 2
        } else if (A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && !F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", " __|", " __|"}; // code for 3
        } else if (!A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()) {
            return new String[]{"    ", "|__|", "   |"}; // code for 4
        } else if (A.isHigh() && !B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", "|__ ", " __|"}; // code for 5
        } else if (A.isHigh() && !B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", "|__ ", "|__|"}; // code for 6
        } else if (A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && !F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", "    |", "    |"}; // code for 7
        } else if (A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", "|__|", "|__|"}; // code for 8
        } else if (A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()) {
            return new String[]{" __ ", "|__|", " __|"}; // code for 9
        } else {
            return new String[]{"   ", "   ", "   "}; // blank display
        }
    }

    public String getDisplayOutput() {
        String[] output = run_ssd();
        return "7-segment display " + id + ":\n" + output[0] + "\n" + output[1] + "\n" + output[2];
    }

    public static void resetInstanceCount() {
        instanceCount = 0;
    }
}




class Resistor implements Peripheral {
    private Pin rpin1;
    private Pin rpin2;
    private double resistance;

    public Resistor() {
        this.resistance = 10_000;
    }

    public Resistor(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 2) {
            this.rpin1 = pins[0];
            this.rpin2 = pins[1];
        } else {
            throw new IllegalArgumentException("Resistor requires 2 pins.");
        }
    }

    public String checkVoltageDrop() {
        if (rpin1.isHigh() && !rpin2.isHigh()) {
            return "Current flows through the resistor with resistance: " + resistance + " ohms.";
        } else {
            return "No current flows through the resistor.";
        }
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }
}

class npn implements Peripheral {
    private static int instanceCount = 0;
    private final int id;
    private Pin base;
    private Pin emitter;
    private Pin collector;

    public npn() {
        this.id = ++instanceCount; // Assign unique id to each instance
    }

    public static void resetInstanceCount() {
        instanceCount = 0; // Resets the count back to 0
    }

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 3) {
            this.base = pins[0];
            this.collector = pins[1];
            this.emitter = pins[2];
        } else {
            throw new IllegalArgumentException("NPN Transistor requires 3 pins.");
        }
    }

    public String checkVoltage() {
        String status = (base.isHigh() && !emitter.isHigh() && collector.isHigh()) ? 
                        "Current flows through the npn" : 
                        "No current flows through the npn";
        return "npn" + id + ": " + status + ".";
    }
}

class pnp implements Peripheral {
    private static int instanceCount = 0;
    private final int id;
    private Pin base;
    private Pin emitter;
    private Pin collector;

    public pnp() {
        this.id = ++instanceCount; // Assign unique id to each instance
    }

    public static void resetInstanceCount() {
        instanceCount = 0; // Resets the count back to 0
    }

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 3) {
            this.base = pins[0];
            this.collector = pins[1];
            this.emitter = pins[2];
        } else {
            throw new IllegalArgumentException("PNP Transistor requires 3 pins.");
        }
    }

    public String checkVoltage() {
        String status = (!base.isHigh() && emitter.isHigh() && !collector.isHigh()) ? 
                        "Current flows through the pnp" : 
                        "No current flows through the pnp";
        return "pnp" + id + ": " + status + ".";
    }
}


class Ground extends Pin {

    public Ground() {
        super("Ground");
        this.setLow(); // Ground is always LOW
    }

    @Override
    public void setHigh() {
        System.out.println("Cannot set ground to HIGH.");
    }

    @Override
    public void setLow() {
        // Ground remains LOW
    }
}
