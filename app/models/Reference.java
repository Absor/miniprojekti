package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.data.validation.*;
import play.data.validation.Constraints.*;


/**
 * Model for references. A reference has various datafields and a reference type that knows which fields are
 * required and which optional.
 *
 */
@Entity
public class Reference extends Model {
	
	@Id
	public Long id;

	@Required
	@ManyToOne
	public ReferenceType referenceType;

	@Required
	@MinLength(2)
	public String author;

	@Required
	@MinLength(1)
	public String title;

	@Required
	@Min(0)
	@Max(2050)
	public int year;

	@Required
	@MinLength(2)
	public String publisher;

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(Long.class, Reference.class);

	/*
	 * BibTeX-format string.
	 */
	@Override
	public String toString() {
		Bibtex bibtex = new Bibtex();
		String years = Integer.toString(year);
		String type[] = { "author", "title", "year", "publisher" };
		String value[] = { author, title, years, publisher };
		return bibtex.generate(referenceType.name, id, type, value);
	}
}
