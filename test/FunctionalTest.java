import org.junit.*;

import com.avaje.ebean.Ebean;

import java.util.*;

import play.mvc.*;
import play.test.*;
import play.libs.Yaml;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class FunctionalTest {
	
	@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
        
        // Loading test references
        Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("test-data.yml");
        Ebean.save(all.get("references"));
    }
	
    @Test
    public void redirectsToReferences() {

               Result result = callAction(controllers.routes.ref.Application.index());

               assertThat(status(result)).isEqualTo(SEE_OTHER);
               assertThat(redirectLocation(result)).isEqualTo("/references");

    }
    
    @Test
    public void listsReferencesOnFrontPage() {

               Result result = callAction(controllers.routes.ref.Application.list());

               assertThat(status(result)).isEqualTo(OK);
               assertThat(contentAsString(result)).contains("References list");

    }
    
    @Test
    public void createANewReference() {

                Result result = callAction(controllers.routes.ref.Application.save());

                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                
                Map<String,String> data = new HashMap<String,String>();
                data.put("author", "badbadbad");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                // should fail with no type
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("<input type=\"text\" id=\"author\" name=\"author\" value=\"badbadbad\" >");
                
                // add the required fields
                data.put("referenceType.id", "1");
                data.put("title", "test");
                data.put("year", "2013");
                data.put("publisher", "otava");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                // should succeed
                assertThat(status(result)).isEqualTo(SEE_OTHER);
                assertThat(redirectLocation(result)).isEqualTo("/references");
                assertThat(flash(result).get("success")).isEqualTo("Reference has been created!");
                
                // should be listed
                result = callAction(controllers.routes.ref.Application.list());
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).contains("badbadbad");
                

    }
    
    @Test
    public void downloadBibtex() {

                Result result = callAction(controllers.routes.ref.Application.generateBib());

                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).contains("author = {\\aa\\\"{o}\\\"{a}\\{\\\"\\$},");

    }
    
}
