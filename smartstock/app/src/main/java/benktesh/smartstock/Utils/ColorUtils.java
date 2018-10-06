package benktesh.smartstock.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import benktesh.smartstock.R;

public class ColorUtils {

    public static int getViewBackGroundColorForStock(Context context, double change) {

        int movement = 0;

        if(change < 0) movement = -1;
        if(change > 0) movement = 1;

        switch (movement) {
            case -1:
                return ContextCompat.getColor(context, R.color.Red);
            case 0:
                return ContextCompat.getColor(context, R.color.Yellow);
            case 1:
                return ContextCompat.getColor(context, R.color.Green);
        }
        return 0;
    }
}
