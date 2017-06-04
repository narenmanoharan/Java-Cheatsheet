package DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Naren on 6/3/17.
 */
public class LocalDateTimeDemo {

  public LocalDate myBirthday() {
    return LocalDate.of(1993, Month.SEPTEMBER, 20);
  }

  public LocalTime myBirthdayTime() {
    return LocalTime.of(5, 20);
  }

  public LocalDateTime myBirthdayDateTime() {
    return LocalDateTime.of(myBirthday(), myBirthdayTime());
  }

  public LocalDateTime timeNow() {
    return LocalDateTime.now();
  }

  public int getDiffBetweenParisLondon() {

    ZonedDateTime paris = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
    ZonedDateTime london = ZonedDateTime.now(ZoneId.of("Europe/London"));

    return paris.getDayOfMonth() - london.getDayOfMonth();

  }
}
