package org.hibernate.infra.develocity.util;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

public final class MavenProperties {

	public static final String BUILD_CACHE_JAVA_VERSION_EXACT = "build-cache.java-version.exact";

	private final PluginParameterExpressionEvaluator evaluator;

	public MavenProperties(MavenSession session, MojoExecution mojoExecution) {
		this.evaluator = new PluginParameterExpressionEvaluator( session, mojoExecution );
	}

	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean( getString( key ) );
	}

	public String getString(String key) {
		return (String) get( key );
	}

	public Object get(String key) {
		try {
			return evaluator.evaluate( "${" + key + "}" );
		}
		catch (ExpressionEvaluationException e) {
			throw new RuntimeException( "Could not get value for %s: %s".formatted( key, e.getMessage() ), e );
		}
	}

	public boolean cacheExactJavaVersion() {
		return getBoolean( BUILD_CACHE_JAVA_VERSION_EXACT );
	}
}
