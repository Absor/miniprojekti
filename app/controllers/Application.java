package controllers;

import java.util.List;

import models.Bibtex;
import models.FieldType;
import models.Reference;
import models.ReferenceType;
import play.data.*;
import play.mvc.*;

import views.html.*;

import static play.data.Form.*;

public class Application extends Controller {

	public static Result GO_HOME = redirect(routes.Application.listReferences(
			"id", "asc", "id", ""));

	public static Result index() {
		return GO_HOME;
	}

	/*
	 * Displays the full list of references.
	 */
	public static Result listReferences(String sortByField, String order,
			String searchField, String searchString) {

		if (!FieldType.fieldNameIsValid(sortByField)) {
			sortByField = "id";
		}
		if (!FieldType.fieldNameIsValid(searchField)) {
			searchField = "id";
			searchString = "";
		}
		List<Reference> references = Reference.findSortedAndOrdered(
				sortByField, order, searchField, searchString);
		return ok(list.render(references, FieldType.find.all(), sortByField,
				order, searchField, searchString));
	}

	/*
	 * Displays the reference type chooser.
	 */
	public static Result referenceTypeEditFormChooser() {
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

		// non existing type or data returns to main page
		if (type == null) {
			return GO_HOME;
		}

		if (referenceForm.hasErrors()) {
			return badRequest(createForm.render(referenceForm, type));
		}

		Reference reference = referenceForm.get();
		reference.save();
		flash("success", "Reference has been created!");
		return GO_HOME;
	}

	/*
	 * Fills forms with existing data for editing purposes
	 */
	public static Result editForm(Long id) {
		Reference reference = Reference.find.fetch("referenceType").where()
				.idEq(id).findUnique();
		Form<Reference> referenceForm = form(Reference.class).fill(reference);
		return ok(editForm.render(id, referenceForm, reference.referenceType));
	}

	/*
	 * updates database with new changes
	 */
	public static Result update(Long id) {
		Form<Reference> referenceForm = form(Reference.class).bindFromRequest();
		Reference reference = Reference.find.fetch("referenceType").where()
				.idEq(id).findUnique();
		
		// non existing type returns to main page
		if (reference == null || reference.referenceType == null) {
			return GO_HOME;
		}
		
		if (referenceForm.hasErrors()) {
			return badRequest(editForm.render(id, referenceForm, reference.referenceType));
		}

		reference = referenceForm.get();
		reference.update(id);
		flash("success", "Reference has been updated!");
		return GO_HOME;
	}

	/*
	 * Handles reference deletion.
	 */
	public static Result delete(Long id) {
		Reference.find.ref(id).delete();
		flash("success", "Reference has been deleted!");
		return GO_HOME;
	}

	/*
	 * Handles bibtex-file generation and serving.
	 */
	public static Result generateBib(String searchField, String searchString) {
		return ok(Bibtex.referencesAsBibtex(searchField, searchString));
	}
	
	public static Result generateBib() {
		return ok(Bibtex.referencesAsBibtex());
	}

}
