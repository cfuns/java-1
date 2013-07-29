package de.benjaminborbe.virt.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.virt.api.VirtMachine;
import de.benjaminborbe.virt.api.VirtMachineIdentifier;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.api.VirtServiceException;
import de.benjaminborbe.virt.api.VirtVirtualMachineIdentifier;
import de.benjaminborbe.virt.core.VirtCoreConstants;
import de.benjaminborbe.virt.core.dao.VirtNetworkBean;
import de.benjaminborbe.virt.core.dao.VirtNetworkDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VirtCoreServiceImpl implements VirtService {

	private final Logger logger;

	private final ValidationExecutor validationExecutor;

	private final VirtNetworkDao virtNetworkDao;

	private final IdGeneratorUUID iIdGenerator;

	private final AuthorizationService authorizationService;

	@Inject
	public VirtCoreServiceImpl(
		final Logger logger,
		final ValidationExecutor validationExecutor,
		final VirtNetworkDao virtNetworkDao,
		final IdGeneratorUUID iIdGeneratorUUID,
		final AuthorizationService authorizationService
	) {
		this.logger = logger;
		this.validationExecutor = validationExecutor;
		this.virtNetworkDao = virtNetworkDao;
		this.iIdGenerator = iIdGeneratorUUID;
		this.authorizationService = authorizationService;
	}

	@Override
	public VirtNetworkIdentifier createNetworkIdentifier(final String id) {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtNetworkIdentifier(id);
		}
	}

	@Override
	public VirtNetworkIdentifier createNetwork(
		final SessionIdentifier sessionIdentifier,
		final VirtNetwork network
	) throws VirtServiceException, PermissionDeniedException, ValidationException {
		try {
			authorizationService.expectPermission(sessionIdentifier, getDefaultPermission());

			final VirtNetworkBean bean = virtNetworkDao.create();
			bean.setId(createNetworkIdentifier(iIdGenerator.nextId()));
			bean.setName(network.getName());
			bean.setIp(network.getIp());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("VirtNetworkBean " + errors.toString());
				throw new ValidationException(errors);
			}

			virtNetworkDao.save(bean
			);

			return bean.getId();

		} catch (StorageException e) {
			throw new VirtServiceException(e);
		} catch (AuthorizationServiceException e) {
			throw new VirtServiceException(e);
		}
	}

	private PermissionIdentifier getDefaultPermission() throws AuthorizationServiceException {
		return authorizationService.createPermissionIdentifier(VirtCoreConstants.PERMISSION_NAME);
	}

	@Override
	public VirtMachineIdentifier createMachineIdentifier(final String id) {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtMachineIdentifier(id);
		}
	}

	@Override
	public VirtMachineIdentifier createMachine(
		final SessionIdentifier sessionIdentifier,
		final VirtMachine machine
	) throws VirtServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, getDefaultPermission());
			return new VirtMachineIdentifier("1337");
		} catch (AuthorizationServiceException e) {
			throw new VirtServiceException(e);
		}
	}

	@Override
	public VirtVirtualMachineIdentifier createVirtualMachineIdentifier(final String id) {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtVirtualMachineIdentifier(id);
		}
	}

	@Override
	public VirtVirtualMachineIdentifier createVirtualMachine(
		final SessionIdentifier sessionIdentifier,
		final VirtMachine machine
	) throws VirtServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, getDefaultPermission());
			return new VirtVirtualMachineIdentifier("1337");
		} catch (AuthorizationServiceException e) {
			throw new VirtServiceException(e);
		}
	}

	@Override
	public VirtNetwork getNetwork(
		final SessionIdentifier sessionIdentifier,
		final VirtNetworkIdentifier networkIdentifier
	) throws VirtServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, getDefaultPermission());
			return virtNetworkDao.load(networkIdentifier);
		} catch (AuthorizationServiceException e) {
			throw new VirtServiceException(e);
		} catch (StorageException e) {
			throw new VirtServiceException(e);
		}
	}
}
