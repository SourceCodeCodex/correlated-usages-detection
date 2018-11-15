package utils;

public class Pair<F, S> {
	private final F _1;
	private final S _2;

	public Pair(F _1, S _2) {
		this._1 = _1;
		this._2 = _2;
	}

	public F getFirst() {
		return _1;
	}

	public S getSecond() {
		return _2;
	}

	public Pair<S, F> swap() {
		return new Pair<S, F>(_2, _1);
	}

	public <T> Pair<T, S> setFirst(T t) {
		return new Pair<T, S>(t, _2);
	}

	public <T> Pair<F, T> setSecond(T t) {
		return new Pair<F, T>(_1, t);
	}
}
