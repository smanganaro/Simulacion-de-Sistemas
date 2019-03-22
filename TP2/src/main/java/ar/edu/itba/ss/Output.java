package ar.edu.itba.ss;
import javax.sound.sampled.Clip;
import java.io.*;
import java.util.List;

public class Output {

    private static Output instance = null;
    private static String filename = CliParser.filename;

    public static Output getInstance(){
        if(instance == null) {
            instance = new Output();
            filename = CliParser.filename+"_noise_"+CliParser.noise+"_output.txt";
        }
        return instance;
    }

    public void write(List<Particle> particles, double time, double Va) throws IOException {
        if(time == 0){
            PrintWriter pw = new PrintWriter(filename);
            pw.close();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        out.write(generateFileString(particles,time,Va));
        out.close();
    }

    public static String generateFileString(List<Particle> particles,double time, double Va){
        StringBuilder builder = new StringBuilder()
                .append(particles.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Velocity X\t Velocity Y\t Radius\t Velocity Angle\t Time: ")
                .append(time)
                .append("\t Va: ")
                .append(Va)
                .append("\r\n");
        for(Particle current: particles){
            builder.append(current.getID())
                    .append(" ")
                    .append(current.getPosition().getX())
                    .append(" ")
                    .append(current.getPosition().getY())
                    .append(" ")
                    .append(current.getVelocity().getX())
                    .append(" ")
                    .append(current.getVelocity().getY())
                    .append(" ")
                    .append(current.getRadius())
                    .append(" ")
                    .append(current.getVelocity().getAngle())
                    .append(" ")
                    .append("\r\n");

        }
        return builder.toString();
    }
}
