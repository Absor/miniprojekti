package controllers;

import models.Reference;
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
        return ok(
            list.render(Reference.find.all())
        );
    }
	
	/*
     * Displays the "new reference form".
     */
    public static Result create() {
        Form<Reference> referenceForm = form(Reference.class);
        return ok(
            createForm.render(referenceForm)
        );
    }
    
    /*
     * Handles the "new reference form" submission.
     */
    public static Result save() {
        Form<Reference> referenceForm = form(Reference.class).bindFromRequest();
        // validation
        if(referenceForm.hasErrors()) {
            return badRequest(createForm.render(referenceForm));
        }
        // save and return to main
        referenceForm.get().save();
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

}