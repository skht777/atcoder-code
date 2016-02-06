/**
 * 
 */
package com.skht777.atcoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 * @author skht777
 *
 */
public enum Extention {
	
	BASH("Bash", "*.sh"),
	C("C", "*.c"),
	CPP("C++", "*.cpp"),
	CSHARP("C#", "*.cs"),
	CLOJURE("Clojure", "*.clj"),
	COLISP("Common Lisp", "*.lisp"),
	D("D", "*.d"),
	GO("Go", "*.go"),
	HASKELL("Haskell", "*.hs", "*.lhs"),
	JAVA("Java", "*.java"),
	JAVASCRIPT("JavaScript", "*.js"),
	OCAML("OCaml", "*.ml"),
	PASCAL("Pascal", "*.pas"),
	PERL("Perl", "*.pl"),
	PHP("PHP", "*.php"),
	PYTHON("Python", "*.py"),
	RUBY("Ruby", "*.rb"),
	SCALA("Scala", "*.scala"),
	SCHEME("Scheme", "*.scm"),
	TEXT("Text", "*.txt");
	
	private String name;
	private String[] extention;
	
	private Extention(String name, String... extention) {
		this.name = name;
		this.extention = extention;
	}
	
	public static List<ExtensionFilter> getExtensionList() {
		return Arrays.stream(Extention.values()).map(e->new ExtensionFilter(e.name, e.extention)).collect(Collectors.toList());
	}
	
	public static ExtensionFilter toExtensionFilter(String name) {
		Extention e = Arrays.stream(Extention.values())
				.filter(ex->ex.name.equals(name.replaceFirst("\\d{0,2} \\(.+\\)", ""))).findFirst().orElse(Extention.TEXT);
		return new ExtensionFilter(e.name, e.extention);
	}
}
