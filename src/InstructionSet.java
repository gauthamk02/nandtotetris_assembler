import java.util.HashMap;

public class InstructionSet {
    HashMap<String, String> destMap;
    HashMap<String, String> compMap;
    HashMap<String, String> jumpMap;
    HashMap<String, String> symMap;

    InstructionSet() {
        initDestMap();
        initCompMap();
        initJumpMap();
        initSymMap();
    }

    private void initSymMap() {
        symMap = new HashMap<>();
        symMap.put("SP", "0");
        symMap.put("LCL", "1");
        symMap.put("ARG", "2");
        symMap.put("THIS", "3");
        symMap.put("THAT", "4");
        symMap.put("R0", "0");
        symMap.put("R1", "1");
        symMap.put("R2", "2");
        symMap.put("R3", "3");
        symMap.put("R4", "4");
        symMap.put("R5", "5");
        symMap.put("R6", "6");
        symMap.put("R7", "7");
        symMap.put("R8", "8");
        symMap.put("R9", "9");
        symMap.put("R10", "10");
        symMap.put("R11", "11");
        symMap.put("R12", "12");
        symMap.put("R13", "13");
        symMap.put("R14", "14");
        symMap.put("R15", "15");
        symMap.put("SCREEN", "16384");
        symMap.put("KBD", "24576");
    }

    private void initDestMap() {
        destMap = new HashMap<>();
        destMap.put("null","000");
        destMap.put("M","001");
        destMap.put("D","010");
        destMap.put("MD","011");
        destMap.put("A","100");
        destMap.put("AM","101");
        destMap.put("AD","110");
        destMap.put("AMD","111");
    }

    private void initCompMap() {
        compMap = new HashMap<>();
        compMap.put("0","101010");
        compMap.put("1","111111");
        compMap.put("-1","111010");
        compMap.put("D","001100");
        compMap.put("X","110000");
        compMap.put("!D","001101");
        compMap.put("!X","110001");
        compMap.put("-D","001111");
        compMap.put("-X","110011");
        compMap.put("D+1","011111");
        compMap.put("X+1","110111");
        compMap.put("D-1","001110");
        compMap.put("X-1","110010");
        compMap.put("D+X","000010");
        compMap.put("D-X","010011");
        compMap.put("X-D","000111");
        compMap.put("D&X","000000");
        compMap.put("D|X","010101");
    }

    private void initJumpMap() {
        jumpMap = new HashMap<>();
        jumpMap.put("null","000");
        jumpMap.put("JGT","001");
        jumpMap.put("JEQ","010");
        jumpMap.put("JGE","011");
        jumpMap.put("JLT","100");
        jumpMap.put("JNE","101");
        jumpMap.put("JLE","110");
        jumpMap.put("JMP","111");
    }
}
