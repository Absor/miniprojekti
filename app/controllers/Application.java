package controllers;

import java.util.ArrayList;
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
	public static Result GO_HOME = redirect(routes.Application.list("id", "asc", "id", ""));

	public static Result index() {
		return GO_HOME;
	}

	/*
	 * Displays the full list of references.
	 */
	public static Result list(String sortBy, String order, String searchField, String searchFilter) {
		// Not only find.all() because we need the reference types joined.
		
		// TODO
		// check searchfield and sortBy for being acceptable
		List<FieldType> fields = FieldType.find.all();
		List<String> fieldNames = new ArrayList<String>();
		for (FieldType field : fields) {
			fieldNames.add(field.fieldName);
		}
		if (!fieldNames.contains(sortBy)) {
			searchField = "id";
		}
		if (!fieldNames.contains(searchField)) {
			searchField = "id";
		}
		
		return ok(list.render(Reference.find.fetch("referenceType").where().ilike(searchField, "%" + searchFilter + "%").orderBy(sortBy + " " + order).findList(), fields, sortBy, order, searchField, searchFilter));
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
		if (!Reference.isReferenceIdUnique(referenceForm.data().get("referenceId"))) {
			flash("failure", "Reference ID has to be unique!");
			return badRequest(createForm.render(referenceForm, type));
		}
		if (referenceForm.hasErrors()) {
			return badRequest(createForm.render(referenceForm, type));
		}

		// check required fields
		Reference reference = referenceForm.get();
		reference.referenceType = type;
		List<String> requiredResults = reference.checkRequired();
		if (!requiredResults.isEmpty()) {
			for (String field : requiredResults) {
				referenceForm.reject(field, "required");
			}
			return badRequest(createForm.render(referenceForm, type));
		}

		// save and return to main
		reference.save();
		reference.generateReferenceId();
		flash("success", "Reference has been created!");
		return GO_HOME;
	}

	/*
	 * Fills forms with existing data for editing purposes
	 */

	public static Result edit(Long id) {
		Reference reference = Reference.find.fetch("referenceType").where().idEq(id).findUnique();
		Form<Reference> referenceForm = form(Reference.class).fill(reference);
		return ok(editForm.render(id, referenceForm, reference.referenceType));
	}

	/*
	 * updates database with new changes
	 */
	public static Result update(Long id) {
		Form<Reference> referenceForm = form(Reference.class).bindFromRequest();
		Reference reference = Reference.find.fetch("referenceType").where().idEq(id).findUnique();
		ReferenceType type = reference.referenceType;
		
		if (!Reference.isUpdatedReferenceIdUnique(id, referenceForm.data().get("referenceId"))) {
			flash("failure", "Reference ID has to be unique!");
			return badRequest(editForm.render(id, referenceForm, type));
		}
		if (referenceForm.hasErrors()) {
			return badRequest(editForm.render(id, referenceForm, type));
		}
		
		reference = referenceForm.get();
		reference.referenceType = type;
		List<String> requiredResults = reference.checkRequired();
		if (!requiredResults.isEmpty()) {
			for (String field : requiredResults) {
				referenceForm.reject(field, "required");
			}
			return badRequest(editForm.render(id, referenceForm, type));
		}
		
		reference.update(id);
		flash("success", "Reference has been updated");
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