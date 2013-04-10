package models;

import java.util.Map;

public class Bibtex {

	public static String formatFieldRow(String type, String text) {
		return "   " + type + " = {" + text + "},\n";
	}

	public static String generate(String reference, Long id, Map<String, String> values) {
		String bibtexString = "@" + reference + "{" + id + ",\n";
		for (String key : values.keySet()) {
			String value = values.get(key);
			if (value != null && !value.isEmpty()) {
				bibtexString += formatFieldRow(key, parseSpecial(value));
			}
		}
		// end , is valid bibtex, no need to remove
		bibtexString += "}";
		return bibtexString;
	}

	// Formats special symbols. http://www.bibtex.org/SpecialSymbols/
	private static String parseSpecial(String parseThis) {
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
