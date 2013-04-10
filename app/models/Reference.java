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
	public String author;

	@Required
	public String title;

	@Required
	public String year;

	@Required
	public String publisher;

	public static Finder<Long, Reference> find = new Finder<Long, Reference>(Long.class, Reference.class);

	/*
	 * BibTeX-format string.
	 */
	@Override
	public String toString() {
		Bibtex bibtex = new Bibtex();
		String type[] = { "author", "title", "year", "publisher" };
		String value[] = { author, title, year, publisher };
		return bibtex.generate(referenceType.name, id, type, value);
	}
}
