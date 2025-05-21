package com.org.projectreactor.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


public class FluxSchedulers {

    private static final Logger logger = LoggerFactory.getLogger(FluxSchedulers.class);

    /**
     * This piece of code runs the first map on the same thread where flux's subscriber call is
     * and the second map is called in the same thread as the subscriber lambda
     */
    public static void publishOnSubscribeOnSchedulerExample() {
        Scheduler s = Schedulers.newParallel("test", 4);

        logger.info("This should be main");

        Flux<Integer> f = Flux.just(1, 2, 3, 4, 5)
                .map(i -> {
                    logger.info("This is first map");
                    return i*2;
                })
                .publishOn(s)
                .map(i -> {
                    logger.info("This is second map");
                    return i*2;
                })
                .subscribeOn(s);

        f.subscribe(i -> logger.info(String.valueOf(i)));
    }

    /**
     * If only publish on is passed, then even the subscribers lambda is run on the same thread.
     * And if we do not specify anything then everything is run on the same thread
     */
    public static void publishOnSchedulerExampleSameThread() {
        Scheduler s = Schedulers.newParallel("test", 4);

        logger.info("This should be main");

        Flux<Integer> f = Flux.just(1, 2, 3, 4, 5)
                .publishOn(s)
                .map(i -> {
                    logger.info("This is first map");
                    return i*2;
                })
                .map(i -> {
                    logger.info("This is second map");
                    return i*2;
                });

        f.subscribe(i -> logger.info(String.valueOf(i)));
    }
}
