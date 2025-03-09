package ua.shpp.feniuk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {
    @Test
    void testIsTransitionValidFromPLANNED() {
        // true
        assertTrue(Status.PLANNED.isTransitionValid(Status.PLANNED));
        assertTrue(Status.PLANNED.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertTrue(Status.PLANNED.isTransitionValid(Status.POSTPONED));
        assertTrue(Status.PLANNED.isTransitionValid(Status.CANCELLED));
        // false
        assertFalse(Status.PLANNED.isTransitionValid(Status.NOTIFIED));
        assertFalse(Status.PLANNED.isTransitionValid(Status.SIGNED));
        assertFalse(Status.PLANNED.isTransitionValid(Status.DONE));
    }

    @Test
    void testIsTransitionValidFromWORK_IN_PROGRESS() {
        // true
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.NOTIFIED));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.SIGNED));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.POSTPONED));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.CANCELLED));
        // false
        assertFalse(Status.WORK_IN_PROGRESS.isTransitionValid(Status.PLANNED));
        assertFalse(Status.WORK_IN_PROGRESS.isTransitionValid(Status.DONE));
    }

    @Test
    void testIsTransitionValidFromPOSTPONED() {
        // true
        assertTrue(Status.POSTPONED.isTransitionValid(Status.POSTPONED));
        assertTrue(Status.POSTPONED.isTransitionValid(Status.NOTIFIED));
        assertTrue(Status.POSTPONED.isTransitionValid(Status.SIGNED));
        assertTrue(Status.POSTPONED.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertTrue(Status.POSTPONED.isTransitionValid(Status.CANCELLED));
        // false
        assertFalse(Status.POSTPONED.isTransitionValid(Status.PLANNED));
        assertFalse(Status.POSTPONED.isTransitionValid(Status.DONE));
    }

    @Test
    void testIsTransitionValidFromNOTIFIED() {
        // true
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.NOTIFIED));
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.DONE));
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.SIGNED));
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.CANCELLED));
        // false
        assertFalse(Status.NOTIFIED.isTransitionValid(Status.PLANNED));
        assertFalse(Status.NOTIFIED.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertFalse(Status.NOTIFIED.isTransitionValid(Status.POSTPONED));
    }

    @Test
    void testIsTransitionValidFromSIGNED() {
        // true
        assertTrue(Status.SIGNED.isTransitionValid(Status.SIGNED));
        assertTrue(Status.SIGNED.isTransitionValid(Status.DONE));
        assertTrue(Status.SIGNED.isTransitionValid(Status.NOTIFIED));
        assertTrue(Status.SIGNED.isTransitionValid(Status.CANCELLED));
        // false
        assertFalse(Status.SIGNED.isTransitionValid(Status.PLANNED));
        assertFalse(Status.SIGNED.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertFalse(Status.SIGNED.isTransitionValid(Status.POSTPONED));
    }

    @Test
    void testIsTransitionValidFromDONE() {
        // false так як фінальні
        assertFalse(Status.DONE.isTransitionValid(Status.PLANNED));
        assertFalse(Status.DONE.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertFalse(Status.DONE.isTransitionValid(Status.POSTPONED));
        assertFalse(Status.DONE.isTransitionValid(Status.NOTIFIED));
        assertFalse(Status.DONE.isTransitionValid(Status.SIGNED));
        assertFalse(Status.DONE.isTransitionValid(Status.CANCELLED));
        assertFalse(Status.DONE.isTransitionValid(Status.DONE));
    }
    @Test
    void testIsTransitionValidFromCANCELLED() {
        // false так як фінальні
        assertFalse(Status.CANCELLED.isTransitionValid(Status.WORK_IN_PROGRESS));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.PLANNED));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.POSTPONED));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.NOTIFIED));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.SIGNED));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.DONE));
        assertFalse(Status.CANCELLED.isTransitionValid(Status.CANCELLED));
    }
}