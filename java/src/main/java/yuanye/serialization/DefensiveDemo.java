package yuanye.serialization;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kali on 2014/7/19.
 */
public class DefensiveDemo {

    private static class Period implements Serializable{
        private static final long serialVersionUID = -1784011910342149200L;

        private  Date start;
        private  Date end;

        public Period(Date start,Date end){
            this.start = start;
            this.end = end;
        }

        private void verify() throws InvalidObjectException {
            if (end.before(start)){
                throw new InvalidObjectException("end should not before start");
            }
        }

        private void readObject(java.io.ObjectInputStream in)
                throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            start = new Date(start.getTime());
            end = new Date(end.getTime());
            verify();
        }
    }
}
