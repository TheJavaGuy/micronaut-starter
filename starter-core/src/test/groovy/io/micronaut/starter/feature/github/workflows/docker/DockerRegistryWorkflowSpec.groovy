package io.micronaut.starter.feature.github.workflows.docker

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class DockerRegistryWorkflowSpec extends BeanContextSpec implements CommandOutputFixture{

    void 'test github workflow readme'(){
        when:
        def output = generate([DockerRegistryWorkflow.NAME])
        def readme = output['README.md']

        then:
        readme
        readme.contains("""
The GitHub secrets in table below needs to be configured:

| Name | Description |
| ---- | ----------- |
| DOCKER_USERNAME | Username for Docker registry authentication. |
| DOCKER_PASSWORD | Docker registry password. |
| DOCKER_ORGANIZATION | Path to the docker image registry, e.g. for image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL | Docker registry url. |
""")
    }

    @Unroll
    void 'test github workflow is created for #buildTool'(BuildTool buildTool, String workflowName) {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, buildTool, JdkVersion.JDK_11),
                [DockerRegistryWorkflow.NAME])
        def workflow = output[".github/workflows/${workflowName}"]

        then:
        workflow

        where:
        buildTool | workflowName
        BuildTool.GRADLE | "gradle.yml"
        BuildTool.MAVEN | "maven.yml"
    }

    void 'test push to docker workflow for maven'() {
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.MAVEN, JdkVersion.JDK_11),
                [DockerRegistryWorkflow.NAME])
        def maven = output['.github/workflows/maven.yml']

        then:
        maven
        maven.contains("DOCKER_IMAGE=`echo \"\${DOCKER_REGISTRY_URL}/\${DOCKER_ORGANIZATION}/foo")
    }

    void 'test docker image is configured in build.gradle'() {
        when:
        def output = generate([DockerRegistryWorkflow.NAME])
        def gradle = output['build.gradle']

        then:
        gradle
        gradle.contains("""
    dockerBuild{
        images = [\"\${System.env.DOCKER_IMAGE}:\$project.version"]
    }""")
    }

    @Unroll
    void 'test github gradle workflow java version for #version'(JdkVersion version){
        when:
        def output = generate(ApplicationType.DEFAULT,
                new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, version),
                [DockerRegistryWorkflow.NAME])
        def workflow = output['.github/workflows/gradle.yml']

        then:
        workflow
        workflow.contains("java-version: ${version.majorVersion()}")

        where:
        version << JdkVersion.values()
    }
}
