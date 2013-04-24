import models.FieldType;
import models.Reference;
import models.ReferenceType;

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
		Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml
				.load("test-data.yml");
		Ebean.save(all.get("references"));
	}

	@Test
	public void redirectsToReferences() {

		Result result = callAction(controllers.routes.ref.Application.index());

		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/references");

	}

	@Test
	public void frontPageShows() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("id", "asc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("References list")).isTrue();

		// buttons
		assertThat(content.contains("Add a new reference")).isTrue();
		assertThat(content.contains("Download bibtex-file")).isTrue();
	}

	@Test
	public void listsReferencesOnFrontPage() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("id", "asc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// all test data shows
		 
		//System.out.println(Ebean.find(Reference.class).findlistReferences());
		assertThat(content.contains("testtit1")).isTrue();
		assertThat(content.contains("testtit2")).isTrue();
		assertThat(content.contains("testtit3")).isTrue();
	}

	@Test
	public void chooserPageShows() {

		Result result = callAction(controllers.routes.ref.Application.referenceTypeEditFormChooser());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("Choose a reference type"));
		// buttons for all types
		List<ReferenceType> types = ReferenceType.find.all();
		for (ReferenceType type : types) {
			assertThat(content.contains(type.name)).isTrue();
		}
	}

	@Test
	public void inproceedingPageShow() {
		Result result = callAction(controllers.routes.ref.Application.create(1));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("Add a reference")).isTrue();
		// all fields
		ReferenceType rType = ReferenceType.find.byId(1l);
		for (FieldType fType : rType.optionalFields) {
			assertThat(content.contains(fType.fieldName)).isTrue();
		}
		for (FieldType fType : rType.requiredFields) {
			assertThat(content.contains(fType.fieldName)).isTrue();
		}
	}

	@Test
	public void badRequestWithoutDataOnCreate() {
		Result result = callAction(controllers.routes.ref.Application.save(1));

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void badRequestWithoutRequiredDataOnCreate() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("author", "badbadbad");
		data.put("referenceType", "1");

		Result result = callAction(controllers.routes.ref.Application.save(1),
				fakeRequest().withFormUrlEncodedBody(data));

		// should fail with no other fields
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		data.put("booktitle", "badbadbad");

		result = callAction(controllers.routes.ref.Application.save(1),
				fakeRequest().withFormUrlEncodedBody(data));

		// should fail with no other fields
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		data.put("title", "badbadbad");

		result = callAction(controllers.routes.ref.Application.save(1),
				fakeRequest().withFormUrlEncodedBody(data));

		// should fail with no other fields
		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void badrequestIdChancedToExistingId() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("referenceId", "ref1");
		data.put("referenceType", "1");
		data.put("author", "a");
		data.put("booktitle", "a");
		data.put("title", "a");
		data.put("year", "a");
		Result result = callAction(controllers.routes.ref.Application.save(1), fakeRequest().withFormUrlEncodedBody(data));
		assertThat(flash(result).get("success")).isEqualTo("Reference has been created!"); 
		
		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("referenceId", "ref2");
		data2.put("referenceType", "1");
		data2.put("author", "b");
		data2.put("booktitle", "b");
		data2.put("title", "b");
		data2.put("year", "b");
		Result result2 = callAction(controllers.routes.ref.Application.save(1), fakeRequest().withFormUrlEncodedBody(data2));
		assertThat(flash(result2).get("success")).isEqualTo("Reference has been created!");  
				
		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("referenceType", "1");
		data3.put("referenceId", "ref1");
		data3.put("author", "b");
		data3.put("booktitle", "b");
		data3.put("title", "b");
		data3.put("year", "b");
		Reference search = Reference.find.where().eq("referenceId", "ref2").findUnique();
		Result result3 = callAction(controllers.routes.ref.Application.update(search.id), fakeRequest().withFormUrlEncodedBody(data3));
		assertThat(status(result3)).isEqualTo(BAD_REQUEST);
	}
	
	@Test
	public void allFieldsWork() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("referenceType", "1");
		data.put("author", "badbadbad1");
		data.put("booktitle", "badbadbad2");
		data.put("title", "badbadbad3");
		data.put("year", "badbadbad4");
		data.put("address", "badbadbad5");
		data.put("editor", "badbadbad6");
		data.put("key", "badbadbad7");
		data.put("month", "badbadbad8");
		data.put("note", "badbadbad9");
		data.put("organization", "badbadbad10");
		data.put("pages", "badbadbad11");
		data.put("publisher", "badbadbad12");
		data.put("series", "badbadbad13");
		data.put("volume", "badbadbad14");

		Result result = callAction(controllers.routes.ref.Application.save(1),
				fakeRequest().withFormUrlEncodedBody(data));

		// should succeed
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/references");
		assertThat(flash(result).get("success")).isEqualTo(
				"Reference has been created!");

		// new addition should be listed with all added info
		result = callAction(controllers.routes.ref.Application.listReferences("id", "asc", "id", ""));
		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		for (int i = 1; i <= 14; i++) {
			assertThat(content.contains("badbadbad" + i)).isTrue();
		}
	}

	@Test
	public void createNewInproceedingsWorksWithRequiredData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("referenceType", "1");
		data.put("author", "badbadbad1");
		data.put("booktitle", "badbadbad2");
		data.put("title", "badbadbad3");
		data.put("year", "badbadbad4");

		Result result = callAction(controllers.routes.ref.Application.save(1),
				fakeRequest().withFormUrlEncodedBody(data));

		// should succeed
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/references");
		assertThat(flash(result).get("success")).isEqualTo(
				"Reference has been created!");

		// new addition should be listed
		result = callAction(controllers.routes.ref.Application.listReferences("id", "asc", "id", ""));
		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("badbadbad1")).isTrue();
		assertThat(content.contains("badbadbad2")).isTrue();
		assertThat(content.contains("badbadbad3")).isTrue();
		assertThat(content.contains("badbadbad4")).isTrue();
	}

	@Test
	public void bibtexGenParsesSpecialCharacters() {

		Result result = callAction(controllers.routes.ref.Application
				.generateBib("id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("author = {\\aa\\\"{o}\\\"{a}\\{\\\"\\$},"));

	}

	@Test
	public void bibtexGenListsAll() {

		Result result = callAction(controllers.routes.ref.Application
				.generateBib("id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("testtit1")).isTrue();
		assertThat(content.contains("testtit2")).isTrue();
		assertThat(content.contains("testtit3")).isTrue();
		assertThat(content.contains("testtit4")).isTrue();
	}
	
	@Test
	public void bibtexFiltersResults() {
		Result result = callAction(controllers.routes.ref.Application
				.generateBib("author", "testaut1"));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("testtit1")).isTrue();
		assertThat(content.contains("testtit4")).isTrue();
		assertThat(content.contains("testtit2")).isFalse();
		assertThat(content.contains("testtit3")).isFalse();
	}
	
	@Test
	public void sortsResultsByAuthorAscending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("author", "asc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut1.*testaut2.*");
	}
	
	@Test
	public void sortsResultsByAuthorDescending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("author", "desc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut2.*testaut1.*");
	}
	
	@Test
	public void sortsResultsByYearAscending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("year", "asc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut1.*testaut2.*");
	}
	
	@Test
	public void sortsResultsByYearDescending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("year", "desc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut2.*testaut1.*");
	}
	
	@Test
	public void sortsResultsByPublisherAscending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("publisher", "asc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut1.*testaut2.*");
	}
	
	@Test
	public void sortsResultsByPublisherDescending() {

		Result result = callAction(controllers.routes.ref.Application.listReferences("publisher", "desc", "id", ""));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content).matches("(?s).*testaut2.*testaut1.*");
	}

}
