public class ArrivingCustomers implements Runnable{

    private EisCafe state;

    public ArrivingCustomers(EisCafe s){
        state = s;
    }

    private boolean shuttingDown = false;

    public void Shutdown(){
        shuttingDown = true;
    }

    @Override public void run()
    {
        try{            
            Thread.sleep(6 * 1000);
        } catch (InterruptedException ex){}
        while(!shuttingDown){
            try{            
                state.NewCustomers();    
                Thread.sleep(6 * 1000);
            } catch (InterruptedException ex){

            }
        }
    }

}