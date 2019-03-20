package ar.edu.itba.ss;
import java.io.*;
import java.util.List;

public class Output {

    private static Output instance = null;

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
        out.write(generateFileString(particles)); //FALTARIA PASARLE A OVITO EL TIEMPO en generateFileString

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
                    .append("1 0 0\r\n");

        }
        return builder.toString();
    }
}
