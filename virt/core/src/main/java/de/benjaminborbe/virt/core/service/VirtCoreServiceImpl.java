package de.benjaminborbe.virt.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.virt.api.VirtMachineIdentifier;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.api.VirtServiceException;
import de.benjaminborbe.virt.core.dao.VirtNetworkBean;
import de.benjaminborbe.virt.core.dao.VirtNetworkDao;
import org.slf4j.Logger;

@Singleton
public class VirtCoreServiceImpl implements VirtService {

	private final Logger logger;

	private final ValidationExecutor validationExecutor;

	private final VirtNetworkDao virtNetworkDao;

	private final IdGeneratorUUID iIdGenerator;

	@Inject
	public VirtCoreServiceImpl(final Logger logger, final ValidationExecutor validationExecutor, final VirtNetworkDao virtNetworkDao, final IdGeneratorUUID iIdGeneratorUUID) {
		this.logger = logger;
		this.validationExecutor = validationExecutor;
		this.virtNetworkDao = virtNetworkDao;
		this.iIdGenerator = iIdGeneratorUUID;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

	@Override
	public VirtNetworkIdentifier createVirtNetworkIdentifier(final String id) throws VirtServiceException {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtNetworkIdentifier(id);
		}
	}

	@Override
	public VirtMachineIdentifier createVirtualMachine() {
		return new VirtMachineIdentifier("1337");
	}

	@Override
	public VirtNetworkIdentifier createVirtNetwork(final VirtNetwork virtNetwork) throws VirtServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {

		try {
			final VirtNetworkBean bean = virtNetworkDao.create();
			bean.setId(createVirtNetworkIdentifier(iIdGenerator.nextId()));
			bean.setName(virtNetwork.getName());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("VirtNetworkBean " + errors.toString());
				throw new ValidationException(errors);
			}

			return bean.getId();

		} catch (StorageException e) {
			throw new VirtServiceException(e);
		}

	}

	@Override
	public VirtMachineIdentifier createVirtMachineIdentifier(final String id) throws VirtServiceException {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtMachineIdentifier(id);
		}
	}

}
