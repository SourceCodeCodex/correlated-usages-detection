package upt.ac.cti.util.time;

import java.time.Duration;
import java.time.Instant;

public class StopWatch {

  private Instant start = Instant.now(), stop = Instant.now();

  public void start() {
    start = Instant.now();
  }

  public void stop() {
    stop = Instant.now();
  }

  public Duration getDuration() {
    return Duration.between(start, stop);
  }

}
