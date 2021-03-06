package PL110_10627153;

import java.util.Scanner;
import java.util.Vector;

class Global {

  // Blow is Token Type
  static final int s_T_LEFT_PAREN = 1;
  static final int s_T_RIGHT_PAREN = 2;
  static final int s_T_BOOLEANOPERATOE = 3; // &&, ||, !, ==, !=
  static final int s_T_ID = 4;
  static final int s_T_OPERATOR = 5; // +, -, *, /, %
  static final int s_T_SEMICOLON = 6; // ;
  static final int s_T_NUM = 7;
  static final int s_T_ASSIGN = 11; // =, +=, -=, *=, /=, %=

} // class Global

class ATOM {
  public String mstamStr;
  public int mtype;

  public ATOM( String input, int defineType ) throws Throwable {
    mstamStr = new String( input );
    mtype = defineType;
  } // ATOM()

} // class ATOM

// 1 LEFT_PAREN, '('
// 2 RIGHT_PAREN, ')'
// 3 BOOLEANOPERATOE, '<=', '>=', '<', '>', '='
// 4 ID,
// 5 OPERATOR, '+', '-', '*', '/'
// 6 SEMICOLON, ';'
// 7 NUM,
// 8 ASSIGN, ':='

class Variable {

  public String mvarName;
  public String mvalue;

  public Variable( String name, String value ) throws Throwable {
    this.mvarName = new String( name );
    this.mvalue = new String( value );
  } // Variable()

} // class Variable

class CutToken {

  private Vector<ATOM> mBuffer;
  private Vector<ATOM> mErrorBuffer;
  private Vector<Variable> mVariables;
  private String mnowLine;
  private Scanner msc;

  public CutToken( Scanner inputSc, Vector<Variable> iV ) throws Throwable {

    mBuffer = new Vector<ATOM>();
    mErrorBuffer = new Vector<ATOM>();
    msc = inputSc;
    mVariables = iV;
    mnowLine = new String();

  } // CutToken()

  public boolean Cutting( Vector<ATOM> stament ) throws Throwable {

    boolean notGetSEMICOLON = true;
    boolean gettingRun = true;

    if ( mBuffer.size() > 0 ) {
      if ( mBuffer.get( 0 ).mstamStr.equals( "quit" ) ) {
        System.out.print( "> " );
        return false;
      } // if

    } // if


    if ( Buffer1HasFullCommend( stament ) ) {
      System.out.print( "> " );

      return true;

    } // if
    else {
      System.out.print( "> " );
    } // else

    if ( mnowLine.isEmpty() )
      InputNextLineTomNowLine();

    while ( notGetSEMICOLON ) {
      if ( mnowLine.isEmpty() )
        InputNextLineTomNowLine();
      try {
        if ( mnowLine.charAt( 0 ) == '(' ) {
          mBuffer.add( new ATOM( "(", 1 ) );
          IsGotTokenProcessFormNowLine( 1 );
          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_ID ) {

              if ( FindVariable( mBuffer.get( mBuffer.size() - 2 ).mstamStr ) == - 1 ) {
                System.out.println( "Undefined identifier : '"
                                    + mBuffer.get( mBuffer.size() - 2 ).mstamStr + "'" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if

            } // if

          } // if
        } // if
        else if ( mnowLine.charAt( 0 ) == ')' ) {
          mBuffer.add( new ATOM( ")", 2 ) );
          IsGotTokenProcessFormNowLine( 1 );
          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_LEFT_PAREN ) {
              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_ID ) {

              if ( FindVariable( mBuffer.get( mBuffer.size() - 2 ).mstamStr ) == - 1 ) {
                System.out.println( "Undefined identifier : '"
                                    + mBuffer.get( mBuffer.size() - 2 ).mstamStr + "'" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if

            } // if

          } // if

          int count = 0;
          for ( int i = 0 ; i < mBuffer.size() ; i++ ) {
            if ( mBuffer.get( i ).mtype == Global.s_T_LEFT_PAREN )
              count = count + 1;
            else if ( mBuffer.get( i ).mtype == Global.s_T_RIGHT_PAREN )
              count = count - 1;

          } // for

          if ( count < 0 ) {
            System.out.println( "Unexpected token : '"
                                + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
            System.out.print( "> " );
            ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
            throw new Throwable();
          } // if

        } // else if
        else if ( mnowLine.charAt( 0 ) == ';' ) {
          mBuffer.add( new ATOM( ";", 6 ) );
          IsGotTokenProcessFormNowLine( 1 );

          notGetSEMICOLON = false;

          if ( mBuffer.size() == 1 ) {
            System.out.println( "Unexpected token : ';'" );
            System.out.print( "> " );  // >>isproblem
            mBuffer.clear();
            notGetSEMICOLON = true;
            throw new Throwable();
          } // if

          if ( Buffer1HasFullCommend( stament ) )
            return true;

        } // else if
        else if ( IsASSIGNInmNowLineFirst() ) {
          if ( mnowLine.length() == 1 ) {
            System.out.println( "Unrecognized token with first char : ':'" );
            System.out.print( "> " );
            ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear();
            throw new Throwable();
          } // if

          if ( mnowLine.charAt( 1 ) != '=' ) {
            System.out.println( "Unrecognized token with first char : ':'" );
            System.out.print( "> " );
            ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear();
            throw new Throwable();
          } // if

          mBuffer.add( new ATOM( mnowLine.substring( 0, 2 ), 8 ) );
          IsGotTokenProcessFormNowLine( 2 );
          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype != 4 ) {
              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear();
              throw new Throwable();
            } // if

          } // if

        } // else if
        else if ( IsOPERATORInmNowLineFirst() ) {
          String gotOPERATOR = mnowLine.substring( 0, 1 );
          mBuffer.add( new ATOM( gotOPERATOR, 5 ) );
          IsGotTokenProcessFormNowLine( 1 );

          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( "+" ) ||
                 mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( "-" ) ||
                 mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( "*" ) ||
                 mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( "/" ) ) {
              if ( gotOPERATOR.equals( "*" ) || gotOPERATOR.equals( "/" ) ) {
                System.out.println( "Unexpected token : '"
                                    + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if

            } // if

          } // if


          if ( mBuffer.size() > 2 ) {
            if ( mBuffer.get( mBuffer.size() - 3 ).mtype == Global.s_T_OPERATOR &&
                 mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_OPERATOR ) {

              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();

            } // if

          } // if

          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_ID ) {

              if ( FindVariable( mBuffer.get( mBuffer.size() - 2 ).mstamStr ) == - 1 ) {
                System.out.println( "Undefined identifier : '"
                                    + mBuffer.get( mBuffer.size() - 2 ).mstamStr + "'" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if

            } // if

          } // if

        } // else if
        else if ( IsNUMInmNowLineFirst() ) {
          String gotNUM;
          gotNUM = GetNUMTokenInmNowLine();

          if ( mBuffer.size() > 0 ) {
            if ( mBuffer.get( mBuffer.size() - 1 ).mtype == Global.s_T_NUM ) {
              System.out.println( "Unexpected token : '"
                                  + gotNUM + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

          if ( gotNUM.charAt( 0 ) == '.' ) {
            mBuffer.add( new ATOM( "0" + gotNUM, 7 ) );
            gotNUM = "0" + gotNUM;
          } // if
          else {
            mBuffer.add( new ATOM( gotNUM, 7 ) );

          } // else

          int nowIndex = mBuffer.size() - 1;

          try {

            if ( mBuffer.get( nowIndex ).mstamStr.contains( "." ) ) {
              Double rv = Double.parseDouble( gotNUM );
              gotNUM = String.format( "%.10f", rv );

            } // if

            int rv = Integer.valueOf( mBuffer.get( nowIndex ).mstamStr ).intValue();
            mBuffer.get( nowIndex ).mstamStr = Integer.toString( rv );

          } // try
          catch ( Throwable throwable ) {

          } // catch

          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( "/" ) ) {
              boolean isnnotAll0orp = false;
              int nowtheIndex = mBuffer.size() - 1;
              for ( int i = 0 ; i < mBuffer.get( nowtheIndex ).mstamStr.length() ; i++ ) {

                if ( gotNUM.charAt( i ) >= '1' && gotNUM.charAt( i ) <= '9' )
                  isnnotAll0orp = true;

              } // for

              if ( ! isnnotAll0orp ) {
                System.out.println( "Error" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if
            } // if

          } // if


        } // else if
        else if ( IsBOOLEANInmNowLineFirst() ) {
          String gotBOOLEANOPERATOE;
          gotBOOLEANOPERATOE = GetBOOLEANOPERATOETokenInmNowLine();
          mBuffer.add( new ATOM( gotBOOLEANOPERATOE, 3 ) );
          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_BOOLEANOPERATOE ) {
              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

          if ( mBuffer.size() == 1 ) {
            if ( mBuffer.get( 0 ).mtype == Global.s_T_BOOLEANOPERATOE ) {
              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( 0 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

        } // else if
        else if ( IsIDInmNowLineFirst() ) {
          String gotID;
          gotID = GetIDTOETokenInmNowLine();
          mBuffer.add( new ATOM( gotID, 4 ) );

          if ( mBuffer.size() > 1 ) {
            if ( mBuffer.get( mBuffer.size() - 2 ).mtype == Global.s_T_ID ) {
              System.out.println( "Unexpected token : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

          if ( mBuffer.size() > 1 ) {

            if ( FindVariable( mBuffer.get( mBuffer.size() - 1 ).mstamStr ) == - 1 ) {
              System.out.println( "Undefined identifier : '"
                                  + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
              System.out.print( "> " );
              ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
              throw new Throwable();
            } // if

          } // if

          try {
            int nowIndex = mBuffer.size();

            if ( nowIndex > 2 ) {
              if ( mBuffer.get( nowIndex - 2 ).mtype == Global.s_T_OPERATOR &&
                   mBuffer.get( nowIndex - 1 ).mtype == Global.s_T_OPERATOR ) {
                System.out.println( "Unexpected token : '"
                                    + mBuffer.get( mBuffer.size() - 1 ).mstamStr + "'" );
                System.out.print( "> " );
                ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
                throw new Throwable();
              } // if

            } // if


          } // try
          catch ( Throwable throwable2 ) {
            throw new Throwable();
          } // catch

          if ( gotID.equals( "quit" ) ) {
            if ( mBuffer.size() > 1 ) {
              if ( mBuffer.get( mBuffer.size() - 2 ).mstamStr.equals( ";" ) ) {
                mnowLine = new String(); // throw new Throwable();
              } // if

            } // if

            if ( mBuffer.size() == 1 && mBuffer.get( 0 ).mstamStr.equals( "quit" ) )
              return false;

            if ( mBuffer.get( 0 ).mstamStr.equals( "quit" ) )
              throw new Throwable();

          } // if

        } // else if
        else {
          if ( ! mnowLine.isEmpty() ) {
            System.out.println( "Unrecognized token with first char : '" + mnowLine.charAt( 0 ) + "'" );
            System.out.print( "> " );
            ProcessBeforSEMICOLONFormNowLine(); // mBuffer.clear() ;
            throw new Throwable();
          } // if

        } // else

      }
      catch ( Throwable throwable ) {

        // System.out.print( "> " );
        // notGetSEMICOLON = true;
        mnowLine = new String();

      } // catch

    } // while

    if ( mBuffer.size() > 0 ) {
      if ( mBuffer.get( 0 ).mstamStr.equals( "quit" ) ) {
        System.out.print( "> " );
        return false;
      } // if

    } // if

    if ( Buffer1HasFullCommend( stament ) ) {

      return true;

    } // if


    return true;

  } // Cutting()

  private void ProcessBeforSEMICOLONFormNowLine() throws Throwable {

    int lestSEMICOLONIndex = - 1;
    for ( int i = 0 ; i < mBuffer.size() ; i++ ) {
      if ( mBuffer.get( i ).mtype == Global.s_T_SEMICOLON )
        lestSEMICOLONIndex = i;
    } // for

    if ( lestSEMICOLONIndex < mBuffer.size() - 1 ) {
      for ( int i = lestSEMICOLONIndex + 1 ; mBuffer.size() > lestSEMICOLONIndex - 1 ; )
        mBuffer.remove( i );

    } // if

    if ( lestSEMICOLONIndex == - 1 )
      mBuffer.clear();


  } // ProcessBeforSEMICOLONFormNowLine()

  private String GetIDTOETokenInmNowLine() throws Throwable {
    String gotID = new String();

    gotID = gotID + mnowLine.substring( 0, 1 );
    RemoveFirstCherFormNowLine();

    while ( IsIDLegalCharInmNowLineFirst() ) {

      gotID = gotID + mnowLine.substring( 0, 1 );
      RemoveFirstCherFormNowLine();

    } // while

    RemoveHeadWhiteCherFormNowLine();

    return gotID;

  } // GetIDTOETokenInmNowLine()

  private int FindVariable( String findVarName ) {

    for ( int i = 0 ; i < mVariables.size() ; i++ ) {
      if ( mVariables.get( i ).mvarName.equals( findVarName ) )
        return i;
    } // for

    return - 1;

  } // FindVariable()

  private boolean IsIDLegalCharInmNowLineFirst() throws Throwable {
    try {
      char firstChar = mnowLine.charAt( 0 );
      if ( IsIDLegalWord( firstChar ) )
        return true;
      else
        return false;

    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

  } // IsIDLegalCharInmNowLineFirst()

  private String GetNUMTokenInmNowLine() throws Throwable {
    String gotNum = new String();
    boolean gettingRun = true;
    boolean gotPoint = false;

    while ( mnowLine.length() > 0 && IsNumberAndNumberLegalWordInmNowLineFirstChar() && gettingRun ) {

      if ( ! gotPoint && mnowLine.charAt( 0 ) != '.' ) {
        gotNum = gotNum + mnowLine.substring( 0, 1 );
        RemoveFirstCherFormNowLine();
      } // if
      else if ( ! gotPoint && mnowLine.charAt( 0 ) == '.' ) {
        gotNum = gotNum + mnowLine.substring( 0, 1 );
        RemoveFirstCherFormNowLine();
        gotPoint = true;
      } // else if
      else if ( gotPoint && mnowLine.charAt( 0 ) != '.' ) {
        gotNum = gotNum + mnowLine.substring( 0, 1 );
        RemoveFirstCherFormNowLine();
      } // else if
      else if ( gotPoint && mnowLine.charAt( 0 ) == '.' ) {
        gettingRun = false;
      } // else if

    } // while

    RemoveHeadWhiteCherFormNowLine();

    // if ( mBuffer.size() > 0 ) {
    //   if ( mBuffer.get( 0 ).mtype == Global.s_T_NUM )
    //     return false;
    // } // if

    if ( IsLegalNUM( gotNum ) ) {
      return gotNum;
    } // if
    else {
      System.out.println( "Unexpected token : '" + gotNum + "'" );
      System.out.print( "> " );
      mBuffer.clear();
      throw new Throwable();
    } // else


  } // GetNUMTokenInmNowLine()

  private String GetBOOLEANOPERATOETokenInmNowLine() throws Throwable {

    String gotBOOLEANOPERATOE = new String();
    int findIndxe = - 1;
    if ( FindStrStarIndex( ">=" ) > - 1 )
      findIndxe = FindStrStarIndex( ">=" );
    else if ( FindStrStarIndex( "<=" ) > - 1 )
      findIndxe = FindStrStarIndex( "<=" );

    if ( findIndxe > - 1 ) {
      gotBOOLEANOPERATOE = mnowLine.substring( 0, 2 );
      IsGotTokenProcessFormNowLine( 2 );
    } // if
    else {
      gotBOOLEANOPERATOE = mnowLine.substring( 0, 1 );
      IsGotTokenProcessFormNowLine( 1 );
    } // else

    return gotBOOLEANOPERATOE;

  } // GetBOOLEANOPERATOETokenInmNowLine()

  private boolean IsIDInmNowLineFirst() throws Throwable {
    try {

      char charStr = mnowLine.charAt( 0 );

      if ( IsIDLegalStar( charStr ) )
        return true;
      else
        return false;

    } // try
    catch ( Throwable throwable ) {

      return false;

    } // catch

  } // IsIDInmNowLineFirst()

  private boolean IsBOOLEANInmNowLineFirst() throws Throwable {
    try {

      char charStr = mnowLine.charAt( 0 );

      if ( IsBOOLEANOPERATOE( charStr ) )
        return true;
      else
        return false;

    } // try
    catch ( Throwable throwable ) {

      return false;

    } // catch

  } // IsBOOLEANInmNowLineFirst()

  private boolean IsBOOLEANOPERATOE( char theWord ) throws Throwable {

    if ( theWord == '>' || theWord == '<' || theWord == '=' )
      return true;
    else
      return false;

  } // IsBOOLEANOPERATOE()

  private boolean IsASSIGNInmNowLineFirst() throws Throwable {

    try {
      if ( mnowLine.charAt( 0 ) == ':' )
        return true;
      else
        return false;

    } // try
    catch ( Throwable throwable ) {
      return false;

    } // catch

  } // IsASSIGNInmNowLineFirst()

  private boolean Buffer1HasFullCommend( Vector<ATOM> buffer ) throws Throwable {

    int semicolonindex = - 1;
    boolean isHas = false;

    for ( int i = 0 ; i < mBuffer.size() ; i++ ) {
      if ( mBuffer.get( i ).mtype == Global.s_T_SEMICOLON ) {
        semicolonindex = i;
        isHas = true;
        i = mBuffer.size();
      } // if
    } // for

    if ( ! isHas ) {
      return false;
    } // if

    for ( int i = 0 ; i < mBuffer.size() ; i++ )
      buffer.add( mBuffer.get( i ) );

    if ( ( buffer.size() - 1 ) != semicolonindex ) {
      int count = buffer.size() - 1 - semicolonindex;

      for ( int i = 0 ; i < count ; i++ )
        buffer.remove( semicolonindex + 1 );

    } // if

    for ( int i = 0 ; i <= semicolonindex ; i++ )
      mBuffer.remove( 0 );

    return isHas;

  } // Buffer1HasFullCommend()

  private boolean IsIDLegalStar( char theWord ) throws Throwable {

    if ( theWord >= 'A' && theWord <= 'Z' )
      return true;

    if ( theWord >= 'a' && theWord <= 'z' )
      return true;

    if ( theWord == '_' )
      return true;

    return false;

  } // IsIDLegalStar()

  private boolean IsIDLegalWord( char theWord ) throws Throwable {

    if ( theWord >= 'A' && theWord <= 'Z' )
      return true;

    if ( theWord >= 'a' && theWord <= 'z' )
      return true;

    if ( theWord >= '0' && theWord <= '9' )
      return true;

    if ( theWord == '_' )
      return true;

    return false;

  } // IsIDLegalWord()

  private boolean IsLegalNUMStar( char theWord ) throws Throwable {

    if ( theWord >= '0' && theWord <= '9' ) {
      return true;
    } // if
    else {
      return false;
    } // else

  } // IsLegalNUMStar()

  private boolean IsLegalNUM( String fNUM ) throws Throwable {

    try {
      if ( fNUM.isEmpty() )
        return false;

      if ( fNUM.equals( "." ) )
        return false;

      // if ( fNUM.charAt( 0 ) == '.' ) {
      //   return false;

      // } // if

      // if ( fNUM.charAt( fNUM.length() - 1 ) == '.' )
      //  return false;

      return true;
    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

  } // IsLegalNUM()

  private boolean IsNumberAndNumberLegalWordInmNowLineFirstChar() throws Throwable {

    try {
      char firstChar = mnowLine.charAt( 0 );
      if ( firstChar >= '0' && firstChar <= '9' )
        return true;
      else if ( firstChar == '.' )
        return true;
      else
        return false;

    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

  } // IsNumberAndNumberLegalWordInmNowLineFirstChar()

  private boolean IsOPERATORInmNowLineFirst() throws Throwable {
    char charStr = mnowLine.charAt( 0 );
    boolean isCon1 = false;
    boolean isCon2 = false;

    if ( charStr == '+' || charStr == '-' )
      isCon1 = true;

    if ( charStr == '*' || charStr == '/' )
      isCon2 = true;

    if ( isCon1 || isCon2 )
      return true;
    else
      return false;

  } // IsOPERATORInmNowLineFirst()

  private boolean IsNUMInmNowLineFirst() throws Throwable {
    try {

      char charStr = mnowLine.charAt( 0 );

      if ( IsLegalNUMStar( charStr ) )
        return true;

      if ( charStr == '.' )
        return true;

      else
        return false;

    } // try
    catch ( Throwable throwable ) {

      return false;

    } // catch

  } // IsNUMInmNowLineFirst()

  private void InputNextLineTomNowLine() throws Throwable {

    mnowLine = msc.nextLine();
    mnowLine = RemoveCommend( mnowLine );
    RemoveHeadWhiteCherFormNowLine();
    RemoveTailWhiteCherFormNowLine();

    while ( mnowLine.isEmpty() ) {
      mnowLine = msc.nextLine();
      mnowLine = RemoveCommend( mnowLine );
      RemoveHeadWhiteCherFormNowLine();
      RemoveTailWhiteCherFormNowLine();
    } // while

  } // InputNextLineTomNowLine()

  private String RemoveCommend( String comm ) throws Throwable {

    int findCommend = FindStrStarIndex( comm, "//" );

    if ( findCommend == 0 ) {
      comm = new String();
    } // if
    else if ( findCommend > - 1 ) {
      comm = mnowLine.substring( 0, findCommend );
    } // else if

    return comm;

  } // RemoveCommend()

  private int FindStrStarIndex( String lineStr, String targetStr ) {

    try {
      for ( int i = 0 ; i < lineStr.length() ; i++ ) {
        String subLine = lineStr.substring( i, i + targetStr.length() );
        if ( subLine.equals( targetStr ) )
          return i;

      } // for

    } // try
    catch ( Throwable throwable ) {
      return - 1;
    } // catch

    return - 1;

  } // FindStrStarIndex()

  private int FindStrStarIndex( String targetStr ) {

    String lineStr = mnowLine;

    try {
      for ( int i = 0 ; i < lineStr.length() ; i++ ) {
        String subLine = lineStr.substring( i, i + targetStr.length() );
        if ( subLine.equals( targetStr ) )
          return i;

      } // for

    } // try
    catch ( Throwable throwable ) {
      return - 1;
    } // catch

    return - 1;

  } // FindStrStarIndex()

  private void RemoveHeadWhiteCherFormNowLine() throws Throwable {

    try {

      for ( int i = 0 ; i < mnowLine.length() ; ) {
        if ( IsWhitSpace( mnowLine.charAt( i ) ) ) {
          if ( mnowLine.length() == 1 )
            mnowLine = new String();
          else
            mnowLine = mnowLine.substring( i + 1 );

        } // if
        else
          i = mnowLine.length();

      } // for

    } // try
    catch ( Throwable throwable ) {

    } // catch


  } // RemoveHeadWhiteCherFormNowLine()

  private void RemoveTailWhiteCherFormNowLine() throws Throwable {

    try {

      for ( int i = mnowLine.length() - 1 ; i > - 1 ; i = i ) {
        if ( IsWhitSpace( mnowLine.charAt( i ) ) ) {
          if ( mnowLine.length() == 1 )
            mnowLine = new String();
          else
            mnowLine = mnowLine.substring( 0, i );

          i = mnowLine.length() - 1;

        } // if
        else
          i = - 1;

      } // for

    } // try
    catch ( Throwable throwable ) {

    } // catch


  } // RemoveTailWhiteCherFormNowLine()

  private void RemoveFirstCherFormNowLine() throws Throwable {

    try {

      if ( mnowLine.length() == 1 || mnowLine.isEmpty() )
        mnowLine = new String();
      else
        mnowLine = mnowLine.substring( 1 );

    } // try
    catch ( Throwable throwable ) {

    } // catch


  } // RemoveFirstCherFormNowLine()

  private void IsGotTokenProcessFormNowLine( int gotCharIndex ) throws Throwable {
    for ( int i = 0 ; i < gotCharIndex ; i++ )
      RemoveFirstCherFormNowLine();

    RemoveHeadWhiteCherFormNowLine();

  } // IsGotTokenProcessFormNowLine()

  private boolean IsWhitSpace( char charStr ) throws Throwable {

    if ( charStr == ' ' || charStr == '\t' )
      return true;

    if ( charStr == '\r' || charStr == '\n' )
      return true;

    if ( charStr == '\u000B' || charStr == '\f' )
      return true;

    if ( charStr == '\u001C' || charStr == '\u001D' )
      return true;

    if ( charStr == '\u001E' || charStr == '\u001F' )
      return true;

    return false;

  } // IsWhitSpace()

} // class CutToken

class Paser {
  public Vector<ATOM> mstament;
  public int mindex;

  public Paser( Vector<ATOM> inputeStament ) throws Throwable {
    mstament = inputeStament;
    mindex = 0;
  } // Paser()

  public boolean GrammarParser() throws Throwable {

    try {

      if ( mstament.get( 0 ).mstamStr.equals( ";" ) ) {
        System.out.println( "Unexpected token : ';'" );
        mstament.clear();
        return false;
      } // if

      if ( this.Command() ) {
        return true;
      } // if ()
      else {
        System.out.println( "Unexpected token : '" + mstament.get( mindex ).mstamStr + "'" );
        return false;
      } // else

    } // try
    catch ( Throwable throwable ) {
      System.out.println( "Unexpected token : '" + mstament.get( mindex ).mstamStr + "'" );
      return false;
    } // catch

  } // GrammarParser()

  private boolean Command() throws Throwable {

    try {

      if ( mstament.get( mindex ).mtype == Global.s_T_ID ) {
        mindex++;
        if ( mstament.get( mindex ).mtype == Global.s_T_ASSIGN ) {
          mindex++;
          if ( ArithExp() ) {
            if ( mstament.get( mindex ).mtype == Global.s_T_SEMICOLON ) {
              mindex++;
              return true;
            } // if ()
            else {
              // System.out.println("Undefined identifier : '" + stament.get(index).mstamStr + "'" );
              // throw new Throwable();
              return false;
            } // else

          } // if ()
          else {
            // System.out.println("Undefined identifier : '" + stament.get(index).mstamStr + "'" );
            // throw new Throwable();
            return false;
          } // else

        } // if ()
        else if ( IDlessArithExpOrBexp() ) {
          if ( mstament.get( mindex ).mtype == Global.s_T_SEMICOLON ) {
            mindex++;
            return true;
          } // if ()
          else {
            // System.out.println("Undefined identifier : '" + stament.get(index).mstamStr + "'" );
            // throw new Throwable();
            return false;
          } // else

        } // else if
        else if ( mstament.get( mindex ).mtype == Global.s_T_SEMICOLON ) {
          // System.out.println("Undefined identifier : '" + stament.get(index).mstamStr + "'" );
          // throw new Throwable();
          return true;

        } // else if
        else {
          return false;

        } // else

      } // if ()
      else if ( NOT_IDStartArithExpOrBexp() ) {
        if ( mstament.get( mindex ).mtype == Global.s_T_SEMICOLON ) {
          mindex++;
          return true;
        } // if ()
        else {
          return false;
        } // else
      } // else if ()
      else {
        // System.out.println("Undefined identifier : '" + stament.get(index).mstamStr + "'" );
        // throw new Throwable();
        return false;
      } // else
    } // try
    catch ( Throwable throwable ) {
      throw new Throwable();
    } // catch
  } // Command()

  private boolean IDlessArithExpOrBexp() throws Throwable {

    boolean isOnePass = false;

    try {
      while ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
              mstament.get( mindex ).mstamStr.equals( "-" ) ||
              mstament.get( mindex ).mstamStr.equals( "*" ) ||
              mstament.get( mindex ).mstamStr.equals( "/" ) ) {

        if ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
             mstament.get( mindex ).mstamStr.equals( "-" ) ) {

          mindex++;

          if ( Term() ) {
            isOnePass = true;
          } // if ()
          else
            return false;

        } // if
        else if ( mstament.get( mindex ).mstamStr.equals( "*" ) ||
                  mstament.get( mindex ).mstamStr.equals( "/" ) ) {
          mindex++;
          if ( Factor() )
            isOnePass = true;
          else
            return false;
        } // else if

      } // while

      if ( BooleanOperator() ) {

        if ( ArithExp() )
          isOnePass = true;
        else
          return false;

      } // if ()

    } // try
    catch ( Throwable throwable ) {
      if ( ! isOnePass )
        return false;
    } // catch

    if ( isOnePass )
      return true;
    else
      return false;

  } // IDlessArithExpOrBexp()

  private boolean BooleanOperator() throws Throwable {

    try {

      if ( mstament.get( mindex ).mtype == Global.s_T_BOOLEANOPERATOE ) {
        mindex++;
        return true;
      } // if
      else {
        return false;
      } // else

    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch


  } // BooleanOperator()

  private boolean NOT_IDStartArithExpOrBexp() throws Throwable {

    boolean isOnePass = false;

    if ( ! NOT_ID_StartArithExp() )
      return false;

    if ( BooleanOperator() ) {
      if ( ArithExp() )
        return true;
      else
        return false;
    } // if

    return true;

  } // NOT_IDStartArithExpOrBexp()

  private boolean NOT_ID_StartArithExp() throws Throwable {

    if ( ! NOT_ID_StartTerm() )
      return false;

    try {

      while ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
              mstament.get( mindex ).mstamStr.equals( "-" ) ) {

        if ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
             mstament.get( mindex ).mstamStr.equals( "-" ) ) {

          mindex++;

          if ( ! Term() )
            return false;

        } // if

      } // while

    } // try
    catch ( Throwable throwable ) {
      ;
    } // catch

    return true;

  } // NOT_ID_StartArithExp()

  private boolean NOT_ID_StartTerm() throws Throwable {

    if ( ! NOT_ID_StartFactor() )
      return false;

    try {
      while ( mstament.get( mindex ).mstamStr.equals( "*" ) ||
              mstament.get( mindex ).mstamStr.equals( "/" ) ) {

        if ( mstament.get( mindex ).mstamStr.equals( "*" ) ||
             mstament.get( mindex ).mstamStr.equals( "/" ) ) {

          mindex++;

          if ( ! Factor() )
            return false;

        } // if

      } // while

    } // try
    catch ( Throwable throwable ) {
      ;
    } // catch
    return true;

  } // NOT_ID_StartTerm()

  private boolean NOT_ID_StartFactor() throws Throwable {

    boolean isOnePass = false;
    try {

      if ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
           mstament.get( mindex ).mstamStr.equals( "-" ) ) {
        mindex++;
      } // if

      if ( mstament.get( mindex ).mtype == Global.s_T_NUM ) {
        mindex++;
        isOnePass = true;
      } // if

    } // try
    catch ( Throwable throwable ) {
      ;
    } // catch

    if ( isOnePass )
      return true;

    try {
      if ( mstament.get( mindex ).mtype == Global.s_T_LEFT_PAREN ) {
        mindex++;
        if ( ArithExp() ) {
          if ( mstament.get( mindex ).mtype == Global.s_T_RIGHT_PAREN ) {
            mindex++;
            return true;
          } // if
          else
            return false;
        } // if
        else
          return false;
      } // if
      else
        return false;
    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

  } // NOT_ID_StartFactor()

  private boolean ArithExp() throws Throwable {
    if ( ! Term() )
      return false;

    try {
      while ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
              mstament.get( mindex ).mstamStr.equals( "-" ) ) {

        if ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
             mstament.get( mindex ).mstamStr.equals( "-" ) ) {

          mindex++;
          if ( ! Term() )
            return false;

        } // if

      } // while

    } // try
    catch ( Throwable throwable ) {
      ;
    } // catch
    return true;

  } // ArithExp()

  private boolean Term() throws Throwable {

    if ( ! Factor() )
      return false;

    try {
      while ( mstament.get( mindex ).mstamStr.equals( "*" ) ||
              mstament.get( mindex ).mstamStr.equals( "/" ) ) {

        if ( mstament.get( mindex ).mstamStr.equals( "*" ) ||
             mstament.get( mindex ).mstamStr.equals( "/" ) ) {

          mindex++;
          if ( ! Factor() )
            return false;

        } // if

      } // while

    } // try
    catch ( Throwable throwable ) {
      ;
    } // catch

    return true;

  } // Term()

  private boolean Factor() throws Throwable {

    boolean isOnePass = false;

    try {

      if ( mstament.get( mindex ).mtype == Global.s_T_ID ) {
        mindex++;
        isOnePass = true;
      } // if

    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

    if ( isOnePass )
      return true;

    try {
      boolean haveSIGN = false;
      String signOperator = new String();

      if ( mstament.get( mindex ).mstamStr.equals( "+" ) ||
           mstament.get( mindex ).mstamStr.equals( "-" ) ) {
        mindex++;
      } // if

      if ( mstament.get( mindex ).mtype == Global.s_T_NUM ) {
        mindex++;
        isOnePass = true;
      } // if

    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

    if ( isOnePass )
      return true;

    try {
      if ( mstament.get( mindex ).mtype == Global.s_T_LEFT_PAREN ) {

        mindex++;
        if ( ArithExp() ) {

          if ( mstament.get( mindex ).mtype == Global.s_T_RIGHT_PAREN ) {
            mindex++;
            return true;
          } // if
          else
            return false;

        } // if
        else
          return false;

      } // if
      else
        return false;
    } // try
    catch ( Throwable throwable ) {
      return false;
    } // catch

  } // Factor()

} // class Paser

class ProcessComment {

  Vector<ATOM> mstament;
  Vector<Variable> mvariables;

  ProcessComment( Vector<ATOM> inPutCommend, Vector<Variable> inputVar ) throws Throwable {
    mstament = inPutCommend;
    mvariables = inputVar;
  } // ProcessComment()

  public void ExcuteComment() throws Throwable {
    try {
      if ( FindATOM( mstament, 6 ) > - 1 ) {
        mstament.remove( FindATOM( mstament, 6 ) );
      } // if

      if ( FindATOM( 8 ) > - 1 ) {
        AssignCommend();
      } // if


      else if ( mstament.size() == 1 && mstament.get( 0 ).mtype == Global.s_T_ID ) {
        String idName = new String( mstament.get( 0 ).mstamStr );
        if ( FindVariable( idName ) > - 1 ) {
          PrintVariable( mvariables.get( FindVariable( idName ) ) );
        } // if
        else {
          System.out.println( "Undefined identifier : '" + mstament.get( 0 ).mstamStr + "'" );
        } // else

      } // else if
      else if ( FindATOM( 3 ) > - 1 ) {
        BooleanCommend();
      } // else if
      else
        JustFormal();
    } // try
    catch ( Throwable throwable ) {

    } // catch
  } // ExcuteComment()

  private int FindVariable( String findVarName ) {

    for ( int i = 0 ; i < mvariables.size() ; i++ ) {
      if ( mvariables.get( i ).mvarName.equals( findVarName ) )
        return i;
    } // for

    return - 1;

  } // FindVariable()

  private int FindATOM( Vector<ATOM> tVactor, int targetType ) throws Throwable {

    for ( int i = 0 ; i < tVactor.size() ; i++ ) {
      if ( tVactor.get( i ).mtype == targetType )
        return i;
    } // for

    return - 1;
  } // FindATOM()

  private int FindATOM( int targetType ) throws Throwable {

    for ( int i = 0 ; i < mstament.size() ; i++ ) {
      if ( mstament.get( i ).mtype == targetType )
        return i;
    } // for

    return - 1;
  } // FindATOM()

  private int FindATOM( String findStr ) throws Throwable {

    for ( int i = 0 ; i < mstament.size() ; i++ ) {
      if ( mstament.get( i ).mstamStr.equals( findStr ) )
        return i;
    } // for

    return - 1;
  } // FindATOM()

  private int FindATOM( Vector<ATOM> tVactor, String findStr ) throws Throwable {

    for ( int i = 0 ; i < tVactor.size() ; i++ ) {
      if ( tVactor.get( i ).mstamStr.equals( findStr ) )
        return i;
    } // for

    return - 1;
  } // FindATOM()

  private Vector<ATOM> CopymStament( int starIndex, int endIndex ) throws Throwable {

    Vector<ATOM> returnVector = new Vector<ATOM>();
    for ( int i = starIndex ; i <= endIndex ; i++ )
      returnVector.add( mstament.get( i ) );
    return returnVector;

  } // CopymStament()

  private Vector<ATOM> CopymStament( Vector<ATOM> tVactor,
                                     int starIndex, int endIndex ) throws Throwable {

    Vector<ATOM> returnVector = new Vector<ATOM>();
    for ( int i = starIndex ; i <= endIndex ; i++ )
      returnVector.add( tVactor.get( i ) );
    return returnVector;

  } // CopymStament()

  private void RemovemStament( int starIndex, int endIndex ) throws Throwable {

    int coutRemove = endIndex - starIndex + 1;
    for ( int i = 0 ; i < coutRemove ; i++ )
      mstament.remove( starIndex );

  } // RemovemStament()

  private void RemovemStament( Vector<ATOM> tVactor, int starIndex, int endIndex ) throws Throwable {

    int coutRemove = endIndex - starIndex + 1;
    for ( int i = 0 ; i < coutRemove ; i++ )
      tVactor.remove( starIndex );

  } // RemovemStament()

  private void PrintVariable( ATOM var ) throws Throwable {

    if ( ( var.mstamStr.contains( "-" ) || var.mstamStr.contains( "+" ) ) && AllIs0( var.mstamStr ) )
      var.mstamStr = var.mstamStr.substring( 1 );

    if ( var.mstamStr.contains( "." ) )
      System.out.println( RegularFloat( Float.parseFloat( var.mstamStr ) ) );
    else
      System.out.println( var.mstamStr );

  } // PrintVariable()

  private void PrintVariable( Variable var ) throws Throwable {
    if ( ( var.mvalue.contains( "-" ) || var.mvalue.contains( "+" ) ) && AllIs0( var.mvalue ) )
      var.mvalue = var.mvalue.substring( 1 );

    if ( var.mvalue.contains( "." ) )
      System.out.println( RegularFloat( Float.parseFloat( var.mvalue ) ) );
    else
      System.out.println( var.mvalue );

  } // PrintVariable()

  private String RegularFloat( double num ) throws Throwable {

    if ( num > 0 )
      num = num + 0.0005;
    else if ( num < 0 )
      num = num - 0.0005;

    String srum = String.format( "%.4f", num );
    // String srum = Float.toString( rnum );


    if ( srum.length() - srum.indexOf( "." ) == 3 ) {
      srum = srum + "0";
    } // if
    else if ( srum.length() - srum.indexOf( "." ) == 2 ) {
      srum = srum + "00";
    } // else if
    else if ( srum.length() - srum.indexOf( "." ) == 1 ) {
      srum = srum + "000";
    } // else if
    else if ( srum.length() - srum.indexOf( "." ) == 5 ) {
      srum = srum.substring( 0, srum.length() - 1 );
    } // else if

    boolean isAll0 = true;
    for ( int i = 0 ; i < srum.length() ; i++ ) {
      if ( srum.charAt( i ) >= '1' && srum.charAt( i ) <= '9' )
        isAll0 = false;
    } // for

    if ( isAll0 )
      ; // return "0";

    return srum;

  } // RegularFloat()

  private boolean AllIs0( String srum ) throws Throwable {
    boolean isAll0 = true;
    for ( int i = 0 ; i < srum.length() ; i++ ) {
      if ( srum.charAt( i ) >= '1' && srum.charAt( i ) <= '9' )
        isAll0 = false;
    } // for

    return isAll0;

  } // AllIs0()

  private String EquRegularFloat( String srum ) throws Throwable {

    if ( srum.length() - srum.indexOf( "." ) == 3 ) {
      srum = srum + "0";
    } // if
    else if ( srum.length() - srum.indexOf( "." ) == 2 ) {
      srum = srum + "00";
    } // else if
    else if ( srum.length() - srum.indexOf( "." ) == 1 ) {
      srum = srum + "000";
    } // else if
    else if ( srum.length() - srum.indexOf( "." ) == 5 ) {
      srum = srum.substring( 0, srum.indexOf( "." ) + 4 );
    } // else if

    int indexp = srum.indexOf( "." );

    if ( srum.contains( "+" ) )
      srum.replace( "+", "" );

    try {
      if ( srum.charAt( indexp ) == '.' && srum.charAt( indexp + 1 ) == '0' &&
           srum.charAt( indexp + 2 ) == '0' && srum.charAt( indexp + 3 ) == '0' )
        srum = srum.substring( 0, indexp );

    } // try
    catch ( Throwable throwable2 ) {

    } // catch

    boolean isAll0 = true;
    for ( int i = 0 ; i < srum.length() ; i++ ) {
      if ( srum.charAt( i ) >= '1' && srum.charAt( i ) <= '9' )
        isAll0 = false;
    } // for

    if ( isAll0 )
      ; // return "0";

    return srum;

  } // EquRegularFloat()

  private void CalculteCommand( Vector<ATOM> calmstament, int starIndex ) throws Throwable {

    try {

      int firstParent = - 1;
      for ( int i = starIndex ; i < calmstament.size() ; i++ ) {

        if ( calmstament.get( i ).mtype == Global.s_T_LEFT_PAREN ||
             calmstament.get( i ).mtype == Global.s_T_RIGHT_PAREN ) {
          firstParent = calmstament.get( i ).mtype;
          i = calmstament.size();
        } // if

      } // for

      if ( firstParent == 1 ) {

        for ( int i = starIndex ; i < calmstament.size() ; i++ ) {

          if ( calmstament.get( i ).mtype == Global.s_T_LEFT_PAREN ) {
            CalculteCommand( calmstament, i + 1 );
            calmstament.remove( i );
            // mstament.add(i, new ATOM(result.toString(), 7));
          } // if

        } // for

      } // if

      if ( FindATOM( calmstament, 2 ) > - 1 ) {

        for ( int i = starIndex ; i < calmstament.size() ; i++ ) {

          if ( calmstament.get( i ).mtype == Global.s_T_RIGHT_PAREN ) {
            calmstament.remove( i );
            Vector<ATOM> formal = CopymStament( calmstament, starIndex, i - 1 );
            RemovemStament( calmstament, starIndex, i - 1 );
            calmstament.add( starIndex, Calculator( formal ) );
            i = calmstament.size();
          } // if

        } // for

      } // if

      if ( FindATOM( calmstament, 1 ) ==
           - 1 && FindATOM( calmstament, 2 ) == - 1 ) {
        Vector<ATOM> processcalmstament = calmstament;
        calmstament = new Vector<ATOM>();
        calmstament.add( Calculator( processcalmstament ) );
      } // if

    } // try
    catch ( Throwable throwable ) {
      throw new Throwable();
    } // catch

  } // CalculteCommand()

  private ATOM Calculator( Vector<ATOM> formal ) throws Throwable {

    try {
      ProcessSIGN( formal );

      while ( FindATOM( formal, "*" ) > - 1 ) {

        int findIndex = FindATOM( formal, "*" );


        formal.get( findIndex - 1 ).mstamStr = new String( Compute( formal.get( findIndex - 1 ).mstamStr,
                                                                    formal.get( findIndex + 1 ).mstamStr,
                                                                    formal.get( findIndex ).mstamStr ) );

        formal.get( findIndex - 1 ).mtype = 7;
        formal.remove( findIndex );
        formal.remove( findIndex );

      } // while

      while ( FindATOM( formal, "/" ) > - 1 ) {

        int findIndex = FindATOM( formal, "/" );


        formal.get( findIndex - 1 ).mstamStr = new String( Compute( formal.get( findIndex - 1 ).mstamStr,
                                                                    formal.get( findIndex + 1 ).mstamStr,
                                                                    formal.get( findIndex ).mstamStr ) );
        formal.get( findIndex - 1 ).mtype = 7;
        formal.remove( findIndex );
        formal.remove( findIndex );

      } // while

      while ( formal.size() > 2 ) {

        if ( formal.get( 0 ).mtype == Global.s_T_ID ) {

          if ( FindVariable( formal.get( 0 ).mstamStr ) > - 1 ) {
            int findIndex = FindVariable( formal.get( 0 ).mstamStr );
            formal.get( 0 ).mstamStr = mvariables.get( findIndex ).mvalue;
            formal.get( 0 ).mtype = 7;
          } // if
          else {
            System.out.println( "Undefined identifier : '" + formal.get( 0 ).mstamStr + "'" );
            throw new Throwable();
          } // else

        } // if

        if ( formal.get( 2 ).mtype == Global.s_T_ID ) {

          if ( FindVariable( formal.get( 2 ).mstamStr ) > - 1 ) {
            int findIndex = FindVariable( formal.get( 2 ).mstamStr );
            formal.get( 2 ).mstamStr = mvariables.get( findIndex ).mvalue;
            formal.get( 2 ).mtype = 7;
          } // if
          else {
            System.out.println( "Undefined identifier : '" + formal.get( 2 ).mstamStr + "'" );
            throw new Throwable();
          } // else

        } // if

        formal.get( 0 ).mstamStr = new String( Compute( formal.get( 0 ).mstamStr,
                                                        formal.get( 2 ).mstamStr,
                                                        formal.get( 1 ).mstamStr ) );
        formal.get( 0 ).mtype = 7;
        formal.remove( 1 );
        formal.remove( 1 );

      } // while

      if ( formal.size() == 1 && formal.get( 0 ).mtype == Global.s_T_ID ) {

        int isDefined = FindVariable( formal.get( 0 ).mstamStr );

        if ( isDefined > - 1 ) {
          formal.get( 0 ).mtype = 7;
          formal.get( 0 ).mstamStr = mvariables.get( isDefined ).mvalue;
        } // if
        else {
          System.out.println( "Undefined identifier : '" + formal.get( 0 ).mstamStr + "'" );
          throw new Throwable();
        } // else

      } // if


      return formal.get( 0 );

    } // try
    catch ( Throwable throwable ) {
      throw new Throwable();
    } // catch

  } // Calculator()

  private void ProcessSIGN( Vector<ATOM> stament ) throws Throwable {
    try {

      if ( stament.size() > 0 ) {

        if ( stament.get( 0 ).mstamStr.equals( "+" ) ||
             stament.get( 0 ).mstamStr.equals( "-" ) ) {

          if ( stament.get( 0 ).mstamStr.equals( "-" ) && stament.get( 1 ).mtype == Global.s_T_NUM &&
               ! AllIs0( stament.get( 1 ).mstamStr ) ) {

            if ( stament.get( 1 ).mstamStr.contains( "-" ) && ! AllIs0( stament.get( 1 ).mstamStr ) ) {
              stament.get( 1 ).mstamStr.replace( "-", "" );
            } // if
            else if ( stament.get( 1 ).mstamStr.contains( "+" ) ) {
              stament.get( 1 ).mstamStr.replace( "+", "-" );
            } // else if
            else
              stament.get( 1 ).mstamStr = "-" + stament.get( 1 ).mstamStr;

            stament.remove( 0 );

          } // if
          else if ( stament.get( 0 ).mstamStr.equals( "+" ) &&
                    stament.get( 1 ).mtype == Global.s_T_NUM &&
                    ! AllIs0( stament.get( 1 ).mstamStr ) )
            stament.remove( 0 );
          else if ( AllIs0( stament.get( 1 ).mstamStr ) )
            stament.remove( 0 );

        } // if

      } // if

      for ( int i = 0 ; i < stament.size() ; i++ ) {

        if ( stament.get( i ).mtype == Global.s_T_LEFT_PAREN &&
             ( ( stament.get( i + 1 ).mstamStr.equals( "+" ) ||
                 stament.get( i + 1 ).mstamStr.equals( "-" ) ) ) ) {

          if ( stament.get( i + 1 ).mstamStr.equals( "-" ) &&
               stament.get( i + 2 ).mtype == Global.s_T_NUM &&
               ! AllIs0( stament.get( i + 2 ).mstamStr ) ) {

            if ( stament.get( i + 2 ).mstamStr.contains( "-" ) ) {
              stament.get( i + 2 ).mstamStr.replace( "-", "" );
            } // if
            else if ( stament.get( i + 2 ).mstamStr.contains( "+" ) ) {
              stament.get( i + 2 ).mstamStr.replace( "+", "-" );
            } // else if
            else if ( ! stament.get( i + 2 ).mstamStr.equals( "0" ) ) {
              stament.get( i + 2 ).mstamStr = "-" + stament.get( i + 2 ).mstamStr;
            } // else if

            stament.remove( i + 1 );

          } // if
          else if ( stament.get( i + 1 ).mstamStr.equals( "+" ) &&
                    stament.get( i + 2 ).mtype == Global.s_T_NUM &&
                    ! AllIs0( stament.get( i + 2 ).mstamStr ) )
            stament.remove( i + 1 );
          else if ( stament.get( i + 2 ).mtype == Global.s_T_NUM &&
                    AllIs0( stament.get( i + 2 ).mstamStr ) )
            stament.remove( i + 1 );

        } // if
        else if ( stament.get( i ).mtype == Global.s_T_OPERATOR &&
                  ( ( stament.get( i + 1 ).mstamStr.equals( "+" ) ||
                      stament.get( i + 1 ).mstamStr.equals( "-" ) ) ) ) {

          if ( stament.get( i + 1 ).mstamStr.equals( "-" ) &&
               stament.get( i + 2 ).mtype == Global.s_T_NUM &&
               ! AllIs0( stament.get( i + 2 ).mstamStr ) ) {

            if ( stament.get( i + 2 ).mstamStr.contains( "-" ) ) {
              stament.get( i + 2 ).mstamStr.replace( "-", "" );
            } // if
            else if ( stament.get( i + 2 ).mstamStr.contains( "+" ) ) {
              stament.get( i + 2 ).mstamStr.replace( "+", "-" );
            } // else if
            else if ( ! stament.get( i + 2 ).mstamStr.equals( "0" ) ) {
              stament.get( i + 2 ).mstamStr = "-" + stament.get( i + 2 ).mstamStr;
            } // else if

            stament.remove( i + 1 );

          } // if
          else if ( stament.get( i + 1 ).mstamStr.equals( "+" ) &&
                    stament.get( i + 2 ).mtype == Global.s_T_NUM &&
                    ! AllIs0( stament.get( i + 2 ).mstamStr ) )
            stament.remove( i + 1 );
          else if ( stament.get( i + 2 ).mtype == Global.s_T_NUM &&
                    AllIs0( stament.get( i + 2 ).mstamStr ) )
            stament.remove( i + 1 );

        } // else if

      } // for

    } // try
    catch ( Throwable throwable ) {

    } // catch

    try {
      for ( int i = 0 ; i < stament.size() ; i++ ) {
        if ( stament.get( i ).mtype == Global.s_T_NUM ) {

          if ( stament.get( i ).mstamStr.contains( "." ) ) {
            Double rv = Double.parseDouble( stament.get( i ).mstamStr );
            stament.get( i ).mstamStr = String.format( "%.10f", rv );

          } // if


          int rv = Integer.valueOf( stament.get( i ).mstamStr ).intValue();
          stament.get( i ).mstamStr = Integer.toString( rv );
        } // if

      } // for

    } // try
    catch ( Throwable throwable ) {

    } // catch

  } // ProcessSIGN()

  private String Compute( String mainNum, String secendNum, String operator ) throws Throwable {

    if ( operator.equals( "/" ) && secendNum.equals( "0" ) ) {
      System.out.println( "Error" );
      throw new Throwable();
    } // if

    if ( FindVariable( mainNum ) > - 1 )
      mainNum = mvariables.get( FindVariable( mainNum ) ).mvalue;

    if ( FindVariable( secendNum ) > - 1 )
      secendNum = mvariables.get( FindVariable( secendNum ) ).mvalue;

    if ( mainNum.contains( "." ) || secendNum.contains( "." ) ) {
      double mainnum = 0;
      double secendnum = 0;

      try {
        mainnum = Double.parseDouble( mainNum );
      } // try
      catch ( Throwable throwable ) {
        System.out.println( "Undefined identifier : '" + mainNum + "'" );
        throw new Throwable();
      } // catch

      try {
        secendnum = Double.parseDouble( secendNum );
      } // try
      catch ( Throwable throwable ) {
        System.out.println( "Undefined identifier : '" + secendNum + "'" );
        throw new Throwable();
      } // catch

      return FloatCompute( mainnum, secendnum, operator );

    } // if
    else {
      int mainnum = 0;
      int secendnum = 0;
      try {
        mainnum = Integer.valueOf( mainNum ).intValue();
      } // try
      catch ( Throwable throwable ) {
        System.out.println( "Undefined identifier : '" + mainNum + "'" );
        throw new Throwable();
      } // catch

      try {
        secendnum = Integer.valueOf( secendNum ).intValue();
      } // try
      catch ( Throwable throwable ) {
        System.out.println( "Undefined identifier : '" + secendNum + "'" );
        throw new Throwable();
      } // catch

      String rs = IntCompute( mainnum, secendnum, operator );
      return rs;

    } // else

  } // Compute()

  private String IntCompute( int mainnum, int secendnum, String operator ) throws Throwable {

    if ( operator.equals( "+" ) ) {
      mainnum = mainnum + secendnum;
    } // if
    else if ( operator.equals( "-" ) ) {
      mainnum = mainnum - secendnum;
    } // else if
    else if ( operator.equals( "*" ) ) {
      mainnum = mainnum * secendnum;
    } // else if
    else if ( operator.equals( "/" ) ) {

      if ( mainnum % secendnum != 0 ) {
        double fmainnum = mainnum;
        double fsecendnum = secendnum;

        fmainnum = fmainnum / fsecendnum;

        String rs = String.format( "%.10f", fmainnum );

        return rs;

      } // if

      else {
        mainnum = mainnum / secendnum;
      } // else

    } // else if

    return Integer.toString( mainnum );
  } // IntCompute()

  private String FloatCompute( double mainnum, double secendnum, String operator ) throws Throwable {

    if ( operator.equals( "+" ) ) {
      mainnum = mainnum + secendnum;
    } // if
    else if ( operator.equals( "-" ) ) {
      mainnum = mainnum - secendnum;
    } // else if
    else if ( operator.equals( "*" ) ) {
      mainnum = mainnum * secendnum;
    } // else if
    else if ( operator.equals( "/" ) ) {
      mainnum = mainnum / secendnum;
    } // else if

    return String.format( "%.10f", mainnum );

  } // FloatCompute()

  private boolean Compare( ATOM oleftNum, ATOM orightNum, String operator ) throws Throwable {

    if ( operator.equals( "=" ) ) {
      return IsEqu( oleftNum, orightNum );

    } // if
    else if ( operator.equals( ">" ) ) {
      return IsBigger( oleftNum, orightNum );

    } // else if
    else if ( operator.equals( "<" ) ) {
      return IsSmaller( oleftNum, orightNum );

    } // else if
    else if ( operator.equals( ">=" ) ) {
      if ( IsEqu( oleftNum, orightNum ) || IsBigger( oleftNum, orightNum ) )
        return true;
      else
        return false;

    } // else if
    else if ( operator.equals( "<=" ) ) {
      if ( IsEqu( oleftNum, orightNum ) || IsSmaller( oleftNum, orightNum ) )
        return true;
      else
        return false;

    } // else if

    return false;

  } // Compare()

  private boolean IsEqu( ATOM oleftNum, ATOM orightNum ) throws Throwable {
    String rightNum;
    String leftNum;
    if ( orightNum.mstamStr.contains( "." ) )
      rightNum = new String( EquRegularFloat( orightNum.mstamStr ) );
    else
      rightNum = new String( orightNum.mstamStr );

    if ( oleftNum.mstamStr.contains( "." ) )
      leftNum = new String( EquRegularFloat( oleftNum.mstamStr ) );
    else
      leftNum = new String( oleftNum.mstamStr );

    if ( rightNum.equals( leftNum ) )
      return true;
    else
      return false;

  } // IsEqu()

  private boolean IsBigger( ATOM oleftNum, ATOM orightNum ) throws Throwable {
    String rightNum = new String( orightNum.mstamStr );
    String leftNum = new String( oleftNum.mstamStr );

    if ( orightNum.mstamStr.contains( "." ) )
      rightNum = new String( EquRegularFloat( orightNum.mstamStr ) );

    if ( oleftNum.mstamStr.contains( "." ) )
      leftNum = new String( EquRegularFloat( oleftNum.mstamStr ) );

    if ( orightNum.mstamStr.contains( "." ) || oleftNum.mstamStr.contains( "." ) ) {

      float fnum1 = Float.parseFloat( leftNum );
      float fnum2 = Float.parseFloat( rightNum );

      if ( fnum1 > fnum2 )
        return true;
      else
        return false;
    } // if

    else {
      int nnum1 = Integer.parseInt( leftNum );
      int nnum2 = Integer.parseInt( rightNum );

      if ( nnum1 > nnum2 )
        return true;

      else
        return false;
    } // else

  } // IsBigger()

  private boolean IsSmaller( ATOM oleftNum, ATOM orightNum ) throws Throwable {
    String rightNum = new String( orightNum.mstamStr );
    String leftNum = new String( oleftNum.mstamStr );

    if ( orightNum.mstamStr.contains( "." ) )
      rightNum = new String( EquRegularFloat( orightNum.mstamStr ) );

    if ( oleftNum.mstamStr.contains( "." ) )
      leftNum = new String( EquRegularFloat( oleftNum.mstamStr ) );

    if ( orightNum.mstamStr.contains( "." ) || oleftNum.mstamStr.contains( "." ) ) {

      float fnum1 = Float.parseFloat( leftNum );
      float fnum2 = Float.parseFloat( rightNum );

      if ( fnum1 < fnum2 )
        return true;
      else
        return false;

    } // if

    else {
      int nnum1 = Integer.parseInt( leftNum );
      int nnum2 = Integer.parseInt( rightNum );

      if ( nnum1 < nnum2 )
        return true;

      else
        return false;
    } // else

  } // IsSmaller()


  private String BecomeToSameLength1( String str1, String str2 ) throws Throwable {

    int strl1 = 0;
    int strl2 = 0;
    int samelength = 0;

    if ( str1.contains( "-" ) ) {
      String str = str1.replace( "-", "" );
      strl1 = str.length();
    } // if
    else {
      strl1 = str1.length();
    } // else

    if ( str2.contains( "-" ) ) {
      String str = str2.replace( "-", "" );
      strl2 = str.length();
    } // if
    else {
      strl2 = str2.length();
    } // else

    if ( strl1 == strl2 )
      return str1;

    if ( strl1 > strl2 ) {
      samelength = strl1;
    } // if
    else {
      samelength = strl2;
    } // else

    if ( str1.contains( "-" ) ) {
      str1.replace( "-", "" );
      String str = new String();
      for ( int i = 0 ; i < samelength - strl1 ; i++ )
        str = str + "0";

      str1 = "-" + str + str1;
    } // if
    else {
      String str = new String();
      for ( int i = 0 ; i < samelength - strl1 ; i++ )
        str = str + "0";

      str1 = str + str1;
    } // else

    if ( str2.contains( "-" ) ) {
      str2.replace( "-", "" );
      String str = new String();
      for ( int i = 0 ; i < samelength - strl2 ; i++ )
        str = str + "0";

      str2 = "-" + str + str2;
    } // if
    else {
      String str = new String();
      for ( int i = 0 ; i < samelength - strl2 ; i++ )
        str = str + "0";

      str2 = str + str2;
    } // else

    return str1;

  } // BecomeToSameLength1()

  private String BecomeToSameLength2( String str1, String str2 ) throws Throwable {

    int strl1 = 0;
    int strl2 = 0;
    int samelength = 0;

    if ( str1.contains( "-" ) ) {
      String str = str1.replace( "-", "" );
      strl1 = str.length();
    } // if
    else {
      strl1 = str1.length();
    } // else

    if ( str2.contains( "-" ) ) {
      String str = str2.replace( "-", "" );
      strl2 = str.length();
    } // if
    else {
      strl2 = str2.length();
    } // else

    if ( strl1 == strl2 )
      return str2;

    if ( strl1 > strl2 ) {
      samelength = strl1;
    } // if
    else {
      samelength = strl2;
    } // else

    if ( str1.contains( "-" ) ) {
      str1.replace( "-", "" );
      String str = new String();
      for ( int i = 0 ; i < samelength - strl1 ; i++ )
        str = str + "0";

      str1 = "-" + str + str1;
    } // if
    else {
      String str = new String();
      for ( int i = 0 ; i < samelength - strl1 ; i++ )
        str = str + "0";

      str1 = str + str1;
    } // else

    if ( str2.contains( "-" ) ) {
      str2.replace( "-", "" );
      String str = new String();
      for ( int i = 0 ; i < samelength - strl2 ; i++ )
        str = str + "0";

      str2 = "-" + str + str2;
    } // if
    else {
      String str = new String();
      for ( int i = 0 ; i < samelength - strl2 ; i++ )
        str = str + "0";

      str2 = str + str2;
    } // else

    return str2;

  } // BecomeToSameLength2()

  // -----------------------Below is three general commend.-------------------------

  private void AssignCommend() throws Throwable {

    if ( FindVariable( mstament.get( 0 ).mstamStr ) > - 1 ) { // if this var has been defined.
      int varIndex = FindVariable( mstament.get( 0 ).mstamStr );
      ATOM result;
      Vector<ATOM> vecResult = CopymStament( FindATOM( 8 ) + 1, mstament.size() - 1 );
      CalculteCommand( vecResult, 0 );
      result = vecResult.get( 0 );
      mvariables.get( varIndex ).mvalue = result.mstamStr;
      PrintVariable( result );
    } // if
    else { // if this var hasn't been defined.
      ATOM result;
      Vector<ATOM> vecResult = CopymStament( FindATOM( 8 ) + 1, mstament.size() - 1 );
      CalculteCommand( vecResult, 0 );
      result = vecResult.get( 0 );
      mvariables.add( new Variable( mstament.get( 0 ).mstamStr, result.mstamStr ) );
      PrintVariable( result );
    } // else

  } // AssignCommend()

  private void BooleanCommend() throws Throwable {

    int booleanOperIndex = FindATOM( 3 );
    ATOM rightResult = null;
    ATOM leftResult = null;

    Vector<ATOM> leftFomal = CopymStament( 0, booleanOperIndex - 1 );
    CalculteCommand( leftFomal, 0 );
    leftResult = leftFomal.get( 0 );

    Vector<ATOM> rightFomal = CopymStament( booleanOperIndex + 1, mstament.size() - 1 );
    CalculteCommand( rightFomal, 0 );
    rightResult = rightFomal.get( 0 );

    if ( Compare( leftResult, rightResult, mstament.get( booleanOperIndex ).mstamStr ) ) {
      System.out.println( "true" );
    } // if
    else {
      System.out.println( "false" );
    } // else

  } // BooleanCommend()

  private void JustFormal() throws Throwable {

    Vector<ATOM> result = CopymStament( mstament, 0, mstament.size() - 1 );
    CalculteCommand( result, 0 );
    PrintVariable( result.get( 0 ) );

  } // JustFormal()

} // class ProcessComment

class Main {

  public static void main( String[] args ) throws Throwable {

    // Test test = new Test();
    // test.test();
    Scanner sc = new Scanner( System.in );
    Vector<Variable> variables = new Vector<Variable>();
    CutToken cutToken = new CutToken( sc, variables );
    Vector<ATOM> stament = new Vector<ATOM>();
    boolean isExit = false;

    String nownum = sc.nextLine();

    if ( nownum.contains( "3" ) )
      ; // System.out.println( stament.get( 0 ).mstamStr );

    System.out.println( "Program starts..." );

    while ( cutToken.Cutting( stament ) ) {

      if ( stament.size() > 0 ) {

        if ( stament.size() != 1 && stament.get( 0 ).mstamStr.equals( ";" ) ) {
          System.out.print( "Program exits..." );
          return;
        } // if

        Paser parser = new Paser( stament );

        if ( parser.GrammarParser() ) {
          ProcessComment processComment = new ProcessComment( stament, variables );
          processComment.ExcuteComment();
        } // if

        stament = new Vector<ATOM>();


      } // if

      stament = new Vector<ATOM>();

    } // while()

    System.out.print( "Program exits..." );

  } // main()

} // class Main