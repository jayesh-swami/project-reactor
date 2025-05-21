package com.org.projectreactor;

import com.org.projectreactor.examples.FluxStreamExample;
import com.org.projectreactor.examples.FluxSchedulers;
import com.org.projectreactor.examples.ParallelFlux;
import com.org.projectreactor.examples.FluxStreamExamplesHot;

public class ProjectReactorApplication {

    public static void main(String[] args) throws InterruptedException {
//        FluxCreateAndGenerate.fluxStreamCreateWithSink();
//        FluxCreateAndGenerate.fluxStreamWithDifferentBackpressureStrategy();
//        FluxCreateAndGenerate.fluxStreamGenerateWithSyncSink();
//        FluxSchedulers.publishOnSubscribeOnSchedulerExample();
//        FluxSchedulers.publishOnSchedulerExampleSameThread();
//        ParallelFlux.parallelObjectMapper();
//        FluxStreamExample.testBasicFluxEmit();
//        FluxStreamExample.testBasicFluxEmitCustomScheduler();
//        FluxStreamExample.testBasicFluxEmitWithDelay();
//        FluxStreamExample.testBasicFluxEmitFromArray();
//        FluxStreamExample.testBasicFluxEmitFromList();

//        FluxStreamExamplesHot.coldExample();
//        FluxStreamExamplesHot.hotExample();
//        FluxStreamExamplesHot.hotExampleDuplicate();
        FluxStreamExamplesHot.hotExampleDuplicateWithCache();
    }

}

