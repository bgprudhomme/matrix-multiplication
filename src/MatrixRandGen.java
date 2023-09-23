import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixRandGen {
    public static void main(String[] args) {
        File f;
        BufferedWriter w;
        int min = -100;
        int max = 100;

        int[] sizes = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

        for(int s : sizes){
            try {
                System.out.println("Size: " + s + "x" + s);
                for(int x=0; x<10; x++){
                    f = new File("../test/" + s + "x" + s + "/" + x + ".txt");
                    
                        w = new BufferedWriter(new FileWriter(f, true));
                        w.write(s + "\n");
                        for(int i=0; i<2*s; i++){
                            for(int j=0; j<s; j++){
                                w.write(ThreadLocalRandom.current().nextInt(min, max + 1) + " ");
                            }
                            w.write("\n");
                        }
                    
                    System.out.println("File created: ../test/" + s + "x" + s + "/" + x + ".txt");
                    w.close();
                }
                
            } catch (IOException e){
                    System.out.println("IOException occurred");
                }
        }
    }
}