package com.org.projectreactor.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class FluxStreamExample {

    private static final Logger logger = LoggerFactory.getLogger(FluxStreamExample.class);

    /**
     * It seems like if we don't specify the thread on which flux should emit elements on main
     *
     * @throws InterruptedException
     */
    public static void testBasicFluxEmit() throws InterruptedException {
        Flux<Integer> integerFlux =
                Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                        .log()
                        .map(integer -> integer * 2);

        integerFlux.subscribe(
                i -> System.out.println("Subscribed: " + i),
                (err) -> System.out.println("Error: " + err),
                () -> System.out.println("Done"));

        integerFlux.subscribe(i -> System.out.println("Subscribed2: " + i));
        integerFlux.subscribe(i -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Subscribed3: " + i);
        });
    }

    /**
     * we can also specify a custom scheduler on which to emit the elements and subscribers would also then
     * run on that thread
     *
     * A very interesting thing is happening here that if we just specify the thread, it does not kill the main
     * thread even after all elements are finished emitting. It stops at
     *     private static native void waitForReferencePendingList(); in Reference.java
     *
     * Also there is no thread called 'Parallel Scheduler #1', there are only Parallel Scheduler #1-[1/2/3]
     * for the subscribers
     *
     * @throws InterruptedException
     */
    public static void testBasicFluxEmitCustomScheduler() throws InterruptedException {
        Scheduler scheduler = Schedulers.newParallel("scheduler");
        Flux<Integer> integerFlux =
                Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                        .log()
                        .publishOn(scheduler)
                        .subscribeOn(scheduler)
                        .map(integer -> integer * 2);

        integerFlux.subscribe(
                i -> System.out.println("Subscribed: " + i),
                (err) -> System.out.println("Error: " + err),
                () -> System.out.println("Done"));

        integerFlux.subscribe(i -> System.out.println("Subscribed2: " + i));
        integerFlux.subscribe(i -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Subscribed3: " + i);
        });

        while(true) {
            Thread.sleep(5000);
            logger.info("Should be main");
        }
    }


    /**
     * Emitting elements with delay makes the flux emit elements on the separate default
     * parallel scheduler and if we don't block the main thread, the program will just end
     *
     * @throws InterruptedException
     */
    public static void testBasicFluxEmitWithDelay() throws InterruptedException {
        Flux<Integer> integerFlux =
                Flux.just(1, 2, 3, 4, 5)
                        .map(integer -> integer * 2)
                        .log()
                        .delayElements(Duration.ofSeconds(1));

        integerFlux.subscribe(i -> System.out.println("Subscribed: " + i));

        Thread.sleep(15000);
    }

    public static void testBasicFluxEmitFromArray() throws InterruptedException {
        String[] list1 = new String[]{"First", "person", "said", "voila"};

        Flux.fromArray(list1).subscribe(System.out::println);
    }

    public static void testBasicFluxEmitFromList() throws InterruptedException {
        List<String> list1 = new ArrayList<>();
        list1.add("First");
        list1.add("person");
        list1.add("said");
        list1.add("voila");

        Flux.fromIterable(list1).subscribe(System.out::println);
    }
}
