package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.data.validation.*;

@Entity
public class Reference extends Model {

	@Id
	public Long id;

	@Constraints.Required
	public String referenceType;
	
	public String author;
	
	public String title;
	
	public String year;
	
	public String publisher;

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(
			Long.class, Reference.class);
}
