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

	/*
	 * BibTeX-format string.
	 */
	@Override
	public String toString() {
		String bibtexString = "@" + referenceType + "{" + id + ",\n";
		if (author != null && !author.isEmpty()) {
			String parsedAuthor = parseSpecial(author);
			bibtexString += "   author = {" + parsedAuthor + "},\n";
		}
		if (title != null && !title.isEmpty()) {
			String parsedTitle = parseSpecial(title);
			bibtexString += "   title = {" + parsedTitle + "},\n";
		}
		if (year != null && !year.isEmpty()) {
			bibtexString += "   year = {" + year + "},\n";
		}
		if (publisher != null && !publisher.isEmpty()) {
			String parsedPublisher = parseSpecial(publisher);
			bibtexString += "   publisher = {" + parsedPublisher + "},\n";
		}
		// remove end , if any
		bibtexString = bibtexString.substring(0, bibtexString.length() - 2);
		bibtexString += "\n}";
		
		return bibtexString;
	}
	
	// Formats special symbols. http://www.bibtex.org/SpecialSymbols/
	private String parseSpecial(String parseThis) {
		String parsed = parseThis.replace("{", "\\{");
		parsed = parsed.replace("\"", "\\\"");
		parsed = parsed.replace("$", "\\$");
		parsed = parsed.replace("ö", "\\\"{o}");
		parsed = parsed.replace("Ö", "\\\"{O}");
		parsed = parsed.replace("å", "\\aa");
		parsed = parsed.replace("Å", "\\AA");
		parsed = parsed.replace("ä", "\\\"{a}");
		parsed = parsed.replace("Ä", "\\\"{A}");
		// At the moment: { " $ ö Ö å Å ä Ä
		return parsed;
	}
}
