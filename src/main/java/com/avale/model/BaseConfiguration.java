package com.avale.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A base implementation of a {@link Configuration} that should be used for other standard configuration.
 * This one take cares of managing configuration updates and notification of updates to listeners.
 */
public abstract class BaseConfiguration implements Configuration {
	/**
	 * A list of listener that are interested in this configuration changes.
	 */
	private List<Consumer<ConfigurationChange>> configurationChangeListeners = new ArrayList<>();


	/**
	 * Changes the configuration content using the given {@code configuration}.
	 */
	protected abstract void setText(String configurationText);

	@Override
	public final void apply(Replacement replacement) {
		ConfigurationChange configurationChange = ConfigurationChange.applyingReplacementOn(replacement, text());
		setText(configurationChange.finalValue);
		configurationChangeListeners.forEach(listener -> listener.accept(configurationChange));
	}

	@Override
	public final void onContentReplacement(Consumer<ConfigurationChange> changeConsumer) {
		configurationChangeListeners.add(changeConsumer);
	}
}
