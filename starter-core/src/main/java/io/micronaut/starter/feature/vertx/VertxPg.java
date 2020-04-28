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
package io.micronaut.starter.feature.vertx;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class VertxPg implements Feature {

    @Override
    public String getName() {
        return "vertx-pg-client";
    }

    @Override
    public String getTitle() {
        return "Vertx Reactive Postgres Client";
    }

    @Override
    public String getDescription() {
        return "Add support for the Reactive Postgres Client in the application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("vertx.pg.client.port", 5432);
        generatorContext.getConfiguration().put("vertx.pg.client.host", "the-host");
        generatorContext.getConfiguration().put("vertx.pg.client.database", "the-db");
        generatorContext.getConfiguration().put("vertx.pg.client.database.user", "user");
        generatorContext.getConfiguration().put("vertx.pg.client.database.password", "password");
        generatorContext.getConfiguration().put("vertx.pg.client.database.maxSize", 5);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}
