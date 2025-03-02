package ua.shpp.feniuk;

public enum Status {
    PLANNED,
    WORK_IN_PROGRESS,
    POSTPONED,
    NOTIFIED,
    SIGNED,
    DONE,
    CANCELLED;

    public boolean isFinal() {
        return this == DONE || this == CANCELLED;
    }

    private boolean isTransitionValid(Status current, Status newStatus) {
        return switch (current) {
            case PLANNED ->
                    newStatus == Status.WORK_IN_PROGRESS || newStatus == Status.POSTPONED;
            case WORK_IN_PROGRESS, POSTPONED ->
                    newStatus == Status.NOTIFIED || newStatus == Status.SIGNED;
            case NOTIFIED, SIGNED ->
                    newStatus == Status.DONE;
            default -> false;
        };
    }
}
