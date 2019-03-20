package ar.edu.itba.ss;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Output {

    private static Output instance = null;

    public static Output getInstance(){
        if(instance == null)
            instance = new Output();
        return instance;
    }

    public void write(List<Particle> particles, double time){
        if(time == 0){
            try{
                PrintWriter pw = new PrintWriter("output.txt");
                pw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)))) {
            out.write(String.valueOf(time) + "\n");
            for(Particle p: particles){
                out.write(p.getID() + " " +  p.getPosition().getX() + " " + p.getPosition().getY() + " " + p.getVelocity().getAngle() + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
