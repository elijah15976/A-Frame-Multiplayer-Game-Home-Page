import java.util.Scanner;
//import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

class Input{
    private static Logger lg = new Logger();

    public static Scanner sc = new Scanner(System.in);
    public static int readInt(){
        int data = sc.nextInt();
        sc.nextLine();//Clear the trailing carriage return
        return data;
    }
    public static double readDouble(){
        return Double.parseDouble(sc.nextLine());
    }
    public static String readString(){
        return sc.nextLine();
    }
    public static char readChar(){
        return sc.nextLine().charAt(0);
    }
    public static boolean readBoolean(){
        return sc.nextBoolean();
    }
    public static String readFile(String fn){
        String file = "";
        try{
            file = new String(Files.readAllBytes(Paths.get(fn)), StandardCharsets.UTF_8);
        }catch(Exception e){

        }
        return  file;
    }
    public static byte[] readFileSpecial(String fn){
        byte[] file;
        try{
            file = getByteArray(fn);
        }catch(Exception e){
            file = new byte[0];
        }
        return file;
    }

    private static byte[] getByteArray(String fn){
        File file = new File(fn);
        byte[] finalArray;
        try{
            FileInputStream fl = new FileInputStream(file);
            
            finalArray = new byte[(int)file.length()];
            
            fl.read(finalArray);
            
            //System.out.println(Arrays.toString(finalArray));
            
            fl.close();
        }
        catch(Exception e){
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            lg.dispMessage(writer.toString(), "crit");
            finalArray = new byte[0];
        }


        return finalArray;
    }
    /*
    public static ArrayList<Row> readCSV(String fn){
        ArrayList<Row> data =  new ArrayList<>();

        String file = readFile(fn);
        String[] line = file.split("\n");
        String header = line[0];
        for(int pos = 1; pos < line.length; pos++){
            data.add(new Row(header,line[pos]));
        }
        return data;
    }*/

}