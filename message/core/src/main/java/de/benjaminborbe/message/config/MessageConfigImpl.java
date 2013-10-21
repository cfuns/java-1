package de.benjaminborbe.message.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionInteger;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.tools.util.ParseUtil;

public class MessageConfigImpl extends ConfigurationBase implements MessageConfig {

    private final ConfigurationDescriptionInteger consumerAmount = new ConfigurationDescriptionInteger(10, "MessageConsumerAmount", "Message Consumer Amount");

    /* s m h d m dw y */
    private final ConfigurationDescriptionString scheduleExpression = new ConfigurationDescriptionString("* * * * * ?", "MessageConsumerScheduleExpression",
            "Message Consumer ScheduleExpression");

    @Inject
    public MessageConfigImpl(
            final Logger logger,
            final ConfigurationService configurationService,
            final ParseUtil parseUtil,
            final ConfigurationServiceCache configurationServiceCache) {
        super(logger, parseUtil, configurationServiceCache);
    }

    @Override
    public Collection<ConfigurationDescription> getConfigurations() {
        final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
        result.add(consumerAmount);
        result.add(scheduleExpression);
        return result;
    }

    @Override
    public String getScheduleExpression() {
        return getValueString(scheduleExpression);
    }

    @Override
    public Integer getConsumerAmount() {
        return getValueInteger(consumerAmount);
    }

}
