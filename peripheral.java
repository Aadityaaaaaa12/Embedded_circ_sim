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

class Pins {
	//port0
    public Pin p0_0 = new Pin("P0_0");//AD0	
    public Pin p0_1 = new Pin("P0_1");//AD1
	public Pin p0_2 = new Pin("P0_2");//AD2
	public Pin p0_3 = new Pin("P0_3");//AD3
	public Pin p0_4 = new Pin("P0_4");//AD4
	public Pin p0_5 = new Pin("P0_5");//AD5
	public Pin p0_6 = new Pin("P0_6");//AD6
	public Pin p0_7 = new Pin("P0_7");//AD7
	
	//port1
    public Pin p1_0 = new Pin("P1_0");
    public Pin p1_1 = new Pin("P1_1");
	public Pin p1_2 = new Pin("P1_2");
	public Pin p1_3 = new Pin("P1_3");
	public Pin p1_4 = new Pin("P1_4");
	public Pin p1_5 = new Pin("P1_5");
	public Pin p1_6 = new Pin("P1_6");	
	public Pin p1_7 = new Pin("P1_7");
	
	//port2
	public Pin p2_0 = new Pin("P2_0");//AD8
    public Pin p2_1 = new Pin("P2_1");//AD9
	public Pin p2_2 = new Pin("P2_2");//AD10
	public Pin p2_3 = new Pin("P2_3");//AD11
	public Pin p2_4 = new Pin("P2_4");//AD12
	public Pin p2_5 = new Pin("P2_5");//AD13
	public Pin p2_6 = new Pin("P2_6");//AD14
	public Pin p2_7 = new Pin("P2_7");//AD15
	
	//port3
	public Pin p3_0 = new Pin("P3_0"); //RXD
    public Pin p3_1 = new Pin("P3_1"); //TXD
	public Pin p3_2 = new Pin("P3_2"); //INT0~
	public Pin p3_3 = new Pin("P3_3"); //INT1~
	public Pin p3_4 = new Pin("P3_4"); //TO
	public Pin p3_5 = new Pin("P3_5"); //T1
	public Pin p3_6 = new Pin("P3_6"); //WR~	
	public Pin p3_7 = new Pin("P3_7"); //RD~

	//non-port pins
	public Pin RST = new Pin("RST");
	public Pin XTAL1 = new Pin("XTAL1");
	public Pin XTAL2 = new Pin("XTAL2");
	public Pin GND = new Pin("GND");
	public Pin VCC = new Pin("VCC");
	public Pin EA_VPP = new Pin("EA_VPP");// EA~/VPP
	public Pin ALE_PROG = new Pin("ALE_PROG"); //ALE/PROG~
	public Pin PSEN = new Pin("PSEN"); //~PSEN
	
	
}

class LED implements Peripheral {
    private Pin anode;
    private Pin cathode;
	private String colour;
	
	public LED(){ //constructor defaults to red
	this.colour = "red";
	}
	
	public LED(String colour){ //constructor takes user defined led colour;
	this.colour = colour;	
	}

    @Override
    public void connect(Pin... pins) {
        if (pins.length == 2) {
            this.anode = pins[0];  // Connect to P1.0
            this.cathode = pins[1]; // Connect to Resistor (which will go to ground)
        } else {
            throw new IllegalArgumentException("LED requires 2 pins (anode and cathode).");
        }
    }

    public void checkState() {
        if (anode.isHigh() && !cathode.isHigh()) {
            System.out.println(colour + " LED is ON");
        } else {
            System.out.println("LED is OFF");
        }
    }

    public Pin getCathode() {
        return cathode;
    }
}

class seven_seg_display implements Peripheral{
private Pin en;
private Pin A;
private Pin	B;
private Pin C;
private Pin D;
private Pin E;
private Pin F;
private Pin G;
	
	
	@Override
	public void connect(Pin... pins){
	if(pins.length == 8){
	this.en = pins[0];
	this.A  = pins[1];
	this.B  = pins[2];
	this.C  = pins[3];
	this.D  = pins[4];
	this.E  = pins[5];
	this.F  = pins[6];
	this.G  = pins[7];
	}else {
      throw new IllegalArgumentException("7 SEGMENT DISPLAY REQUIRES 8 PINS (EN AND 7 DAT PINS).");
       }
	}
		
	public void run_ssd(){ //function to run the seven segment display
		if(en.isHigh()){
			if(A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && !G.isHigh()){ //code for 0
				System.out.println(" __");
				System.out.println("|  |");
				System.out.println("|__|");
			}else if(!A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && !F.isHigh() && !G.isHigh()){ //code for 1
				System.out.println("|");
				System.out.println("|");
			}else if(A.isHigh() && B.isHigh() && !C.isHigh() && D.isHigh() && E.isHigh() && !F.isHigh() && G.isHigh()){ //code for 2
				System.out.println(" __");
				System.out.println(" __|");
				System.out.println("|__");
			}else if(A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && !F.isHigh() && G.isHigh()){ //code for 3
				System.out.println(" __");
				System.out.println(" __|");
				System.out.println(" __|");
			}else if(!A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()){ //code for 4
				System.out.println("|__|");
				System.out.println("   |");
			}else if(A.isHigh() && !B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()){ //code for 5
				System.out.println(" __");
				System.out.println("|__");
				System.out.println(" __|");
			}else if(A.isHigh() && !B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && G.isHigh()){ //code for 6
				System.out.println(" __");
				System.out.println("|__");
				System.out.println("|__|");
			}else if(A.isHigh() && B.isHigh() && C.isHigh() && !D.isHigh() && !E.isHigh() && !F.isHigh() && G.isHigh()){ //code for 7
				System.out.println(" __");
				System.out.println("   |");
				System.out.println("   |");
			}else if(A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && E.isHigh() && F.isHigh() && G.isHigh()){ // code for 8
				System.out.println(" __");
				System.out.println("|__|");
				System.out.println("|__|");
			}else if(A.isHigh() && B.isHigh() && C.isHigh() && D.isHigh() && !E.isHigh() && F.isHigh() && G.isHigh()){ //code for 9
				System.out.println(" __");
				System.out.println("|__|");
				System.out.println(" __|");
			}
		}else{
			System.out.println("Display disabled!!!!!");
		}
	}	
		
	
} 



class Resistor implements Peripheral {
    private Pin rpin1;
    private Pin rpin2;
    private double resistance;  // Store resistance value 

    // Default constructor with default resistance of 10k ohms
    public Resistor() {
        this.resistance = 10_000;  // Default resistance 10k ohms
    }

    // Overloaded constructor to specify resistance
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


    // Method to check the voltage drop across the resistor
    public void checkVoltageDrop() {
        System.out.println("Checking voltage between " + rpin1.getPinName() + " and " + rpin2.getPinName());
        if (rpin1.isHigh() && !rpin2.isHigh()) {
            System.out.println("Current flows through the resistor with resistance: " + resistance + " ohms.");
        } else {
            System.out.println("No current flows through the resistor.");
        }
    }

    // Getters for rpin1, rpin2, and resistance
    public Pin getRPin1() {
        return rpin1;
    }

    public Pin getRPin2() {
        return rpin2;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }
}






class npn extends VoltageChecker implements Peripheral{
	private Pin base;
	private Pin emitter;
	private Pin collector;
	
	@Override
    public void connect(Pin... pins) {
        if (pins.length == 3) {
            this.base = pins[0]; 
            this.collector = pins[1];  
			this.emitter = pins[2]; 
        } else {
            throw new IllegalArgumentException("Transistor requires 3 pins.");
        }
    }
	
	@Override
	public void checkVoltage(){
		if(base.isHigh() && !emitter.isHigh() && collector.isHigh()){ //will only work if emitter is low and rest are high
			System.out.println("Current flows through the npn transistor.");
		}else {
            System.out.println("No current flows through the npn transistor.");
        }
	}
	
	
}


class pnp extends VoltageChecker implements Peripheral{
	private Pin base;
	private Pin emitter;
	private Pin collector;
	
	@Override
    public void connect(Pin... pins) {
        if (pins.length == 3) {
            this.base = pins[0]; 
            this.collector = pins[1];  
			this.emitter = pins[2]; 
        } else {
            throw new IllegalArgumentException("Transistor requires 3 pins.");
        }
    }
	
	@Override
	public void checkVoltage(){ //will only work if emitter is high and rest are low
		if(!base.isHigh() && emitter.isHigh() && !collector.isHigh()){
			System.out.println("Current flows through the pnp transistor.");
		}else {
            System.out.println("No current flows through the pnp transistor.");
        }
	}
	
	
}





class Ground extends Pin {
	
    public Ground() {
        super("Ground");
        this.setLow(); // Ground is always LOW
    }

    @Override
    public void setHigh() {
        // Do nothing because ground should always be LOW
        System.out.println("Cannot set ground to HIGH.");
    }

    @Override
    public void setLow() {
        // Do nothing because ground is always LOW
    }
}
