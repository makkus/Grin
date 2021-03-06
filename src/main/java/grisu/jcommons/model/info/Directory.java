package grisu.jcommons.model.info;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import grisu.jcommons.constants.Constants;
import grisu.jcommons.utils.EndpointHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A Directory is an object that points to a url in grid-space (consisting of a
 * {@link FileSystem} and a path.
 * <p/>
 * It also contains information on which VO is needed to access the url.
 *
 * @author Markus Binsteiner
 */
public class Directory extends AbstractPhysicalResource implements
        Comparable<Directory> {

    public static final Logger myLogger = LoggerFactory.getLogger(Directory.class);

    public static boolean isShared(Directory d) {
        String shared = d.getOptions().get(Constants.INFO_DIRECTORY_SHARED_KEY);

        return Boolean.parseBoolean(shared);
    }

    public static boolean isVolatileDirectory(Directory d) {
        String vol = d.getOptions().get(Constants.INFO_IS_VOLATILE_KEY);

        return Boolean.parseBoolean(vol);
    }

    public static String getOption(Directory d, String key) {
        String val = d.getOptions().get(Constants.INFO_IS_VOLATILE_KEY);
        return val;
    }

    private static String fixMdsLegacies(String path) {

        path = path.replace("${GLOBUS_USER_HOME}", "~");

        int i = path.indexOf("[");
        if (i > 0) {
            path = path.substring(0, i);
        }

        return path;
    }

    private static String slash(String path) {

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.endsWith("/")) {
            return path + "/";
        } else {
            return path;
        }
    }

    private FileSystem filesystem;
    private String path = "/~/";
    private Collection<Group> groups = Sets.newHashSet(Group.NO_VO_GROUP);

    private Directory() {
    }

    public Directory(FileSystem fs, String path, Set<Group> fqans, String alias) {
        setFileSystem(fs);
        if (path.endsWith("/")) {
            setPath(slash(fixMdsLegacies(path.substring(0, path.length() - 1))));
        } else {
            setPath(slash(fixMdsLegacies(path)));
        }

        setGroups(Sets.newTreeSet(fqans));
        // if (StringUtils.isBlank(alias)) {
        // alias = EndpointHelpers.translateIntoEndpointName(fs.getHost(),
        // fqan.getFqan());
        // }
        setAlias(alias);
    }


    public int compareTo(Directory o) {

        int r = filesystem.compareTo(o.getFilesystem());
        if (r == 0) {
            r = path.compareTo(o.getPath());
            if (r == 0) {

                // TODO compare both groups
                // r = groups.compareTo(o.getGroups());
            }
        }
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Directory) {
            Directory other = (Directory) o;

            if (getFilesystem().equals(other.getFilesystem())
                    && path.equals(other.getPath())) {
                // TODO equals for groups
                // && groups.equals(other.getGroup())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String getContactString() {
        return toUrl();
    }

    @Override
    public Set<AbstractResource> getDirectConnections() {

        Set<AbstractResource> result = Sets.newHashSet();
        result.addAll(getGroups());
        result.add(getFilesystem());
        return result;
    }

    public FileSystem getFilesystem() {
        return filesystem;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public String getHost() {
        return getFilesystem().getHost();
    }

    public String getPath() {
        return path;
    }




    public String getRelativePath(String url) {
        if (EndpointHelpers.isGlobusOnlineUrl(url)) {
            String username = EndpointHelpers.extractUsername(url);
            String epName = EndpointHelpers.extractEndpointName(url);

            if (!epName.equals(EndpointHelpers.extractEndpointName(getAlias()))) {
                throw new IllegalStateException(
                        "Url not in this directory filespace.");
            }

            String otherPath = EndpointHelpers.extractPathPart(url);

            if (!otherPath.startsWith(path)) {
                throw new IllegalStateException(
                        "Url not in this directory filespace.");
            }

            return otherPath.substring(path.length());

        } else {

            String thisUrl = toUrl();

            if (!url.startsWith(thisUrl)) {
                throw new IllegalStateException(
                        "Url not in this directory filespace.");
            }

            return url.substring(thisUrl.length());

        }
    }

    protected Set<Class> getExcludeConnections() {
        HashSet<Class> temp = new HashSet<Class>();
        temp.add(Group.class);
        return temp;
    }


    @Override
    public Site getSite() {
        return getFilesystem().getSite();
    }

    public String toUrl() {
        return filesystem.toString() + path;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(filesystem, path, groups);
    }

    private void setFileSystem(FileSystem fs) {
        this.filesystem = fs;
    }

    private void setGroups(Set<Group> fqans) {
        myLogger.debug("Directory {}: setting groups {}", this.toUrl(), fqans);
        this.groups = Sets.newTreeSet(fqans);
    }

    private void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return toUrl();
    }

}
