package com.org.projectreactor.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ParallelFlux {

    private static final Logger logger = LoggerFactory.getLogger(ParallelFlux.class);

    /**
     * Using a parallel flux stream for object mapper can definitely speed up things
     * when the amount of data in the stream is high. Even after joining and sorting.
     *
     * @throws InterruptedException
     */
    public static void parallelObjectMapper() throws InterruptedException {
        Flux<Integer> f = Flux
                .range(1, 100000)
                .parallel(4)
                .runOn(Schedulers.boundedElastic())
                .map(i -> i * 2)
                .sequential()
                .sort();

        f.subscribe(System.out::println);

        Thread.sleep(500);
    }

}
