
//
// This is the grammar specification from the Final Draft of the generic spec.
// It has been modified by Philippe Charles and Vijay Saraswat for use with 
// X10. 
// (1) Removed TypeParameters from class/interface/method declarations
// (2) Removed TypeParameters from types.
// (3) Removed Annotations -- cause conflicts with @ used in places.
// (4) Removed EnumDeclarations.
// 12/28/2004// 12/25/2004
// This is the basic X10 grammar specification without support for generic types.
//Intended for the Feb 2005 X10 release.
package x10.parser;

interface X10Parsersym {
    public final static int
      TK_IntegerLiteral = 44,
      TK_LongLiteral = 45,
      TK_FloatingPointLiteral = 46,
      TK_DoubleLiteral = 47,
      TK_CharacterLiteral = 48,
      TK_StringLiteral = 49,
      TK_MINUS_MINUS = 37,
      TK_OR = 94,
      TK_MINUS = 59,
      TK_MINUS_EQUAL = 108,
      TK_NOT = 65,
      TK_NOT_EQUAL = 95,
      TK_REMAINDER = 90,
      TK_REMAINDER_EQUAL = 109,
      TK_AND = 96,
      TK_AND_AND = 97,
      TK_AND_EQUAL = 110,
      TK_LPAREN = 1,
      TK_RPAREN = 5,
      TK_MULTIPLY = 86,
      TK_MULTIPLY_EQUAL = 111,
      TK_COMMA = 63,
      TK_DOT = 62,
      TK_DIVIDE = 91,
      TK_DIVIDE_EQUAL = 112,
      TK_COLON = 64,
      TK_SEMICOLON = 35,
      TK_QUESTION = 103,
      TK_AT = 88,
      TK_LBRACKET = 58,
      TK_RBRACKET = 69,
      TK_XOR = 98,
      TK_XOR_EQUAL = 113,
      TK_LBRACE = 60,
      TK_OR_OR = 104,
      TK_OR_EQUAL = 114,
      TK_RBRACE = 72,
      TK_TWIDDLE = 66,
      TK_PLUS = 61,
      TK_PLUS_PLUS = 38,
      TK_PLUS_EQUAL = 115,
      TK_LESS = 74,
      TK_LEFT_SHIFT = 87,
      TK_LEFT_SHIFT_EQUAL = 116,
      TK_LESS_EQUAL = 92,
      TK_EQUAL = 89,
      TK_EQUAL_EQUAL = 99,
      TK_GREATER = 67,
      TK_ELLIPSIS = 105,
      TK_RANGE = 129,
      TK_ARROW = 100,
      TK_abstract = 50,
      TK_assert = 76,
      TK_boolean = 7,
      TK_break = 77,
      TK_byte = 8,
      TK_case = 106,
      TK_catch = 120,
      TK_char = 9,
      TK_class = 68,
      TK_const = 130,
      TK_continue = 78,
      TK_default = 107,
      TK_do = 79,
      TK_double = 10,
      TK_enum = 131,
      TK_else = 117,
      TK_extends = 121,
      TK_false = 51,
      TK_final = 56,
      TK_finally = 122,
      TK_float = 11,
      TK_for = 80,
      TK_goto = 132,
      TK_if = 81,
      TK_implements = 127,
      TK_import = 123,
      TK_instanceof = 93,
      TK_int = 12,
      TK_interface = 70,
      TK_long = 13,
      TK_native = 124,
      TK_new = 39,
      TK_null = 52,
      TK_package = 128,
      TK_private = 53,
      TK_protected = 54,
      TK_public = 40,
      TK_return = 82,
      TK_short = 14,
      TK_static = 41,
      TK_strictfp = 57,
      TK_super = 42,
      TK_switch = 83,
      TK_synchronized = 73,
      TK_this = 43,
      TK_throw = 84,
      TK_throws = 125,
      TK_transient = 101,
      TK_true = 55,
      TK_try = 85,
      TK_void = 36,
      TK_volatile = 102,
      TK_while = 75,
      TK_activity = 28,
      TK_async = 15,
      TK_ateach = 16,
      TK_atomic = 6,
      TK_await = 17,
      TK_boxed = 33,
      TK_clocked = 18,
      TK_current = 29,
      TK_extern = 126,
      TK_finish = 19,
      TK_foreach = 20,
      TK_fun = 34,
      TK_future = 21,
      TK_here = 22,
      TK_local = 30,
      TK_method = 31,
      TK_next = 23,
      TK_now = 24,
      TK_nullable = 25,
      TK_or = 4,
      TK_place = 32,
      TK_reference = 3,
      TK_unsafe = 118,
      TK_value = 2,
      TK_when = 26,
      TK_IDENTIFIER = 27,
      TK_Comment = 71,
      TK_GREATER_EQUAL = 133,
      TK_RIGHT_SHIFT = 134,
      TK_UNSIGNED_RIGHT_SHIFT = 135,
      TK_RIGHT_SHIFT_EQUAL = 136,
      TK_UNSIGNED_RIGHT_SHIFT_EQUAL = 137,
      TK_EOF_TOKEN = 119,
      TK_ActualTypeArgument = 138,
      TK_ConcreteDistribution = 139,
      TK_$error = 140;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "LPAREN",
                 "value",
                 "reference",
                 "or",
                 "RPAREN",
                 "atomic",
                 "boolean",
                 "byte",
                 "char",
                 "double",
                 "float",
                 "int",
                 "long",
                 "short",
                 "async",
                 "ateach",
                 "await",
                 "clocked",
                 "finish",
                 "foreach",
                 "future",
                 "here",
                 "next",
                 "now",
                 "nullable",
                 "when",
                 "IDENTIFIER",
                 "activity",
                 "current",
                 "local",
                 "method",
                 "place",
                 "boxed",
                 "fun",
                 "SEMICOLON",
                 "void",
                 "MINUS_MINUS",
                 "PLUS_PLUS",
                 "new",
                 "public",
                 "static",
                 "super",
                 "this",
                 "IntegerLiteral",
                 "LongLiteral",
                 "FloatingPointLiteral",
                 "DoubleLiteral",
                 "CharacterLiteral",
                 "StringLiteral",
                 "abstract",
                 "false",
                 "null",
                 "private",
                 "protected",
                 "true",
                 "final",
                 "strictfp",
                 "LBRACKET",
                 "MINUS",
                 "LBRACE",
                 "PLUS",
                 "DOT",
                 "COMMA",
                 "COLON",
                 "NOT",
                 "TWIDDLE",
                 "GREATER",
                 "class",
                 "RBRACKET",
                 "interface",
                 "Comment",
                 "RBRACE",
                 "synchronized",
                 "LESS",
                 "while",
                 "assert",
                 "break",
                 "continue",
                 "do",
                 "for",
                 "if",
                 "return",
                 "switch",
                 "throw",
                 "try",
                 "MULTIPLY",
                 "LEFT_SHIFT",
                 "AT",
                 "EQUAL",
                 "REMAINDER",
                 "DIVIDE",
                 "LESS_EQUAL",
                 "instanceof",
                 "OR",
                 "NOT_EQUAL",
                 "AND",
                 "AND_AND",
                 "XOR",
                 "EQUAL_EQUAL",
                 "ARROW",
                 "transient",
                 "volatile",
                 "QUESTION",
                 "OR_OR",
                 "ELLIPSIS",
                 "case",
                 "default",
                 "MINUS_EQUAL",
                 "REMAINDER_EQUAL",
                 "AND_EQUAL",
                 "MULTIPLY_EQUAL",
                 "DIVIDE_EQUAL",
                 "XOR_EQUAL",
                 "OR_EQUAL",
                 "PLUS_EQUAL",
                 "LEFT_SHIFT_EQUAL",
                 "else",
                 "unsafe",
                 "EOF_TOKEN",
                 "catch",
                 "extends",
                 "finally",
                 "import",
                 "native",
                 "throws",
                 "extern",
                 "implements",
                 "package",
                 "RANGE",
                 "const",
                 "enum",
                 "goto",
                 "GREATER_EQUAL",
                 "RIGHT_SHIFT",
                 "UNSIGNED_RIGHT_SHIFT",
                 "RIGHT_SHIFT_EQUAL",
                 "UNSIGNED_RIGHT_SHIFT_EQUAL",
                 "ActualTypeArgument",
                 "ConcreteDistribution",
                 "$error"
             };

    public final static boolean isValidForParser = true;
}
