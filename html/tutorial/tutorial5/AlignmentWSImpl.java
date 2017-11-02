package example.ws.matcher;

import eu.sealsproject.omt.ws.matcher.AlignmentWS;

import java.net.URI;

import javax.jws.WebService;


/**
 * WebService
 */
@WebService(endpointInterface="eu.sealsproject.omt.ws.matcher.AlignmentWS")
/**
 * Class AlignmentWSImpl
 */
public class AlignmentWSImpl implements AlignmentWS {

	   public String align(URI source, URI target) {
		  		   
		   // your implementation
                   return alignment; 
	}
}
