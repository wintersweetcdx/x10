


package x10.parser;


import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;

import com.ibm.lpg.*;

public class X10Lexer extends LpgLexStream implements RuleAction, X10Parsersym, X10Lexersym
{
    private PrsStream prsStream;
    private ParseTable prs = new X10Lexerprs();
    private LexParser lexParser = new LexParser(this, prs, this);

    public X10Lexer(String filename, int tab) throws java.io.IOException 
    {
        super(filename, tab);
    }

    public X10Lexer(char[] input_chars, String filename, int tab)
    {
        super(input_chars, filename, tab);
    }

    public X10Lexer(char[] input_chars, String filename)
    {
        this(input_chars, filename, 1);
    }

    public String[] orderedExportedSymbols() { return X10Parsersym.orderedTerminalSymbols; }

    public void lexer(PrsStream prsStream)
    {
        this.prsStream = prsStream;

        prsStream.makeToken(0, 0, 0); // Token list must start with a bad token
            
        lexParser.parseCharacters();  // Lex the input characters
            
        int i = getStreamIndex();
        prsStream.makeToken(i, i, TK_EOF_TOKEN); // and end with the end of file token
        prsStream.setSize();
            
        return;
    }
    //
    // The Lexer contains an array of characters as the input stream to be parsed.
    // There are methods to retrieve and classify characters.
    // The lexparser "token" is implemented simply as the index of the next character in the array.
    // The Lexer extends the abstract class LpgLexStream with an implementation of the abstract
    // method getKind.  The template defines the Lexer class and the lexer() method.
    // A driver creates the action class, "Lexer", passing an Option object to the constructor.
    // The Option object gives access to the input character arrary, the file name and other options.
    //
    Option option;
    X10KWLexer kwLexer;
    boolean printTokens;
    private final static int ECLIPSE_TAB_VALUE = 4;

    public X10Lexer(String filename) throws java.io.IOException
    {
        this(filename, ECLIPSE_TAB_VALUE);
        kwLexer = new X10KWLexer(getInputChars(), TK_IDENTIFIER);
    }
    
    public X10Lexer(Option option)
    {
        this(option.getInputChars(), option.getFileName(), ECLIPSE_TAB_VALUE);
        this.option = option;
        printTokens = option.printTokens();
        kwLexer = new X10KWLexer(getInputChars(), TK_IDENTIFIER);
    }

    final void makeDocComment()
    {
        int startOffset = lexParser.getToken(1),
            endOffset = lexParser.getLastToken();
        skipToken();
    }

    final void makeToken(int kind)
    {
        int startOffset = lexParser.getToken(1),
            endOffset = lexParser.getLastToken();
        prsStream.makeToken(startOffset, endOffset, kind);
        if (printTokens) printValue(startOffset, endOffset);
    }

    final void skipToken()
    {
        if (printTokens) printValue(lexParser.getToken(1), lexParser.getLastToken());
    }
    
    final void checkForKeyWord()
    {
        int startOffset = lexParser.getToken(1),
            endOffset = lexParser.getLastToken(),
        kwKind = kwLexer.lexer(startOffset, endOffset);
        prsStream.makeToken(startOffset, endOffset, kwKind);
        if(printTokens) printValue(startOffset, endOffset);
    }
    
    final void printValue(int startOffset, int endOffset)
    {
        String s = new String(getInputChars(), startOffset, endOffset - startOffset + 1);
        System.out.print(s);
    }

    //
    //
    //
    private static boolean differ_tokens = false;
    private static int changeCount = 0,
                       insertCount = 0,
                       deleteCount = 0,
                       replaceCount = 0,
                       moveCount = 0;

    private static void compareFiles(String old_file, String new_file)
    {
        try
        {
            X10Lexer old_lexer = new X10Lexer(old_file),
                                      new_lexer = new X10Lexer(new_file);

            PrsStream old_stream = new PrsStream(old_lexer);
            old_lexer.lexer(old_stream);

            PrsStream new_stream = new PrsStream(new_lexer);
            new_lexer.lexer(new_stream);

            Differ diff = (differ_tokens ? (Differ) new DifferTokens(old_stream, new_stream)
                                         : (Differ) new DifferLines(old_stream, new_stream));
            diff.compare();

            if (diff.getChangeCount() > 0)
            {
                diff.outputChanges();

                changeCount += diff.getChangeCount();
                insertCount += diff.getInsertCount();
                deleteCount += diff.getDeleteCount();
                replaceCount += diff.getReplaceCount();
                moveCount += diff.getMoveCount();
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }


    private static void compareDirectories(File old_dir, File new_dir)
    {
        try
        {
            File old_file[] = old_dir.listFiles(),
                 new_file[] = new_dir.listFiles();
            HashMap old_map = new HashMap();
            for (int i = 0; i < old_file.length; i++)
                old_map.put(old_file[i].getName(), old_file[i]);

            for (int i = 0; i < new_file.length; i++)
            {
                File file = (File) old_map.get(new_file[i].getName());
                if (file != null)
                {
                    old_map.remove(new_file[i].getName());

                    if (file.isDirectory() && new_file[i].isDirectory())
                         compareDirectories(file, new_file[i]);
                    else compareFiles(file.getPath(), new_file[i].getPath());
                }
                else ; /* TODO: file is a new file */ 
            }

            for (Iterator i = old_map.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry e = (Map.Entry) i.next();
                File file = (File) e.getValue();
                // TODO: file was deleted
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        String new_file = "new file",
               old_file = "old file";

        int i;
        for (i = 0; i < args.length; i++)
        {
            if (args[i].charAt(0) == '-')
            {
                if (args[i].equals("-t"))
                    differ_tokens = true;
                else if (args[i].equals("-l"))
                    differ_tokens = false;
            }
            else break;
        }
        if (i < args.length) 
            new_file = args[i++];
        if (i < args.length) 
            old_file = args[i++];
        for (; i < args.length; i++)
            System.err.println("Invalid argument: " + args[i]);

        File old_dir = new File(old_file),
             new_dir = new File(new_file);
        if (old_dir.isDirectory() && new_dir.isDirectory())
             compareDirectories(old_dir, new_dir);
        else compareFiles(old_file, new_file);

        if (changeCount == 0)
            System.out.println("***** No difference *****");
        else
        {
            System.out.println("***** " +
                               changeCount +
                               " different " +
                               (changeCount == 1 ? "section" : "sections") + " *****");
            System.out.println("    " + insertCount  + (differ_tokens ? " tokens" : " lines") + " inserted");
            System.out.println("    " + deleteCount  + (differ_tokens ? " tokens" : " lines") + " deleted");
            System.out.println("    " + replaceCount + (differ_tokens ? " tokens" : " lines") + " replaced");
            System.out.println("    " + moveCount    + (differ_tokens ? " tokens" : " lines") + " moved");
        }

        return;
    }

    //
    //
    //
    public final static int tokenKind[] =
    {
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_HT,
        Char_LF,
        Char_CtlCharNotWS,
        Char_FF,
        Char_CR,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_CtlCharNotWS,
        Char_Space,
        Char_Exclamation,
        Char_DoubleQuote,
        Char_Sharp,
        Char_DollarSign,
        Char_Percent,
        Char_Ampersand,
        Char_SingleQuote,
        Char_LeftParen,
        Char_RightParen,
        Char_Star,
        Char_Plus,
        Char_Comma,
        Char_Minus,
        Char_Dot,
        Char_Slash,
        Char_0,
        Char_1,
        Char_2,
        Char_3,
        Char_4,
        Char_5,
        Char_6,
        Char_7,
        Char_8,
        Char_9,
        Char_Colon,
        Char_SemiColon,
        Char_LessThan,
        Char_Equal,
        Char_GreaterThan,
        Char_QuestionMark,
        Char_AtSign,
        Char_A,
        Char_B,
        Char_C,
        Char_D,
        Char_E,
        Char_F,
        Char_G,
        Char_H,
        Char_I,
        Char_J,
        Char_K,
        Char_L,
        Char_M,
        Char_N,
        Char_O,
        Char_P,
        Char_Q,
        Char_R,
        Char_S,
        Char_T,
        Char_U,
        Char_V,
        Char_W,
        Char_X,
        Char_Y,
        Char_Z,
        Char_LeftBracket,
        Char_BackSlash,
        Char_RightBracket,
        Char_Caret,
        Char__,
        Char_BackQuote,
        Char_a,
        Char_b,
        Char_c,
        Char_d,
        Char_e,
        Char_f,
        Char_g,
        Char_h,
        Char_i,
        Char_j,
        Char_k,
        Char_l,
        Char_m,
        Char_n,
        Char_o,
        Char_p,
        Char_q,
        Char_r,
        Char_s,
        Char_t,
        Char_u,
        Char_v,
        Char_w,
        Char_x,
        Char_y,
        Char_z,
        Char_LeftBrace,
        Char_VerticalBar,
        Char_RightBrace,
        Char_Tilde,
        Char_AfterASCII, // for all chars in range 128..65534
        Char_EOF         // for '\uffff' or 65535 
    };
            
    public final int getKind(int i)  // Classify character at ith location
    {
        char c = (i >= getStreamLength() ? '\uffff' : getCharValue(i));
        return (c < 128 // ASCII Character
                  ? tokenKind[c]
                  : c == '\uffff'
                       ? Char_EOF
                       : Char_AfterASCII);
    }

    public void ruleAction( int ruleNumber)
    {
        switch(ruleNumber)
        {
 
            //
            // Rule 1:  Token ::= Identifier
            //
            case 1: { 
                checkForKeyWord();
                break;
            }
     
            //
            // Rule 2:  Token ::= " SLBody "
            //
            case 2: { 
                makeToken(TK_StringLiteral);
                break;
            }
     
            //
            // Rule 3:  Token ::= ' NotSQ '
            //
            case 3: { 
                makeToken(TK_CharacterLiteral);
                break;
            }
     
            //
            // Rule 4:  Token ::= IntegerLiteral
            //
            case 4: { 
                makeToken(TK_IntegerLiteral);
                break;
            }
     
            //
            // Rule 5:  Token ::= LongLiteral
            //
            case 5: { 
                makeToken(TK_LongLiteral);
                break;
            }
     
            //
            // Rule 6:  Token ::= FloatingPointLiteral
            //
            case 6: { 
                makeToken(TK_FloatingPointLiteral);
                break;
            }
     
            //
            // Rule 7:  Token ::= DoubleLiteral
            //
            case 7: { 
                makeToken(TK_DoubleLiteral);
                break;
            }
     
            //
            // Rule 8:  Token ::= / * Inside Stars /
            //
            case 8: { 
                if (getKind(lexParser.getFirstToken(3)) == Char_Star && getKind(getNext(lexParser.getFirstToken(3))) != Char_Star)
                    makeDocComment();
                break;
            }
     
            //
            // Rule 9:  Token ::= SLC
            //
            case 9: { 
                skipToken();
                break;
            }
     
            //
            // Rule 10:  Token ::= WS
            //
            case 10: { 
                skipToken();
                break;
            }
     
            //
            // Rule 11:  Token ::= +
            //
            case 11: { 
                makeToken(TK_PLUS);
                break;
            }
     
            //
            // Rule 12:  Token ::= -
            //
            case 12: { 
                makeToken(TK_MINUS);
                break;
            }
     
            //
            // Rule 13:  Token ::= *
            //
            case 13: { 
                makeToken(TK_MULTIPLY);
                break;
            }
     
            //
            // Rule 14:  Token ::= /
            //
            case 14: { 
                makeToken(TK_DIVIDE);
                break;
            }
     
            //
            // Rule 15:  Token ::= (
            //
            case 15: { 
                makeToken(TK_LPAREN);
                break;
            }
     
            //
            // Rule 16:  Token ::= )
            //
            case 16: { 
                makeToken(TK_RPAREN);
                break;
            }
     
            //
            // Rule 17:  Token ::= =
            //
            case 17: { 
                makeToken(TK_EQUAL);
                break;
            }
     
            //
            // Rule 18:  Token ::= ,
            //
            case 18: { 
                makeToken(TK_COMMA);
                break;
            }
     
            //
            // Rule 19:  Token ::= :
            //
            case 19: { 
                makeToken(TK_COLON);
                break;
            }
     
            //
            // Rule 20:  Token ::= ;
            //
            case 20: { 
                makeToken(TK_SEMICOLON);
                break;
            }
     
            //
            // Rule 21:  Token ::= ^
            //
            case 21: { 
                makeToken(TK_XOR);
                break;
            }
     
            //
            // Rule 22:  Token ::= %
            //
            case 22: { 
                makeToken(TK_REMAINDER);
                break;
            }
     
            //
            // Rule 23:  Token ::= ~
            //
            case 23: { 
                makeToken(TK_TWIDDLE);
                break;
            }
     
            //
            // Rule 24:  Token ::= |
            //
            case 24: { 
                makeToken(TK_OR);
                break;
            }
     
            //
            // Rule 25:  Token ::= &
            //
            case 25: { 
                makeToken(TK_AND);
                break;
            }
     
            //
            // Rule 26:  Token ::= <
            //
            case 26: { 
                makeToken(TK_LESS);
                break;
            }
     
            //
            // Rule 27:  Token ::= >
            //
            case 27: { 
                makeToken(TK_GREATER);
                break;
            }
     
            //
            // Rule 28:  Token ::= .
            //
            case 28: { 
                makeToken(TK_DOT);
                break;
            }
     
            //
            // Rule 29:  Token ::= !
            //
            case 29: { 
                makeToken(TK_NOT);
                break;
            }
     
            //
            // Rule 30:  Token ::= [
            //
            case 30: { 
                makeToken(TK_LBRACKET);
                break;
            }
     
            //
            // Rule 31:  Token ::= ]
            //
            case 31: { 
                makeToken(TK_RBRACKET);
                break;
            }
     
            //
            // Rule 32:  Token ::= {
            //
            case 32: { 
                makeToken(TK_LBRACE);
                break;
            }
     
            //
            // Rule 33:  Token ::= }
            //
            case 33: { 
                makeToken(TK_RBRACE);
                break;
            }
     
            //
            // Rule 34:  Token ::= ?
            //
            case 34: { 
                makeToken(TK_QUESTION);
                break;
            }
     
            //
            // Rule 35:  Token ::= @
            //
            case 35: { 
                makeToken(TK_AT);
                break;
            }
     
            //
            // Rule 36:  Token ::= + +
            //
            case 36: { 
                makeToken(TK_PLUS_PLUS);
                break;
            }
     
            //
            // Rule 37:  Token ::= - -
            //
            case 37: { 
                makeToken(TK_MINUS_MINUS);
                break;
            }
     
            //
            // Rule 38:  Token ::= = =
            //
            case 38: { 
                makeToken(TK_EQUAL_EQUAL);
                break;
            }
     
            //
            // Rule 39:  Token ::= < =
            //
            case 39: { 
                makeToken(TK_LESS_EQUAL);
                break;
            }
     
            //
            // Rule 40:  Token ::= ! =
            //
            case 40: { 
                makeToken(TK_NOT_EQUAL);
                break;
            }
     
            //
            // Rule 41:  Token ::= < <
            //
            case 41: { 
                makeToken(TK_LEFT_SHIFT);
                break;
            }
     
            //
            // Rule 42:  Token ::= + =
            //
            case 42: { 
                makeToken(TK_PLUS_EQUAL);
                break;
            }
     
            //
            // Rule 43:  Token ::= - =
            //
            case 43: { 
                makeToken(TK_MINUS_EQUAL);
                break;
            }
     
            //
            // Rule 44:  Token ::= * =
            //
            case 44: { 
                makeToken(TK_MULTIPLY_EQUAL);
                break;
            }
     
            //
            // Rule 45:  Token ::= / =
            //
            case 45: { 
                makeToken(TK_DIVIDE_EQUAL);
                break;
            }
     
            //
            // Rule 46:  Token ::= & =
            //
            case 46: { 
                makeToken(TK_AND_EQUAL);
                break;
            }
     
            //
            // Rule 47:  Token ::= | =
            //
            case 47: { 
                makeToken(TK_OR_EQUAL);
                break;
            }
     
            //
            // Rule 48:  Token ::= ^ =
            //
            case 48: { 
                makeToken(TK_XOR_EQUAL);
                break;
            }
     
            //
            // Rule 49:  Token ::= % =
            //
            case 49: { 
                makeToken(TK_REMAINDER_EQUAL);
                break;
            }
     
            //
            // Rule 50:  Token ::= < < =
            //
            case 50: { 
                makeToken(TK_LEFT_SHIFT_EQUAL);
                break;
            }
     
            //
            // Rule 51:  Token ::= | |
            //
            case 51: { 
                makeToken(TK_OR_OR);
                break;
            }
     
            //
            // Rule 52:  Token ::= & &
            //
            case 52: { 
                makeToken(TK_AND_AND);
                break;
            }
     
            //
            // Rule 53:  Token ::= . . .
            //
            case 53: { 
                makeToken(TK_ELLIPSIS);
                break;
            }
     
            //
            // Rule 350:  Token ::= . .
            //
            case 350: { 
                 makeToken(TK_RANGE);
                 break;
            }
      
            //
            // Rule 351:  Token ::= - >
            //
            case 351: { 
                makeToken(TK_ARROW);
                break;
            }
     
            //
            // Rule 352:  IntLiteralAndRange ::= Integer . .
            //
            case 352: { 
                makeToken(lexParser.getToken(1), lexParser.getLastToken(1), TK_IntegerLiteral);
                makeToken(lexParser.getToken(2), lexParser.getToken(3), TK_RANGE);
                break;
            }
        
            default:
                break;
        }
        return;
    }
}
