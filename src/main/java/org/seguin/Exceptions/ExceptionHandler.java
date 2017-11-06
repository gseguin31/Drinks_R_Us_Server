package org.seguin.Exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Gabriel on 24/10/2017.
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception>{
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST).entity((e.getClass().getSimpleName())).build();
    }
}
