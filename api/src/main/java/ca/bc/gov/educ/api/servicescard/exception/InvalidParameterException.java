package ca.bc.gov.educ.api.servicescard.exception;

/**
 * InvalidParameterException to provide error details when unexpected parameters are passed to endpoint
 *
 * @author John Cox
 *
 */

public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = -2325104800954988680L;

	public InvalidParameterException(String... searchParamsMap) {
        super(InvalidParameterException.generateMessage(searchParamsMap));
    }

    private static String generateMessage(String... searchParams) {
        StringBuilder message = new StringBuilder("Unexpected request parameters provided: ");
        String prefix = "";
        for (String parameter:searchParams) {
            message.append(prefix);
            prefix = ",";
            message.append(parameter);
        }
        return message.toString();
    }
}
