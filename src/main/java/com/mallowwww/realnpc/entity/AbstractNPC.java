package com.mallowwww.realnpc.entity;

import com.mallowwww.realnpc.ModRegistries;
import com.mallowwww.realnpc.api.Action;
import com.mallowwww.realnpc.exception.ActionException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class AbstractNPC extends LivingEntity {
    public Action currentAction;
    public AbstractNPC(EntityType<? extends LivingEntity> entityType, Level level) {

        super(entityType, level);
        currentAction = initialAction();
    }

    public abstract Action initialAction();

    public void moveWithLook(MoveDirection direction, double amount) {
//        var move = direction.local().cross(look).scale(amount);
        var angle = (float)Math.toRadians(yHeadRotO);
        var move = direction.local().yRot(-angle).scale(amount);
        addDeltaMovement(move);


    }
    @Override
    public void tick() {
        super.tick();
        if (currentAction.done()) {
            var action = ModRegistries.ACTIONS.getOptional(currentAction.NEXT_ACTION).orElse(null);
            if (action == null) {
                throw new ActionException.ActionDoesNotExistException(currentAction.NEXT_ACTION);
            } else {
                currentAction = action;

            }
            return;
        }
        currentAction.tick(this);

    }
    public enum MoveDirection {
        NORTH,
        NORTH_WEST,
        WEST,
        SOUTH_WEST,
        SOUTH,
        SOUTH_EAST,
        EAST,
        NORTH_EAST;
        public static MoveDirection fromKeys(boolean forward, boolean backward, boolean left, boolean right) {
            if (forward & !backward) {
                if (left & !right) {
                    return NORTH_EAST;
                } else if (right & !left) {
                    return NORTH_WEST;
                } else {
                    return NORTH;
                }
            } else if (backward & !forward) {
                if (left & !right) {
                    return SOUTH_EAST;
                } else if (right & !left) {
                    return SOUTH_WEST;
                } else {
                    return SOUTH;
                }
            } else {
                if (left & !right) {
                    return EAST;
                } else if (right & !left) {
                    return WEST;
                } else {
                    return NORTH;
                }
            }
        }
        public Vec3 local() {
            return (switch (this) {
                case NORTH -> new Vec3(0, 0, 1);
                case NORTH_EAST -> new Vec3(-1, 0, 1);
                case EAST -> new Vec3(-1, 0, 0);
                case SOUTH_EAST -> new Vec3(-1, 0, -1);
                case SOUTH -> new Vec3(0, 0, -1);
                case SOUTH_WEST -> new Vec3(1, 0, -1);
                case WEST -> new Vec3(1, 0, 0);
                case NORTH_WEST -> new Vec3(1, 0, 1);
            }).normalize();
        }

    }
}
