package com.org.projectreactor;

import com.org.projectreactor.examples.FluxStreamExample;
import com.org.projectreactor.examples.FluxSchedulers;
import com.org.projectreactor.examples.ParallelFlux;

public class ProjectReactorApplication {

    public static void main(String[] args) throws InterruptedException {
//        FluxCreateAndGenerate.fluxStreamCreateWithSink();
//        FluxCreateAndGenerate.fluxStreamWithDifferentBackpressureStrategy();
//        FluxCreateAndGenerate.fluxStreamGenerateWithSyncSink();
//        FluxSchedulers.publishOnSubscribeOnSchedulerExample();
//        FluxSchedulers.publishOnSchedulerExampleSameThread();
//        ParallelFlux.parallelObjectMapper();
//        FluxStreamExample.testBasicFluxEmit();
        FluxStreamExample.testBasicFluxEmitCustomScheduler();
//        FluxStreamExample.testBasicFluxEmitWithDelay();
//        FluxStreamExample.testBasicFluxEmitFromArray();
//        FluxStreamExample.testBasicFluxEmitFromList();
    }

}

