import models.FieldType;
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

		Result result = callAction(controllers.routes.ref.Application.list());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("References list"));

		// buttons
		assertThat(content.contains("Add a new reference"));
		assertThat(content.contains("Download bibtex-file"));
	}

	@Test
	public void listsReferencesOnFrontPage() {

		Result result = callAction(controllers.routes.ref.Application.list());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// all test data shows
		assertThat(content.contains("testtit1"));
		assertThat(content.contains("testtit2"));
		assertThat(content.contains("testtit3"));
	}

	@Test
	public void chooserPageShows() {

		Result result = callAction(controllers.routes.ref.Application.choose());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("Choose a reference type"));
		// buttons for all types
		List<ReferenceType> types = ReferenceType.find.all();
		for (ReferenceType type : types) {
			assertThat(content.contains(type.name));
		}
	}

	@Test
	public void inproceedingPageShow() {
		Result result = callAction(controllers.routes.ref.Application.create(1));

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);

		// title
		assertThat(content.contains("Add a reference"));
		// all fields
		ReferenceType rType = ReferenceType.find.byId(1l);
		for (FieldType fType : rType.optionalFields) {
			assertThat(content.contains(fType.fieldName));
		}
		for (FieldType fType : rType.requiredFields) {
			assertThat(content.contains(fType.fieldName));
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
	public void createNewInproceedingsWorksWithRequiredData() {
		Map<String, String> data = new HashMap<String, String>();
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
		result = callAction(controllers.routes.ref.Application.list());
		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("badbadbad1"));
		assertThat(content.contains("badbadbad2"));
		assertThat(content.contains("badbadbad3"));
		assertThat(content.contains("badbadbad4"));
	}

	@Test
	public void bibtexGenParsesSpecialCharacters() {

		Result result = callAction(controllers.routes.ref.Application
				.generateBib());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains(
				"author = {\\aa\\\"{o}\\\"{a}\\{\\\"\\$},"));

	}
	
	@Test
	public void bibtexGenListsAll() {

		Result result = callAction(controllers.routes.ref.Application
				.generateBib());

		assertThat(status(result)).isEqualTo(OK);
		String content = contentAsString(result);
		assertThat(content.contains("testtit1"));
		assertThat(content.contains("testtit2"));
		assertThat(content.contains("testtit3"));
	}

}
