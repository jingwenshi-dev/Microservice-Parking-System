package ca.mcmaster.cas735.acmepark.violation.business.errors;

import java.util.List;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entityName, String identifier) {
        super(String.format("%s %s not found.", entityName, identifier));
    }

    public NotFoundException(List<String> entityNames, List<String> identifiers) {
        super(buildMessage(entityNames, identifiers));
    }

    private static String buildMessage(List<String> entityNames, List<String> identifiers) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < entityNames.size(); i++) {
            message.append(String.format("%s %s not found.\n", entityNames.get(i), identifiers.get(i)));
        }
        return message.toString();
    }

}