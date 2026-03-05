package com.mallowwww.example;

import com.mallowwww.realnpc.ModActions;
import com.mallowwww.realnpc.api.Action;
import com.mallowwww.realnpc.entity.AbstractNPC;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
@EventBusSubscriber
public class DummyNPC extends AbstractNPC {
    private ItemStack[] armor = {ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
    private static AttributeSupplier ATTRIBUTES = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH, 20)
            .add(Attributes.SCALE, 1)
            .add(Attributes.GRAVITY)
            .add(Attributes.MAX_ABSORPTION, 20)
            .add(Attributes.ARMOR, 0)
            .add(Attributes.ARMOR_TOUGHNESS, 0)
            .add(Attributes.STEP_HEIGHT)
            .add(Attributes.MOVEMENT_EFFICIENCY)
            .add(Attributes.MOVEMENT_SPEED)
            .add(Attributes.ATTACK_SPEED)
            .add(Attributes.ATTACK_KNOCKBACK)
            .add(Attributes.SAFE_FALL_DISTANCE)
            .add(Attributes.ATTACK_DAMAGE)
            .add(Attributes.FALL_DAMAGE_MULTIPLIER)
            .add(Attributes.BLOCK_BREAK_SPEED)
            .build();
    private static EntityDataAccessor<CompoundTag> ACTION_DATA = SynchedEntityData.defineId(DummyNPC.class, EntityDataSerializers.COMPOUND_TAG);
    public DummyNPC(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);


    }


    @Override
    public void aiStep() {
        super.aiStep();



    }

    @Override
    public Action initialAction() {
        return ModActions.IDLE_ACTION.get();

    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION_DATA, new CompoundTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Arrays.stream(armor).toList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;

    }



    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }
    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(ExampleModEntities.DUMMY_NPC_TYPE.get(), ATTRIBUTES);

    }
}
