/**
 * Created by eason on 16-11-4.
 */
class Utils{
    static giveMeChar(keycode, keyboardEvent) {
        var resultChar = '',
            charRelated = String.fromCharCode(keycode);

        if ((keycode>=48) && (keycode<=57)
            || (keycode>=65) && (keycode<=90)
            || (keycode>=97) && (keycode<=122)) {

            resultChar = charRelated;

            if (keyboardEvent.shiftKey) {
                switch (keycode) {
                    case 57:
                        resultChar = '(';
                        break;
                    case 48:
                        resultChar = ')';
                        break;
                }
            } else if(keyboardEvent.ctrlKey){
                switch (keycode){
                    case 85:
                        resultChar = '$${Clear}';
                        break;
                }
            } else {
                resultChar = resultChar.toLowerCase();
            }
        } else {
            switch (keycode) {
                case 32:
                    resultChar = " ";
                    break;
                case 47:
                    resultChar = "/";
                    break;
                case 126:
                    resultChar = "~";
                    break;
                case 189:
                    resultChar = "-";
                    break;
                case 190:
                    resultChar = ".";
                    break;
                case 191:
                    resultChar = "/";
                    break;
                case 220:
                    resultChar = "\\";
                    break;
                case 222:
                    resultChar = "'";
                    break;
                default:
                    resultChar = "";
                    break;
            }
        }

        return resultChar;
    }
}