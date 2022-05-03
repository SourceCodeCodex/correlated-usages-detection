package upt.ac.cti.util.time;

import java.time.Duration;
import java.time.Instant;

public class StopWatch {

  private Instant start = Instant.now(), end = Instant.now();

  public void start() {
    start = Instant.now();
  }

  public void end() {
    end = Instant.now();
  }

  public Duration getDuration() {
    return Duration.between(start, end);
  }

}
