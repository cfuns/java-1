package de.benjaminborbe.bookmark.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkDeletionException;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkMatch;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.bookmark.dao.BookmarkBean;
import de.benjaminborbe.bookmark.dao.BookmarkDao;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.util.ComparatorBase;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BookmarkServiceImpl implements BookmarkService {

	private final class BookmarkMatchImpl implements BookmarkMatch {

		private final BeanMatch<Bookmark> match;

		private BookmarkMatchImpl(final BeanMatch<Bookmark> match) {
			this.match = match;
		}

		@Override
		public Bookmark getBookmark() {
			return match.getBean();
		}

		@Override
		public int getMatchCounter() {
			return match.getMatchCounter();
		}
	}

	private final class BookmarkSearcher extends BeanSearcher<Bookmark> {

		private static final String KEYWORDS = "keywords";

		private static final String DESCRIPTION = "description";

		private static final String NAME = "name";

		private static final String URL = "url";

		@Override
		protected Map<String, String> getSearchValues(final Bookmark bookmark) {
			final Map<String, String> values = new HashMap<String, String>();
			values.put(URL, bookmark.getUrl());
			values.put(NAME, bookmark.getName());
			values.put(DESCRIPTION, bookmark.getDescription());
			values.put(KEYWORDS, StringUtils.join(bookmark.getKeywords(), " "));
			return values;
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			final Map<String, Integer> values = new HashMap<String, Integer>();
			values.put(URL, 2);
			values.put(NAME, 2);
			values.put(DESCRIPTION, 1);
			values.put(KEYWORDS, 5);
			return values;
		}
	}

	private final class BookmarkByNameComparator extends ComparatorBase<Bookmark, String> {

		@Override
		public String getValue(final Bookmark o) {
			return o.getName() != null ? o.getName().toLowerCase() : null;
		}
	}

	private final Comparator<Bookmark> bookmarkComparator = new BookmarkByNameComparator();

	private final Logger logger;

	private final BookmarkDao bookmarkDao;

	private final AuthenticationService authenticationService;

	private final ValidationExecutor validationExecutor;

	private final AuthorizationService authorizationService;

	@Inject
	public BookmarkServiceImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final BookmarkDao bookmarkDao,
		final ValidationExecutor validationExecutor
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.bookmarkDao = bookmarkDao;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public List<Bookmark> getBookmarks(final SessionIdentifier sessionIdentifier) throws BookmarkServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.expectPermission(sessionIdentifier, permissionIdentifier);

			logger.trace("getBookmarks");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final EntityIterator<BookmarkBean> i = bookmarkDao.getByUsername(userIdentifier);
			final List<Bookmark> bookmarks = new ArrayList<Bookmark>();
			while (i.hasNext()) {
				bookmarks.add(i.next());
			}
			Collections.sort(bookmarks, bookmarkComparator);
			return bookmarks;
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (EntityIteratorException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		} catch (AuthenticationServiceException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public List<Bookmark> getBookmarkFavorite(final SessionIdentifier sessionIdentifier) throws BookmarkServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.expectPermission(sessionIdentifier, permissionIdentifier);

			logger.trace("getBookmarkFavorite");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final EntityIterator<BookmarkBean> i = bookmarkDao.getFavorites(userIdentifier);
			final List<Bookmark> bookmarks = new ArrayList<Bookmark>();
			while (i.hasNext()) {
				bookmarks.add(i.next());
			}
			Collections.sort(bookmarks, bookmarkComparator);
			return bookmarks;
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (EntityIteratorException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		} catch (AuthenticationServiceException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public List<BookmarkMatch> searchBookmarks(
		final SessionIdentifier sessionIdentifier,
		final int limit,
		final List<String> words
	) throws BookmarkServiceException,
		LoginRequiredException, PermissionDeniedException {
		final List<Bookmark> bookmarks = getBookmarks(sessionIdentifier);
		final BeanSearcher<Bookmark> beanSearch = new BookmarkSearcher();
		final List<BeanMatch<Bookmark>> matches = beanSearch.search(bookmarks, limit, words);
		final List<BookmarkMatch> result = new ArrayList<BookmarkMatch>();
		for (final BeanMatch<Bookmark> match : matches) {
			result.add(new BookmarkMatchImpl(match));
		}
		return result;
	}

	@Override
	public void createBookmark(
		final SessionIdentifier sessionIdentifier,
		final String url,
		final String name,
		final String description,
		final List<String> keywords
	) throws BookmarkServiceException, LoginRequiredException, ValidationException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final BookmarkBean bookmark = bookmarkDao.create();
			bookmark.setId(createBookmarkIdentifier(sessionIdentifier, url));
			bookmark.setUrl(url);
			bookmark.setName(name);
			bookmark.setDescription(description);
			bookmark.setKeywords(keywords);
			bookmark.setOwner(userIdentifier);

			final ValidationResult errors = validationExecutor.validate(bookmark);
			if (errors.hasErrors()) {
				logger.warn("Bookmark " + errors.toString());
				throw new ValidationException(errors);
			}
			bookmarkDao.save(bookmark);
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		} catch (AuthenticationServiceException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public BookmarkIdentifier createBookmarkIdentifier(final SessionIdentifier sessionIdentifier, final String url) throws BookmarkServiceException {
		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			return new BookmarkIdentifier(userIdentifier.getId() + "-" + url);
		} catch (final AuthenticationServiceException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public void deleteBookmark(
		final SessionIdentifier sessionIdentifier,
		final BookmarkIdentifier bookmarkIdentifier
	) throws BookmarkServiceException, BookmarkDeletionException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			final BookmarkBean bookmark = bookmarkDao.load(bookmarkIdentifier);
			if (bookmark == null) {
				logger.info("delete bookmark failed, not found");
				throw new BookmarkDeletionException("bookmark not found");
			}
			authorizationService.expectUser(sessionIdentifier, bookmark.getOwner());
			bookmarkDao.delete(bookmark);
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public void updateBookmark(
		final SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier, final String url, final String name, final String description,
		final List<String> keywords, final boolean favorite
	) throws BookmarkServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.info("updateBookmark");

			final BookmarkBean bookmark = bookmarkDao.load(bookmarkIdentifier);
			authorizationService.expectUser(sessionIdentifier, bookmark.getOwner());

			deleteBookmark(sessionIdentifier, bookmarkIdentifier);
			createBookmark(sessionIdentifier, url, name, description, keywords);
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		} catch (BookmarkDeletionException e) {
			throw new BookmarkServiceException(e);
		}
	}

	@Override
	public Bookmark getBookmark(
		final SessionIdentifier sessionIdentifier,
		final BookmarkIdentifier bookmarkIdentifier
	) throws BookmarkServiceException, PermissionDeniedException,
		LoginRequiredException {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.info("updateBookmark");
			final BookmarkBean bookmark = bookmarkDao.load(bookmarkIdentifier);
			authorizationService.expectUser(sessionIdentifier, bookmark.getOwner());
			return bookmark;
		} catch (final AuthorizationServiceException e) {
			throw new BookmarkServiceException(e);
		} catch (StorageException e) {
			throw new BookmarkServiceException(e);
		}
	}
}
