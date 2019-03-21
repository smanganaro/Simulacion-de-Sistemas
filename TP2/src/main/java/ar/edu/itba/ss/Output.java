package ar.edu.itba.ss;
import java.io.*;
import java.util.List;

public class Output {

    private static Output instance = null;
    private static final int MAX_COLOR = 255;

    public static Output getInstance(){
        if(instance == null)
            instance = new Output();
        return instance;
    }

    public void write(List<Particle> particles, double time) throws IOException {
        if(time == 0){
            PrintWriter pw = new PrintWriter("output.txt");
            pw.close();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", true)));
        out.write(generateFileString(particles));
        out.close();
    }

    public static String generateFileString(List<Particle> particles){
        StringBuilder builder = new StringBuilder()
                .append(particles.size())
                .append("\r\n")
                .append("//ID\t X\t Y\t Radius\t R\t G\t B\t\r\n");
        for(Particle current: particles){
            builder.append(current.getID())
                    .append(" ")
                    .append(current.getPosition().getX())
                    .append(" ")
                    .append(current.getPosition().getY())
                    .append(" ")
                    .append(current.getRadius())
                    .append(" ")
                    .append(getParticleColour(current))
                    .append("\r\n");

        }
        return builder.toString();
    }

    private static String getParticleColour(Particle p){
        StringBuilder builder = new StringBuilder();
        double angle = p.getVelocity().getAngle() + Math.PI; //0 - 2PI
        int colour = (int) (MAX_COLOR*angle/(2*Math.PI));

        builder.append(colour)//R TODO ARREGLAR ESTO
                .append(" ")
                .append(MAX_COLOR)//G
                .append(" ")
                .append(MAX_COLOR);//B

        return builder.toString();
    }

}
