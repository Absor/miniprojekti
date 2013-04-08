package models;

public class Bibtex {

	private String bibtexString;

	public void normalPrint(String type, String text) {
		bibtexString += "   " + type + " = {" + text + "},\n";
	}

	public String generate(String reference, Long id, String type[], String value[]) {
		String text;
		bibtexString = "@" + reference + "{" + id + ",\n";
		for (int i = 0; i < type.length; i++) {
			if (value[i] != null && !value[i].isEmpty()) {
				text = parseSpecial(value[i]);
				normalPrint(type[i], text);
			}
		}
		// end , is valid bibtex, no need to remove
		bibtexString += "}";
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
