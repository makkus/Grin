package grisu.jcommons.model.info;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class DefaultQueueUpdater implements QueueUpdater {

	private class UpdateQueueTimerTask extends TimerTask {

		public UpdateQueueTimerTask() {
		}

		@Override
		public void run() {
			updateQueues();
		}
	}

	public static Logger myLogger = LoggerFactory.getLogger(DefaultQueueUpdater.class);

	private final Set<Queue> queues = Sets.newHashSet();

	private final Timer timer = new Timer();
	private TimerTask updateTask;

	private int delay_in_seconds = 120;

	public DefaultQueueUpdater() {

		startInfoUpdate(delay_in_seconds);
	}

	public void addQueue(Queue q) {
		this.queues.add(q);
	}

	public void removeQueue(Queue q) {
		this.queues.remove(q);
	}

	public void startInfoUpdate(int delay_in_seconds) {
		this.delay_in_seconds = delay_in_seconds;
		if (updateTask != null) {
			updateTask.cancel();
		}

		updateTask = new UpdateQueueTimerTask();
		timer.scheduleAtFixedRate(updateTask, 1, delay_in_seconds * 1000);
	}

	public void stopInfoUpdate() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	private void updateQueue(Queue q) {
		myLogger.debug("Updating dynamic properties of queue: " + q.getName());
	}

	private synchronized void updateQueues() {

		for (Queue q : queues) {
			updateQueue(q);
		}

	}

}
