package marvin;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EisCafe{

        private static Executor pool = Executors.newFixedThreadPool(5);

        Random r;

        private final ReentrantLock serverLock = new ReentrantLock();
        private final Condition serverLockCondition = serverLock.newCondition();

        private final ReentrantLock seatLock = new ReentrantLock();
        private final ReentrantLock customerIdLock = new ReentrantLock();

        private int freeSeats = 20;
        private int peopleInQueue = 0;

        private Date timeOpened;

        private int availableServers = 3;

        private int currentCustomerId = 0;

        public EisCafe(){
            r = new Random();
            timeOpened = new Date();
        }

        public void GetServer(){
            serverLock.lock();
            try{
                while (availableServers == 0){
                    try{
                        serverLockCondition.await();
                    } catch (InterruptedException ex){};
                }
                availableServers--;                    
            }
            finally {
                serverLock.unlock();
            }
        }

        public String TimeSinceStart(){
            Date now = new Date();

            return "Zeit: " + ((now.getTime() - timeOpened.getTime()) / 1000) + "s Nachricht: ";
        }

        public void ReturnServer(){
            serverLock.lock();
            try{
                availableServers++;
                serverLockCondition.signal();
            }
            finally{
                serverLock.unlock();
            }
        }

        public void CustomerLeave(){
            seatLock.lock();
            try{
                freeSeats++;
                if (peopleInQueue > 0){
                    createNewCustomers(1);
                    peopleInQueue--;
                }
            }
            finally{
                seatLock.unlock();
            }
        }

        private void createNewCustomers(int cus){
            for (int i = 0; i < cus; i++){
                customerIdLock.lock();
                try{
                    pool.execute(new Kunde(this, currentCustomerId++));
                } finally {
                    customerIdLock.unlock();
                }
            }
        }

        public void NewCustomers(){
            int newCustomers = r.nextInt(5) + 1;
            seatLock.lock();
            try{
                if (freeSeats < newCustomers){
                    peopleInQueue += (newCustomers - freeSeats);
                    createNewCustomers(freeSeats);
                    freeSeats = 0;
                } else{
                    createNewCustomers(newCustomers);
                    freeSeats -= newCustomers;
                }
            } finally {
                seatLock.unlock();
            }
            System.out.println(TimeSinceStart() + "Neuer Kunde! Aktuell " + freeSeats + " freie Plätze verfügbar und " + peopleInQueue + " Kunden in der Warteschlange.");
        }

        public static void main (String[] args)
        {
             System.out.println("Willkommen im Eiscafe");
             EisCafe s = new EisCafe();
             ArrivingCustomers ac = new ArrivingCustomers(s);
             pool.execute(ac);
             System.out.println("Bitte Enter zum Beenden drücken.");
             System.console().readLine();
             ac.Shutdown();
        }
}