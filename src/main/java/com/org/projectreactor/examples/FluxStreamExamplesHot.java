package com.org.projectreactor.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.stream.Stream;

public class FluxStreamExamplesHot {

    private static final Logger logger = LoggerFactory.getLogger(FluxStreamExamplesHot.class);

    private static Stream<String> getMovieScenes() {
        logger.info("Getting movie scenes...");

        return Stream.of("Scene 1", "Scene 2", "Scene 3", "Scene 4", "Scene 5");
    }

    /**
     * Interesting to note-
     *  1. Supplier is called each time and a new stream is created for each subscriber
     *  2. If we don't create a new scheduler and use the default parallel one then it is closed and main is
     *      also closed
     *
     * @throws InterruptedException
     */
    public static void coldExample() throws InterruptedException {

        Flux<String> moviesStream = Flux
                .fromStream(FluxStreamExamplesHot::getMovieScenes)
                .publishOn(Schedulers.parallel())
                .delayElements(Duration.ofSeconds(1));

        moviesStream.subscribe(s -> {
            try {
                Thread.sleep(1000);
                System.out.println(s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        moviesStream.subscribe(s -> {
            try {
                Thread.sleep(1200);
                System.out.println("Another guy : " + s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(10000);
    }

    /**
     * Interesting to note-
     *  1. Supplier is not called each time
     *  2. If you join late you lose data
     *
     * @throws InterruptedException
     */
    public static void hotExample() throws InterruptedException {

        Flux<String> moviesStream = Flux
                .fromStream(FluxStreamExamplesHot::getMovieScenes)
                .publishOn(Schedulers.parallel())
                .delayElements(Duration.ofSeconds(1))
                .share();

        moviesStream.subscribe(s -> {
            try {
                Thread.sleep(1000);
                System.out.println(s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(2000);

        moviesStream.subscribe(s -> {
            try {
                Thread.sleep(1200);
                System.out.println("Another guy : " + s);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread.sleep(10000);
    }

    /**
     * Interesting to note-
     *  1. Supplier is called again even when sharing if the flux stream has completed emitting
     *  2. It is possible to cache the result of previous emissions
     *
     * @throws InterruptedException
     */
    public static void hotExampleDuplicate() throws InterruptedException {

        Flux<String> moviesStream = Flux
                .fromStream(FluxStreamExamplesHot::getMovieScenes)
                .publishOn(Schedulers.parallel())
                .delayElements(Duration.ofSeconds(1))
                .share();

        moviesStream.subscribe(System.out::println);

        Thread.sleep(7000);

        moviesStream.subscribe(s -> { System.out.println("Another guy : " + s); });
        Thread.sleep(10000);
    }


    /**
     * Interesting to note-
     *  1. Supplier is not called again even when sharing if the flux stream has completed emitting when using cache
     *  2. Cache does not delay the elements and if specified the history it will only give a truncated list of elements
     *
     * @throws InterruptedException
     */
    public static void hotExampleDuplicateWithCache() throws InterruptedException {

        Flux<String> moviesStream = Flux
                .fromStream(FluxStreamExamplesHot::getMovieScenes)
                .publishOn(Schedulers.parallel())
                .delayElements(Duration.ofSeconds(1))
                .share()
                .cache(3);

        moviesStream.subscribe(System.out::println);

        Thread.sleep(7000);

        moviesStream.subscribe(s -> { System.out.println("Another guy : " + s); });
        Thread.sleep(10000);
    }

}
