package com.mallowwww.realnpc.exception;

import net.minecraft.resources.ResourceLocation;

public abstract class ActionException extends RuntimeException {
    public ActionException(String message) {
        super(message);
    }
    public static class MissingActionStatesException extends ActionException {
        public MissingActionStatesException() {
            super("An action was built without any states.");
        }
    }
    public static class ActionTickedInInvalidStateException extends ActionException {
        public ActionTickedInInvalidStateException() {
            super("An action was ticked while having no valid state.");
        }
    }
    public static class ActionDoesNotExistException extends ActionException {
        public ActionDoesNotExistException(ResourceLocation action) {
            super("Action \""+action.toString()+"\" was called for but does not exist.");
        }
    }
}
