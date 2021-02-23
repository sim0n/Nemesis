package dev.sim0n.anticheat.violation.base;

public interface Violation {

    /**
     * The violation level that will be used to determine action
     * @return The violation level
     */
    default int getPoints() {
        return 1;
    }

    /**
     * @return The data for the violation
     */
    default String getData() {
        return null;
    }
}
