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
	
	AWK("Awk", "*.awk"),
	BASH("Bash", "*.sh"),
	BRAINFUCK("Brainfuck", "*.bf"),
	C("C", "*.c"),
	CPP("C++", "*.cpp"),
	CSHARP("C#", "*.cs"),
	CEYLON("Ceylon", "*.ceylon"),
	CLOJURE("Clojure", "*.clj"),
	COLISP("Common Lisp", "*.lisp"),
	CRYSTAL("Crystal", "*.cr"),
	FSHARP("F#", ".*fs", "*.fsx"),
	FORTRAN("Fortran", "*.f08", "*.f", "*.for", "*.f90", "*.f95", "*.F", "*.F90", "*.F95"),
	D("D", "*.d"),
	GO("Go", "*.go"),
	HASKELL("Haskell", "*.hs", "*.lhs"),
	JAVA("Java", "*.java"),
	JAVASCRIPT("JavaScript", "*.js"),
	JULIA("Julia", "*.jl"),
	KOTLIN("Kotlin", "*.kt"),
	LUA("Lua", "*.lua"),
	LUAJIT("LuaJIT", "*.lua"),
	MOONSCRIPT("MoonScript", "*.moon"),
	NIM("Nim", "*.nim"),
	OBJECTIVE_C("Objective-C", "*.m"),
	OCAML("OCaml", "*.ml"),
	OCTAVE("Octave", "*.m"),
	PASCAL("Pascal", "*.pas"),
	PERL("Perl", "*.pl"),
	PHP("PHP", "*.php"),
	PYTHON("Python", "*.py"),
	PYPY("PyPy", "*.py"),
	RUBY("Ruby", "*.rb"),
	RUST("Rust", "*.rs"),
	SCALA("Scala", "*.scala"),
	SCHEME("Scheme", "*.scm"),
	SED("Sed", "*.sed"),
	STDML("Standard ML", "*.ml"),
	SWIFT("Swift", "*.swift"),
	TEXT("Text", "*.txt"),
	TYPESCRIPT("TypeScript", "*.ts"),
	UNLAMBDA("Unlambda", "*.unl"),
	VB("Visual Basic", "*.vb");
	
	private ExtensionFilter filter;
	
	private Extention(String name, String... extention) {
		filter = new ExtensionFilter("name", extention);
	}
	
	public static List<ExtensionFilter> getExtensionList() {
		return Arrays.stream(Extention.values()).map(e->e.filter).collect(Collectors.toList());
	}
	
	public static ExtensionFilter toExtensionFilter(String name) {
		return Arrays.stream(Extention.values()).filter(ex->ex.filter.getDescription().equalsIgnoreCase(name.replaceFirst("\\d{0,2} \\(.+\\)", "")))
				.findFirst().orElse(Extention.TEXT).filter;
	}
}
