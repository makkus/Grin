package grisu.grin.model.resources;

import java.util.Set;

import com.google.common.collect.Sets;

public abstract class AbstractResource {

	public final static Set<AbstractResource> getRecursiveConnections(
			Class<AbstractResource> arc, AbstractResource r) {

		if (r.getClass() == arc) {
			return Sets.newHashSet();
		}

		Set<AbstractResource> result = Sets.newHashSet(r);

		for (AbstractResource temp : r.getDirectConnections()) {
			if (!temp.getClass().equals(arc)) {
				result.add(temp);
				for (AbstractResource tmp2 : getRecursiveConnections(arc, temp)) {
					if (!tmp2.getClass().equals(arc)) {
						result.add(tmp2);
					}
				}
			}
		}

		return result;
	}

	private final Set<AbstractResource> connections = Sets.newHashSet();

	protected void addConnection(AbstractResource res) {

		if (res.getClass().equals(this.getClass())) {
			return;
		}

		if (!connections.contains(res)) {
			connections.add(res);
		}

		res.connections.add(this);

	}

	public Set<AbstractResource> getConnections() {

		return connections;

	}

	public abstract Set<AbstractResource> getDirectConnections();

	public final void popluateConnections() {

		for (AbstractResource ar : getDirectConnections()) {
			ar.addConnection(this);
		}

	}

	public final void populateConnections2() {

		Set<AbstractResource> temp = Sets.newHashSet(connections);

		for (AbstractResource ar : temp) {
			Set<AbstractResource> set = getRecursiveConnections(
					(Class<AbstractResource>) this.getClass(), ar);
			connections.addAll(set);
			for (AbstractResource r : set) {
				addConnection(r);
			}
		}

	}

}
