package com.mallowwww.example;

import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class ExampleModCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent e) {
        var dispatcher = e.getDispatcher();
        dispatcher.register(Commands.literal("npc")
                .requires(s -> s.hasPermission(2))
                .then(Commands.literal("spawn")
                        .executes(ctx -> {
                            ctx.getSource().getLevel().addFreshEntity(new DummyNPC(ExampleModEntities.DUMMY_NPC_TYPE.get(), ctx.getSource().getLevel()));
                            return 0;
                        })
                )

        );
    }
}
