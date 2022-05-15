package upt.ac.cti.util.computation;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class Either<L, R> {
  public static <L, R> Either<L, R> left(L value) {
    return new Either<>(Optional.of(value), Optional.empty());
  }

  public static <L, R> Either<L, R> right(R value) {
    return new Either<>(Optional.empty(), Optional.of(value));
  }

  private final Optional<L> left;
  private final Optional<R> right;

  private Either(Optional<L> l, Optional<R> r) {
    left = l;
    right = r;
  }

  public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc) {
    return new Either<>(left.map(lFunc), right);
  }

  public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
    return new Either<>(left, right.map(rFunc));
  }

  public boolean isRight() {
    return right.isPresent();
  }

  public boolean isLeft() {
    return left.isPresent();
  }

  public R getRight() {
    return right.get();
  }

  public L getLeft() {
    return left.get();
  }

  @Override
  public String toString() {
    if (right.isPresent()) {
      return "Right(" + right.get() + ")";
    }

    return "Left(" + left.get() + ")";
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Either<?, ?> other)) {
      return false;
    }
    return Objects.equals(left, other.left) && Objects.equals(right, other.right);
  }



}
