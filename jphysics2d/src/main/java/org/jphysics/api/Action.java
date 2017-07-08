package org.jphysics.api;

/**
 * Created by luis on 05/07/17.
 */
public class Action {
    private long whenExecute;
    private Runnable action;
    private boolean executed;

    public Action(long whenExecute, Runnable action) {
        this.whenExecute = whenExecute;
        this.action = action;
    }

    public long getWhenExecute() {
        return whenExecute;
    }

    public Action setWhenExecute(long whenExecute) {
        this.whenExecute = whenExecute;
        return this;
    }

    public Runnable getAction() {
        return action;
    }

    public Action setAction(Runnable action) {
        this.action = action;
        return this;
    }

    public boolean isExecuted() {
        return executed;
    }

    public Action setExecuted(boolean executed) {
        this.executed = executed;
        return this;
    }
}
