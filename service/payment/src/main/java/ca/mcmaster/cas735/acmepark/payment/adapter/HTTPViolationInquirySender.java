package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.dto.TicketDTO;
import ca.mcmaster.cas735.acmepark.payment.ports.ViolationInquirySender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class HTTPViolationInquirySender implements ViolationInquirySender {

    private final RestTemplate restTemplate;
    @Value("${app.custom.messaging.HTTP.violation}")
    String violationServiceUrl;

    public HTTPViolationInquirySender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<TicketDTO> sendInquiry(String licensePlate) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(violationServiceUrl)
                    .path("/tickets/lookup")
                    .queryParam("licensePlate", licensePlate)
                    .toUriString();

            TicketDTO[] responseArray = restTemplate.getForObject(url, TicketDTO[].class);

            if (responseArray != null) {
                return Arrays.asList(responseArray);
            }
            return Collections.emptyList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}