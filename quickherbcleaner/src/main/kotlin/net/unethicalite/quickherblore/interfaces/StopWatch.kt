package net.unethicalite.quickherblore.interfaces

/**
 * A simple java stopwatch
 */
class StopWatch
/**
 * Default Constructor
 */
{
    /**
     * Determines if the Stopwatch is running (could be paused)
     *
     * @return Whether the stopwatch is currently running
     */
    /**
     * Whether the stopwatch is running
     */
    var isRunning = false
        private set
    /**
     * Whether this stopwatch is paused
     *
     * @return true if it is currently paused
     */
    /**
     * Whether the Stopwatch has been paused... not actively counting but the start time should be preserved
     */
    var isPaused = false
        private set

    /**
     * The start time in microseconds
     */
    private var start: Long = 0

    /**
     * The Start time for the current paused time
     */
    private var pausedStart: Long = 0

    /**
     * The end time
     */
    private var end: Long = 0

    /**
     * Starts the Stopwatch
     */
    fun start() {
        start = System.currentTimeMillis()
        isRunning = true
        isPaused = false
        pausedStart = -1
    }

    /**
     * Stops the stopwatch and returns the time elapsed
     *
     * @return Stops the StopWatch
     */
    fun stop(): Long {
        return if (!isRunning) {
            -1
        } else if (isPaused) {
            isRunning = false
            isPaused = false
            pausedStart - start
        } else {
            end = System.currentTimeMillis()
            isRunning = false
            end - start
        }
    }

    /**
     * Pauses the Stopwatch
     *
     * @return The time elapsed so far
     */
    fun pause(): Long {
        return if (!isRunning) {
            -1
        } else if (isPaused) {
            pausedStart - start
        } else {
            pausedStart = System.currentTimeMillis()
            isPaused = true
            pausedStart - start
        }
    }

    /**
     * Resumes the StopWatch from it's paused state
     */
    fun resume() {
        if (isPaused && isRunning) {
            start = System.currentTimeMillis() - (pausedStart - start)
            isPaused = false
        }
    }

    /**
     * Returns the total time elapsed
     *
     * @return The total time elapsed
     */
    fun elapsedMilliseconds(): Long {
        return if (isRunning) {
            if (isPaused) pausedStart - start else System.currentTimeMillis() - start
        } else end - start
    }

    /**
     * Returns the total time elapsed
     *
     * @return The total time elapsed
     */
    fun elapsedSeconds(): Long {
        return if (elapsedMilliseconds() > 0) {
            (elapsedMilliseconds() / 1000) % 60
        } else 0
    }


    /**
     * Returns the number of seconds this Stopwatch has elapsed
     *
     * @return The String of the number of seconds
     */
    override fun toString(): String {
        val enlapsed = elapsedMilliseconds()
        return (enlapsed.toDouble() / 1000000000.0).toString() + " Seconds"
    }
}