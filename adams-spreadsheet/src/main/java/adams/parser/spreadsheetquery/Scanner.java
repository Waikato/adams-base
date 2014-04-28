/* The following code was generated by JFlex 1.4.2 on 27/08/13 12:45 PM */

/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/*
 * Scanner.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.parser.spreadsheetquery;

import java_cup.runtime.SymbolFactory;
import java.io.*;

/**
 * A scanner for spreadsheet queries.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */

public class Scanner implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int STRING = 2;
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\37\1\36\1\0\1\37\1\45\22\0\1\37\1\0\1\43"+
    "\5\0\1\41\1\42\1\25\1\0\1\40\1\33\1\32\1\0\1\31"+
    "\11\34\2\0\1\26\1\27\1\30\2\0\1\6\1\16\1\4\1\11"+
    "\1\2\1\35\1\22\1\13\1\20\2\35\1\3\1\24\1\21\1\15"+
    "\1\10\1\35\1\14\1\1\1\5\1\7\1\35\1\12\1\23\1\17"+
    "\1\35\1\0\1\44\2\0\1\35\1\0\1\6\1\16\1\4\1\11"+
    "\1\2\1\35\1\22\1\13\1\20\2\35\1\3\1\24\1\21\1\15"+
    "\1\10\1\35\1\14\1\1\1\5\1\7\1\35\1\12\1\23\1\17"+
    "\1\35\uff85\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\14\2\1\3\1\4\1\5\1\6\1\7"+
    "\1\1\1\2\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\2\2\1\20\5\2\1\21\1\22\1\23"+
    "\2\2\1\24\1\25\1\26\1\2\1\0\1\7\1\2"+
    "\1\27\1\30\1\31\1\32\1\2\1\33\1\2\1\34"+
    "\1\35\7\2\1\36\1\2\1\7\1\0\1\37\3\2"+
    "\1\40\4\2\1\41\1\0\1\7\1\2\1\42\2\2"+
    "\1\43\1\2\1\44\1\45\1\46\1\47\1\50";

  private static int [] zzUnpackAction() {
    int [] result = new int[93];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\46\0\114\0\162\0\230\0\276\0\344\0\u010a"+
    "\0\u0130\0\u0156\0\u017c\0\u01a2\0\u01c8\0\u01ee\0\u0214\0\114"+
    "\0\u023a\0\114\0\u0260\0\u0286\0\u02ac\0\u02d2\0\114\0\114"+
    "\0\114\0\114\0\114\0\u02f8\0\114\0\u031e\0\u0344\0\u036a"+
    "\0\u0390\0\u03b6\0\u03dc\0\u0402\0\u0428\0\u044e\0\u0474\0\230"+
    "\0\230\0\u049a\0\u04c0\0\114\0\114\0\114\0\u04e6\0\u02ac"+
    "\0\u050c\0\u0532\0\114\0\114\0\114\0\114\0\u0558\0\230"+
    "\0\u057e\0\230\0\230\0\u05a4\0\u05ca\0\u05f0\0\u0616\0\u063c"+
    "\0\u0662\0\u0688\0\230\0\u06ae\0\u06d4\0\u06fa\0\u0720\0\u0746"+
    "\0\u076c\0\u0792\0\230\0\u07b8\0\u07de\0\u0804\0\u082a\0\230"+
    "\0\u0850\0\u0876\0\u089c\0\230\0\u08c2\0\u08e8\0\230\0\u090e"+
    "\0\230\0\230\0\230\0\230\0\230";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[93];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\2\5\1\7\1\10\1\5"+
    "\1\11\1\12\1\5\1\13\1\14\1\15\1\5\1\16"+
    "\1\17\3\5\1\20\1\21\1\22\1\23\1\24\1\25"+
    "\1\26\1\24\1\5\2\27\1\30\1\31\1\32\1\33"+
    "\1\3\1\27\36\34\1\0\4\34\1\35\1\36\1\3"+
    "\47\0\1\5\1\37\22\5\4\0\1\5\1\0\3\5"+
    "\11\0\24\5\4\0\1\5\1\0\3\5\11\0\17\5"+
    "\1\40\4\5\4\0\1\5\1\0\3\5\11\0\1\41"+
    "\17\5\1\42\3\5\4\0\1\5\1\0\3\5\11\0"+
    "\7\5\1\43\14\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\5\1\44\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\12\5\1\45\11\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\5\1\46\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\13\5\1\47\10\5\4\0\1\5\1\0\3\5\11\0"+
    "\16\5\1\50\5\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\51\23\5\4\0\1\5\1\0\3\5\11\0\6\5"+
    "\1\52\5\5\1\53\7\5\4\0\1\5\1\0\3\5"+
    "\37\0\1\54\1\55\44\0\1\56\17\0\1\5\1\57"+
    "\22\5\4\0\1\24\1\60\1\5\1\24\1\5\41\0"+
    "\1\61\2\0\1\61\12\0\24\5\4\0\1\5\1\0"+
    "\1\62\2\5\10\0\36\34\1\0\4\34\10\0\1\63"+
    "\6\0\1\64\4\0\1\65\21\0\1\66\3\0\2\5"+
    "\1\67\1\5\1\70\17\5\4\0\1\5\1\0\3\5"+
    "\11\0\23\5\1\71\4\0\1\5\1\0\3\5\11\0"+
    "\3\5\1\72\20\5\4\0\1\5\1\0\3\5\11\0"+
    "\10\5\1\73\13\5\4\0\1\5\1\0\3\5\11\0"+
    "\10\5\1\74\13\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\75\1\5\1\76\21\5\4\0\1\5\1\0\3\5"+
    "\11\0\1\5\1\77\22\5\4\0\1\5\1\0\3\5"+
    "\11\0\21\5\1\100\2\5\4\0\1\5\1\0\3\5"+
    "\11\0\10\5\1\101\13\5\4\0\1\5\1\0\3\5"+
    "\11\0\2\5\1\102\21\5\4\0\1\5\1\0\3\5"+
    "\11\0\4\5\1\103\17\5\4\0\1\5\1\0\3\5"+
    "\11\0\24\5\4\0\1\5\1\0\1\104\1\105\1\5"+
    "\12\0\1\106\26\0\1\61\2\0\1\61\11\0\1\107"+
    "\24\62\4\107\1\62\1\107\3\62\1\0\7\107\1\0"+
    "\1\5\1\110\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\17\5\1\111\4\5\4\0\1\5\1\0\3\5\11\0"+
    "\5\5\1\112\16\5\4\0\1\5\1\0\3\5\11\0"+
    "\3\5\1\113\20\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\5\1\114\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\13\5\1\115\10\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\5\1\116\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\1\5\1\117\22\5\4\0\1\5\1\0\3\5\11\0"+
    "\2\5\1\120\21\5\4\0\1\5\1\0\3\5\11\0"+
    "\24\5\4\0\1\5\1\0\1\5\1\105\1\5\11\0"+
    "\24\5\4\0\1\105\1\0\1\5\1\105\1\5\43\0"+
    "\1\121\1\122\11\0\36\107\1\0\7\107\1\0\3\5"+
    "\1\123\20\5\4\0\1\5\1\0\3\5\11\0\4\5"+
    "\1\124\17\5\4\0\1\5\1\0\3\5\11\0\4\5"+
    "\1\125\17\5\4\0\1\5\1\0\3\5\11\0\4\5"+
    "\1\126\17\5\4\0\1\5\1\0\3\5\11\0\1\5"+
    "\1\127\22\5\4\0\1\5\1\0\3\5\11\0\22\5"+
    "\1\130\1\5\4\0\1\5\1\0\3\5\11\0\13\5"+
    "\1\131\10\5\4\0\1\5\1\0\3\5\44\0\1\122"+
    "\42\0\1\122\2\0\1\122\12\0\4\5\1\132\17\5"+
    "\4\0\1\5\1\0\3\5\11\0\1\5\1\133\22\5"+
    "\4\0\1\5\1\0\3\5\11\0\1\5\1\134\22\5"+
    "\4\0\1\5\1\0\3\5\11\0\7\5\1\135\14\5"+
    "\4\0\1\5\1\0\3\5\10\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[2356];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\14\1\1\11\1\1\1\11\4\1\5\11"+
    "\1\1\1\11\16\1\3\11\1\1\1\0\2\1\4\11"+
    "\17\1\1\0\12\1\1\0\14\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[93];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */
  // Author: FracPete (fracpete at waikato dot ac dot nz)
  // Version: $Revision$
  protected SymbolFactory sf;

  public Scanner(InputStream r, SymbolFactory sf){
    this(r);
    this.sf = sf;
  }
  StringBuilder string = new StringBuilder();


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scanner(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Scanner(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 164) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 21: 
          { return sf.newSymbol("Not qquals", sym.NOT_EQ);
          }
        case 41: break;
        case 37: 
          { return sf.newSymbol("Select", sym.SELECT);
          }
        case 42: break;
        case 32: 
          { return sf.newSymbol("Desc",   sym.DESC);
          }
        case 43: break;
        case 23: 
          { string.append('\t');
          }
        case 44: break;
        case 12: 
          { string.setLength(0); yybegin(STRING);
          }
        case 45: break;
        case 8: 
          { /* ignore white space. */
          }
        case 46: break;
        case 9: 
          { return sf.newSymbol("Comma", sym.COMMA);
          }
        case 47: break;
        case 35: 
          { return sf.newSymbol("Where",  sym.WHERE);
          }
        case 48: break;
        case 14: 
          { yybegin(YYINITIAL);
                  return sf.newSymbol("String", sym.STRING, string.toString());
          }
        case 49: break;
        case 19: 
          { return sf.newSymbol("Is",     sym.IS);
          }
        case 50: break;
        case 36: 
          { return sf.newSymbol("Order",  sym.ORDER);
          }
        case 51: break;
        case 15: 
          { string.append('\\');
          }
        case 52: break;
        case 7: 
          { return sf.newSymbol("Number", sym.NUMBER, new Double(yytext()));
          }
        case 53: break;
        case 4: 
          { return sf.newSymbol("Less than", sym.LT);
          }
        case 54: break;
        case 24: 
          { string.append('\r');
          }
        case 55: break;
        case 3: 
          { return sf.newSymbol("All", sym.ALL);
          }
        case 56: break;
        case 11: 
          { return sf.newSymbol("Right Bracket", sym.RPAREN);
          }
        case 57: break;
        case 39: 
          { return sf.newSymbol("Delete", sym.DELETE);
          }
        case 58: break;
        case 2: 
          { return sf.newSymbol("Column", sym.COLUMN, new String(yytext()));
          }
        case 59: break;
        case 27: 
          { return sf.newSymbol("Set",    sym.SET);
          }
        case 60: break;
        case 26: 
          { string.append('\"');
          }
        case 61: break;
        case 38: 
          { return sf.newSymbol("Update", sym.UPDATE);
          }
        case 62: break;
        case 5: 
          { return sf.newSymbol("Equals", sym.EQ);
          }
        case 63: break;
        case 22: 
          { return sf.newSymbol("Greater or equal than", sym.GE);
          }
        case 64: break;
        case 31: 
          { /* ignore line comments. */
          }
        case 65: break;
        case 34: 
          { return sf.newSymbol("Limit", sym.LIMIT);
          }
        case 66: break;
        case 16: 
          { return sf.newSymbol("As",     sym.AS);
          }
        case 67: break;
        case 25: 
          { string.append('\n');
          }
        case 68: break;
        case 20: 
          { return sf.newSymbol("Less or equal than", sym.LE);
          }
        case 69: break;
        case 10: 
          { return sf.newSymbol("Left Bracket", sym.LPAREN);
          }
        case 70: break;
        case 13: 
          { string.append(yytext());
          }
        case 71: break;
        case 40: 
          { return sf.newSymbol("RegExp", sym.REGEXP);
          }
        case 72: break;
        case 1: 
          { System.err.println("Illegal character: "+yytext());
          }
        case 73: break;
        case 30: 
          { return sf.newSymbol("Not", sym.NOT);
          }
        case 74: break;
        case 17: 
          { return sf.newSymbol("Or", sym.OR);
          }
        case 75: break;
        case 33: 
          { return sf.newSymbol("Null",   sym.NULL);
          }
        case 76: break;
        case 6: 
          { return sf.newSymbol("Greater than", sym.GT);
          }
        case 77: break;
        case 18: 
          { return sf.newSymbol("By",     sym.BY);
          }
        case 78: break;
        case 28: 
          { return sf.newSymbol("Asc",    sym.ASC);
          }
        case 79: break;
        case 29: 
          { return sf.newSymbol("And", sym.AND);
          }
        case 80: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
              {     return sf.newSymbol("EOF",sym.EOF);
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
