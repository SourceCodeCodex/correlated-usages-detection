package upt.ac.cti.util.computation;

import java.util.ArrayList;
import java.util.List;

public class TokensUtil {

  public static List<String> splitCamelCase(String str) {
    if (str == null) {
      return null;
    }
    if (str.length() == 0) {
      return new ArrayList<>();
    }
    var c = str.toCharArray();
    var list = new ArrayList<String>();
    var tokenStart = 0;
    var currentType = Character.getType(c[tokenStart]);
    for (var pos = tokenStart + 1; pos < c.length; pos++) {
      var type = Character.getType(c[pos]);
      if (type == currentType) {
        continue;
      }
      if (type == Character.LOWERCASE_LETTER
          && currentType == Character.UPPERCASE_LETTER) {
        var newTokenStart = pos - 1;
        if (newTokenStart != tokenStart) {
          list.add(new String(c, tokenStart, newTokenStart - tokenStart));
          tokenStart = newTokenStart;
        }
      } else {
        list.add(new String(c, tokenStart, pos - tokenStart));
        tokenStart = pos;
      }
      currentType = type;
    }
    list.add(new String(c, tokenStart, c.length - tokenStart));
    return list;
  }
}
