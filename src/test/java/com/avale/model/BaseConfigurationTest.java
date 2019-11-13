package com.avale.model;

import org.junit.Test;

import static com.avale.model.TestConfiguration.Builder.testConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseConfigurationTest {

	@Test
	public void onAPply_onContentReplacement_listenersAreNotified() {
		// GIVEN a configuration
		String originalConfigurationContent = "foobarfoo";
		TestConfiguration configuration = testConfiguration().withLines(originalConfigurationContent).build();
		// AND a replacement that will be applied on it
		Replacement replacement = new Replacement(0, 3, "ENC(FOO)");

		// WHEN applying the replacement on the configuration
		configuration.apply(replacement);

		// THEN the test configuration that listen itself have been notified of the change.
		ConfigurationChange configurationChange = ConfigurationChange.applyingReplacementOn(replacement, originalConfigurationContent);
		assertThat(configuration.changeHistory()).containsOnly(configurationChange);
	}

}