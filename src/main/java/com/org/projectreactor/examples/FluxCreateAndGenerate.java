package com.org.projectreactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.stream.IntStream;

public class FluxCreateAndGenerate {

    /**
     * Basically we get a sink in which we can call FluxSink::next to emit an element
     * Is it a good idea to do preprocessing here?
     *
     * @throws InterruptedException
     */
    public static void fluxStreamCreateWithSink() throws InterruptedException {
        Flux<Integer> stream = Flux.create((FluxSink<Integer> fluxSink) -> {
            IntStream.of(1, 2, 3, 4, 5, 6)
                    .map(i -> i * 2)
                    .forEach(fluxSink::next);
        }).delayElements(Duration.ofSeconds(1));

        stream.subscribe(System.out::println);

        Thread.sleep(10000);
    }

    /**
     * Also the elements using a sink are emitted as soon as next is called and subscribers can start processing
     * as soon as they are emitted
     *
     * @throws InterruptedException
     */
    public static void fluxStreamWithDifferentBackpressureStrategy() throws InterruptedException {
        Flux<Integer> stream = Flux.create((FluxSink<Integer> fluxSink) -> {
                        IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                                .map(i -> i * 2)
                                .forEach(i -> {
                                    try {
                                        Thread.sleep(1000);
                                        fluxSink.next(i);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                }, FluxSink.OverflowStrategy.BUFFER);

        stream.subscribe(System.out::println);

        Thread.sleep(10000);
    }

    /**
     * Generate only emits one element at a time, basically it is aware of the availability
     * of the subscriber whereas in contrast create will emit and store them in buffer or whatever the
     * buffer strategy is specified
     *
     * @throws InterruptedException
     */
    public static void fluxStreamGenerateWithSyncSink() throws InterruptedException {

        Flux<Integer> f = Flux.generate((SynchronousSink<Integer> sink) -> {
            System.out.println("Emitting now...");
            sink.next(2);
        });


        f.subscribe(i -> {
            try {
                Thread.sleep(1000);
                System.out.println("Processing now...");
                System.out.println(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
