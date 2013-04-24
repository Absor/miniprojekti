package models;

import java.util.List;
import java.util.Map;

public class Bibtex {

	public static String referencesAsBibtex() {
		String bibtexedReferences = "";
		List<Reference> references = Reference.findSortedAndOrdered("id",
				"asc", "id", "");
		for (Reference reference : references) {
				bibtexedReferences += oneReferenceAsBibTex(
						reference.referenceType.name, reference.getFields()) + "\n\n";
		}
		return bibtexedReferences;
	}

	private static String oneReferenceAsBibTex(String referenceType, Map<String, String> values) {
		String refId = values.remove("referenceId");
		String bibtexString = "@" + referenceType + "{" + refId + ",\n";
		for (String key : values.keySet()) {
			String value = values.get(key);
			if (value != null && !value.isEmpty()) {
				bibtexString += formatRow(key, parseSpecial(value));
			}
		}
		// end , is valid bibtex, no need to remove
		bibtexString += "}";
		return bibtexString;
	}

	private static String formatRow(String type, String text) {
		return "   " + type + " = {" + text + "},\n";
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
