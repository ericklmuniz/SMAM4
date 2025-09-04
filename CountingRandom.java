import java.util.*;

public class CountingRandom {
    private final Random delegate;
    private final long maxDraws;
    private long used = 0;

    public CountingRandom(long seed, long maxDraws) {
        this.delegate = new Random(seed);
        this.maxDraws = maxDraws;
    }

    public boolean canDraw() {
        return used < maxDraws;
    }

    public double nextDouble() {
        if (!canDraw()) {
            throw new IllegalStateException("Limite de aleatórios atingido: " + maxDraws);
        }
        used++;
        return delegate.nextDouble();
    }

    public long getUsed() { return used; }
    public long getMaxDraws() { return maxDraws; }
}
