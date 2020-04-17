package io.micronaut.starter.api;

import io.micronaut.core.io.Writable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.starter.ContextFactory;
import io.micronaut.starter.Project;
import io.micronaut.starter.command.CreateAppCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.ZipOutputHandler;
import io.micronaut.starter.util.NameUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

@Controller("/create")
public class CreateController implements CreateOperations {
    private final CreateAppCommand.CreateAppFeatures createAppFeatures;
    private final FeatureValidator featureValidator;
    private final ContextFactory contextFactory;

    public CreateController(
            CreateAppCommand.CreateAppFeatures createAppFeatures,
            FeatureValidator featureValidator,
            ContextFactory contextFactory) {
        this.createAppFeatures = createAppFeatures;
        this.featureValidator = featureValidator;
        this.contextFactory = contextFactory;
    }

    @Override
    public Writable createApp(String name, @Nullable List<String> features) {
        Project project = NameUtils.parse(name);
        return new Writable() {
            @Override
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                CreateAppCommand createAppCommand = new CreateAppCommand(
                        createAppFeatures,
                        featureValidator,
                        contextFactory

                );
                try {
                    createAppCommand.generate(project, new ZipOutputHandler(outputStream));
                    outputStream.flush();
                } catch (Exception e) {
                    throw new IOException(e.getMessage(), e);
                }
            }

            @Override
            public void writeTo(Writer out) throws IOException {
                // no-op, output stream used
            }
        };
    }
}
