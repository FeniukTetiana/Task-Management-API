package ua.shpp.feniuk;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public enum Status {
    PLANNED,
    WORK_IN_PROGRESS,
    POSTPONED,
    NOTIFIED,
    SIGNED,
    DONE,
    CANCELLED;

    private static final Map<Status, Set<Status>> VALID_TRANSITIONS = Map.of(
            PLANNED, Set.of(WORK_IN_PROGRESS, POSTPONED, CANCELLED, PLANNED),
            WORK_IN_PROGRESS, Set.of(NOTIFIED, SIGNED, POSTPONED, WORK_IN_PROGRESS, CANCELLED),
            POSTPONED, Set.of(NOTIFIED, SIGNED, POSTPONED, WORK_IN_PROGRESS, CANCELLED),
            NOTIFIED, Set.of(SIGNED, NOTIFIED, DONE, CANCELLED),
            SIGNED, Set.of(SIGNED, NOTIFIED, DONE, CANCELLED)
    );

    public boolean isTransitionValid(Status newStatus) {
        return VALID_TRANSITIONS.getOrDefault(this, Collections.emptySet()).contains(newStatus);
    }
}
