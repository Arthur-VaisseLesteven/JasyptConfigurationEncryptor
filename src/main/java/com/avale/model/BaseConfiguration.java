package com.avale.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
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

	private final Stack<ConfigurationChange> appliedChanges = new Stack<>();
	private final Stack<ConfigurationChange> revertedChanges = new Stack<>();

	/**
	 * Changes the configuration content using the given {@code configuration}.
	 */
	protected abstract void setText(String configurationText);

	@Override
	public final void onContentReplacement(Consumer<ConfigurationChange> changeConsumer) {
		configurationChangeListeners.add(changeConsumer);
	}

	@Override
	public final void apply(Replacement replacement) {
		ConfigurationChange configurationChange = ConfigurationChange.applyingReplacementOn(replacement, text());
		setTextAndNotifyUpdate(configurationChange);

		appliedChanges.push(configurationChange);
		revertedChanges.clear();
	}

	private void setTextAndNotifyUpdate(ConfigurationChange configurationChange) {
		setText(configurationChange.finalValue);
		configurationChangeListeners.forEach(listener -> listener.accept(configurationChange));
	}

	@Override
	public void undoLastChange() {
		moveConfigurationChange(appliedChanges, revertedChanges).map(ConfigurationChange::reverting).ifPresent(this::setTextAndNotifyUpdate);
	}

	@Override
	public void redoLastRevertedChange() {
		moveConfigurationChange(revertedChanges, appliedChanges).ifPresent(this::setTextAndNotifyUpdate);
	}

	/**
	 * Push onto the {@code destination} the top element of the {@code source} {@link Stack}, if any.
	 *
	 * @return The {@link ConfigurationChange} that moved from one stack to another if any, empty otherwise.
	 */
	private Optional<ConfigurationChange> moveConfigurationChange(Stack<ConfigurationChange> source, Stack<ConfigurationChange> destination) {
		return source.isEmpty() ? Optional.empty() : Optional.of(destination.push(source.pop()));
	}
}
