package ar.edu.itba.ss;
import java.io.*;
import java.util.List;

public class OutputParticle {

    private static OutputParticle instance = null;
    private static String filename = CliParser.filename;

    public static OutputParticle getInstance(){
        if(instance == null) {
            instance = new OutputParticle();
            filename = CliParser.filename+"_noise_"+CliParser.noise+"_output.txt";
        }
        return instance;
    }

    public void write(List<Particle> particles,double time) throws IOException {
        if(time == 0){
            PrintWriter pw = new PrintWriter(filename);
            pw.close();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        out.write(generateFileString(particles,time));
        out.close();
    }

    public static String generateFileString(List<Particle> particles,double time){
        StringBuilder builder = new StringBuilder()
                .append(particles.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Velocity X\t Velocity Y\t Radius\t Velocity Angle\t Time: ")
                .append(time)
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
