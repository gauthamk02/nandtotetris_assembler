import java.io.*;
import java.util.*;

public class Assembler{
    public static void main(String args[]) {
        if(args.length != 1) {
            System.out.println("Argument Error!");
            System.out.println(args.length);
            return;
        }

        String inFilename = args[0];
        String outFilename = inFilename.substring(0,inFilename.lastIndexOf('.')) + ".hack";
        File infile = new File(inFilename);
        Scanner filein;
        ArrayList<String> codeFile = new ArrayList<>();
        ArrayList<String> asmFile = new ArrayList<>();

        try{
            filein = new Scanner(infile);
            while(filein.hasNextLine()){
                String line = filein.nextLine();
                
                //Remove inline comments and and skip comment lines
                if(line.startsWith("//") || line.isEmpty()) continue;
                if(line.contains("//")) {
                    line = line.substring(0, line.indexOf("//"));
                } 

                codeFile.add(line.replaceAll("\\s+", ""));
            }

            Parser parser = new Parser(codeFile);
            parser.readLabels();

            while(parser.hasMoreCommands()) {
                asmFile.add(parser.convertnextCommand());
            }

            printFile(asmFile);

            writetoFile(asmFile, outFilename);
        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println(e.getStackTrace());
        }

        System.out.println("Assembly code stored at " + outFilename);
    }
    static void writetoFile(ArrayList<String> list, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for(String str : list) {
            fw.write(str + System.lineSeparator());
        }

        fw.close();
    }
    static void printFile(ArrayList<String> file){
        for(String str : file) System.out.println(str);
        System.out.println();
    }
}