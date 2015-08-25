package rx;

import java.util.concurrent.TimeUnit;
import rx.util.functions.Action1;

/**
 *
 * @author Burgy Benjamin
 */
public class FxUI extends Scheduler
{
    @Override
    public Subscription schedule(Action1<Inner> actn)
    {
        return new Subscription()
        {
            @Override
            public void unsubscribe()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isUnsubscribed()
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    public Subscription schedule(Action1<Inner> actn, long l, TimeUnit tu)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
