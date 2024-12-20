package org.hibernate.infra.develocity;

import java.util.List;

import com.gradle.develocity.agent.maven.api.scan.BuildScanPublishing;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.hibernate.infra.develocity.normalization.Normalization;
import org.hibernate.infra.develocity.plugins.CompilerConfiguredPlugin;
import org.hibernate.infra.develocity.plugins.FailsafeConfiguredPlugin;
import org.hibernate.infra.develocity.plugins.ForbiddenApisConfiguredPlugin;
import org.hibernate.infra.develocity.plugins.SurefireConfiguredPlugin;
import org.hibernate.infra.develocity.scan.BuildScanMetadata;

import com.gradle.develocity.agent.maven.api.DevelocityApi;
import com.gradle.develocity.agent.maven.api.DevelocityListener;

@SuppressWarnings("deprecation")
@Component(role = DevelocityListener.class, hint = "hibernate-maven-based-project-build-cache",
        description = "Configures Develocity for the Hibernate projects using Maven as a build tool")
public class HibernateMavenBasedProjectDevelocityListener implements DevelocityListener {


    @Override
    public void configure(DevelocityApi develocityApi, MavenSession mavenSession) {
        develocityApi.getBuildScan().getPublishing()
                .onlyIf( BuildScanPublishing.PublishingContext::isAuthenticated );

        BuildScanMetadata.addMainMetadata(develocityApi.getBuildScan(), mavenSession);

        Normalization.configureNormalization(develocityApi.getBuildCache());

        List<ConfiguredPlugin> configuredGoals = List.of(
                new CompilerConfiguredPlugin(),
                new SurefireConfiguredPlugin(),
                new FailsafeConfiguredPlugin(),
                new ForbiddenApisConfiguredPlugin()
        );

        for (ConfiguredPlugin configuredGoal : configuredGoals) {
            configuredGoal.configureBuildCache(develocityApi, mavenSession);
        }
    }
}
