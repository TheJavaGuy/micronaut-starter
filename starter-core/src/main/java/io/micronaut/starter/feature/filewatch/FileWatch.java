/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.filewatch;

import io.micronaut.context.condition.OperatingSystem;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ConfiguredFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeatureConfiguration;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class FileWatch extends ConfiguredFeature {

    private final FileWatchOsx fileWatchOsx;

    public FileWatch(@Named("filewatch") FeatureConfiguration featureConfiguration,
                     FileWatchOsx fileWatchOsx) {
        super(featureConfiguration);
        this.fileWatchOsx = fileWatchOsx;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (OperatingSystem.getCurrent().isMacOs()) {
            featureContext.addFeature(fileWatchOsx);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.io.watch.paths", "src/main");
        generatorContext.getConfiguration().put("micronaut.io.watch.restart", true);
    }

}
