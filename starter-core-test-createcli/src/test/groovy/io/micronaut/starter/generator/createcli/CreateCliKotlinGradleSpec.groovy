package io.micronaut.starter.generator.createcli

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateCliKotlinGradleSpec extends CommandSpec {
    @Override
    String getTempDirectoryPrefix() {
        "starter-core-test-createcli-createclikotlingradlespec"
    }

    @Unroll
    void '#applicationType with #lang and #buildTool and test framework: #testFramework'(ApplicationType applicationType,
                                                                                     Language lang,
                                                                                     BuildTool buildTool) {

        given:
        generateProject(lang, buildTool, [], applicationType)

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand('run --args="-v"')
        } else {
            executeMavenCommand("mn:run -Dmn.appArgs=-v")
        }

        then:
        testOutputContains("Hi")

        where:
        applicationType     | lang            | buildTool
        ApplicationType.CLI | Language.KOTLIN | BuildTool.GRADLE
    }
}