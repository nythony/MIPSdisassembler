# MIPS Disassembler
A program that takes in a set of 32-bit machine instructions (set values stored as an array of hexadecimals) and disassembles them to the original MIPS instructions that outputs them. To mimic actual processors, the program counter is automatically incremented at the beginning of the loop (clock cycle).

The 9 MIPS instructions that this program accounts for: 
  1. add (addition)
  2. sub (subtraction)
  3. and (bitwise AND)
  4. or  (bitwise OR)
  5. slt (set on less than)
  6. lw  (load)
  7. sw  (store)
  8. bne (branch not equal)
  9. beq (branch on equal)

Initial program counter (PC) starts at address 0x0009A040. Since this is a 32-bit machine, a word is equivalent to 4 bytes. Thus, the PC is incremented by 4.

Dependent on the opcode, the instruction is broken down into its component using bitmasks.

R-formats: {e.g. add $3, $1, $2}
  opcode: 6 bits = 000000
  src: 5 bits = register number for source {1}
  src: 5 bits = register number for source {2}
  dest: 5 bits = register number for destination {3}
  shamt: 5 bits = for this program, it is always 0.
  func: 6 bits = the function code for the instruction {add = x20}
 
I-format: {e.g. lw $5, -10($4)}
  opcode: 6 bits = determines the instruction {lw = x23}
  src: 5 bits = register number for source {4}
  dest: 5 bits = register number for destination {5}
  const: 16 bits = constant
      - loads/stores: the offset from the register number to the address that the instruction needs to access {-10}. 
      - branches: the offset value from the program counter to the target address of the branch.

  For branches, instead of a label, the program will output the address where that label resides. 
    e.g. bne $4, $5, address 0x0009A044

