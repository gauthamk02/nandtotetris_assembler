import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Parser{

    ArrayList<String> codelist;
    HashMap<String, String> symMap;
    HashMap<String, String> labelMap;
    HashMap<String, String> varMap;
    HashMap<String, String> destMap;
    HashMap<String, String> compMap;
    HashMap<String, String> jumpMap;
    int currLine;
    int variablePosition;

    enum Command {
        Ainst,
        Cinst,
        Label,
    };

    Parser(ArrayList<String> codelist) {
        this.codelist = codelist;
        currLine = 0;
        variablePosition = 0;
        labelMap = new HashMap<String, String>();
        varMap = new HashMap<String, String>();
        initDestMap();
        initCompMap();
        initJumpMap();
        initSymMap();
    }

    Boolean hasMoreCommands() {
        if(currLine < codelist.size()) return true;
        return false;
    }

    String convertnextCommand() throws Exception{
        System.out.println(codelist.get(currLine));
        return toAssembly(codelist.get(currLine++));
    }
    
    private String toAssembly(String code) throws Exception{
        String asm = "";
        Command inst = identifyCommand(code);
        if(inst == Command.Cinst) {
            asm = "111";
            asm += parseComp(code); 
            asm += parseDest(code);
            asm += parseJump(code);

        }
        else if (inst == Command.Ainst) {
            asm = parseAinst(code);
        }
        return asm;
    }

    void readLabels() throws Exception{
        for(int i  = 0; i < codelist.size(); i++) {
            String code = codelist.get(i);
            if (identifyCommand(code) == Command.Label) {
                String label = code.replace("(", "");
                label = label.replace(")", "");
                if(symMap.containsKey(label)) {
                    throw new Exception("Label name " + label + " at line" + (i + 1) + " already exist as symbol");
                }
                if(labelMap.containsKey(label)) {
                    throw new Exception("Duplicate label " + label + " at line " + (i + 1));
                }
                labelMap.put(label, Integer.toString(i));
                codelist.remove(i--);
            }
        }
    }

    private String parseAinst(String code) throws Exception {
        String hackinst = "0";
        String Ainst = code.replace("@", "");
        //@112554....
        if(Ainst.chars().allMatch(Character::isDigit)) {
            Ainst = Integer.toBinaryString(Integer.parseInt(Ainst)); ///0000001010111
            hackinst += String.join("", Collections.nCopies(15 - Ainst.length(), "0")) + Ainst;
        }
        //@R0, @R7, @SP....
        else if(symMap.containsKey(Ainst)) {
            Ainst = symMap.get(Ainst);
            Ainst = Integer.toBinaryString(Integer.parseInt(Ainst));
            hackinst += String.join("", Collections.nCopies(15 - Ainst.length(), "0")) + Ainst;
        }
        //@LOOP, @STOP....
        else if(labelMap.containsKey(Ainst)) {
            Ainst = labelMap.get(Ainst);
            Ainst = Integer.toBinaryString(Integer.parseInt(Ainst));
            hackinst += String.join("", Collections.nCopies(15 - Ainst.length(), "0")) + Ainst;
        }
        //@i, @x....
        else if(varMap.containsKey(Ainst)){
            Ainst = varMap.get(Ainst);
            Ainst = Integer.toBinaryString(Integer.parseInt(Ainst));
            hackinst += String.join("", Collections.nCopies(15 - Ainst.length(), "0")) + Ainst;
        }
        //new variable
        else {
            String pos = Integer.toBinaryString(varMap.size() + 16);
            varMap.put(Ainst, Integer.toString(varMap.size() + 16));
            hackinst += String.join("", Collections.nCopies(15 - pos.length(), "0")) + pos;
        }
        return hackinst;
    }

    private Command identifyCommand(String code) {
        if(code.charAt(0) == '@') {
            return Command.Ainst;
        }
        else if(code.charAt(0) == '(') {
            return Command.Label;
        }
        else return Command.Cinst;
    }

    private String parseDest(String code) {
        if(!code.contains("=")) {
            return destMap.get("null");
        }

        String dest = code.substring(0, code.indexOf("="));
        return destMap.get(dest);
    }

    private String parseComp(String code){
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

    private String parseJump(String code) {
        if(code.contains(";")) {
            String jmp = code.substring(code.indexOf(";") + 1, code.length());
            return jumpMap.get(jmp);
        }

        return jumpMap.get("null");
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