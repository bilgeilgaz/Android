package com.bignerdranch.android.bikeShareGPS;

/**
 * used to parse network input in this format
 * rides:{what,start,end:}*
 * where what, start and end are strings
 */

public class RideParse {
  public static String stringToken(String s, String delim) {
    int cPos=s.indexOf(delim);
    return s.substring(0, cPos);
  }

  public static String stringSkip(String s, String token){
    int cPos= s.indexOf(token);
    return s.substring(cPos+token.length());
  }
}
