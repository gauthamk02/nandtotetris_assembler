import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler{
    public static void main(String args[]) {
        if(args.length != 1) {
            System.out.println("Argument Error!");
            System.out.println(args.length);
            return;
        }

        String inFilename = args[0];
        File infile = new File(inFilename);
        Scanner filein;
        ArrayList<String> file = new ArrayList<>();

        try{
            filein = new Scanner(infile);
            while(filein.hasNextLine()){
                file.add(filein.nextLine().replaceAll("\\s+", ""));
            }
            
            printFile(file);

            Parser parser = new Parser();

            for(String str : file) {
                System.out.println(parser.toAssembly(str));
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
    static void printFile(ArrayList<String> file){
        for(String str : file) System.out.println(str);
    }
}