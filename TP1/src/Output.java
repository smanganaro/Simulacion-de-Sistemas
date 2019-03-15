import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

public class Output {

    private static Output instance = null;

    public static Output getInstace(){
        if(instance == null)
            instance = new Output();
        return instance;
    }
    public void write(Map<Particle,Set<Particle>> map) {
        Writer writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
            for(Map.Entry<Particle, Set<Particle>> entry: map.entrySet()){
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
