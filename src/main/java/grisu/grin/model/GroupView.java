package grisu.grin.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupView {

	public static final Logger myLogger = LoggerFactory
			.getLogger(GroupView.class);

	public static void main(String[] args) {
		//
		// YnfoManager ym = new YnfoManager(
		// "/home/markus/Workspaces/Goji/grin/src/main/resources/default_config.groovy");
		//
		// Grid grid = ym.getGrid();
		// Group group = grid.getGroups().iterator().next();
		//
		// GroupView gv = new GroupView(grid);
		//
		// Collection<Package> packages = gv.getResources(Package.class,
		// new Application("Java"), new Version("1.7.0"));
		//
		// for (Package p : packages) {
		// System.out.println("Package: " + p.toString());
		// }
	}

	private final Grid grid;

	public GroupView(Grid grid) {
		this.grid = grid;
	}

	// public Collection<Package> getPackages(Object... filters) {
	//
	// Predicate<Object> p = Filters.filterResource(filters);
	//
	// Collection<Package> filtered = Collections2.filter(grid.getPackages(),
	// p);
	//
	// return filtered;
	// }
	//
	// public <T> Collection<T> getResources(Class<T> resource, Object...
	// filters) {
	//
	// Class<T> filterClass = resource;
	// String filterName = filterClass.getSimpleName();
	//
	// Method m = null;
	// try {
	// m = Grid.class.getMethod("get" + filterName + "s");
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// myLogger.debug("Can't get method to retrieve: " + filterName);
	// return null;
	// }
	//
	// try {
	// Collection<T> result = (Collection<T>) m.invoke(grid);
	//
	// Predicate<Object> p = Filters.filterResource(filters);
	//
	// Collection<T> filtered = Collections2.filter(result, p);
	//
	// return filtered;
	// } catch (Exception e) {
	// e.printStackTrace();
	// myLogger.debug("Can't execute method to retrieve: " + filterName);
	// return null;
	// }
	//
	// }
}
