package com.avale.model;

import org.junit.Test;

import static com.avale.model.TestConfiguration.Builder.testConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class BaseConfigurationTest {

	private final static String originalConfigurationContent = "foobarfoo";

	@Test
	public void onAPply_onContentReplacement_listenersAreNotified() {
		// GIVEN a configuration
		TestConfiguration configuration = configuration();

		// WHEN applying a replacement on the configuration
		configuration.apply(replacement());

		// THEN the test configuration that listen itself have been notified of the change.
		ConfigurationChange configurationChange = ConfigurationChange.applyingReplacementOn(replacement(), originalConfigurationContent);
		assertThat(configuration.changeHistory()).containsOnly(configurationChange);
	}

	private TestConfiguration configuration() {
		return testConfiguration().withLines(originalConfigurationContent).build();
	}

	private Replacement replacement() {
		return new Replacement(0, 3, "ENC(FOO)");
	}

	@Test
	public void undoLastChange_hasNoEffectUponConfigurationWithoutChanges() {
		Configuration configuration = configuration();

		assertThatCode(configuration::undoLastChange).doesNotThrowAnyException();
		assertThat(configuration.text()).isEqualTo(originalConfigurationContent);
	}

	@Test
	public void undoLastChange_properlyRevertLastChange() {
		Configuration configuration = configuration();
		configuration.apply(replacement());
		configuration.undoLastChange();

		assertThat(configuration.text()).isEqualTo(originalConfigurationContent);
	}

	@Test
	public void undoLastChange_calledTwiceProperlyRevertBothChanges() {
		Configuration configuration = configuration();
		configuration.apply(replacement());
		configuration.apply(replacement());
		configuration.undoLastChange();
		configuration.undoLastChange();

		assertThat(configuration.text()).isEqualTo(originalConfigurationContent);
	}

	@Test
	public void redoLastRevertedChange_hasNoEffectUponConfigurationWithoutRevertedChange() {
		Configuration configuration = configuration();
		assertThatCode(configuration::redoLastRevertedChange).doesNotThrowAnyException();
		assertThat(configuration.text()).isEqualTo(originalConfigurationContent);
	}

	@Test
	public void redoLastRevertedChange_correctlyApplyAgainRevertedChange() {
		Configuration configuration = configuration();
		configuration.apply(replacement());

		String updatedConfigurationContent = configuration.text();

		configuration.undoLastChange();
		configuration.redoLastRevertedChange();

		assertThat(configuration.text()).isEqualTo(updatedConfigurationContent);
	}

	@Test
	public void redoLastRevertedChange_hasNoEffectWhenChangesOccurredSinceTheLastUndo() {
		// GIVEN a configuration
		Configuration configuration = configuration();
		// having a reverted change
		configuration.apply(replacement());
		configuration.undoLastChange();
		// that is overridden by a new change
		configuration.apply(new Replacement(0, 3, "ENC(BAR)"));
		String updatedConfigurationContent = configuration.text();
		// WHEN asking to redo the last reverted change
		configuration.redoLastRevertedChange();
		// THEN nothing changed
		assertThat(configuration.text()).isEqualTo(updatedConfigurationContent);
	}

	@Test
	public void redoLasrRevertedChange_and_undoLastCHange_canBeAlternatedAsManyTimesAsRequired() {
		TestConfiguration configuration = configuration();
		configuration.apply(replacement());

		configuration.undoLastChange();
		configuration.redoLastRevertedChange();
		configuration.undoLastChange();

		assertThat(configuration.text()).isEqualTo(originalConfigurationContent);
	}

	@Test
	public void enable_alreadyEnabledFeatureHasNoEffect() {
		TestConfiguration configuration = configuration();
		configuration.enable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isTrue();
		configuration.enable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isTrue();
	}

	private ConfigurationFeatures aFeature() {
		return ConfigurationFeatures.SAVE_META_DATA;
	}

	@Test
	public void enable_disabledFeatureEnableIt() {
		TestConfiguration configuration = configuration();
		assertThat(configuration.isEnabled(aFeature())).isFalse();
		configuration.enable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isTrue();
	}

	@Test
	public void disable_disabledFeatureHasNoEffect() {
		TestConfiguration configuration = configuration();
		assertThat(configuration.isEnabled(aFeature())).isFalse();
		configuration.disable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isFalse();
	}

	@Test
	public void disable_enabledFeatureDisableIt() {
		TestConfiguration configuration = configuration();
		configuration.enable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isTrue();
		configuration.disable(aFeature());
		assertThat(configuration.isEnabled(aFeature())).isFalse();
	}

}