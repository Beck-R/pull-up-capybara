package beck.pullup.capybara.entity.custom;

import beck.pullup.capybara.PullUpCapybara;
import beck.pullup.capybara.entity.ModEntities;
import beck.pullup.capybara.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CapybaraEntity extends TameableEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public CapybaraEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity passiveEntity) {
        return ModEntities.CAPYBARA.create(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.MELON_SLICE;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1000.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 0.5));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0, 10.f, 2.0f, false));
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(3, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.goalSelector.add(4, new SwimAroundGoal(this, 2.0, 10));
        this.goalSelector.add(5, new MoveIntoWaterGoal(this));
        this.goalSelector.add(6, new WanderAroundPointOfInterestGoal(this, 0.75f, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && !this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walking", true));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        this.world.playSound(null, this.getBlockPos(), PullUpCapybara.CAPYBARA_AMBIENT,
                SoundCategory.AMBIENT, 1.0f, 1.0f);
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_DOLPHIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FOX_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
    }

    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(CapybaraEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.ORANGE;

        if (isBreedingItem(itemstack)) {

            return super.interactMob(player, hand);
        }

        if (item == itemForTaming && !isTamed()) {
            if (this.world.isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                if (!this.world.isClient()) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setTarget(null);
                    this.world.sendEntityStatus(this, (byte) 7);
                    setSit(true);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && !this.world.isClient() && hand == Hand.MAIN_HAND && isOwner(player)) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(100.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(1000.0f);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double) 0.4f);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
        nbt.putInt("Variant", this.getTypeVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
    }

    @Override
    public AbstractTeam getScoreboardTeam() {
        return super.getScoreboardTeam();
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(DATA_ID_TYPE_VARIANT, 0);
    }

    /* VARIANTS */
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT = DataTracker.registerData(CapybaraEntity.class,
            TrackedDataHandlerRegistry.INTEGER);

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
            SpawnReason spawnReason, @Nullable EntityData entityData,
            @Nullable NbtCompound entityNbt) {
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    private int getTypeVariant() {
        return this.dataTracker.get(DATA_ID_TYPE_VARIANT);
    }

}