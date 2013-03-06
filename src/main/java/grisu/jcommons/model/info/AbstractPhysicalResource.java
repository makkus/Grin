package grisu.jcommons.model.info;




abstract public class AbstractPhysicalResource extends AbstractResource {
	
	private boolean available = true;
	
	public void setAvailable(boolean av) {
		this.available = av;
	}
	
	public boolean isAvailable() {
		return available;
	}

	abstract public String getContactString();

	abstract public Site getSite();

	public boolean isAccessibleVia(String url) {

		if (getContactString().contains(url)) {
			return true;
		} else {
			return false;
		}

	}

}
