package grisu.jcommons.model.info;

public interface QueueUpdater {

	public abstract void addQueue(Queue q);

	public abstract void removeQueue(Queue q);

	public abstract void startInfoUpdate(int delay_in_seconds);

	public abstract void stopInfoUpdate();

}