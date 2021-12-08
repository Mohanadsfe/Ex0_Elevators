package ex0;

import java.util.Comparator;

public class SpeedCompare implements Comparator<Elevator> {

    public int compare(Elevator m1, Elevator m2) {
        int ans = 0;
        if (m1.getSpeed() > m2.getSpeed())
            ans = 1;
        else if (m1.getSpeed() < m2.getSpeed())
            ans = -1;


        return ans;
    }

}