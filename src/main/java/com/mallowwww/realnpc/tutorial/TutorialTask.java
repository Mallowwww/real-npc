package com.mallowwww.realnpc.tutorial;

import com.mallowwww.realnpc.spook.SpookState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface TutorialTask {
    boolean successCondition(Level level, SpookState state);
    boolean failCondition(Level level, SpookState state);
    String text(SpookState state);
    ResourceLocation location();
    default void start(Level level, SpookState state) {}
    default void stop(Level level, SpookState state) {}
    class Builder {
        private BiFunction<Level, SpookState, Boolean> success;
        private BiFunction<Level, SpookState, Boolean> fail;
        private BiConsumer<Level, SpookState> start;
        private BiConsumer<Level, SpookState> stop;
        private String text;
        private ResourceLocation location;

        public Builder() {

        }
        public Builder success(BiFunction<Level, SpookState, Boolean> success) {
            this.success = success;
            return this;
        }
        public Builder fail(BiFunction<Level, SpookState, Boolean> fail) {
            this.fail = fail;
            return this;
        }
        public Builder start(BiConsumer<Level, SpookState> start) {
            this.start = start;
            return this;
        }
        public Builder stop(BiConsumer<Level, SpookState> stop) {
            this.stop = stop;
            return this;
        }
        public Builder text(String text) {
            this.text = text;
            return this;
        }
        public Builder location(ResourceLocation location) {
            this.location = location;
            return this;
        }
        public TutorialTask build() {
            return new TutorialTask() {
                @Override
                public boolean successCondition(Level level, SpookState state) {
                    return success.apply(level, state);
                }

                @Override
                public boolean failCondition(Level level, SpookState state) {
                    return fail.apply(level, state);
                }

                @Override
                public String text(SpookState state) {
                    return text;
                }

                @Override
                public ResourceLocation location() {
                    return location;
                }

                @Override
                public void start(Level level, SpookState state) {
                    start.accept(level, state);
                }

                @Override
                public void stop(Level level, SpookState state) {
                    stop.accept(level, state);
                }
            };
        }
    }
}
