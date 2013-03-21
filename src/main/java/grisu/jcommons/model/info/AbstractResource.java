package grisu.jcommons.model.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
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

	protected String alias;
	
	private LinkedHashMap<String, String> options = Maps.newLinkedHashMap();

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

	public synchronized String getAlias() {
		
		return alias;
	}
	
	public Map<String, String> getOptions() {
		return options;
	}
	
	public void setOptions(Map<String, String> options) {
		this.options = Maps.newLinkedHashMap(options);
	}

	public Set<AbstractResource> getConnections() {

		return connections;

	}

	protected abstract Set<AbstractResource> getDirectConnections();
	

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

	final public void setAlias(String alias) {
		this.alias = alias;
	}

}
