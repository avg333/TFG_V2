package distribution;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Distribution {

  private final Random rand = new Random();
  private final DistributionMode distributionMode;
  private final double param1;
  private final double param2;

  public final void setSeed(final long seed) {
    rand.setSeed(seed);
  }

  public final double getRandom() {
    return switch (distributionMode) {
      case DETERMINISTIC -> param1;
      case UNIFORM -> rand.nextDouble() * (param1 - param2) + param2;
      case EXPONENTIAL -> param1 != 0 ? Math.log(1 - rand.nextDouble()) / (-param1) : 0;
    };
  }
}
