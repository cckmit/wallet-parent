package org.wallet.service.batch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String from;
    private String inviteCodeSubject;
    private String inviteCodeContent;
}
