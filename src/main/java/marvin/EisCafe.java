package marvin;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class EisCafe{

        private static Executor pool = Executors.newFixedThreadPool(20);
        private Random r;
        private boolean shuttingDown = false;
        private Semaphore seats = new Semaphore(10);
        private Date timeOpened;
        private Semaphore waiter = new Semaphore(1);
        private AtomicInteger currentCustomerId = new AtomicInteger(0);

        public EisCafe(){
            r = new Random();
            timeOpened = new Date();
            System.out.println("Willkommen im Eiscafe");
            simulateArrivingCustomers();
            System.out.println("Bitte Enter zum Beenden drücken.");
            System.console().readLine();
            shuttingDown = true;
        }

        private String TimeSinceStart(){
            return "Zeit: " + (((new Date()).getTime() - timeOpened.getTime()) / 1000) + "s Nachricht: ";
        }

        private void createNewCustomer(){
            int thisCustomerId = currentCustomerId.getAndIncrement();
            pool.execute(() ->  {
                try{
                    seats.acquire();
                    System.out.println(TimeSinceStart() + "Kunde " + thisCustomerId + " hat sich hingesetzt.");
                    Thread.sleep(1 * 1000);
                    System.out.println(TimeSinceStart() + "Kunde " + thisCustomerId + " sucht Kellner.");
                    try{
                        waiter.acquire();
                        System.out.println(TimeSinceStart() + "Kunde " + thisCustomerId + " wird bedient.");
                        int time = r.nextInt(6) + 2;
                        Thread.sleep(time * 1000);
                        System.out.println(TimeSinceStart() + "Kunde " + thisCustomerId + " ist bedient worden.");
                    }
                    finally {
                        waiter.release();
                    }
                    Thread.sleep(3 * 1000);
                    System.out.println(TimeSinceStart() + "Kunde " + thisCustomerId + " ist fertig.");
                } catch (InterruptedException ex) {


                }
                finally {
                    seats.release();
                }
            });
        }

        private void NewCustomers(){
            int newCustomers = r.nextInt(5) + 1;
            for (int i = 0; i < newCustomers; i++) {
                createNewCustomer();
            }
            System.out.println(TimeSinceStart() +
                    (newCustomers == 1 ? "Ein neuer Kunde! " : newCustomers + " neue Kunden! ")
                    + "Aktuell " + seats.availablePermits() + " freie Plätze verfügbar. "
                    + seats.getQueueLength() + " Kunden in der Warteschlange.");
        }

        private void simulateArrivingCustomers(){
            pool.execute(()-> {
                while(!shuttingDown){
                    try {
                        Thread.sleep(6 * 1000);
                        NewCustomers();
                    } catch (InterruptedException ex){

                    }
                }
                System.out.println(">>>> " + TimeSinceStart() + "EisCafe wird geschlossen, aktuelle Kunden werden noch fertig bedient.");
            });
        }

        public static void main (String[] args)
        {
             EisCafe s = new EisCafe();
        }
}