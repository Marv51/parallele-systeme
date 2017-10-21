import java.util.Random;

public class EisCafe{

        Random r;

        private int freeSeats = 20;
        private int peopleInQueue = 0;

        private int availableServers = 3;

        public EisCafe(){
            r = new Random();
        }

        public void GetServer(){
            synchronized(this){
                while (availableServers == 0){
                    try{
                        wait();
                    } catch (InterruptedException ex){};
                }
                availableServers--;                    
            }
        }

        public void ReturnServer(){
            synchronized(this){
                availableServers++;
                notify();
            }
        }

        public void CustomerLeave(){
            synchronized(this){
                freeSeats++;
                if (peopleInQueue > 0){
                    createNewCustomers(1);
                    peopleInQueue--;
                }
            }
        }

        private void createNewCustomers(int cus){
            for (int i = 0; i < cus; i++){
                Thread c = new Thread( new Kunde(this) );
                c.start();
            }
        }

        public void NewCustomers(){
            int newCustomers = r.nextInt(5) + 1;
            synchronized(this){
                if (freeSeats < newCustomers){
                    peopleInQueue += (newCustomers - freeSeats);
                    createNewCustomers(freeSeats);
                    freeSeats = 0;
                } else{
                    createNewCustomers(newCustomers);
                    freeSeats -= newCustomers;
                }
            }
            System.out.println("Neuer Kunde! Aktuell " + freeSeats + " freie Plätze verfügbar und " + peopleInQueue + " Kunden in der Warteschlange.");
        }

        public static void main (String[] args)
        {
             System.out.println("Willkommen im Eiscafe");
             EisCafe s = new EisCafe();
             ArrivingCustomers ac = new ArrivingCustomers(s);
             Thread act = new Thread(ac);
             act.start();
             System.out.println("Bitte Enter zum Beenden drücken.");
             System.console().readLine();
             ac.Shutdown();
        }
}