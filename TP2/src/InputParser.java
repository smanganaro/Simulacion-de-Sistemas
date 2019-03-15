import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;



public class InputParser {
    private static double Rmax = 0;
    private static int N = 0;
    private static int L = 0;
    private static double actualTime = 0;

    public static void readParticles(String dPath, String sPath, Set<Particle> particles, boolean includes_velocity) throws FileNotFoundException {
        File dFile = new File(dPath);
        File sFile = new File(sPath);
        Scanner dRead = new Scanner(dFile);
        Scanner sRead = new Scanner(sFile);
        actualTime = Double.parseDouble(dRead.next());
        N = Integer.parseInt(sRead.next());
        L = Integer.parseInt(sRead.next());
        double radius;
        double color;
        Coordinates position;
        while(dRead.hasNext() && sRead.hasNext()){
            radius = Double.parseDouble(sRead.next());
            if(radius > Rmax)
                Rmax = radius;
            color = Double.parseDouble(sRead.next());
            position = new Coordinates(Double.parseDouble(sRead.next()) , Double.parseDouble(sRead.next()));
            if(includes_velocity){

            }
            particles.add(new Particle(position,radius,color));
        }
        dRead.close();
        sRead.close();
    }

    public static int getL() {
        return L;
    }

    public static double getActualTime() {
        return actualTime;
    }

    public static int getN() {
        return N;
    }

    public static double getRmax() {
        return Rmax;
    }
}
