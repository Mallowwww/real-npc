package com.mallowwww.realnpc.action;

import com.mallowwww.realnpc.api.Action;
import com.mallowwww.realnpc.api.ActionState;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class LookAtEntityAction extends Action {
    protected LookAtEntityAction(List<ActionState> _STATES, ResourceLocation NEXT_ACTION) {
        super(_STATES, NEXT_ACTION);
    }


}
