package grisu.jcommons.model.info;

import grisu.jcommons.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class Application extends AbstractResource implements
		Comparable<Application> {

	public static final Application GENERIC_APPLICATION = new Application(
			Constants.GENERIC_APPLICATION_NAME);
	
	public static Collection<String> PREFERRED_APPLICATION_NAMES = Sets.newHashSet();
	
	public static void setApplicationNameList(String path) {
		try {
			List<String> lines = Files.readLines(new File(path), Charsets.UTF_8);
			Set<String> temp = Sets.newHashSet();
			for (String line : lines) {
				if ( StringUtils.isNotBlank(line)) {
					temp.add(line.trim());
				}
			}
			
			PREFERRED_APPLICATION_NAMES = temp;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	private final static Map<String, Application> cached = Maps.newHashMap();

	public static synchronized Application get(String application) {

		if (cached.get(application) == null) {
			if ( PREFERRED_APPLICATION_NAMES != null ) {
				for ( String app : PREFERRED_APPLICATION_NAMES ) {
					if ( app.equalsIgnoreCase(application) ) {
						application = app;
						break;
					}
				}
			}
			cached.put(application, new Application(application));
		}

		return cached.get(application);
	}
	
	public static void clearCache() {
		cached.clear();
	}

	private String name;

	private Application() {
	}

	public Application(String name) {
		this.name = name;
	}

	public int compareTo(Application o) {
		return this.name.compareTo(o.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Application other = (Application) obj;
		return Objects.equal(this.getName(), other.getName());
	}

	@Override
	public Set<AbstractResource> getDirectConnections() {

		Set<AbstractResource> result = Sets.newHashSet();
		return result;

	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getName());
	}

	@Override
	public String toString() {
		return getName();
	}

}
