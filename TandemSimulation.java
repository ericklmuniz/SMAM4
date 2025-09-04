import java.util.*;

public class TandemSimulation {
    public static void main(String[] args) {
        long maxRandomDraws = 100000L;
        double arrivalMin = 1.0, arrivalMax = 4.0;
        double service1Min = 3.0, service1Max = 4.0;
        double service2Min = 2.0, service2Max = 3.0;

        CountingRandom countingRandom = new CountingRandom(42, maxRandomDraws);

        QueueSystem queue1 = new QueueSystem(2, 3, service1Min, service1Max, countingRandom);
        QueueSystem queue2 = new QueueSystem(1, 5, service2Min, service2Max, countingRandom);
        queue1.setNextQueue(queue2);

        PriorityQueue<Event> scheduler = new PriorityQueue<>();
        double nextArrival = 1.5;
        scheduler.add(new Event(nextArrival, EventType.ARRIVAL, queue1, new Customer(nextArrival)));

        double currentTime = 0.0;

        while (!scheduler.isEmpty()) {
            Event event = scheduler.poll();
            currentTime = event.time;

            if (event.type == EventType.ARRIVAL) {
                event.queue.addCustomer(event.customer, currentTime, scheduler);
                if (event.queue == queue1 && countingRandom.canDraw()) {
                    double interArrivalU = countingRandom.nextDouble();
                    double interArrival = arrivalMin + (arrivalMax - arrivalMin) * interArrivalU;
                    double nextArrivalTime = currentTime + interArrival;
                    scheduler.add(new Event(nextArrivalTime, EventType.ARRIVAL, queue1, new Customer(nextArrivalTime)));
                }
            } else if (event.type == EventType.DEPARTURE) {
                event.queue.finishService(currentTime, scheduler);

                if (event.queue.nextQueue != null) {
                    event.queue.nextQueue.addCustomer(new Customer(currentTime), currentTime, scheduler);
                }
            }
            if (!countingRandom.canDraw()) {
                queue1.updateState(currentTime);
                queue2.updateState(currentTime);
                break;
            }
        }

        System.out.println("Fila 1 (G/G/2/3)");
        queue1.printStats(currentTime);

        System.out.println("\nFila 2 (G/G/1/5)");
        queue2.printStats(currentTime);

        System.out.printf("\nTempo global da simulação: %.2f\n", currentTime);

        System.out.printf("Aleatórios usados: %d / %d\n", countingRandom.getUsed(), countingRandom.getMaxDraws());
    }
}
