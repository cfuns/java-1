package de.benjaminborbe.crawler.api;


public class CrawlerInstructionBuilder implements CrawlerInstruction {

	private final String url;

	private int depth;

	private boolean followDomainLinksAllowed;

	public CrawlerInstructionBuilder(final String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public boolean isfollowDomainLinksAllowed() {
		return followDomainLinksAllowed;
	}

	public CrawlerInstructionBuilder setFollowDomainLinksAllowed(final boolean followDomainLinksAllowed) {
		this.followDomainLinksAllowed = followDomainLinksAllowed;
		return this;
	}

	public CrawlerInstructionBuilder setDepth(final int depth) {
		this.depth = depth;
		return this;
	}

}
