/*
 * Name: Alina Chaiyasarikul
 * Date: June 18th, 2019
 * 
 * MIPS Disassember: A partial disassembler for MIPS instruction.
 * 	Starting with an address of 9A040, the program will convert 32-bit machine instructions that a compiler or assembler
 * 	produces, to the original source instructions that created those 32-bit machine instructions. 
 * 
 */

public class MIPSdisassember {

	public static void main(String[] args) {

		MIPSdisassember  me = new MIPSdisassember();
		
		//Initial address given to begin with
		int pCounter = 0x0009A040;
		
		//Test values: int testValues[] = {0x00A63820, 0x8D070004, 0xAE57FFFC, 0x10E80004};
		
		//Input values to decode:
		int testValues[] = {0x032BA020, 0x8CE90014, 0x12A90003, 0x022DA822, 0xADB30020, 0x02697824, 0xAE8FFFF4, 
		0x018C6020, 0x02A4A825, 0x158FFFF7};//, 0x8ECDFFF0};
		
		//Decoding each instruction
		for (int x : testValues) {
			pCounter = me.MIPSdisassemble(x, pCounter);
		}
		
	}

	
	//Method that takes in the current address and 32-bit machine instruction, and returns the program counter 
	//after printing the original source instructions that would have created those 32-bit machine instructions.
	public int MIPSdisassemble(int test, int pCounter){
		
		//Format components
		int opco  = 0xFC000000; //0b 1111 1100 0000 0000 0000 0000 0000 0000;
		int reg1s = 0x03E00000; //0b 0000 0011 1110 0000 0000 0000 0000 0000;
		int reg2  = 0x001F0000; //0b 0000 0000 0001 1111 0000 0000 0000 0000;
		int reg3d = 0x0000F800; //0b 0000 0000 0000 0000 1111 1000 0000 0000;
		int func  = 0x0000003F; //0b 0000 0000 0000 0000 0000 0000 0011 1111;
		int cons  = 0x0000FFFF; //0b 0000 0000 0000 0000 1111 1111 1111 1111;
		
		//Format (R vs I): If I-format, tells you the instruction
		int tform = (test & opco) >>> 26;
		
		//Registers of test instruction
		int treg1s; 
		int treg2;
		int treg3d; //Will not have a value for the I-format
		
		int tfunct; //R-format FUNCTION
		short tcons; //I-format OFFSET/CONSTANT - 16 bits since it can be signed
		
		System.out.print(String.format("%05X", pCounter) + " ");
		
		//Incrementing program counter
		pCounter += 0b0100;
		
		if ((tform) == 0) { //R-format
			
			treg1s = (test & reg1s) >>> 21; //source
			treg2 = (test & reg2) >>> 16; //source
			treg3d = (test & reg3d) >>> 11; //destination
			
			tfunct = test & func; //Switch based on function call
			
			switch (tfunct){
				case 0x20:
					System.out.print("add");
					break;
					
				case 0x22:
					System.out.print("sub");
					break;
				
				case 0x24:
					System.out.print("and");
					break;
					
				case 0x25:
					System.out.print("or ");
					break;
					
				case 0x2A:
					System.out.print("slt");
					break;
				
				default:
					System.out.println("Unknown command");	
			}
			
			//Printing out dest, src, src - instruction already printed
			
			System.out.println(" $" + treg3d + ", $" + treg1s + ", $" + treg2);
		} 
		
		
		else { //I-format
			
			treg1s = (test & reg1s) >>> 21; //source (the one after the number)
			treg2 = (test & reg2) >>> 16; //source or destination
			tcons = (short) ((test & cons));
			
			//tform determines what instruction
			
			//Load
			if (tform == 0x23) {
				//Printing out instruc src/dest , const(src)
				System.out.println("lw  $" + treg2 + ", " + tcons + "($" + treg1s + ")");
			}
			
			//Store
			else if (tform == 0x2B) {
				//Printing out instruc src/dest , const(src)
				System.out.println("sw  $" + treg2 + ", " + tcons + "($" + treg1s + ")");
			}
			
			//Branches
			else {
				
				//The MIPS instruction already takes into account the +4 to program counter.
				//Since we are decoding the instruction, we only need to shift the offset value by 2.
				int branchAddress = pCounter + (tcons << 2);
				
				//Equal
				if (tform == 0x4) {
					System.out.print("beq");
				}
				
				//Not Equal
				if (tform == 0x5) {
					System.out.print("bne");
				}
				
				//Printing out src , src, target address - instruction already printed
				System.out.println(" $" + treg1s + ", $" + treg2 + ", address " + String.format("%05X", branchAddress));
			}
		}

		return pCounter;
	}
}
