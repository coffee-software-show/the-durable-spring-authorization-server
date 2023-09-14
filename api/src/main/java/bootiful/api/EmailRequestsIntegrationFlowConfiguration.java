package bootiful.api;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
class EmailRequestsIntegrationFlowConfiguration {

    private final String destinationName = "emails";

    @Bean
    IntegrationFlow emailRequestsIntegrationFlow(MessageChannel requests, AmqpTemplate template) {
        // <1>
        var outboundAmqpAdapter = Amqp
                .outboundAdapter(template)
                .routingKey(this.destinationName);

        return IntegrationFlow
                .from(requests)// <2>
                .transform(new ObjectToJsonTransformer()) // <3>
                .handle(outboundAmqpAdapter) // <4>
                .get();
    }

    @Bean
    DirectChannelSpec requests() {
        return MessageChannels.direct();
    }
}
