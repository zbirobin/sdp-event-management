package ch.epfl.sweng.eventmanager.repository.data;

import android.os.Parcel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Louis Vialar
 */
public class EventTicketingConfigurationTest {
    private EventTicketingConfiguration configuration = new EventTicketingConfiguration("login", "config", "scan");

    private static Parcel getParcel() {
        return Parcel.obtain();
    }

    @Test
    public void parcelConversion() {
        Parcel target = getParcel();
        configuration.writeToParcel(target, 0);

        target.setDataPosition(0);


        assertEquals("login", target.readString());
        assertEquals("config", target.readString());
        assertEquals("scan", target.readString());
        target.setDataPosition(0);

        EventTicketingConfiguration created = EventTicketingConfiguration.CREATOR.createFromParcel(target);
        assertEquals(configuration, created);

        // Edge case: null fields
        EventTicketingConfiguration conf = new EventTicketingConfiguration();

        target = getParcel();
        conf.writeToParcel(target, 0);
        target.setDataPosition(0);

        for (int i = 0; i < 3; ++i)
            assertNull(target.readString());
        target.setDataPosition(0);

        created = EventTicketingConfiguration.CREATOR.createFromParcel(target);
        assertEquals(conf, created);
    }

}