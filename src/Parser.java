import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Parser{

    ArrayList<String> codelist;
    HashMap<String, Integer> labelMap;
    HashMap<String, Integer> varMap;
    InstructionSet instSet;

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
        instSet = new InstructionSet();
        labelMap = new HashMap<String, Integer>();
        varMap = new HashMap<String, Integer>();
    }

    //Returns true if there are more commands left to parse
    Boolean hasMoreCommands() {
        if(currLine < codelist.size()) return true;
        return false;
    }

    String convertnextCommand() throws Exception {
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

    void readLabels() throws Exception {
        for(int i  = 0; i < codelist.size(); i++) {
            String code = codelist.get(i);
            if (identifyCommand(code) == Command.Label) {
                String label = code.replace("(", "");
                label = label.replace(")", "");
                if(instSet.symMap.containsKey(label)) {
                    throw new Exception("Label name " + label + " at line" + (i + 1) + " already exist as symbol");
                }
                if(labelMap.containsKey(label)) {
                    throw new Exception("Duplicate label " + label + " at line " + (i + 1));
                }
                labelMap.put(label, i);
                codelist.remove(i--);
            }
        }
    }

    private String parseAinst(String code) throws Exception {
        String hackinst = "0";
        String Ainst = code.replace("@", "");
        //Constant A-instruction @112554....
        if(Ainst.chars().allMatch(Character::isDigit)) {
            Ainst = Integer.toBinaryString(Integer.parseInt(Ainst));
            hackinst += to16bitInstruction(Ainst);
        }
        //Predefined symbols @R0, @R7, @SP....
        else if(instSet.symMap.containsKey(Ainst)) {
            int symval = instSet.symMap.get(Ainst);
            Ainst = Integer.toBinaryString(symval);
            hackinst += to16bitInstruction(Ainst);
        }
        //Labels @LOOP, @STOP....
        else if(labelMap.containsKey(Ainst)) {
            int labelval = labelMap.get(Ainst);
            Ainst = Integer.toBinaryString(labelval);
            hackinst += to16bitInstruction(Ainst);
        }
        //Variables @i, @x....
        else if(varMap.containsKey(Ainst)) {
            int varval = varMap.get(Ainst);
            Ainst = Integer.toBinaryString(varval);
            hackinst += to16bitInstruction(Ainst);
        }
        //New variable, add to variable map
        else {
            String pos = Integer.toBinaryString(varMap.size() + 16);
            varMap.put(Ainst, varMap.size() + 16);
            hackinst += to16bitInstruction(pos);
        }
        return hackinst;
    }

    private String to16bitInstruction(String Ainst) {
        return String.join("", Collections.nCopies(15 - Ainst.length(), "0")) + Ainst;
    }

    private Command identifyCommand(String code) throws Exception {
        if(code.charAt(0) == '@') {
            return Command.Ainst;
        }
        else if(code.charAt(0) == '(') {
            if(code.charAt(code.length() - 1) != ')') throw new Exception("Sytax Error at line " + currLine);
            return Command.Label;
        }
        else return Command.Cinst;
    }

    private String parseDest(String code) {
        if(!code.contains("=")) {
            return instSet.destMap.get("null");
        }

        String dest = code.substring(0, code.indexOf("="));
        return instSet.destMap.get(dest);
    }

    private String parseComp(String code) {
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
            asm += instSet.compMap.get(comp);
        }
        else if(abit == '1') {
            asm += "1";
            comp = comp.replace("M", "X");
            asm += instSet.compMap.get(comp);
        }
        return asm;
    }

    private String parseJump(String code) {
        if(code.contains(";")) {
            String jmp = code.substring(code.indexOf(";") + 1, code.length());
            return instSet.jumpMap.get(jmp);
        }

        return instSet.jumpMap.get("null");
    }
}