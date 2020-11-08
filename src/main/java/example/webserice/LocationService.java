package example.webserice ;

import java.util.Collection ;

import javax.ws.rs.PathParam ;

import org.apache.cxf.jaxrs.model.wadl.Description ;

//@Path("/location/")
//@WebService
public interface LocationService
{

  // @WebMethod
  // @GET
  // @Path("{location}")
  // @Descriptions({ @Description(value = "returns a location data ", target =
  // DocTarget.METHOD), @Description(value = "the location data", target =
  // DocTarget.RETURN) })
  public LocationData readLocation(@Description(value = "the string representation of the location") @PathParam("location") String location) throws Exception ;

  // @WebMethod
  // @GET
  // @Path("*")
  // @Descriptions({ @Description(value = "returns all locations", target =
  // DocTarget.METHOD), @Description(value = "the location data", target =
  // DocTarget.RETURN) })
  public Collection<LocationData> readAllLocations() ;

  // @WebMethod
  // @POST
  // @Descriptions({ @Description(value = "stores a new location data", target =
  // DocTarget.METHOD), @Description(value = "the newly created location data",
  // target = DocTarget.RETURN) })
  public LocationData createLocation(LocationData locationData) throws Exception ;

  // @WebMethod
  // @PUT
  // @Descriptions({ @Description(value =
  // "updates or creates a new location data", target = DocTarget.METHOD),
  // @Description(value = "the newly created location data", target =
  // DocTarget.RETURN) })
  public LocationData updateorCreateLocation(LocationData locationData) ;

  // @WebMethod
  // @DELETE
  // @Path("{location}")
  // @Descriptions({ @Description(value = "deletes a location data", target =
  // DocTarget.METHOD), @Description(value = "the location data", target =
  // DocTarget.RETURN) })
  public void deleteLocation(@Description(value = "the string representation of the location") @PathParam("location") String location) throws Exception ;

  // @WebMethod
  // @DELETE
  // @Path("*")
  // @Descriptions({ @Description(value = "deletes All location data", target =
  // DocTarget.METHOD) })
  public void deleteAllLocation() ;

}
