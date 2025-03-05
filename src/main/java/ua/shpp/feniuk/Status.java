package ua.shpp.feniuk;

public enum Status {
    PLANNED,
    WORK_IN_PROGRESS,
    POSTPONED,
    NOTIFIED,
    SIGNED,
    DONE,
    CANCELLED;

    // todo collection
    public boolean isTransitionValid(Status newStatus) {
        return switch (this) {
            case PLANNED ->
                    newStatus == WORK_IN_PROGRESS ||
                    newStatus == POSTPONED ||
                    newStatus == Status.CANCELLED ||
                    newStatus == Status.PLANNED;
            case WORK_IN_PROGRESS ->
                    newStatus == Status.NOTIFIED ||
                    newStatus == Status.SIGNED ||
                    newStatus == Status.POSTPONED  ||
                    newStatus == Status.WORK_IN_PROGRESS ||
                    newStatus == Status.CANCELLED;
            case POSTPONED ->
                    newStatus == Status.WORK_IN_PROGRESS ||
                    newStatus == Status.POSTPONED ||
                    newStatus == Status.NOTIFIED ||
                    newStatus == Status.SIGNED ||
                    newStatus == Status.CANCELLED;
            case NOTIFIED ->
                    newStatus == Status.SIGNED ||
                    newStatus == Status.NOTIFIED ||
                    newStatus == Status.DONE ||
                    newStatus == Status.CANCELLED;
            case SIGNED ->
                    newStatus == Status.NOTIFIED ||
                    newStatus == Status.SIGNED ||
                    newStatus == Status.CANCELLED ||
                    newStatus == Status.DONE;
            default -> false;
        };
    }
}
