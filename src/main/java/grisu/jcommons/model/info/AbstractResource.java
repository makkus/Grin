package grisu.jcommons.model.info;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

public abstract class AbstractResource {


    public final static Set<AbstractResource> getRecursiveConnections(
            Class<AbstractResource> arc, AbstractResource r, Set<Class> excludes) {

        if (r.getClass() == arc || excludes.contains(r.getClass())) {
            return Sets.newHashSet();
        }

        Set<AbstractResource> result = Sets.newHashSet(r);

        for (AbstractResource temp : r.getDirectConnections()) {
            if (!temp.getClass().equals(arc)) {
                if ( excludes.contains(temp.getClass()) ) {
                    continue;
                }
                result.add(temp);

                Set<AbstractResource> recCon = getRecursiveConnections(arc, temp, excludes);
                for (AbstractResource tmp2 : recCon) {
                    if (!tmp2.getClass().equals(arc)) {
                        if ( excludes.contains(tmp2.getClass()) ) {
                            continue;
                        }
                        result.add(tmp2);

                    }
                }
            }
        }

        return result;
    }

    protected String alias;

    public final UUID uuid = UUID.randomUUID();

    private LinkedHashMap<String, String> options = Maps.newLinkedHashMap();

    protected final Set<AbstractResource> connections = Sets.newHashSet();

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

    protected Set<Class> getExcludeConnections() {
        HashSet<Class> tem = new HashSet<Class>();
        return tem;
    }


    public final void popluateConnections() {

        for (AbstractResource ar : getDirectConnections()) {
            ar.addConnection(this);
        }

    }

    public final void populateConnections2() {

        Set<AbstractResource> temp = Sets.newHashSet(connections);

        for (AbstractResource ar : temp) {
            Set<AbstractResource> set = getRecursiveConnections(
                    (Class<AbstractResource>) this.getClass(), ar, getExcludeConnections());
            connections.addAll(set);
            for (AbstractResource r : set) {
                if ( ! r.getExcludeConnections().contains(this.getClass())) {
                    addConnection(r);
                }
            }
        }

    }

    final public void setAlias(String alias) {
        this.alias = alias;
    }

}
