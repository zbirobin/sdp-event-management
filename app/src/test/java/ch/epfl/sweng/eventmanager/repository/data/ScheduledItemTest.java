package ch.epfl.sweng.eventmanager.repository.data;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

public class ScheduledItemTest {

    private final Date when = new Calendar.Builder()
            .setFields(Calendar.YEAR, 2018,
                    Calendar.MONTH, Calendar.DECEMBER,
                    Calendar.DAY_OF_MONTH, 25,
                    Calendar.HOUR_OF_DAY, 12,
                    Calendar.MINUTE, 0,
                    Calendar.SECOND, 0).build().getTime();
    private final String mj = "Michael Jackson";
    private final String pop = "Pop";
    private final String des = "Amazing event as it is the resurrection of the King!";
    private final double dur = 3.5;
    private final UUID uuid = UUID.randomUUID();
    private final String type = "Concert";
    private final String location = "Polyv";

    private ScheduledItem c1 = new ScheduledItem(when, mj, pop, des, -12, uuid, type, location);
    private ScheduledItem c2 = new ScheduledItem(when, mj, pop, des, dur, uuid, type, location);

    @Test
    public void getDate() {
        assertEquals(c2.getDate(), when);
    }

    @Test
    public void getArtist() {
        assertEquals(c2.getArtist(), mj);
    }

    @Test
    public void getDescription() {
        assertEquals(c2.getDescription(), des);
    }

    @Test
    public void getGenre() {
        assertEquals(c2.getGenre(), pop);
    }

    @Test
    public void getDuration() {
        double STANDARD_DURATION = ScheduledItem.STANDARD_DURATION;
        assertEquals(c1.getDuration(), STANDARD_DURATION, 0.0);
        assertEquals(c2.getDuration(), dur, 0.0);
    }

    @Test
    public void equals() {
        assertNotEquals(c1, c2);

        ScheduledItem copy2 = new ScheduledItem(when, mj, pop, des, dur, uuid, type, location);
        assertEquals(c2, copy2);

        ScheduledItem fakeName = new ScheduledItem(when, "Michael Fackson", pop, des, dur, uuid, type, location);
        ScheduledItem fakeGenre = new ScheduledItem(when, mj, "Funky", des, dur, uuid, type, location);
        ScheduledItem fakeDes = new ScheduledItem(when, mj, pop, "Wow!", dur, uuid, type, location);
        ScheduledItem fakeDur = new ScheduledItem(when, mj, pop, des, 5, uuid, type, location);
        ScheduledItem fakeUuid = new ScheduledItem(when, mj, pop, des, 5, UUID.randomUUID(), type, location);
        ScheduledItem fakeType = new ScheduledItem(when, mj, pop, des, 5, uuid, "Something", location);
        ScheduledItem fakeLocation = new ScheduledItem(when, mj, pop, des, 5, uuid, type, "Somewhere");


        assertNotEquals(c2, fakeName);
        assertNotEquals(c2, fakeGenre);
        assertNotEquals(c2, fakeDes);
        assertNotEquals(c2, fakeDur);
        assertNotEquals(c2, fakeUuid);
        assertNotEquals(c2, fakeType);
        assertNotEquals(c2, fakeLocation);
    }

    @Test
    public void dateAsString() {
        SimpleDateFormat f = new SimpleDateFormat("dd MMMM yyyy 'at' kk'h'mm");
        String now_s = f.format(when);
        assertEquals(now_s, c2.dateAsString());
    }

    @Test
    public void getEndOfScheduledItem() {
         Date end = new Date(when.getYear(), when.getMonth(), when.getDate(), when.getHours()+3, when.getMinutes() +30);
         assertEquals(setPrecisionToMinutes(end), setPrecisionToMinutes(c2.getEndOfConcert()));
    }

    @Test
    public void getStatus() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2999);
        ScheduledItem fake1 = new ScheduledItem(c.getTime(), mj, pop, des, dur, uuid, type, location);
        c.set(Calendar.YEAR, 1970);
        ScheduledItem fake2 = new ScheduledItem(c.getTime(), mj, pop, des, dur, uuid, type, location);

        System.out.print(fake1.dateAsString());
        assertEquals(ScheduledItem.ScheduledItemStatus.NOT_STARTED, fake1.getStatus());
        assertEquals(ScheduledItem.ScheduledItemStatus.PASSED, fake2.getStatus());
        assertEquals(ScheduledItem.ScheduledItemStatus.IN_PROGRESS, c2.getStatus());
    }

    @Test
    public void testWriteAsIcalendar() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(outContent);

        c2.printAsIcalendar(stream);
        String expected =
                "BEGIN:VEVENT\n" +
                        "SUMMARY:" + mj + "\n" +
                        "DTSTART;VALUE=DATE-TIME:20181225T120000\n" +
                        "DTEND;VALUE=DATE-TIME:20181225T153000\n" +
                        "LOCATION:" + location + "\n" +
                        "END:VEVENT\n";

        assertEquals(expected, outContent.toString());

    }

    /**
     * Erases seconds precision from the String
     * @param date String with format DDDD MMMM dd HH:mm:ss GMT yyyy
     * @return the broader precision string
     */
    public String setPrecisionToMinutes(Date date) {
        String dateS = date.toString();
        String[] splitted = dateS.split(" ");
        String[] times = splitted[3].split(":");
        splitted[3] = times[0] + ":" + times[1];

        StringBuilder builder = new StringBuilder();
        for (String part : splitted)
            builder.append(part).append(" ");

        String ret = builder.toString();
        return ret.substring(0, ret.length() - 1); // drop last space
    }


}
