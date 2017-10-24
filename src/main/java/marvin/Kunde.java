package marvin;

import java.util.Random;

public class Kunde implements Runnable{

        private Random r;
        private EisCafe state;
        private int customerId;
    
        public Kunde(EisCafe s, int customerId){
            state = s;
            r = new Random();
            this.customerId = customerId;
        }
    
        public void run()
        {
            try{               
                System.out.println(state.TimeSinceStart() + "Kunde " + customerId + " hat sich hingesetzt.");
                Thread.sleep(1 * 1000);
                System.out.println(state.TimeSinceStart() + "Kunde " + customerId + " sucht Kellner.");
                state.GetServer();
                System.out.println(state.TimeSinceStart() + "Kunde " + customerId + " wird bedient.");
                int time = r.nextInt(6) + 2;
                Thread.sleep(time * 1000);
                System.out.println(state.TimeSinceStart() + "Kunde " + customerId + " ist bedient worden.");
                state.ReturnServer();
                Thread.sleep(3 * 1000);
                System.out.println(state.TimeSinceStart() + "Kunde " + customerId + " ist fertig.");
                state.CustomerLeave();
            } catch (InterruptedException ex){

            }
        }
    
    }