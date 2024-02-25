package domain;

import static types.Constants.NO_TASK_TO_PROCESS;

public record Task(long id, double size, double tArrive) {

  public boolean isEmpty() {
    return size == NO_TASK_TO_PROCESS.getValue();
  }
}
