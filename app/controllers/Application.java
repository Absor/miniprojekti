package controllers;

import java.util.List;
import java.util.Map;

import models.FieldType;
import models.Reference;
import models.ReferenceType;
import play.*;
import play.data.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	/*
	 * Redirects to list of references.
	 */
	public static Result GO_HOME = redirect(routes.Application.list());

	public static Result index() {
		return GO_HOME;
	}

	/*
	 * Displays the full list of references.
	 */
	public static Result list() {
		// Not only find.all() because we need the reference types joined.
		return ok(list.render(Reference.find.fetch("referenceType").findList(),
				FieldType.find.all()));
	}

	/*
	 * Displays the reference type chooser.
	 */
	public static Result choose() {
		return ok(createFormChooser.render(ReferenceType.find.all()));
	}

	/*
	 * Displays the new reference form.
	 */
	public static Result create(Long typeId) {
		ReferenceType type = ReferenceType.find.byId(typeId);
		Form<Reference> referenceForm = form(Reference.class);
		return ok(createForm.render(referenceForm, type));
	}

	/*
	 * Handles the new reference form submission.
	 */
	public static Result save(Long typeId) {
		Form<Reference> referenceForm = form(Reference.class).bindFromRequest();
		ReferenceType type = ReferenceType.find.byId(typeId);
		// bad type returns to main page
		if (type == null) {
			return GO_HOME;
		}

		// validation
		if (referenceForm.hasErrors()) {
			return badRequest(createForm.render(referenceForm, type));
		}
		// check required fields
		Reference reference = referenceForm.get();
		reference.referenceType = type;
		List<String> requiredResults = reference.checkRequired();
		if (!requiredResults.isEmpty()) {
			for (String field : requiredResults) {
				referenceForm.reject(field, "Fill all required fields.");
			}
			return badRequest(createForm.render(referenceForm, type));
		}

		// save and return to main
		reference.save();
		flash("success", "Reference has been created!");
		return GO_HOME;
	}

	/*
	 * Handles reference deletion.
	 */
	public static Result delete(Long id) {
		Reference.find.ref(id).delete();
		flash("success", "Reference has been deleted");
		return GO_HOME;
	}

	/*
	 * Handles bibtex-file generation and serving.
	 */
	public static Result generateBib() {
		String bibtexedReferences = "";
		List<Reference> references = Reference.find.all();
		for (Reference reference : references) {
			bibtexedReferences += reference.toString() + "\n\n";
		}
		return ok(bibtexedReferences);
	}

}