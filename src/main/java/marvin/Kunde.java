import java.util.Random;

public class Kunde implements Runnable{

        private Random r;
        private EisCafe state;
    
        public Kunde(EisCafe s){
            state = s;
            r = new Random();
        }
    
        @Override public void run()
        {
            try{               
                System.out.println("Kunde hat sich hingesetzt.");
                Thread.sleep(1 * 1000);
                System.out.println("Kunde sucht Kellner.");
                state.GetServer();
                System.out.println("Kunde wird bedient.");
                int time = r.nextInt(6) + 2;
                Thread.sleep(time * 1000);
                System.out.println("Kunde ist bedient worden.");
                state.ReturnServer();
                Thread.sleep(3 * 1000);
                System.out.println("Kunde ist fertig.");
                state.CustomerLeave();
            } catch (InterruptedException ex){

            }
        }
    
    }