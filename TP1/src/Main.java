import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String args []){
        /*if(args.length<1 || (Rc=Integer.parseInt(args[0]))<0)
            throw new RuntimeException("Must provide Rc");*/
        int Rc = 1;
        int N = 10000;
        int Mmax;
        double r = 0.25;
        double L = 20;

        //Para Fuerza Bruta
        Mmax = 20;

        /*//Para Cell Index
        double MOpt = L/(Rc-2*r);
        Mmax = (int) Math.floor(MOpt);*/
        for(int in = 0; in < N;in += 500){
            System.out.println("FOR N ="+in);
            benchmark(in,Mmax,L,Rc,r);
        }

    }

    private static void benchmark(int N, int Mmax, double L, int Rc, double r){
        if(Mmax<=0){
            throw new RuntimeException("M has to be positive");
        }

        if(L*1.0/Mmax<Rc){
            throw new RuntimeException("No valid M for Cell Index Method. Remember L/M>=Rc");
        }

        System.out.println("M - Miliseconds");
        int tries = 3;
        for(int i=Mmax;i<=Mmax;i++){
            long total = 0;

            for(int j=0; j<tries; j++){
                List<Particle> particles = new ArrayList<Particle>();
                generateRandomParticles(particles, N, L, r);
                long valStart = System.currentTimeMillis();

                Grid grid = new LinearGrid(L,i,particles);

                CellIndexMethod cellIndexMethod = new CellIndexMethod(grid, 1, 1, Rc);
                cellIndexMethod.run();

                total += System.currentTimeMillis()-valStart;
            }
            System.out.println(i + " - " + ((double)total/tries));
        }
    }

    private static void generateRandomParticles(List<Particle> particles, int N, double L, double r){

       // System.out.println("Generating " + N + " random particles, with L: " + L);

        for(int i=0; i<N; i++){
            particles.add(new Particle(new Coordinates(Math.random()*L,Math.random()*L),r,1));
        }

    }



}
