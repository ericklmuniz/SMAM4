import java.util.*;

public class TandemSimulation {
    public static void main(String[] args) {
        int totalEvents = 100000;
        double arrivalMin = 1.0, arrivalMax = 4.0;
        double service1Min = 3.0, service1Max = 4.0;
        double service2Min = 2.0, service2Max = 3.0;

        Random random = new Random(42);

        QueueSystem queue1 = new QueueSystem(2, 3, service1Min, service1Max, random);
        QueueSystem queue2 = new QueueSystem(1, 5, service2Min, service2Max, random);
        queue1.setNextQueue(queue2);

        PriorityQueue<Event> scheduler = new PriorityQueue<>();
        double currentTime = 2.0;

        for (int i = 0; i < totalEvents; i++) {
            Customer customer = new Customer(currentTime);
            scheduler.add(new Event(currentTime, EventType.ARRIVAL, queue1, customer));

            double interArrival = arrivalMin + (arrivalMax - arrivalMin) * random.nextDouble();
            currentTime += interArrival;
        }

        currentTime = 0;
        while (!scheduler.isEmpty()) {
            Event event = scheduler.poll();
            currentTime = event.time;

            if (event.type == EventType.ARRIVAL) {
                event.queue.addCustomer(event.customer, currentTime, scheduler);
            } else if (event.type == EventType.DEPARTURE) {
                event.queue.finishService(currentTime, scheduler);

                if (event.queue.nextQueue != null) {
                    event.queue.nextQueue.addCustomer(new Customer(currentTime), currentTime, scheduler);
                }
            }
        }

        System.out.println("Fila 1 (G/G/2/3)");
        queue1.printStats();

        System.out.println("\nFila 2 (G/G/1/5)");
        queue2.printStats();

        System.out.printf("\nTempo global da simulação: %.2f\n", currentTime);
    }
}
