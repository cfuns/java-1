package de.benjaminborbe.dns.core.check;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.api.DnsServiceException;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DnsServerMonitoringCheck implements MonitoringCheck {

	public static final String ID = "1142fcf6-128c-46f1-81af-eb7a2307546d";

	private static final String DNS_SERVER_NAME = "dns_server_name";

	private static final String DNS_LOOKUP_NAME = "dns_target_name";

	private final ValidationConstraintValidator validationConstraintValidator;

	private final DnsService dnsService;

	@Inject
	public DnsServerMonitoringCheck(
		final ValidationConstraintValidator validationConstraintValidator, final DnsService dnsService) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.dnsService = dnsService;
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return "DnsServer";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(DNS_SERVER_NAME, DNS_LOOKUP_NAME);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			final String dnsServer = parameter.get(DNS_SERVER_NAME);
			final String domain = parameter.get(DNS_LOOKUP_NAME);
			final List<String> result = dnsService.lookup(dnsServer, domain);
			if (result != null && !result.isEmpty()) {
				return new MonitoringCheckResultDto(this, true);
			} else {
				return new MonitoringCheckResultDto(this, false, "lookup domain " + domain + " via server " + dnsServer + " failed");
			}
		} catch (DnsServiceException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check for " + parameter.get(DNS_LOOKUP_NAME) + " via " + parameter.get(DNS_SERVER_NAME);
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<>();

		// DNS_SERVER_NAME
		{
			final String value = parameter.get(DNS_SERVER_NAME);
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate(DNS_SERVER_NAME, value, constraints));
		}

		// DNS_SERVER_NAME
		{
			final String value = parameter.get(DNS_LOOKUP_NAME);
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate(DNS_LOOKUP_NAME, value, constraints));
		}

		return result;
	}

}
