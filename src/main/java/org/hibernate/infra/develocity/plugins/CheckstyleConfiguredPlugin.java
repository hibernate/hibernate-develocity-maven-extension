package org.hibernate.infra.develocity.plugins;

import org.hibernate.infra.develocity.GoalMetadataProvider;
import org.hibernate.infra.develocity.SimpleConfiguredPlugin;

import java.util.Map;

public class CheckstyleConfiguredPlugin extends SimpleConfiguredPlugin {

    @Override
    protected String getPluginName() {
        return "maven-checkstyle-plugin";
    }

    @Override
    protected Map<String, GoalMetadataProvider> getGoalMetadataProviders() {
        return Map.of(
                "check", CheckstyleConfiguredPlugin::configure
        );
    }

    private static void configure(GoalMetadataProvider.Context context) {
        context.metadata().inputs(inputs -> {
            inputs.fileSet("testSourceDirectories", fileSet ->
                    fileSet.exclude("**/target/**")
            );
            inputs.fileSet("sourceDirectories", fileSet ->
                    fileSet.exclude("**/target/**")
            );
        });
    }
}
