package marvin;

public class ArrivingCustomers implements Runnable{

    private EisCafe state;

    public ArrivingCustomers(EisCafe s){
        state = s;
    }

    private boolean shuttingDown = false;

    public void Shutdown(){
        shuttingDown = true;
    }

    public void run()
    {
        while(!shuttingDown){
            try {
                Thread.sleep(6 * 1000);
                state.NewCustomers();    
            } catch (InterruptedException ex){

            }
        }
        System.out.println(">>>> " + state.TimeSinceStart() + "EisCafe wird geschlossen, aktuelle Kunden werden noch fertig bedient.");
    }

}