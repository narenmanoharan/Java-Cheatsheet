package DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Naren on 6/3/17.
 */
public class LocalDateTimeTest {

  private LocalDateTimeDemo dateTimeDemo;

  @Before
  public void before() {
    dateTimeDemo = new LocalDateTimeDemo();
  }

  @Test
  public void myBirthdayTest() {

    final LocalDate myBirthday = dateTimeDemo.myBirthday();

    assertEquals(1993, myBirthday.getYear());
    assertEquals(20, myBirthday.getDayOfMonth());
    assertEquals(9, myBirthday.getMonthValue());
  }

  @Test
  public void myBirthdayTimeTest() {

    final LocalTime myBirthdayTime = dateTimeDemo.myBirthdayTime();

    assertEquals(5, myBirthdayTime.getHour());
    assertEquals(20, myBirthdayTime.getMinute());
  }

  @Test public void timeNowTest() {

    final LocalDateTime now = dateTimeDemo.timeNow();

    assertEquals(LocalDateTime.now().getHour(), now.getHour());
  }

  @Test public void getDifferenceParisLondonTest() {

    final int diff = dateTimeDemo.getDiffBetweenParisLondon();

    assertEquals(0, diff);
  }

  @Test public void myBirthdayDateTimeTest() throws Exception {

    final LocalDateTime myBirthdayDateTime = dateTimeDemo.myBirthdayDateTime();

    assertEquals(20, myBirthdayDateTime.getDayOfMonth());
    assertEquals(1993, myBirthdayDateTime.getYear());

  }
}
