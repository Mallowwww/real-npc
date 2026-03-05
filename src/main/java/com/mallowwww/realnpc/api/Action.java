package com.mallowwww.realnpc.api;

import com.mallowwww.realnpc.ModActions;
import com.mallowwww.realnpc.entity.AbstractNPC;
import com.mallowwww.realnpc.exception.ActionException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Action {
    private final List<ActionState> STATES;
    private final ResourceLocation NEXT;
    private ActionState currentState;
    private int stateIndex = 0;

    protected Action(List<ActionState> STATES, ResourceLocation NEXT) {
        this.STATES = STATES;
        this.NEXT = NEXT;
        currentState = STATES.getFirst();
    }
    public Optional<ActionState> currentState() {
        return Optional.ofNullable(currentState);
    }
    public boolean done() {
        return stateIndex == STATES.size()-1;
    }
    public boolean advanceState() {
        if (done())
            return false;
        currentState = STATES.get(++stateIndex);
        return true;
    }

    public static class Builder {
        private final List<ActionState> STATES;
        private ResourceLocation NEXT = ResourceLocation.parse("realnpc:idle");
        public Builder() {
            STATES = new LinkedList<>();
        }
        public Builder cancellable(Consumer<AbstractNPC> tick, int lengthInTicks, List<ResourceLocation> cancellableConditions) {
            STATES.add(new ActionState() {

                private int ticks = 0;
                private final int maxTicks = lengthInTicks;
                @Override
                public void tick(AbstractNPC npc) {
                    tick.accept(npc);
                }

                @Override
                public boolean done() {
                    return ticks >= lengthInTicks && lengthInTicks >= 0;
                }

                @Override
                public List<ResourceLocation> cancellableBy() {
                    return cancellableConditions;
                }
            });
            return this;
        }
        public Builder runnable(Consumer<AbstractNPC> tick, int lengthInTicks) {
            cancellable(tick, lengthInTicks, List.of());
            return this;
        }
        public Builder wait(int lengthInTicks) {
            runnable((npc) -> {}, lengthInTicks);
            return this;
        }
        public Builder idle() {
            runnable((npc) -> {}, -1);
            return this;
        }
        public Action build() {
            if (STATES.isEmpty()) {
                throw new ActionException.MissingActionStatesException();
            }
            return new Action(STATES, NEXT);
        }
    }
}
