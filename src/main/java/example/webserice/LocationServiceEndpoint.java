package example.webserice ;

import java.util.Collection ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.UUID ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.validation.BeanPropertyBindingResult ;
import org.springframework.validation.FieldError ;
import org.springframework.validation.Validator ;

@javax.jws.WebService(serviceName = "LocationServiceEndpointService", targetNamespace = "http://webservice.cxfexample.exampledriven.org/")
public class LocationServiceEndpoint
{

  private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

  private Map<String, LocationData> locations = new HashMap<String, LocationData>() ;

  @Autowired
  private Validator validator ;

  // security handled by URL mapping in the xml
  // @Secured("ROLE_RESTCLIENT")
  public LocationData readLocation(String location) throws Exception
  {

    LocationData locationData = locations.get(location) ;

    if (locationData == null)
    {
      throw new Exception() ;
    }

    return locationData ;

  }

  public LocationData createLocation(LocationData locationData) throws Exception
  {

    if (locations.get(locationData.getLocation()) != null)
    {
      throw new Exception() ;
    }

    BeanPropertyBindingResult br = new BeanPropertyBindingResult(locationData, "locationData") ;
    // validator.validate(locationData, br) ;
    if (br.hasErrors())
    {
      Map<String, String> errors = new HashMap<String, String>() ;
      for (FieldError e : br.getFieldErrors())
      {

        logger.debug(e.getDefaultMessage()) ;
        errors.put(e.getField(), e.getDefaultMessage()) ;
      }

      // throw new Exception(errors) ;
      throw new Exception() ;

    }

    setNewID(locationData) ;
    storeLocation(locationData) ;

    return locationData ;
  }

  public LocationData updateorCreateLocation(LocationData locationData)
  {

    if (locations.get(locationData.getLocation()) == null)
    {
      setNewID(locationData) ;
    }

    storeLocation(locationData) ;

    return locationData ;

  }

  private void setNewID(LocationData locationData)
  {
    // setting the ID
    String id = UUID.randomUUID().toString() ;
    locationData.setId(id) ;

  }

  private void storeLocation(LocationData locationData)
  {

    locations.put(locationData.getLocation(), locationData) ;

  }

  public Collection<LocationData> readAllLocations()
  {
    return locations.values() ;
  }

  public void deleteLocation(String location) throws Exception
  {
    LocationData locationData = locations.get(location) ;

    if (locationData == null)
    {
      throw new Exception() ;
    }

    locations.remove(location) ;

  }

  public void deleteAllLocation()
  {

    locations.clear() ;

  }

}