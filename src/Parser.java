import java.util.Collections;
import java.util.HashMap;

public class Parser{
    HashMap<String, String> symMap;
    HashMap<String, String> destMap;
    HashMap<String, String> compMap;
    HashMap<String, String> jumpMap;

    enum Instruction {
        Ainst,
        Cinst,
        Label,
    };

    enum Destcode {
        
    }
    Parser() {
        initDestMap();
        initCompMap();
        initJumpMap();
        initSymMap();
    }
    String toAssembly(String code) throws Exception{
        String asm = "";
        Instruction inst = identifyInstruction(code);
        if(inst == Instruction.Cinst) {
            asm = "111";
            asm += parseComp(code); 
            asm += parseDest(code);
            asm += parseJump(code);

        }
        else if (inst == Instruction.Ainst) {
            asm += "0";
            String sym = code.replace("@", "");
            if(symMap.containsKey(sym)) {
                asm += symMap.get(sym);
            }
            else if(sym.chars().allMatch(Character::isDigit)) {
                sym = Integer.toBinaryString(Integer.parseInt(sym));
                asm += String.join("", Collections.nCopies(15 - sym.length(), "0")) + sym;
            }
            else {
                throw new Exception("Unidentified Symbol");
            }
        }
        return asm;
    }

    Instruction identifyInstruction(String code) {
        if(code.charAt(0) == '@') {
            return Instruction.Ainst;
        }
        else return Instruction.Cinst;
    }

    String parseDest(String code) {
        if(!code.contains("=")) {
            return destMap.get("null");
        }

        String dest = code.substring(0, code.indexOf("="));
        return destMap.get(dest);
    }

    String parseComp(String code){
        String asm = "";
        String comp;
        char abit = '0';
        int startind = 0;
        int endind = code.length();

        if(code.contains("=")) {
            startind = code.indexOf("=") + 1;
        }
        if(code.contains(";")) {
            endind = code.indexOf(";");
        }
        comp = code.substring(startind, endind);
        //System.out.print(comp + " --- ");
        if(comp.contains("M")) abit = '1';

        if(abit == '0') {
            asm += "0";
            comp = comp.replace("A", "X");
            asm += compMap.get(comp);
        }
        else if(abit == '1') {
            asm += "1";
            comp = comp.replace("M", "X");
            asm += compMap.get(comp);
        }
        return asm;
    }

    String parseJump(String code) {
        if(code.contains(";")) {
            String jmp = code.substring(code.indexOf(";") + 1, code.length());
            return jumpMap.get(jmp);
        }

        return jumpMap.get("null");
    }

    void initSymMap() {
        symMap = new HashMap<>();
    }

    void initDestMap() {
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

    void initCompMap() {
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
        compMap.put("D+1","001110");
        compMap.put("X+1","110111");
        compMap.put("D-1","001110");
        compMap.put("X-1","110010");
        compMap.put("D+X","000010");
        compMap.put("D-X","010011");
        compMap.put("X-D","000111");
        compMap.put("D&X","000000");
        compMap.put("D|X","010101");
    }

    void initJumpMap() {
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